package fun.tans.seckill.controller;

import com.alibaba.druid.util.StringUtils;
import fun.tans.seckill.domain.MiaoshaUser;
import fun.tans.seckill.redis.GoodsKey;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;


    @GetMapping(value = "/to_list")
    @ResponseBody
    public String list(Model model, MiaoshaUser user, HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("user", user);
        String html = "";

        //从缓存中取,如果存在那么返回
        html = redisService.get(GoodsKey.getGoodList, "", String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }

        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsVos);
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        //如果缓存中未命中
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", context);
        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodList, "", html);
        }
        return html;
    }

    @GetMapping(value = "/to_detail/{goodsId}")
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
