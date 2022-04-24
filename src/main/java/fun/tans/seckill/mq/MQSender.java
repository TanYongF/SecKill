package fun.tans.seckill.mq;

import fun.tans.seckill.config.MQConfig;
import fun.tans.seckill.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Describe: 消息队列发送数据
 * @Author: tyf
 * @CreateTime: 2022/4/24
 **/
@Service
public class MQSender {

    private static Logger logger = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    AmqpTemplate amqpTemplate;

    public void send(Object object){
        String str = JSONUtil.beanToString(object);
        for (int i = 0; i < 1000; i++) {
            amqpTemplate.convertAndSend(MQConfig.QUEUE_NAME, str);
        }
        logger.info("send message " + str);
    }

    public void sendTopic(Object object){
        String str = JSONUtil.beanToString(object);
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key1", str);
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key2", str);
        logger.info("send topic message " + str);
    }
}
