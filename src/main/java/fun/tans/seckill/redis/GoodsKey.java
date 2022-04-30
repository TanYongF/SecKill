package fun.tans.seckill.redis;

/**
 * @Describe: Goods key描述
 * @Author: tyf
 * @CreateTime: 2022/4/16
 **/
public class GoodsKey extends BasePrefix {

    public static final int GOODS_LIST_EXPIRE_TIME = 60;

    public static GoodsKey getGoodList = new GoodsKey(GOODS_LIST_EXPIRE_TIME, "gl");
    public static GoodsKey getGoodDetail = new GoodsKey(GOODS_LIST_EXPIRE_TIME, "gd");
    public static GoodsKey getGoodStock = new GoodsKey(0, "gs");

    public GoodsKey(int expireTime, String prefix) {
        super(expireTime, prefix);
    }


}
