package fun.tans.seckill.dao;

import fun.tans.seckill.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {

    @Select("select * from user where id = #{id}")
    User getById(long id);

    @Insert("insert into user(id, name)values(#{id},#{name})")
    int insert(User user);
}
