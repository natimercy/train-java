package org.example.algorithm;

import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.example.entity.CreateTableSql;
import org.example.mapper.CommonMapper;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

/**
 * ShardingAlgorithmTool
 *
 * @author <a href="mailto:natimercy93@gmail.com">natimercy</a>
 * @version 1.0.0
 */
public class ShardingAlgorithmTool<T extends Comparable<?>> implements StandardShardingAlgorithm<T> {

    private static CommonMapper commonMapper;

    private static final HashSet<String> tableNameCache = new HashSet<>();

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<T> shardingValue) {
        return null;
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<T> shardingValue) {
        return null;
    }

    @Override
    public Properties getProps() {
        return null;
    }

    @Override
    public void init(Properties props) {

    }

    public String shardingTablesCheckAndCreatAndReturn(String logicTableName, String resultTableName) {

        synchronized (logicTableName.intern()) {
            // 缓存中有此表 返回
            if (shardingTablesExistsCheck(resultTableName)) {
                return resultTableName;
            }

            // 缓存中无此表 建表 并添加缓存
            CreateTableSql createTableSql = commonMapper.selectTableCreateSql(logicTableName);
            String sql = createTableSql.getCreateTable();
            sql = sql.replace("CREATE TABLE", "CREATE TABLE IF NOT EXISTS");
            sql = sql.replace(logicTableName, resultTableName);
            commonMapper.executeSql(sql);
            tableNameCache.add(resultTableName);
        }

        return resultTableName;
    }

    /**
     * 判断表是否存在于缓存中
     *
     * @param resultTableName 表名
     * @return 是否存在于缓存中
     */
    public boolean shardingTablesExistsCheck(String resultTableName) {
        return tableNameCache.contains(resultTableName);
    }

    /**
     * 缓存重载方法
     *
     * @param schemaName 待加载表名所属数据库名
     */
    public static void tableNameCacheReload(String schemaName) {
        // 读取数据库中所有表名
        List<String> tableNameList = commonMapper.getAllTableNameBySchema(schemaName);
        // 删除旧的缓存(如果存在)
        ShardingAlgorithmTool.tableNameCache.clear();
        // 写入新的缓存
        ShardingAlgorithmTool.tableNameCache.addAll(tableNameList);
    }
}
