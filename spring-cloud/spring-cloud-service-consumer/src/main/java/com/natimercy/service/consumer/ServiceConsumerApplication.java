package com.natimercy.service.consumer;

import com.natimercy.service.consumer.entity.User;
import com.natimercy.service.consumer.feign.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * ServiceConsumerApplication
 *
 * @author <a href="mailto:mercyblitz@gmail.com">natimercy</a>
 * @since 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@RestController
@RequestMapping
public class ServiceConsumerApplication {

    private final UserService userService;

    public ServiceConsumerApplication(UserService userService) {
        this.userService = userService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ServiceConsumerApplication.class, args);
    }

    @GetMapping("/user/getAllUsers")
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

}
