package fun.tans.seckill.dao;

import fun.tans.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Describe: 类描述
 * @Author: tyf
 * @CreateTime: 2022/4/17
 **/
@Mapper
public interface GoodsDao {

    @Select("select g.*, g.id, mg.stock_count, mg.start_date, mg.end_date, mg.miaosha_price from miaosha_goods mg " +
            "left join " +
            "goods g " +
            "on " +
            "mg" +
            ".goods_id = g.id")
    List<GoodsVo> getGoodsVoList();


    @Select("select g.*, g.id, mg.stock_count, mg.start_date, mg.end_date, mg.miaosha_price from miaosha_goods mg " +
            "left join goods g on mg.goods_id = g.id where g.id = #{goodsId}")
    GoodsVo getGoodsVoListById(@Param("goodsId") long goodsId);

    @Update("update miaosha_goods set stock_count = stock_count-1 where goods_id = #{goodsId} and stock_count > 0")
    int reduceStockByGoodsId(@Param("goodsId") Long goodsId);
}
