package fun.tans.seckill.service;

import fun.tans.seckill.dao.MiaoshaOrderDao;
import fun.tans.seckill.domain.MiaoshaOrder;
import fun.tans.seckill.redis.MiaoshaKey;
import fun.tans.seckill.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Describe: 秒杀订单接口
 * @Author: tyf
 * @CreateTime: 2022/4/18
 **/
@Service
public class MiaoshaOrderService {

    @Autowired
    MiaoshaOrderDao miaoshaOrderDao;

    @Autowired
    RedisService redisService;

    public MiaoshaOrder getMiaoshaOrderByGoodsIdUserId(long goodsId, long userId) {
        return miaoshaOrderDao.getMiaoshaOrderByGoodsIdUserId(goodsId, userId);
    }

    public long getMiaoshaResult(Long userId, long goodsId) {
        MiaoshaOrder miaoshaOrder = getMiaoshaOrderByGoodsIdUserId(goodsId, userId);
        //如果可以查到
        if (miaoshaOrder != null) {
            return miaoshaOrder.getOrderId();
        } else {
            //判断商品是否已经卖完
            boolean isOver = getMiaoshaGoodsOver(goodsId);
            if (isOver) {
                //如果已经卖完
                return -1;
            } else {
                //如果未卖完
                return 0;
            }
        }
    }

    private boolean getMiaoshaGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver, "" + goodsId);
    }

}
