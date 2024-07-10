package org.example;

import org.apache.shardingsphere.driver.jdbc.core.datasource.ShardingSphereDataSource;
import org.apache.shardingsphere.driver.jdbc.core.driver.DriverDataSourceCache;
import org.example.Scheduling.InitActualDataNodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

/**
 * ${NAME}
 *
 * @author <a href="mailto:natimercy93@gmail.com">natimercy</a>
 * @version 1.0.0
 */
@SpringBootTest
@Import(InitActualDataNodes.class)
public class ShardingJdbcSpringBootApplicationTest {

    @Autowired
    private InitActualDataNodes initialDataNodes;

    private final DriverDataSourceCache dataSourceCache = new DriverDataSourceCache();

    @BeforeEach
    public void before() {
        DataSource dataSource = dataSourceCache.get("jdbc:shardingsphere:classpath:sharding.yaml");
        initialDataNodes.setDataSource((ShardingSphereDataSource) dataSource);
    }

    @Test
    public void generateActualDataNodesTest() {
        // 行表达式 至于行表达式看不懂，自己找资料 或者执行testInline 看下有哪些节点
        String inlineStr = "ds_$->{0..1}.student_$->{0..2},ds_1.student_12347,ds_0.student_147258";
        // logicTableName 逻辑表名 actualDataNodes 行表达式
        initialDataNodes.generateActualDataNodes("t_order", inlineStr);
        // 行表达式List 和上面的结果是一样的做了一层转换而已 这样写更舒服一点
        List<String> inlineList = Arrays.asList("ds_$->{0..1}.student_$->{0..2}", "ds_1.student_12347", "ds_0.student_147258");
        initialDataNodes.generateActualDataNodes("t_order", inlineList);
    }

}
