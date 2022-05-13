package fun.tans.seckill.controller;

import fun.tans.seckill.domain.MiaoshaOrder;
import fun.tans.seckill.domain.MiaoshaUser;
import fun.tans.seckill.domain.OrderInfo;
import fun.tans.seckill.mq.MQSender;
import fun.tans.seckill.mq.MiaoshaMessage;
import fun.tans.seckill.redis.GoodsKey;
import fun.tans.seckill.redis.MiaoshaKey;
import fun.tans.seckill.redis.RedisService;
import fun.tans.seckill.result.CodeMsg;
import fun.tans.seckill.result.Result;
import fun.tans.seckill.service.GoodsService;
import fun.tans.seckill.service.MiaoshaOrderService;
import fun.tans.seckill.service.MiaoshaService;
import fun.tans.seckill.service.MiaoshaUserService;
import fun.tans.seckill.util.MD5Util;
import fun.tans.seckill.validator.NeedAuth;
import fun.tans.seckill.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: tyf
 * @CreateTime: 2022/4/18
 * @Describe: 秒杀模块Controller
 **/
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    MiaoshaOrderService miaoshaOrderService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    MQSender mqSender;

    Map<Long, Boolean> localOverMap = new HashMap();

    /**
     * 系统初始化将预缓存redis
     */
    @Override
    public void afterPropertiesSet() {
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        if (goodsVos == null) return;
        for (GoodsVo goodsVo : goodsVos) {
            redisService.set(GoodsKey.getGoodStock, "" + goodsVo.getId(), goodsVo.getStockCount());
            localOverMap.put(goodsVo.getId(), false);
        }
    }

    /**
     * 秒杀具体逻辑
     *
     * @param model
     * @param user:用户上下文信息
     * @param goodsId:秒杀商品id
     * @return modelName
     */
    @PostMapping("/do_miaosha")
    @ResponseBody
    @NeedAuth
    public Result<OrderInfo> miaosha(Model model, MiaoshaUser user,
                                     @RequestParam("goodsId") long goodsId) {
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

        //判断库存
        if (goods.getStockCount() <= 0) {
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_NO_STOCK.getMsg());
            return Result.error(CodeMsg.MIAO_SHA_NO_STOCK);
        }

        //判断用户是否已经秒杀过
        MiaoshaOrder order = miaoshaOrderService.getMiaoshaOrderByGoodsIdUserId(goodsId, user.getId());
        if (order != null) {
            model.addAttribute("errmsg", CodeMsg.MIA_SHA_REPEAT.getMsg());
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }

        //执行秒杀流程
        OrderInfo orderInfo = miaoshaService.miaosha(goods, user);

        return Result.success(orderInfo);
    }

    /**
     * 秒杀具体逻辑 rabbit优化版本
     * 1.系统初始化，将商品库存数量加载到redis
     * 2.收到请求，Redis预减库存，库存不足，直接返回，否则进入3
     * 3.请求入队，立即返回排队中
     * 4.请求出队，生成订单，较少库存
     * 5.客户端轮询，是否秒杀成功
     *TODO
     * @param model
     * @param user:用户上下文信息
     * @param goodsId:秒杀商品id
     * @return modelName
     */
    @PostMapping("/{path}/do_quick_miaosha")
    @ResponseBody
    @NeedAuth
    public Result<Integer> miaosha2(Model model, MiaoshaUser user,
                                    @RequestParam("goodsId") long goodsId, 
                                    @PathVariable String path) {

        //验证path


        //访问内存标记
        boolean over = localOverMap.get(goodsId);
        if (over) {
            return Result.error(CodeMsg.MIAO_SHA_NO_STOCK);
        }
        //判断库存
        Long stock = redisService.decr(GoodsKey.getGoodStock, "" + goodsId);
        if (stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.MIAO_SHA_NO_STOCK);
        }
        //判断用户是否已经秒杀过
        MiaoshaOrder order = miaoshaOrderService.getMiaoshaOrderByGoodsIdUserId(goodsId, user.getId());
        if (order != null) {
            model.addAttribute("errmsg", CodeMsg.MIA_SHA_REPEAT.getMsg());
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //入队
        MiaoshaMessage msg = new MiaoshaMessage(user, goodsId);
        mqSender.sendMiaosha(msg);
        return Result.success(0);

    }


    /**
     * 返回秒杀结果
     * -1:秒杀失败
     * 0：排队中
     * 其他：生成订单ID
     *
     * @param user
     * @param goodsId
     * @return
     */
    @GetMapping("/result")
    @ResponseBody
    @NeedAuth
    public Result<Long> miaoshaResult(MiaoshaUser user, @RequestParam("goodsId") long goodsId) {

        long result = miaoshaOrderService.getMiaoshaResult(user.getId(), goodsId);

        return Result.success(result);
    }

    /**
     * 返回秒杀结果
     * -1:秒杀失败
     * 0：排队中
     * 其他：生成订单ID
     *
     * @param user
     * @param goodsId
     * @return
     */
    @GetMapping("/path")
    @ResponseBody
    @NeedAuth
    public Result<String> miaoshaPath(MiaoshaUser user, @RequestParam("goodsId") long goodsId) {
        UUID uuid = UUID.randomUUID();
        String path = MD5Util.md5(uuid + "123456");
        //添加
        redisService.set(MiaoshaKey.getMiaoshaPath, "" + user.getId() + "_" + goodsId, path);
        return Result.success(path);
    }


}
