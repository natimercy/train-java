package org.natimercy.eureka.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * EurekaSpringApplication
 *
 * @author natimercy
 * @since 1.0.0
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerSpringApplication.class, args);
    }
}