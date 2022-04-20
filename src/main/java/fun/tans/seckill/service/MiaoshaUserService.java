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

    public MiaoshaUser getById(long id) {

        //取缓存
        MiaoshaUser user = redisService.get(MiaoshaUserKey.getById, id + "", MiaoshaUser.class);
        if(user != null){
            return user;
        }
        user = miaoshaUserDao.getById(id);

        /*加载到缓存中*/
        if(user != null){
            redisService.set(MiaoshaUserKey.getById, id+"", user);
        }
        return user;
    }

    /**
     * 更新用户密码接口
     * @param id           用户id
     * @param formPassword 用户表单密码
     * @param token        用户token
     * @return 是否更改成功
     */
    public boolean updatePassword(long id, String formPassword, String token){
        MiaoshaUser user;
        if((user = miaoshaUserDao.getById(id)) == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }

        //update the data in database
        MiaoshaUser toBeUpdate = new MiaoshaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDbPass(formPassword, user.getSalt()));
        miaoshaUserDao.update(toBeUpdate);

        //remove the data in redis
        redisService.remove(MiaoshaUserKey.getById, id+"");

        //update the token in redis
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(MiaoshaUserKey.token, token, user.getPassword());
        return true;
    }

    public boolean login(LoginVo loginVo, HttpServletResponse response) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        //检验用户
        String mobile = loginVo.getMobile();
        MiaoshaUser user = getById(Long.parseLong(mobile));

        if (user == null) {
            throw new LoginException(CodeMsg.MOBILE_NOT_EXIST, loginVo);
        }

        String formPass = loginVo.getPassword();
        String dbPass = MD5Util.formPassToDbPass(formPass, user.getSalt());
        if (!dbPass.equals(user.getPassword())) {
            throw new LoginException(CodeMsg.PASSWORD_ERROR, loginVo);
        }
        String token = UUIDUtil.uuid();
        addCookie(response, user, token);
        return true;
    }

    private void addCookie(HttpServletResponse response, MiaoshaUser user, String token) {
        if (user == null || StringUtils.isEmpty(token)) return;
        //生成Cookie
        redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSecond());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 获取用户token并将其token更新
     *
     * @param response
     * @param token
     * @return
     */
    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);

        //更新token有效期
        addCookie(response, user, token);
        return user;
    }
}
