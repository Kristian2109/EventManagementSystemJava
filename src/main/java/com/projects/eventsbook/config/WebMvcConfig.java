package com.projects.eventsbook.config;

import com.projects.eventsbook.middleware.LoggedUserResolver;
import com.projects.eventsbook.middleware.SessionInterceptor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final SessionInterceptor sessionInterceptor;
    private final LoggedUserResolver loggedUserResolver;

    @Autowired
    public WebMvcConfig(SessionInterceptor sessionInterceptor, LoggedUserResolver loggedUserResolver) {
        this.sessionInterceptor = sessionInterceptor;
        this.loggedUserResolver = loggedUserResolver;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns("/groups/**")
                .addPathPatterns("/profile/**")
                .addPathPatterns("/card/**")
                .addPathPatterns("/events/**");
    }

    @Override
    public void addArgumentResolvers(@NonNull List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loggedUserResolver);
    }

}
