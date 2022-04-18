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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Describe: 物品管理模块Controller
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
    public String list(Model model, MiaoshaUser user) {
        model.addAttribute("user", user);
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsVos);
        return "goods_list";
    }

    @GetMapping("/to_detail/{goodsId}")
    public String detail(Model model, MiaoshaUser user, @PathVariable("goodsId") long goodsId) {

        //秒杀状态
        int miaoshaStatus = 0;

        //秒杀开始剩余时间
        long remainSeconds = 0;

        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goodsVo);

        long startTime = goodsVo.getStartDate().getTime();
        long endTime = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();

        if (now < startTime) {
            //秒杀未开始
            miaoshaStatus = 0;
            remainSeconds = (startTime - now) / 1000L;
        } else if (now > endTime) {
            //秒杀已结束
            miaoshaStatus = 2;
            remainSeconds = -1L;
        } else {
            //秒杀正在进行中
            miaoshaStatus = 1;
            remainSeconds = 0L;
        }

        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("user", user);
        return "goods_detail";

    }
}
