package com.markly;

import com.markly.datasource.DynamicDataSourceConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@MapperScan({"com.markly.mapper"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Import({DynamicDataSourceConfig.class})
@RestController
public class ThinkInSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ThinkInSpringBootApplication.class);
        // springApplication.setBannerMode(Banner.Mode.OFF);
        ConfigurableApplicationContext applicationContext = springApplication.run(args);
        Arrays.asList(applicationContext.getBeanDefinitionNames()).forEach(System.out::println);
    }

    @GetMapping("hello/world")
    public String helloWorld() {
        return "hello world";
    }

    @GetMapping("/echo/{message}")
    public String echo(@PathVariable("message") @DefaultValue("test") String message) {
        return "message：" + message;
    }

    @Bean
    @EventListener(ApplicationListener.class)
    public String message() {
        System.out.println("11111111");
        return "this is message";
    }

}
