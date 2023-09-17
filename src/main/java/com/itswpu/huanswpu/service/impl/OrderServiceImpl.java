package com.itswpu.huanswpu.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itswpu.huanswpu.common.BaseContext;
import com.itswpu.huanswpu.common.CustomException;

import com.itswpu.huanswpu.entity.*;
import com.itswpu.huanswpu.mapper.DishEmployeeMapper;
import com.itswpu.huanswpu.mapper.OrderMapper;
import com.itswpu.huanswpu.mapper.OrdersEmployeeMapper;
import com.itswpu.huanswpu.service.*;
import com.itswpu.huanswpu.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    private OrdersEmployeeMapper ordersEmployeeMapper;

    @Autowired
    private DishEmployeeMapper dishEmployeeMapper;
    /**
     * 用户下单 不用购物车数据是因为 直接从userid查shopcart表的数据
     * @param orders
     */
    @Transactional
    public void submit(Orders orders) {
        //获得当前用户id
        Long userId = BaseContext.getCurrentId();

        //查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(wrapper);

        if(shoppingCarts == null || shoppingCarts.size() == 0){
            throw new CustomException("购物车为空，不能下单");
        }

        //查询用户数据
        User user = userService.getById(userId);

        //查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if(addressBook == null){
            throw new CustomException("用户地址信息有误，不能下单");
        }

        long orderId = IdWorker.getId();//自动生成订单号

        //计算总金额
        AtomicInteger amount = new AtomicInteger(0);

        //Stream流 遍历购物车
        List<OrderDetail> orderDetails = shoppingCarts.
                stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            //累加金额
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        //set  订单数据
        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());//结账时间
        orders.setStatus(2);//派送状态
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(userId);
        orders.setUserId(null);//刚下单，无人接单
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());//收货人
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        //向订单表插入数据，一条数据
        this.save(orders);

            //向关联表添加数据
            {
            OrdersEmployee oe = new OrdersEmployee();
            oe.setOrderId(orderId);
            LambdaQueryWrapper<DishEmployee> qw = new LambdaQueryWrapper<>();
            qw.eq(DishEmployee::getDishId,
                    shoppingCarts.get(0).getDishId());
            DishEmployee de = dishEmployeeMapper.selectOne(qw);
            oe.setEmployeeId(de.getEmployeeId());
            ordersEmployeeMapper.insert(oe);
            log.info("下单添加订单 商家关联信息" + oe.toString());
            }

        //向订单明细表插入数据，多条数据
        orderDetailService.saveBatch(orderDetails);

        //通过websocket向客户端浏览器推送消息 type orderId content
        Map map = new HashMap();
        map.put("type",1); // 1表示来单提醒 2表示客户催单
        map.put("orderId",orders.getId());
        map.put("content","订单号：" + orderId);

        String json = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(json);
        log.info("向客户端浏览器推送消息"+json);
        //清空购物车数据
        shoppingCartService.remove(wrapper);
    }

    /**
     * 骑手接单
     * @param orders
     */
    @Transactional
    public void receive(Orders orders) {
        //获得当前用户id
        Long deliveryId = BaseContext.getCurrentId();

        //查询用户数据
        User user = userService.getById(deliveryId);

        //查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if(addressBook == null){
            throw new CustomException("用户地址信息有误，不能接单");
        }

        //set  订单数据
        orders.setStatus(3);//已派送状态
        orders.setUserId(deliveryId);//接单
        //向订单表更新数据
        this.updateById(orders);

        log.info("骑手"+deliveryId+"接单"+orders);
    }

    @Override
    public void delete(Orders orders) {
        //获得当前用户id
        Long deliveryId = BaseContext.getCurrentId();

        if(orders.getDeliveryId()==null){
            //set  订单数据
            orders.setStatus(5);//取消状态

            //向订单表更新数据
            this.updateById(orders);

            log.info("取消订单编号:"+deliveryId);
        }
        else{
            throw new CustomException("骑手已接单，无法取消订单");
        }

    }


}