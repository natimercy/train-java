package org.example.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.entity.CreateTableSql;

import java.util.List;

/**
 * CommonMapper
 *
 * @author <a href="mailto:natimercy93@gmail.com">natimercy</a>
 * @version 1.0.0
 */
public interface CommonMapper {

    /**
     * 查询数据库中的所有表名
     *
     * @param schema 数据库名
     * @return 表名列表
     */
    List<String> getAllTableNameBySchema(@Param("schema") String schema);

    /**
     * 查询建表语句
     *
     * @param tableName 表名
     * @return 建表语句
     */
    CreateTableSql selectTableCreateSql(@Param("tableName") String tableName);

    /**
     * 执行SQL
     *
     * @param sql 待执行SQL
     */
    void executeSql(@Param("sql") String sql);

}
