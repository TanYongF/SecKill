package fun.tans.seckill.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Describe: 类描述
 * @Author: tyf
 * @CreateTime: 2022/4/15
 **/
@Component
public class RedisFactory {

    Logger logger = LoggerFactory.getLogger(RedisFactory.class);


    @Autowired
    RedisConfig redisConfig;

    @Bean
    public JedisPool JedisFactory(){
        JedisPoolConfig config = new JedisPoolConfig();
        logger.info("redis连接池配置信息："+ redisConfig.toString());
        config.setMaxIdle(redisConfig.getPoolMaxIdle());
        config.setMaxTotal(redisConfig.getPoolMaxTotal());
        config.setMaxWaitMillis(redisConfig.getPoolMaxWait());
        JedisPool pool = new JedisPool(config, redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeout() * 1000,
                redisConfig.getPassword(), redisConfig.getDatabase());
        return pool;
    }
}