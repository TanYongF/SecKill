package fun.tans.seckill.service;

import com.alibaba.druid.util.StringUtils;
import fun.tans.seckill.dao.MiaoshaUserDao;
import fun.tans.seckill.domain.MiaoshaUser;
import fun.tans.seckill.exception.GlobalException;
import fun.tans.seckill.exception.LoginException;
import fun.tans.seckill.redis.MiaoshaUserKey;
import fun.tans.seckill.redis.RedisService;
import fun.tans.seckill.result.CodeMsg;
import fun.tans.seckill.util.MD5Util;
import fun.tans.seckill.util.UUIDUtil;
import fun.tans.seckill.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @Describe: 类描述
 * @Author: tyf
 * @CreateTime: 2022/4/16
 **/

@Service
public class MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    private MiaoshaUserDao miaoshaUserDao;

    @Autowired
    private RedisService redisService;

    public MiaoshaUser getById(long id){
        return miaoshaUserDao.getById(id);
    }

    public boolean login (LoginVo loginVo, HttpServletResponse response){
        if(loginVo == null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        //检验用户
        String mobile = loginVo.getMobile();
        MiaoshaUser user = getById(Long.parseLong(mobile));

        if(user==null){
            throw new LoginException(CodeMsg.MOBILE_NOT_EXIST, loginVo);
        }

        String formPass = loginVo.getPassword();
        String dbPass   = MD5Util.formPassToDbPass(formPass, user.getSalt());
        if(!dbPass.equals(user.getPassword())){
           throw new LoginException(CodeMsg.PASSWORD_ERROR, loginVo);
        }
        String token = UUIDUtil.uuid();
        addCookie(response, user, token);
        return true;
    }

    private void addCookie(HttpServletResponse response, MiaoshaUser user, String token){
        if(user == null || StringUtils.isEmpty(token)) return;
        //生成Cookie
        redisService.set(MiaoshaUserKey.token,token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSecond());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if(StringUtils.isEmpty(token)){
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);

        //延长有效期
        addCookie(response, user, token);
        return user;
    }
}
