package com.innoventes.test.app.config;

import com.innoventes.test.app.util.PostMethodInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final PostMethodInterceptor postMethodInterceptor;

    @Autowired
    public WebMvcConfig(PostMethodInterceptor postMethodInterceptor) {
        this.postMethodInterceptor = postMethodInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Register the interceptor only for POST methods
        registry.addInterceptor(postMethodInterceptor).addPathPatterns("/**").excludePathPatterns("/static/**");
    }
}
