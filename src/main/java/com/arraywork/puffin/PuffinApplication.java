package com.arraywork.puffin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

import com.arraywork.springforce.BaseApplication;

/**
 * 应用启动类
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/21
 */
@SpringBootApplication
@EnableAsync
@EnableCaching
public class PuffinApplication extends BaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(PuffinApplication.class, args);
    }

}