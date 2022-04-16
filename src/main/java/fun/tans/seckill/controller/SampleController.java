package fun.tans.seckill.controller;

import fun.tans.seckill.domain.User;
import fun.tans.seckill.result.Result;
import fun.tans.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Describe: 类描述
 * @Author: tyf
 * @CreateTime: 2022/4/15
 **/

@RestController
public class SampleController {

    @Autowired
    private UserService userService;

    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet(){
        User user = userService.getById(1);
        return Result.success(user);
    }

    @Transactional
    @RequestMapping("/db/tx")
    public Result<Boolean> dbInsert(){
        User cao = new User(2, "cao");
        userService.insert(cao);
        User tan = new User(2, "tan");
        userService.insert(tan);
        return Result.success(true);
    }

}
