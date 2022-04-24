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
    public void receive(String message){
        logger.info("receive message" + message);
    }
}
