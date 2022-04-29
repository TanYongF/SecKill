package fun.tans.seckill.mq;

import fun.tans.seckill.config.MQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @Describe: MQ接收数据
 * @Author: tyf
 * @CreateTime: 2022/4/24
 **/
@Service
public class MQReceiver {

    private static Logger logger = LoggerFactory.getLogger(MQReceiver.class);

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

}


