package com.itswpu.huanswpu.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置MP的分页插件
 */
@Configuration//配置类注释
public class MybatisPlusConfig {
    @Bean//表示让Spring管理这个类
    public MybatisPlusInterceptor mybatisPlusInterceptor(){//拦截器MybatisPlusInterceptor
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        //add一个分页拦截器
        return mybatisPlusInterceptor;
    }
}
