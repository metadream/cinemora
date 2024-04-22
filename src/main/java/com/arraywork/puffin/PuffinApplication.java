package com.arraywork.puffin;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.arraywork.springhood.BaseApplication;

/**
 * Puffin Application
 * @author AiChen
 * @created 2024/04/21
 */
@SpringBootApplication
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