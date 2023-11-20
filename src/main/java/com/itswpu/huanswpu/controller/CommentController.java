package com.itswpu.huanswpu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itswpu.huanswpu.common.BaseContext;
import com.itswpu.huanswpu.common.R;
import com.itswpu.huanswpu.dto.DishDto;
import com.itswpu.huanswpu.entity.*;
import com.itswpu.huanswpu.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
        log.info("新增comment:{}",comment);
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
