package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器 检查用户是否已经完成登录
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
//要过滤的东西（过滤器名字，拦截哪些路径）
@Slf4j
public class LoginCheckFilter implements Filter{
    //AntPathMatcher路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;//两个转型
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //1、获取本次请求的URI
        String requestURI = request.getRequestURI();// /backend/index.html
        log.info("拦截到请求：{}",requestURI);//{}表示占位符 代表,后面的东西

        String[] urls = new String[]{ //定义不需要处理的请求路径
                "/employee/login",//登录请求路径
                "/employee/logout",//员工退出系统
                "/backend/**",//后端页面静态资源
                "/front/**",//移动端页面静态资源
                "/common/**",//上传文件等的控制层资源
                "/user/sendMsg", //移动端发送短信
                "/user/login"  //移动端登录
        };
        //2、判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

        //3、如果不需要处理，则直接放行
        if(check){
            log.info("本次请求{}不需要处理,直接放行",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //4-1、需要处理，判断 员工 登录状态，如果已登录，则直接放行filterChain.doFilter
        if(request.getSession().getAttribute("employee") != null){
            log.info("员工已登录，员工id为：{}",request.getSession().getAttribute("employee"));
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }

        //4-2、判断 用户 登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("user") != null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("user"));
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);//获得ID给元数据处理器用
            filterChain.doFilter(request,response);
            return;
        }

        log.info("用户未登录");
        //5、如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        //把R转成JSON 输出’NOTLOGIN‘和request.js里面响应拦截器的条件匹配
        return;
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     */
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
