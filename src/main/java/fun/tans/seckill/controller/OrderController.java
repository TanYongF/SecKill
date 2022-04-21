package fun.tans.seckill.controller;

import fun.tans.seckill.domain.MiaoshaUser;
import fun.tans.seckill.domain.OrderInfo;
import fun.tans.seckill.redis.RedisService;
import fun.tans.seckill.result.CodeMsg;
import fun.tans.seckill.result.Result;
import fun.tans.seckill.service.GoodsService;
import fun.tans.seckill.service.MiaoshaUserService;
import fun.tans.seckill.service.OrderService;
import fun.tans.seckill.validator.NeedAuth;
import fun.tans.seckill.vo.GoodsVo;
import fun.tans.seckill.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Describe: 订单模块Controller
 * @Author: tyf
 * @CreateTime: 2022/4/18
 **/
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    /**
     * 获取订单信息具体逻辑
     *
     * @param user:用户上下文信息
     * @param orderId:订单ID
     * @return modelName
     */
    @PostMapping("/detail")
    @ResponseBody
    @NeedAuth
    public Result<OrderDetailVo> info(MiaoshaUser user, @RequestParam("orderId") long orderId) {

        OrderInfo orderInfo = orderService.getByOrderId(orderId);
        if (orderInfo == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(orderInfo.getGoodsId());
        OrderDetailVo orderDetailVo = new OrderDetailVo(goodsVo, orderInfo);
        return Result.success(orderDetailVo);
    }
}
