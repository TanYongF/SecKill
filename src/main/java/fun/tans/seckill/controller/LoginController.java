package fun.tans.seckill.controller;

import com.alibaba.druid.util.StringUtils;
import fun.tans.seckill.redis.RedisService;
import fun.tans.seckill.result.CodeMsg;
import fun.tans.seckill.result.Result;
import fun.tans.seckill.service.MiaoshaService;
import fun.tans.seckill.util.ValidatorUtil;
import fun.tans.seckill.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Describe: 登陆接口
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
    private MiaoshaService miaoshaService;


    @GetMapping("/do_login")
    public String login(){
        return "login";
    }

    @PostMapping("/do_login")
    @ResponseBody
    public Result<CodeMsg> doLogin(@Valid LoginVo loginVo){

        logger.info("【用户登陆提醒】" +loginVo.toString() + "尝试登陆....");
//        String passInput = loginVo.getPassword();
//        String mobileInput = loginVo.getMobile();

//        if(StringUtils.isEmpty(passInput)){
//            return Result.error(CodeMsg.PASSWORD_EMPTY);
//        }
//        if(StringUtils.isEmpty(mobileInput)){
//            return Result.error(CodeMsg.MOBILE_ERROR);
//        }
//
//        if(!ValidatorUtil.isMobile(mobileInput)){
//            return Result.error(CodeMsg.MOBILE_ERROR);
//        }
        CodeMsg msg = miaoshaService.login(loginVo);

        if(msg.getCode() == 0){
            logger.info(loginVo.getMobile() + "登陆成功！");
            return Result.success(CodeMsg.SUCCESS);
        }else{
            logger.error(loginVo.getMobile() +  "登陆失败！");
            return Result.error(msg);
        }
    }
}
