package com.itswpu.huanswpu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itswpu.huanswpu.common.BaseContext;
import com.itswpu.huanswpu.common.R;
import com.itswpu.huanswpu.dto.OrderDetailDto;
import com.itswpu.huanswpu.dto.OrderReceiveDto;
import com.itswpu.huanswpu.entity.*;
import com.itswpu.huanswpu.mapper.OrdersEmployeeMapper;
import com.itswpu.huanswpu.service.AddressBookService;
import com.itswpu.huanswpu.service.OrderDetailService;
import com.itswpu.huanswpu.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
@Api("订单接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private AddressBookService addressBookService;

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
     * @param ids
     * @return
     */
    @PostMapping("/receive")
    public R<String> receive(@RequestBody Long[] ids){
        log.info("订单数据：{}",ids);
        orderService.receive(ids);
        return R.success("接单成功");
    }
    /**
     * 历史订单
     * @param
     * @return
     */
    @GetMapping("/history")
    public R<List<Orders>> getHistoryOrders(){
        Long deliveryId = BaseContext.getCurrentId();

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件
        queryWrapper.eq(Orders::getDeliveryId,deliveryId);
        //添加过滤条件
        List<Orders> list = orderService.list(queryWrapper);
        return R.success(list);
    }

    @Autowired
    private OrdersEmployeeMapper ordersEmployeeMapper;

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

            //获取商家关联菜品id列表
            LambdaQueryWrapper<OrdersEmployee> qw = new LambdaQueryWrapper<>();
            qw.eq(OrdersEmployee::getEmployeeId, BaseContext.getCurrentId());
            List<OrdersEmployee> lists = ordersEmployeeMapper.selectList(qw);


            ArrayList<Long> ids = new ArrayList<>();
            for (OrdersEmployee oe : lists) {
                ids.add(oe.getOrderId());
            }
            if(!CollectionUtils.isNotEmpty(ids)){
                log.info("数据库中未查到相关数据");
                return R.success(pageInfo.setTotal(0) );
            }

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
        return R.success("取消成功");
    }

    /**
     * 查询未接单订单
     * @return
     */
    @ApiOperation("查询未接单订单")
    @GetMapping("/receive")
    public R<List<OrderReceiveDto>> receive(){
        List<OrderReceiveDto> orderReceiveDtos = new ArrayList<>();
        //条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件
        queryWrapper.orderByDesc(Orders::getOrderTime);
        //添加过滤条件
        queryWrapper.eq(Orders::getStatus,Orders.TO_BE_CONFIRMED);

        List<Orders> list = orderService.list(queryWrapper);
        for (int i = 0; i < list.size(); i++) {
            Orders orders = list.get(i);
            OrderReceiveDto orderReceiveDto = OrderReceiveDto.builder()
                    .userAddress(addressBookService.getById(orders.getAddressBookId()).getDetail())
                    .employeeAddress(orders.getAddress())
                    .amount(orders.getAmount())
                    .userName(orders.getUserName())
                    .orderTime(orders.getOrderTime())
                    .orderId(orders.getId())
                    .build();
            BeanUtils.copyProperties(list.get(i),orderReceiveDto);
            orderReceiveDtos.add(orderReceiveDto);
        }
        return R.success(orderReceiveDtos);
    }

    /**
     *接单详细
     * @param id
     * @return
     */
    @ApiOperation("接单详细")
    @GetMapping("/receive/{id}")
    public R<OrderDetailDto> receiveConfirm(@PathVariable Long id){
        OrderDetailDto orderDetailDto = new OrderDetailDto();
        //条件构造器
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.eq(OrderDetail::getOrderId,id);

        List<OrderDetail> list = orderDetailService.list(queryWrapper);
        orderDetailDto.setPhone(orderService.getById(id).getPhone());
        orderDetailDto.setOrderDetails(list);
        return R.success(orderDetailDto);
    }

    /**
     * 历史订单
     */

}