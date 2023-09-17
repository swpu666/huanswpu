package com.itswpu.huanswpu.dto;

import com.itswpu.huanswpu.entity.Employee;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("用户浏览商家Dto")
public class BrowseDto {
    private static final long serialVersionUID = 1L;

    //商家头像
    private String avatar;

    //商家名称
    private String name;


}
