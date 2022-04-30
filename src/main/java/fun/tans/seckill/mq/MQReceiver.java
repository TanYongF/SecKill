package fun.tans.seckill.mq;

import fun.tans.seckill.config.MQConfig;
import fun.tans.seckill.domain.MiaoshaOrder;
import fun.tans.seckill.domain.MiaoshaUser;
import fun.tans.seckill.service.GoodsService;
import fun.tans.seckill.service.MiaoshaOrderService;
import fun.tans.seckill.service.MiaoshaService;
import fun.tans.seckill.util.JSONUtil;
import fun.tans.seckill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Describe: MQ接收数据
 * @Author: tyf
 * @CreateTime: 2022/4/24
 **/
@Service
public class MQReceiver {

    private static final Logger logger = LoggerFactory.getLogger(MQReceiver.class);
    @Autowired
    GoodsService goodsService;
    @Autowired
    MiaoshaOrderService miaoshaOrderService;
    @Autowired
    MiaoshaService miaoshaService;

    @RabbitListener(queues = MQConfig.QUEUE_NAME)
    public void receive(String message) {
        logger.info("receive message" + message);
    }

    @RabbitListener(queues = MQConfig.FANOUT_QUEUE)
    public void receiveFanout(String message) {
        logger.info("receive fanout message" + message);
    }

    /**
     * 监听header_queue
     *
     * @param message
     */
    @RabbitListener(queues = MQConfig.HEADER_QUEUE)
    public void receiveHeader(byte[] message) {
        logger.info("receive header message" + new String(message));
    }

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE_NAME)
    public void receiveMiaosha(String message) {
        logger.info("receive header message" + message);
        MiaoshaMessage mm = JSONUtil.stringToBean(message, MiaoshaMessage.class);
        MiaoshaUser user = mm.getMiaoshaUser();
        long goodsId = mm.getGoodId();

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        //判断库存
        if (goods.getStockCount() <= 0) {
            return;
        }

        //判断是重复下单
        MiaoshaOrder order = miaoshaOrderService.getMiaoshaOrderByGoodsIdUserId(goodsId, user.getId());
        if (order != null) {
            return;
        }
        //执行秒杀流程
        miaoshaService.miaosha(goods, user);
    }

}


