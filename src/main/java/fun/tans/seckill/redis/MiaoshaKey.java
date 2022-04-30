package fun.tans.seckill.redis;

/**
 * @Describe: 秒杀商品键
 * @Author: tyf
 * @CreateTime: 2022/4/30
 **/
public class MiaoshaKey extends BasePrefix {

    //用来存储售空商品键前缀
    public static MiaoshaKey isGoodsOver = new MiaoshaKey("go");

    public MiaoshaKey(String prefix) {
        super(prefix);
    }

}
