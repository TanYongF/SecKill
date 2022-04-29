package fun.tans.seckill.controller;

import fun.tans.seckill.domain.User;
import fun.tans.seckill.mq.MQReceiver;
import fun.tans.seckill.mq.MQSender;
import fun.tans.seckill.redis.RedisService;
import fun.tans.seckill.redis.UserPrefix;
import fun.tans.seckill.result.Result;
import fun.tans.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Describe: 测试数据库和redis
 * @Author: tyf
 * @CreateTime: 2022/4/15
 **/
@RestController
public class TestController {

    @Autowired
    RedisService redisService;

    @Autowired
    private UserService userService;

    @Autowired
    private MQReceiver mqReceiver;

    @Autowired
    private MQSender mqSender;


    @RequestMapping("/redis/get")
    public Result<User> get() {
        User user = redisService.get(UserPrefix.getById, "" + 3, User.class);
        return Result.success(user);
    }

    @RequestMapping("/redis/set")
    public Result<Boolean> set() {
        User tan1 = new User("tan1", 3);
        redisService.set(UserPrefix.getById, "" + tan1.getId(), tan1);
        return Result.success(true);
    }

    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet() {
        User user = userService.getById(1);
        return Result.success(user);
    }

    @Transactional
    @RequestMapping("/db/tx")
    public Result<Boolean> dbInsert() {
        User cao = new User("cao", 2);
        userService.insert(cao);
        User tan = new User("tan", 2);
        userService.insert(tan);
        return Result.success(true);
    }

    @RequestMapping("/mq/send")
    public Result<String> mqSend() {
        mqSender.send("Hello Mq");
        return Result.success("发送完成");
    }

    @RequestMapping("/mq/receive")
    public Result<String> mqReceive() {
        mqSender.send("Hello Mq");
        return Result.success("发送完成");
    }

    @RequestMapping("/mq/send_topic")
    public Result<String> mqTopicSend() {
        mqSender.sendTopic("hello topic message");
        return Result.success("发送topic message完成");
    }

    @ResponseBody
    @RequestMapping("/mq/send_fanout")
    public Result<String> mqFanoutSend() {
        mqSender.sendFanout("hello fanout message");
        return Result.success("发送fanout message完成");
    }

    @ResponseBody
    @RequestMapping("/mq/send_header")
    public Result<String> mqHeadersSend() {
        mqSender.sendFanout("hello header message");
        return Result.success("发送fanout message完成");
    }

}
