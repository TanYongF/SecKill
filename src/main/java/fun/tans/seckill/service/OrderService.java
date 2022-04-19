package fun.tans.seckill.service;

import fun.tans.seckill.dao.MiaoshaOrderDao;
import fun.tans.seckill.domain.MiaoshaOrder;
import fun.tans.seckill.domain.MiaoshaUser;
import fun.tans.seckill.domain.OrderInfo;
import fun.tans.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Describe: 类描述
 * @Author: tyf
 * @CreateTime: 2022/4/18
 **/
@Service
public class OrderService {

    @Autowired
    private MiaoshaOrderDao orderDao;

    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, GoodsVo goods) {
        OrderInfo info = new OrderInfo();
        info.setGoodsId(goods.getId());
        info.setCreateDate(new Date());
        info.setDeliveryAddrId(0L);
        info.setGoodsCount(1);
        info.setUserId(user.getId());
        info.setStatus(0);
        info.setOrderChannel(1);
        info.setGoodsName(goods.getGoodsName());
        info.setGoodsPrice(goods.getMiaoshaPrice());
        info.setPayDate(new Date());
        orderDao.insert(info);
        long orderId = info.getId();
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setOrderId(orderId);
        miaoshaOrder.setUserId(user.getId());
        miaoshaOrder.setGoodsId(goods.getId());
        orderDao.insertMiaoshaOrder(miaoshaOrder);
        return info;
    }
}
