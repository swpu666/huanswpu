package com.itswpu.huanswpu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itswpu.huanswpu.entity.Delivery;
import com.itswpu.huanswpu.entity.User;
import com.itswpu.huanswpu.mapper.DeliveryMapper;
import com.itswpu.huanswpu.mapper.UserMapper;
import com.itswpu.huanswpu.service.DeliveryService;
import com.itswpu.huanswpu.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class DeliveryServiceImpl extends ServiceImpl<DeliveryMapper, Delivery> implements DeliveryService {
}
