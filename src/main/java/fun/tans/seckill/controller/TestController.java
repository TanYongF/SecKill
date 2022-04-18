package fun.tans.seckill.controller;

import fun.tans.seckill.domain.User;
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
}
