package com.cskaoyan.wordmemorize.auth;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    // 注册 Sa-Token 的拦截器

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册路由拦截器，自定义认证规则
        registry.addInterceptor(new SaInterceptor(handler -> {
            // 后台用户登录校验 -- 拦截所有路由，并排除/user/login 用于开放登录
            SaRouter.match("/admin/**")
                    .notMatch("/admin/login")
                    .check(r -> StpKit.ADMIN.checkLogin());

            // 普通用户登录校验 -- -- 拦截所有路由，并排除/user/login/phone 用于开放登录
            SaRouter
                    .match("/product", "/user/**")
                    .notMatch("/user/login/phone", "/user/pay/notice")
                    .check(r -> StpKit.USER.checkLogin());

        })).addPathPatterns("/**").excludePathPatterns( "/doc.html",
                "/webjars/**",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/favicon.ico");
    }
}

