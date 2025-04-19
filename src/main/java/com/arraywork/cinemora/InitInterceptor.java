package com.arraywork.cinemora;

import java.io.IOException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.arraywork.cinemora.entity.Settings;
import com.arraywork.cinemora.service.SettingService;

/**
 * 初始化拦截器
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/04/23
 */
@Component
public class InitInterceptor implements HandlerInterceptor, WebMvcConfigurer {

    @Resource
    private SettingService settingService;
    private String initUrl = "/~/init";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws IOException {

        if (!isRestErrorRequest(request)) {
            Settings settings = settingService.getSettings();
            if (settings == null) {
                response.sendRedirect(initUrl);
                return false;
            }
        }
        return true;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this).addPathPatterns("/**")
            .excludePathPatterns(initUrl, "/~/settings", "/assets/**");
    }

    private boolean isRestErrorRequest(HttpServletRequest request) {
        return "POST".equals(request.getMethod()) && "/error".equals(request.getRequestURI());
    }

}