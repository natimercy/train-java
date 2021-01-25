package org.example.entity;

import com.mysql.cj.MysqlType;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表信息，关联到当前字段信息
 *
 * @author hq
 * @date 2020-12-07
 */
@Data
public class TableInfo {

    private String dbName;

    private String className;

    private List<String> fieldNames = new ArrayList<>();

    private List<String> columnNames = new ArrayList<>();

    private Map<String, MysqlType> fieldRelationMysqlType = new HashMap<>();

    private Map<String, String> fieldRelationColumns = new HashMap<>();

    private Map<String, Object> columnRelationValue = new HashMap<>();

    @Override
    public String toString() {
        return "EntityInfo{" +
                "className='" + className + '\'' +
                ", columnNames='" + columnNames + '\'' +
                ", fieldNames=" + fieldNames +
                ", fieldRelationMysqlType=" + fieldRelationMysqlType +
                '}';
    }
}
