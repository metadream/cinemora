package com.arraywork.puffin;

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

    public static void main(String[] args) {
        SpringApplication.run(PuffinApplication.class, args);
    }

}