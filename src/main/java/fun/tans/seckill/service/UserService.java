package fun.tans.seckill.service;

import fun.tans.seckill.dao.UserDao;
import fun.tans.seckill.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Describe: 类描述
 * @Author: tyf
 * @CreateTime: 2022/4/15
 **/
@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public User getById(long id) {
        return userDao.getById(id);
    }

    public int insert(User user) {
        return userDao.insert(user);
    }

}
