package fun.tans.seckill.service;

import fun.tans.seckill.domain.MiaoshaUser;
import fun.tans.seckill.domain.OrderInfo;
import fun.tans.seckill.redis.MiaoshaKey;
import fun.tans.seckill.redis.RedisService;
import fun.tans.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Describe: 类描述
 * @Author: tyf
 * @CreateTime: 2022/4/18
 **/
@Service
public class MiaoshaService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    @Transactional
    public OrderInfo miaosha(GoodsVo goods, MiaoshaUser user) {
        //减库存
        boolean success = goodsService.reduceStock(goods);
        //减库存失败
        if (!success) {
            setGoodsOver(goods.getId());
            return null;
        }
        //下订单，写入秒杀订单
        OrderInfo order = orderService.createOrder(user, goods);
        return order;
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver, "" + goodsId, true);
    }

}
