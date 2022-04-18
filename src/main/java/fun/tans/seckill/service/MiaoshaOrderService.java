package fun.tans.seckill.service;

import fun.tans.seckill.dao.MiaoshaOrderDao;
import fun.tans.seckill.domain.MiaoshaOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Describe: 类描述
 * @Author: tyf
 * @CreateTime: 2022/4/18
 **/
@Service
public class MiaoshaOrderService {

    @Autowired
    MiaoshaOrderDao miaoshaOrderDao;

    public MiaoshaOrder getMiaoshaOrderByGoodsIdUserId(long goodsId, long userId) {
        return miaoshaOrderDao.getMiaoshaOrderByGoodsIdUserId(goodsId, userId);
    }
}
