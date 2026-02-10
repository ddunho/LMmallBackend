package com.lfmall.backend.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")          // 모든 경로
                .allowedOrigins("*")        // 모든 Origin 허용
                .allowedMethods("*")        // GET, POST, PUT, DELETE, OPTIONS 전부
                .allowedHeaders("*");       // 모든 헤더 허용
    }
}
