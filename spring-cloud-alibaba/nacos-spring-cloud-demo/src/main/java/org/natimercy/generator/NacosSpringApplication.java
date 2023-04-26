package org.natimercy.generator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@RestController
@RefreshScope
public class NacosSpringApplication {

    public static void main(String[] args) {

        SpringApplication.run(NacosSpringApplication.class);
    }

    @Value("${project.name}")
    private String projectName;

    @Value("${project.file-extension}")
    private String fileExtension;

    @GetMapping("/getConfig")
    public Map<String, Object> getConfig() {
        Map<String, Object> map = new HashMap<>();
        map.put("projectName", projectName);
        map.put("projectFileExtension", fileExtension);
        return map;
    }

    @GetMapping("/getUser")
    public User getUser() {
        User user = new User();
        user.setAddress("113123");
        user.setAge(18);
        user.setUsername("natimercy");
        return user;
    }


    public static class User {

        private String username;

        private String address;

        @JsonIgnore
        private Integer age;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

    }

}
