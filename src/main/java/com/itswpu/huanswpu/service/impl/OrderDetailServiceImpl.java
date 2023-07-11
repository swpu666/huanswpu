package com.itswpu.huanswpu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itswpu.huanswpu.entity.OrderDetail;
import com.itswpu.huanswpu.mapper.OrderDetailMapper;
import com.itswpu.huanswpu.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}