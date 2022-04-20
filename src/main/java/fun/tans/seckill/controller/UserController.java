package fun.tans.seckill.controller;

import fun.tans.seckill.domain.MiaoshaUser;
import fun.tans.seckill.redis.RedisService;
import fun.tans.seckill.result.CodeMsg;
import fun.tans.seckill.result.Result;
import fun.tans.seckill.service.GoodsService;
import fun.tans.seckill.service.MiaoshaUserService;
import fun.tans.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Describe: 用户管理模块Controller
 * @Author: tyf
 * @CreateTime: 2022/4/17
 **/
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @GetMapping("/info")
    @ResponseBody
    public Result<MiaoshaUser> list(Model model, MiaoshaUser user) {
        return Result.success(user);
    }

//    @GetMapping("/updatePassword")
//    @ResponseBody
//    public Result<MiaoshaUser> updatePass(Model model, MiaoshaUser user) {
////        if(user == null) return Result.error(CodeMsg)
//        userService.updatePassword(user.getId(), )
//        return Result.success(user);
//    }


}
