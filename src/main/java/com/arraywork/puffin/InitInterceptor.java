package com.arraywork.puffin;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.arraywork.puffin.entity.Preference;
import com.arraywork.puffin.service.PreferenceService;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 初始化拦截器
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/23
 */
@Component
public class InitInterceptor implements HandlerInterceptor, WebMvcConfigurer {

    @Resource
    private PreferenceService prefsService;

    @Value("${puffin.init-url}")
    private String initUrl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws ServletException, IOException {

        if (!isRestErrorRequest(request)) {
            Preference prefs = prefsService.getPreference();
            if (prefs == null) {
                response.sendRedirect(initUrl);
                return false;
            }
        }
        return true;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this).addPathPatterns("/**")
            .excludePathPatterns(initUrl, "/preference", "/assets/**");
    }

    private boolean isRestErrorRequest(HttpServletRequest request) {
        return "POST".equals(request.getMethod()) && "/error".equals(request.getRequestURI());
    }

}