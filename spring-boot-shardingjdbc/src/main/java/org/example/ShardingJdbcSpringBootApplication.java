package org.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * ${NAME}
 *
 * @author <a href="mailto:natimercy93@gmail.com">natimercy</a>
 * @version 1.0.0
 */
@SpringBootApplication
@MapperScan("org.example.**.mapper")
@EnableScheduling
public class ShardingJdbcSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShardingJdbcSpringBootApplication.class, args);
    }

}
