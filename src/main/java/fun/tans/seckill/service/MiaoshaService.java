package fun.tans.seckill.service;

import com.sun.org.apache.bcel.internal.classfile.Code;
import fun.tans.seckill.dao.MiaoshaUserDao;
import fun.tans.seckill.domain.MiaoshaUser;
import fun.tans.seckill.result.CodeMsg;
import fun.tans.seckill.util.MD5Util;
import fun.tans.seckill.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Describe: 类描述
 * @Author: tyf
 * @CreateTime: 2022/4/16
 **/

@Service
public class MiaoshaService {

    @Autowired
    private MiaoshaUserDao miaoshaUserDao;

    public MiaoshaUser getById(long id){
        return miaoshaUserDao.getById(id);
    }

    public CodeMsg login(LoginVo loginVo){
        if(loginVo == null){
            return CodeMsg.SERVER_ERROR;
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        MiaoshaUser user = getById(Long.parseLong(mobile));

        if(user==null){
            return CodeMsg.MOBILE_NOT_EXIST;
        }

        if(MD5Util.formPassToDbPass(formPass, user.getSalt()).equals(user.getPassword())){
            return CodeMsg.SUCCESS;
        }else{
            return CodeMsg.PASSWORD_ERROR;
        }

    }

}
