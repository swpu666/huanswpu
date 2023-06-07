package com.itswpu.huanswpu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itswpu.huanswpu.entity.Employee;
import com.itswpu.huanswpu.mapper.EmployeeMapper;
import com.itswpu.huanswpu.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService {
}
