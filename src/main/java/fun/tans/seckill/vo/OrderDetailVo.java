package fun.tans.seckill.vo;

import fun.tans.seckill.domain.OrderInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Describe: 类描述
 * @Author: tyf
 * @CreateTime: 2022/4/21
 **/
@Data
@AllArgsConstructor
public class OrderDetailVo {
    private GoodsVo goodsVo;
    private OrderInfo orderInfo;
}
