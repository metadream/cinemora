package com.arraywork.cinemora;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import com.arraywork.autumn.BaseApplication;
import com.arraywork.autumn.helper.DirectoryMonitor;
import com.arraywork.cinemora.service.LibraryListener;

/**
 * 应用启动类
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/04/21
 */
@SpringBootApplication
@EnableAsync
@EnableCaching
public class BootApplication extends BaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }

    @Bean
    public DirectoryMonitor libraryMonitor(LibraryListener listener) {
        return new DirectoryMonitor(5000, listener);
    }

}