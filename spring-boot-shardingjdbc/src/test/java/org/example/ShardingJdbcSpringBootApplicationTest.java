package org.example;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * ${NAME}
 *
 * @author <a href="mailto:natimercy93@gmail.com">natimercy</a>
 * @version 1.0.0
 */
@SpringBootTest
public class ShardingJdbcSpringBootApplicationTest {

    @Test
    public void test() throws SQLException {
        HikariDataSource source = new HikariDataSource();
        source.setJdbcUrl("jdbc:mysql://localhost:3306/shardingsphere-db0?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=UTF-8");
        source.setDriverClassName("com.mysql.cj.jdbc.Driver");
        source.setUsername("root");
        source.setPassword("123456");

        Connection connection = source.getConnection();
        System.out.println(connection);
    }

}
