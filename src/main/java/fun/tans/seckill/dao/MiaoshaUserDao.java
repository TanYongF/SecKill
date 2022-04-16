package fun.tans.seckill.dao;

import fun.tans.seckill.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Describe: 类描述
 * @Author: tyf
 * @CreateTime: 2022/4/16
 **/
@Mapper
public interface MiaoshaUserDao {

    @Select("select * from miaosha_user user where id = #{id}")
    public MiaoshaUser getById(long id);
}
