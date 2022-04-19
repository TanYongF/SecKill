package fun.tans.seckill.redis;

/**
 * @Describe: Goods key描述
 * @Author: tyf
 * @CreateTime: 2022/4/16
 **/
public class GoodsKey extends BasePrefix {

    public static final int GOODS_LIST_EXPIRE_TIME = 60;

    public static GoodsKey getGoodList = new GoodsKey("gl");
    public static GoodsKey getGoodDetail = new GoodsKey("gd");

    public GoodsKey(String prefix) {
        super(GOODS_LIST_EXPIRE_TIME, prefix);
    }


}
