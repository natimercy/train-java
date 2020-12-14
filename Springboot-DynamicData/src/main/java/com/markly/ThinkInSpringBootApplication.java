package com.markly;

import com.markly.datasource.DynamicDataSourceConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

@MapperScan({"com.markly.mapper"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Import({DynamicDataSourceConfig.class})
public class ThinkInSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ThinkInSpringBootApplication.class);
        // springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run(args);
    }

}
