package com.itswpu.huanswpu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itswpu.huanswpu.entity.AddressBook;
import com.itswpu.huanswpu.entity.CategoryEmployee;
import com.itswpu.huanswpu.mapper.AddressBookMapper;
import com.itswpu.huanswpu.mapper.CategoryEmployeeMapper;
import com.itswpu.huanswpu.service.AddressBookService;
import com.itswpu.huanswpu.service.CategoryEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryEmployeeServiceImpl extends ServiceImpl<CategoryEmployeeMapper, CategoryEmployee> implements CategoryEmployeeService {
    @Autowired
    private CategoryEmployeeMapper categoryEmployeeMapper;
}
