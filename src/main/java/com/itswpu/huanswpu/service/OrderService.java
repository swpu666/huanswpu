package com.itswpu.huanswpu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itswpu.huanswpu.entity.OrderDetail;
import com.itswpu.huanswpu.entity.Orders;

import java.util.List;

public interface OrderService extends IService<Orders> {

    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);

    /**
     * 骑手接单
     * @param ids
     */
    public void receive(Long[] ids);


    void delete(Orders orders);

    /**
     * 根据订单id来查询订单明细的数据的方法
     * @param orderId
     * @return
     */
    public List<OrderDetail> getOrderDetailListByOrderId(Long orderId);

}
