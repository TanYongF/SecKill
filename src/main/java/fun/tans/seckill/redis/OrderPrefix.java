package fun.tans.seckill.redis;

/**
 * @Describe: 类描述
 * @Author: tyf
 * @CreateTime: 2022/4/16
 **/
public class OrderPrefix extends BasePrefix {

    public OrderPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
}
