package fun.tans.seckill.controller;

import fun.tans.seckill.domain.MiaoshaOrder;
import fun.tans.seckill.domain.MiaoshaUser;
import fun.tans.seckill.domain.OrderInfo;
import fun.tans.seckill.redis.RedisService;
import fun.tans.seckill.result.CodeMsg;
import fun.tans.seckill.service.GoodsService;
import fun.tans.seckill.service.MiaoshaOrderService;
import fun.tans.seckill.service.MiaoshaService;
import fun.tans.seckill.service.MiaoshaUserService;
import fun.tans.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Describe: 秒杀模块Controller
 * @Author: tyf
 * @CreateTime: 2022/4/18
 **/
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {

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

    /**
     * 秒杀具体逻辑
     *
     * @param model
     * @param user:用户上下文信息
     * @param goodsId:秒杀商品id
     * @return modelName
     */
    @PostMapping("/do_miaosha")
    public String list(Model model, MiaoshaUser user,
                       @RequestParam("goodsId") long goodsId) {
        if (user == null) return "login";
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

        //判断库存
        if (goods.getStockCount() <= 0) {
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_NO_STOCK.getMsg());
            return "miaosha_fail";
        }

        //判断用户是否已经秒杀过
        MiaoshaOrder order = miaoshaOrderService.getMiaoshaOrderByGoodsIdUserId(goodsId, user.getId());
        if (order != null) {
            model.addAttribute("errmsg", CodeMsg.MIA_SHA_REPEAT.getMsg());
            return "miaosha_fail";
        }

        //执行秒杀流程
        OrderInfo orderInfo = miaoshaService.miaosha(goods, user);

        //填充model数据
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);
        model.addAttribute("user", user);
        return "order_detail";
    }
}
