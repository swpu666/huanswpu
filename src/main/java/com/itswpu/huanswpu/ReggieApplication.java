package com.itswpu.huanswpu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j //lombok提供的 输出日志注解
@SpringBootApplication//SpringBoot启动
@ServletComponentScan//扫描WebFilter注解,实现过滤器功能
@EnableTransactionManagement//开启事务注解的支持
@EnableCaching //开启 SpringCache 注解方式的缓存功能
@EnableScheduling//定时任务
//@EnableAsync//mangoDB开启异步 记录 关键词
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class,args);
        log.info("项目启动成功...");//@Slf4j的输出日志方法
    }
}
