package fun.tans.seckill.controller;

import fun.tans.seckill.domain.User;
import fun.tans.seckill.redis.RedisService;
import fun.tans.seckill.redis.UserPrefix;
import fun.tans.seckill.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Describe: 类描述
 * @Author: tyf
 * @CreateTime: 2022/4/15
 **/
@RestController
public class myController {

    @Autowired
    RedisService redisService;


    @RequestMapping("/redis/get")
    public Result<User> get(){
        User user = redisService.get(UserPrefix.getById,"" + 3, User.class);
        return Result.success(user);
    }

    @RequestMapping("/redis/set")
    public Result<Boolean> set(){
        User tan1 = new User(3, "tan1");
        redisService.set(UserPrefix.getById, "" + tan1.getId(), tan1);
        return Result.success(true);
    }
}
