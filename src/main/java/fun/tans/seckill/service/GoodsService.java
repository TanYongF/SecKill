package fun.tans.seckill.service;

import fun.tans.seckill.dao.GoodsDao;
import fun.tans.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Describe: 类描述
 * @Author: tyf
 * @CreateTime: 2022/4/17
 **/
@Service
public class GoodsService {
    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo() {
        return goodsDao.getGoodsVoList();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoListById(goodsId);
    }


    public boolean reduceStock(GoodsVo goods) {
        int ret = goodsDao.reduceStockByGoodsId(goods.getId());
        return ret >= 0;
    }
}
