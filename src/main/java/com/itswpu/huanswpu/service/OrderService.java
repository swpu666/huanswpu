package com.itswpu.huanswpu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itswpu.huanswpu.entity.Orders;

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
}
