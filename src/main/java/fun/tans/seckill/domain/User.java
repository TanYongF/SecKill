package fun.tans.seckill.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Describe: 类描述
 * @Author: tyf
 * @CreateTime: 2022/4/15
 **/
@Data
@AllArgsConstructor
public class User {

    private long id;
    public String name;
}
