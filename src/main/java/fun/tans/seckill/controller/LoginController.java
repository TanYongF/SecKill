package fun.tans.seckill.controller;

import fun.tans.seckill.domain.MiaoshaUser;
import fun.tans.seckill.redis.RedisService;
import fun.tans.seckill.result.CodeMsg;
import fun.tans.seckill.result.Result;
import fun.tans.seckill.service.MiaoshaUserService;
import fun.tans.seckill.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * @Describe: 登陆模块Controller
 * @Author: tyf
 * @CreateTime: 2022/4/16
 **/

@Controller
@RequestMapping("/login")
public class LoginController {

    Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    private MiaoshaUserService miaoshaUserService;


    @GetMapping("/do_login")
    public String login(HttpServletResponse response, MiaoshaUser user) {
        if (user != null) {
            try {
                response.sendRedirect("/goods/to_list");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return "login";
    }

    /**
     * 登陆逻辑
     *
     * @param loginVo:用户登陆表单
     * @param response:      Response
     * @return Result数据
     */
    @PostMapping("/do_login")
    @ResponseBody
    public Result<CodeMsg> doLogin(@Valid LoginVo loginVo, HttpServletResponse response) {
        logger.info("【用户登陆提醒】" + loginVo.toString() + "尝试登陆....");
        miaoshaUserService.login(loginVo, response);
        return Result.success(CodeMsg.SUCCESS);
    }
}
