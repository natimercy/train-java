package org.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

/**
 * ${NAME}
 *
 * @author <a href="mailto:natimercy93@gmail.com">natimercy</a>
 * @version 1.0.0
 */
@SpringBootTest
@PropertySource(value = {"classpath:application.yaml", "classpath:sharding-sphere.yaml"})
public class ShardingJdbcSpringBootApplicationTest {

    @Test
    public void test() {
        System.out.println("Hello world!");

    }

}
