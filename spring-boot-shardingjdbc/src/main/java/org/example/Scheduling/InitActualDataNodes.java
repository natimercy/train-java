package org.example.Scheduling;

import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.driver.jdbc.core.datasource.ShardingSphereDataSource;
import org.apache.shardingsphere.infra.datanode.DataNode;
import org.apache.shardingsphere.infra.rule.ShardingSphereRule;
import org.apache.shardingsphere.infra.util.expr.InlineExpressionParser;
import org.apache.shardingsphere.mode.manager.ContextManager;
import org.apache.shardingsphere.sharding.rule.ShardingRule;
import org.apache.shardingsphere.sharding.rule.TableRule;
import org.example.util.LambadaTools;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * InitActualDataNodes
 *
 * @author <a href="mailto:natimercy93@gmail.com">natimercy</a>
 * @version 1.0.0
 */
@Component
@Slf4j
public class InitActualDataNodes {

    private ShardingSphereDataSource dataSource;

    public void setDataSource(ShardingSphereDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @SneakyThrows(ReflectiveOperationException.class)
    public static ContextManager getContextManager(ShardingSphereDataSource dataSource) {
        Field field = ShardingSphereDataSource.class.getDeclaredField("contextManager");
        field.setAccessible(true);
        return (ContextManager) field.get(dataSource);
    }

    public void generateActualDataNodes(String logicTableName, String actualDataNodes) {
        // generate actualDataNodes
        this.updateShardRuleActualDataNodes(dataSource, logicTableName, actualDataNodes);
    }

    public void generateActualDataNodes(String logicTableName, List<String> actualDataNodes) {
        // generate actualDataNodes
        String actualDataNode = String.join(",", actualDataNodes);
        this.updateShardRuleActualDataNodes(dataSource, logicTableName, actualDataNode);
    }

    public void updateShardRuleActualDataNodes(ShardingSphereDataSource dataSource, String logicTableName, String actualDataNodes) {
        // 根据inline 表达式转换DataNode节点
        List<String> newStrDataNodes = new InlineExpressionParser(actualDataNodes).splitAndEvaluate();
        //sharding数据源
        ContextManager contextManager = getContextManager(dataSource);
        // 所有的拆分表
        Collection<ShardingSphereRule> tableRules = contextManager.getMetaDataContexts()
                .getMetaData()
                .getDatabase("shardingsphere-db0")
                .getRuleMetaData()
                .getRules();
        try {
            for (ShardingSphereRule shardingSphereRule : tableRules) {
                if (shardingSphereRule instanceof ShardingRule) {
                    ShardingRule rule = (ShardingRule) shardingSphereRule;
                    TableRule tableRule = rule.getTableRule(logicTableName);
                    // 动态刷新 actualDataNodes
                    List<DataNode> newDataNodes = setActualDataNodes(newStrDataNodes, tableRule);

                    // 动态刷新 actualTables
                    setActualTables(newDataNodes, tableRule);

                    // 动态刷新 dataNodeIndexMap
                    setDataNodeIndexMap(newDataNodes, tableRule);

                    // 动态刷新 datasourceToTablesMap
                    setDataSourceToTablesMap(newDataNodes, tableRule);
                }
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            log.error("刷新节点报错了", e);
        }
    }

    private static List<DataNode> setActualDataNodes(List<String> newStrDataNodes, TableRule tableRule) throws NoSuchFieldException, IllegalAccessException {
        Field actualDataNodesField = TableRule.class.getDeclaredField("actualDataNodes");
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        // 设置修饰符：private
        modifiersField.setInt(actualDataNodesField, actualDataNodesField.getModifiers() & ~Modifier.FINAL);
        //  新节点 循环动态拼接所有节点表名
        List<DataNode> newDataNodes = newStrDataNodes.stream().map(DataNode::new).collect(Collectors.toList());
        actualDataNodesField.setAccessible(true);
        // 数据更新回去
        actualDataNodesField.set(tableRule, newDataNodes);
        return newDataNodes;
    }

    private static void setActualTables(List<DataNode> newDataNodes, TableRule tableRule) throws NoSuchFieldException, IllegalAccessException {
        Set<String> actualTables = newDataNodes.stream()
                .map(DataNode::getTableName)
                .collect(Collectors.toCollection(() -> new TreeSet<>(String.CASE_INSENSITIVE_ORDER)));
        // 动态刷新：actualTables
        Field actualTablesField = TableRule.class.getDeclaredField("actualTables");
        actualTablesField.setAccessible(true);
        actualTablesField.set(tableRule, actualTables);
    }

    private static void setDataNodeIndexMap(List<DataNode> newDataNodes, TableRule tableRule) throws NoSuchFieldException, IllegalAccessException {
        // dataNodeIntegerMap
        Map<DataNode, Integer> dataNodeIntegerMap = Maps.newHashMap();
        newDataNodes.forEach(LambadaTools.forEachWithIndex(dataNodeIntegerMap::put));
        Field dataNodeIndexMapField = TableRule.class.getDeclaredField("dataNodeIndexMap");
        dataNodeIndexMapField.setAccessible(true);
        dataNodeIndexMapField.set(tableRule, dataNodeIntegerMap);
    }

    private static void setDataSourceToTablesMap(List<DataNode> newDataNodes, TableRule tableRule) throws NoSuchFieldException, IllegalAccessException {
        Map<String, Collection<String>> datasourceToTablesMap = Maps.newHashMap();
        // 不同数据源，表不一样
        Map<String, List<DataNode>> dataSourceNodes = newDataNodes.stream().collect(Collectors.groupingBy(DataNode::getDataSourceName));
        dataSourceNodes.forEach((ds, node) -> {
            datasourceToTablesMap.put(ds, node.stream().map(DataNode::getTableName).collect(Collectors.toSet()));
        });

        Field datasourceToTablesMapField = TableRule.class.getDeclaredField("dataSourceToTablesMap");
        datasourceToTablesMapField.setAccessible(true);
        datasourceToTablesMapField.set(tableRule, datasourceToTablesMap);
    }

}
