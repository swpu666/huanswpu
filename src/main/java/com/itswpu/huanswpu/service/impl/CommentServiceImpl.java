package com.itswpu.huanswpu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itswpu.huanswpu.entity.Comment;
import com.itswpu.huanswpu.mapper.CommentMapper;
import com.itswpu.huanswpu.service.CommentService;
import org.springframework.stereotype.Service;

@Service//使Spring管理
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}
