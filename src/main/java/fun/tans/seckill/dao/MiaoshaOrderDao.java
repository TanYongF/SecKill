package fun.tans.seckill.dao;

import fun.tans.seckill.domain.MiaoshaOrder;
import fun.tans.seckill.domain.OrderInfo;
import org.apache.ibatis.annotations.*;

/**
 * @Describe: 订单持久层
 * @Author: tyf
 * @CreateTime: 2022/4/18
 **/
@Mapper
public interface MiaoshaOrderDao {

    @Select("select * from miaosha_order mo where mo.user_id = #{userId} and mo.goods_id = #{goodsId}")
    MiaoshaOrder getMiaoshaOrderByGoodsIdUserId(@Param("goodsId") long goodsId, @Param("userId") long userId);

    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values("
            + "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
    @SelectKey(keyColumn="id", keyProperty="id", resultType= Long.class, before= false, statement="SELECT " +
            "LAST_INSERT_ID" +
            "() AS id")
    public void insert(OrderInfo orderInfo);

    @Insert("insert into miaosha_order (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
    public int insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);

}
