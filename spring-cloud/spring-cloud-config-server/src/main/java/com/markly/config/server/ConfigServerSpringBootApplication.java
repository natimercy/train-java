package com.markly.config.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableConfigServer
@RestController
public class ConfigServerSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ConfigServerSpringBootApplication.class);
        springApplication.run(args);
    }

    @RequestMapping("/home")
    public String home() {
        return "Hello World!";
    }

}
