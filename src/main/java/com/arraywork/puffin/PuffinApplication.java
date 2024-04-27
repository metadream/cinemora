package com.arraywork.puffin;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import com.arraywork.springforce.BaseApplication;

/**
 * 应用启动类
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/21
 */
@SpringBootApplication
@EnableCaching
public class PuffinApplication extends BaseApplication {

    // 初始化（静态块最先执行）
    static {
        File dataFolder = new File("data");
        if (!dataFolder.exists()) dataFolder.mkdirs();
    }

    // 启动应用程序
    public static void main(String[] args) {
        SpringApplication.run(PuffinApplication.class, args);
    }

}