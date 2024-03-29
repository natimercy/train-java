package org.natimercy.generator.entity;

import com.mysql.cj.MysqlType;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表信息，关联到当前字段信息
 *
 * @author natimercy
 * @date 2020-12-07
 * @version 2.0.0
 */
@Data
public class TableMetaData {

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
