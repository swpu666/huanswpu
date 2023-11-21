package com.itswpu.huanswpu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itswpu.huanswpu.common.BaseContext;
import com.itswpu.huanswpu.common.R;
import com.itswpu.huanswpu.dto.DishDto;
import com.itswpu.huanswpu.entity.*;
import com.itswpu.huanswpu.mapper.OrdersEmployeeMapper;
import com.itswpu.huanswpu.service.CommentService;
import com.itswpu.huanswpu.service.EmployeeService;
import com.itswpu.huanswpu.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 分类管理
 */
@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private OrdersEmployeeMapper oem;
    /**
     * 新增评论
     * @param comment
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Comment comment){
        //获得当前用户id
        Long userId = BaseContext.getCurrentId();

        comment.setId(userId);
        Long orderId = comment.getEmployeeId();//前端实际传递的是订单id
        if(orderId == null) {
            return R.error("订单"+orderId+"不存在");
        }
        LambdaQueryWrapper<OrdersEmployee> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.eq(orderId!=null,OrdersEmployee::getOrderId,orderId);
        OrdersEmployee ordersEmployee = oem.selectOne(queryWrapper);
        if(ordersEmployee == null) {
            return R.error("订单"+orderId+"未关联商家");
        }

        comment.setEmployeeId(ordersEmployee.getEmployeeId());
        log.info("新增comment为:{}",comment);
        if (comment == null){
            return R.error("接收评论为空:comment"+comment);
        }
        commentService.save(comment);

        return R.success("新增分类成功");
    }

    /**
     * 评论查询
     * @param employeeId
     * @return
     */
    @GetMapping("/get")
    public R<List<Comment>> page(Long employeeId){


        //构造查询条件
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(employeeId != null ,Comment::getEmployeeId,employeeId);

        //添加排序条件 排序顺序，创建时间倒序
        queryWrapper.orderByDesc(Comment::getCreateTime);
        List<Comment> commentList = commentService.list(queryWrapper);
        if(commentList == null){
            return R.error("未查询到该商家的评论");
        }

        return R.success(commentList);
    }




}
