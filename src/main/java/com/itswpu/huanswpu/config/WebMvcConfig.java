package com.itswpu.huanswpu.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.itswpu.huanswpu.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@Slf4j
@Configuration//说明是个配置类
@EnableSwagger2//开启Swagger功能
@EnableKnife4j
public class WebMvcConfig extends WebMvcConfigurationSupport {

    /**
     * 设置静态资源放行映射
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射...");//前后台静态资源放行
        // 框架自动帮忙生成这doc.html 页面用于展示我们的接口信息，我们需要将他们放行
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");


        //registry.addResourceHandle("想放行的静态资源").addResourceLocations("要映射到的位置(即静态资源到这个位置)");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        //classpath 代表resources
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
        registry.addResourceHandler("/delivery/**").addResourceLocations("classpath:/delivery/");
    }
    /**
     * 扩展mvc框架的消息转换器
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器...");
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用Jackson将Java对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器对象追加到mvc框架的转换器集合中
        converters.add(0,messageConverter);//0代表使用转换器的顺序
    }

    @Bean
    public Docket createRestApi() {
        // Swagger定义的文档类型
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.itswpu.huanswpu.controller"))
                //让框架扫描到Controller 生成接口文档
                .paths(PathSelectors.any())
                .build();
    }

    //封装 apiInfo
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("环西柚")
                .version("1.0")
                .description("环西柚接口文档")
                .build();
    }
}
