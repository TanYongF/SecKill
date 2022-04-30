package fun.tans.seckill.mq;

import fun.tans.seckill.domain.MiaoshaUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Describe: Rabbit队列存储信息
 * @Author: tyf
 * @CreateTime: 2022/4/30
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MiaoshaMessage {
    private MiaoshaUser miaoshaUser;
    private long GoodId;
}
