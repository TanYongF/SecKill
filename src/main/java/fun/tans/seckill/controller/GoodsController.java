package fun.tans.seckill.controller;

import fun.tans.seckill.domain.MiaoshaUser;
import fun.tans.seckill.redis.RedisService;
import fun.tans.seckill.service.GoodsService;
import fun.tans.seckill.service.MiaoshaUserService;
import fun.tans.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Describe: 类描述
 * @Author: tyf
 * @CreateTime: 2022/4/17
 **/
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;



    @GetMapping("/to_list")
    public String list(Model model,MiaoshaUser user){
        if(user == null) return "login";
        model.addAttribute("user", user);
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsVos);
        return "goods_list";
    }

    @GetMapping("/to_detail")
    public String detail(Model model, MiaoshaUser user){
        if(user == null) return "login";
        model.addAttribute("user", user);
        return "goods_detail";

    }
}
