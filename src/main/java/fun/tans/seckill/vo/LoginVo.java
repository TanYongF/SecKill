package fun.tans.seckill.vo;

import fun.tans.seckill.validator.IsMobile;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * @Describe: 用户登陆表单视图实体
 * @Author: tyf
 * @CreateTime: 2022/4/16
 **/
@Data
@ToString
public class LoginVo {

    @NotEmpty
    @IsMobile()
    private String mobile;

    @Length(min = 32)
    @NotEmpty
    private String password;
}
