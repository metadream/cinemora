package com.arraywork.cinemora;

import java.io.File;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 初始化程序
 *
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/27
 */
@Component
public class ContextInitializer implements ServletContextListener {

    @Value("${app.covers}")
    private String coverDir;

    // 创建应用所需目录
    // 此方法晚于静态代码块（无法获取配置）、早于@PostConstruct执行且可获取配置
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        File dataFolder = new File(coverDir);
        if (!dataFolder.exists()) dataFolder.mkdirs();
    }

}