package com.markly.config.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@MapperScan({"com.markly.mapper"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ConfigServerSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ConfigServerSpringBootApplication.class);
        springApplication.run(args);
    }

}
