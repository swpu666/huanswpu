package com.itswpu.huanswpu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itswpu.huanswpu.entity.ShoppingCart;
import com.itswpu.huanswpu.mapper.ShoppingCartMapper;
import com.itswpu.huanswpu.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
