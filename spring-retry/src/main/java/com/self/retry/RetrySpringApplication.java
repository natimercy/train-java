package com.self.retry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 启动类
 *
 * @author natimercy
 * @since  2022-09-05
 * @version 1.0.0
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@RestController
@EnableRetry
public class RetrySpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(RetrySpringApplication.class);
    }

    @GetMapping
    public String message(String message) {
        return "this is message : {} " + message;
    }

}
