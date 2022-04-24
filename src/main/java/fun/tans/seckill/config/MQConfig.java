package fun.tans.seckill.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Describe: 类描述
 * @Author: tyf
 * @CreateTime: 2022/4/24
 **/

@Configuration
public class MQConfig {

    public static final String QUEUE_NAME = "queue";
    public static final String TOPIC_QUEUE_NAME1 = "topic_queue1";
    public static final String TOPIC_QUEUE_NAME2 = "topic_queue2";

    public static final String TOPIC_EXCHANGE = "topicExchange";

    public static final String ROUTING_KEY1 = "topic.key1";
    public static final String ROUTING_KEY2 = "topic.#";

    /**
     * 直通模式
     * @return
     */
    @Bean
    public Queue queue(){
        return new Queue(QUEUE_NAME, true);
    }

    /**
     * Exchange模式
     * @return
     */
    @Bean
    public Queue topicQueue1(){
        return new Queue(TOPIC_QUEUE_NAME1, true);
    }

    @Bean
    public Queue topicQueue2(){
        return new Queue(TOPIC_QUEUE_NAME2, true);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Binding topicBinding1(){
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with(ROUTING_KEY1);
    }

    @Bean
    public Binding topicBinding2(){
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with(ROUTING_KEY2);
    }
}
