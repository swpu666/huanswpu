package com.itswpu.huanswpu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itswpu.huanswpu.common.BaseContext;
import com.itswpu.huanswpu.common.R;
import com.itswpu.huanswpu.entity.Orders;
import com.itswpu.huanswpu.service.OrderDetailService;
import com.itswpu.huanswpu.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户下单
     * @param orders 不用购物车数据是因为 直接从userid查shopcart表的数据
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }
    /**
     * 骑手接单
     * @param orders
     * @return
     */
    @PostMapping("/receive")
    public R<String> receive(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.receive(orders);
        return R.success("接单成功");
    }

    /**
     * 订单信息分页查询
     * @param page=1&pageSize=10&number=1664134&beginTime=2023-06-06 00:00:00&endTime=2023-07-12 23:59:59
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String number, String beginTime,String endTime){

//        log.info("page数据 {}，{}",page,pageSize);
        // 1.具有转换功能的对象
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

//3.LocalDate发动，将字符串转换成 df格式的LocalDateTime对象，的功能
//        LocalDateTime bTime = LocalDateTime.parse(beginTime,df);
//        LocalDateTime eTime = LocalDateTime.parse(beginTime,df);
//        System.out.println("String类型的时间转成LocalDateTime："+bTime);
        //构造分页构造器对象
        Page<Orders> pageInfo = new Page<>(page,pageSize);

        //条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件
        queryWrapper.orderByDesc(Orders::getOrderTime);
        //添加过滤条件
        queryWrapper.like(number != null,Orders::getNumber,number);
//        queryWrapper.gt(beginTime != null,Orders::getOrderTime,LocalDateTime.parse(beginTime,df));
//        queryWrapper.lt(endTime != null,Orders::getOrderTime,LocalDateTime.parse(endTime,df));
        queryWrapper.gt(beginTime != null,Orders::getOrderTime,beginTime);
        queryWrapper.lt(endTime != null,Orders::getOrderTime,endTime);

        //执行分页查询
        orderService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 订单信息分页查询
     * @param ://localhost:8080/order/userPage?page=1&pageSize=5
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> userPage(int page,int pageSize){

//        log.info("page数据 {}，{}",page,pageSize);
        //设置用户id，指定当前是哪个用户的订单数据
        Long currentId = BaseContext.getCurrentId();
        //构造分页构造器对象
        Page<Orders> pageInfo = new Page<>(page,pageSize);

        //条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件
        queryWrapper.orderByDesc(Orders::getOrderTime);
        //添加过滤条件
        queryWrapper.eq(Orders::getUserId,currentId);

        //执行分页查询
        orderService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }
    @PutMapping("/cancel")
    public R<String> cancel(@RequestBody Orders orders){
        orderService.delete(orders);
        log.info("订单数据：{}",orders);
        return R.success("删除成功");
    }


}