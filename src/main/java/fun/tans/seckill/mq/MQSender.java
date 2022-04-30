package fun.tans.seckill.mq;

import fun.tans.seckill.config.MQConfig;
import fun.tans.seckill.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

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

    public void send(Object object) {
        String str = JSONUtil.beanToString(object);
        for (int i = 0; i < 1000; i++) {
            amqpTemplate.convertAndSend(MQConfig.QUEUE_NAME, str);
        }
        logger.info("send message " + str);
    }

    public void sendTopic(Object object) {
        String str = JSONUtil.beanToString(object);
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key1", str);
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key2", str);
        logger.info("send topic message " + str);
    }

    public void sendFanout(Object object) {
        String str = JSONUtil.beanToString(object);
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, "", str);
        logger.info("send fanout message " + str);
    }

    /**
     * 发送Header模式数据
     *
     * @param object
     */
    public void sendHeader(Object object) {
        String str = JSONUtil.beanToString(object);
        MessageProperties properties = new MessageProperties();
        properties.setHeader("header1", "value1");
        properties.setHeader("header2", "value2");
        Message msg = new Message(str.getBytes(StandardCharsets.UTF_8), properties);
        amqpTemplate.convertAndSend(MQConfig.HEADER_EXCHANGE, msg);
        logger.info("send headers message " + str);
    }

    public void sendMiaosha(MiaoshaMessage msg) {
        String str = JSONUtil.beanToString(msg);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE_NAME, str);
        logger.info("send message " + str);
    }


}
