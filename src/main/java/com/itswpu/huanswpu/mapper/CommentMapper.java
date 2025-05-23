package com.itswpu.huanswpu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itswpu.huanswpu.entity.AddressBook;
import com.itswpu.huanswpu.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}
