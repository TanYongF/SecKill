package fun.tans.seckill.redis;

/**
 * @Describe: 类描述
 * @Author: tyf
 * @CreateTime: 2022/4/17
 **/
public class MiaoshaUserKey extends BasePrefix {

    public static final int TOKEN_EXPIRE_TIME = 24 * 60 * 60;
    public static MiaoshaUserKey token = new MiaoshaUserKey(TOKEN_EXPIRE_TIME, "tk");
    public static MiaoshaUserKey getById = new MiaoshaUserKey(0, "id");

    public MiaoshaUserKey(int time, String prefix) {
        super(time, prefix);
    }

}
