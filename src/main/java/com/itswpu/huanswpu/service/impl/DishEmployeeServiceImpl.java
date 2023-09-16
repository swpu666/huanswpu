package com.itswpu.huanswpu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itswpu.huanswpu.entity.CategoryEmployee;
import com.itswpu.huanswpu.entity.DishEmployee;
import com.itswpu.huanswpu.mapper.CategoryEmployeeMapper;
import com.itswpu.huanswpu.mapper.DishEmployeeMapper;
import com.itswpu.huanswpu.service.CategoryEmployeeService;
import com.itswpu.huanswpu.service.DishEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DishEmployeeServiceImpl extends ServiceImpl<DishEmployeeMapper, DishEmployee> implements DishEmployeeService {
    @Autowired
    private CategoryEmployeeMapper categoryEmployeeMapper;
}
