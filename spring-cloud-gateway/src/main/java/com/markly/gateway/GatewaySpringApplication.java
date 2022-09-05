package com.markly.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@RestController
public class GatewaySpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewaySpringApplication.class);
    }


    @GetMapping
    public String message(String message) {
        return "this is message : {} " + message;
    }

}
