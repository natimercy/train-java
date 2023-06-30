package comnatimercy.config.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@RestController
public class ConfigClientSpringBootApplication implements EnvironmentAware {

    private Environment environment;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ConfigClientSpringBootApplication.class);

        springApplication.run(args);
    }

    @RequestMapping("/home")
    public String home() {
        return "Hello World!" + environment.getProperty("name");
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
