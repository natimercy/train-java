package org.natimercy.generator.platform;

import com.mysql.cj.MysqlType;
import org.apache.commons.lang3.StringUtils;
import org.natimercy.generator.enums.JavaType;
import org.natimercy.generator.util.DBManager;
import org.natimercy.generator.entity.ProcedureParameter;
import org.natimercy.generator.entity.TableMetaData;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

/**
 * （达梦）DMExecutor
 *
 * @author qian.he
 * @since 2023-04-25
 * @version 1.0.0
 */
public class DMExecutor {

    private final String procedureInformationSQL =
            "select a.NAME                                              as parameterName, " +
                    " (case a.INFO1 when 0 then 'IN' when 1 then 'OUT' " +
                    "               when 2 then 'INOUT' else 'IN' end) as parameterMode, " +
                    "       a.TYPE$                                    as dataType " +
                    "from syscolumns a " +
                    "         inner join dba_objects b on a.id = b.object_id " +
                    "where OWNER = '%s' " +
                    "  and OBJECT_NAME = '%s' " +
                    "order by a.COLID asc;";

    private final List<String> classes = new ArrayList<>();

    private final int defaultValue = 1;

    private final String mode = "IN";

    private DBManager dbManager;

    public DMExecutor(Properties properties) {
        classes.add(Integer.class.getName());
        classes.add(int.class.getName());
        classes.add(Long.class.getName());
        classes.add(long.class.getName());

        classes.add(Float.class.getName());
        classes.add(float.class.getName());
        classes.add(Double.class.getName());
        classes.add(double.class.getName());

        this.dbManager = new DBManager(properties);
    }

    /**
     * 获取表信息
     *
     * @param procedure 存储过程名称
     * @return EntityInfo 集合
     */
    public List<TableMetaData> getEntityList(String procedure) {
        List<TableMetaData> entityList = new ArrayList<>();

        List<ProcedureParameter> parameters = getProcedureParameters(procedure);

        CallableStatement cs;
        String sql = builderSql(procedure, parameters);
        System.out.println("sql: " + sql);
        try {
            cs = dbManager.getConnection().prepareCall(sql);
            cs.execute();

            boolean next = true;
            do {
                if (next) {
                    ResultSet resultSet = cs.getResultSet();
                    while (resultSet.next()) {
                        TableMetaData entityInfo = new TableMetaData();
                        ResultSetMetaData metadata = resultSet.getMetaData();
                        int columnCount = metadata.getColumnCount();
                        if (columnCount <= 0) {
                            break;
                        }

                        for (int i = 1; i <= columnCount; i++) {
                            String columnName = metadata.getColumnName(i);
                            entityInfo.setClassName(tableNameConverterClassName(columnName));
                            entityInfo.getColumnNames().add(columnName.toLowerCase());
                            String javaField = columnConverterField(columnName.toLowerCase());
                            entityInfo.getFieldNames().add(javaField);
                            int type = metadata.getColumnType(i);
                            entityInfo.getFieldRelationMysqlType().put(javaField, MysqlType.getByJdbcType(type));
                            entityInfo.getFieldRelationColumns().put(javaField, columnName);
                            entityList.add(entityInfo);
                        }
                    }
                }

                next = cs.getMoreResults();
            } while (next);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return entityList;
    }

    /**
     * 获取该存储过程的参数
     *
     * @param procedure 存储过程名称
     * @return 存储过程的参数列表
     */
    private List<ProcedureParameter> getProcedureParameters(String procedure) {
        List<ProcedureParameter> parameters = new LinkedList<>();
        String sql = String.format(procedureInformationSQL, dbManager.getDatabase(), procedure);
        System.out.println(sql);
        PreparedStatement ps;
        try {
            ps = dbManager.getConnection().prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            if (Objects.nonNull(resultSet)) {
                while (resultSet.next()) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    Field[] fields = ProcedureParameter.class.getDeclaredFields();
                    ProcedureParameter parameter = new ProcedureParameter();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnLabel = metaData.getColumnLabel(i);
                        Optional<Field> existOptional = Arrays.stream(fields)
                                .filter(field -> field.getName().equalsIgnoreCase(columnLabel))
                                .findFirst();
                        if (existOptional.isPresent()) {
                            Field field = existOptional.get();
                            Object value = resultSet.getObject(i);
                            ReflectionUtils.makeAccessible(field);
                            ReflectionUtils.setField(field, parameter, value);
                        }
                    }
                    parameters.add(parameter);
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return parameters;
    }

    /**
     * 构建sql
     *
     * @param procedure 过程名称
     * @param procedureParameters 该过程参数列表
     * @return sql
     */
    private String builderSql(String procedure, List<ProcedureParameter> procedureParameters) {
        StringBuilder builder = new StringBuilder();

        if (procedureParameters.isEmpty()) {
            builder.append("Call ").append(procedure).append("();");
            return builder.toString();
        }

        builder.append("Call ").append(dbManager.getDatabase()).append(".").append(procedure).append("(");

        procedureParameters.forEach(procedureParameter -> {
            String parameterMode = procedureParameter.getParameterMode();
            if (mode.equalsIgnoreCase(parameterMode)) {
                String dataType = procedureParameter.getDataType();
                MysqlType mysqlType = MysqlType.getByName(dataType);
                Optional<JavaType> optional = Arrays.stream(JavaType.values())
                        .filter(javaType -> {
                            String[] simpleClasses = javaType.getSimpleClasses();
                            for (String simpleClass : simpleClasses) {
                                if (simpleClass.equalsIgnoreCase(mysqlType.getClassName())) {
                                    return true;
                                }
                            }
                            return false;
                        })
                        .findFirst();
                if (optional.isPresent()) {
                    builder.append(defaultValue).append(",");
                } else {
                    builder.append(procedureParameter.getValue()).append(",");
                }
            }
        });

        String substring = builder.substring(0, builder.length() - 1);
        return substring + ");";
    }

    public Object getNull() {
        return null;
    }

    public String columnConverterField(String columnName) {
        StringBuilder fieldName = new StringBuilder(16);
        if (StringUtils.startsWithIgnoreCase(columnName, "c_")) {
            String[] nameSplit = StringUtils.split(columnName, '_');
            if (1 < nameSplit.length) {
                fieldName.append(changedFirstLowerCase(nameSplit[1]));

                for (int j = 2; j < nameSplit.length; ++j) {
                    fieldName.append(changedFirstUpperCase(nameSplit[j]));
                }
            }
        }

        return fieldName.toString();
    }

    public String tableNameConverterClassName(String tableName) {
        StringBuilder className = new StringBuilder(16);
        if (StringUtils.contains(tableName, "_")) {
            String[] nameSplit = StringUtils.split(tableName, '_');
            if (1 < nameSplit.length) {
                className.append(changedFirstUpperCase(nameSplit[0]));
                for (int j = 1; j < nameSplit.length; ++j) {
                    className.append(changedFirstUpperCase(nameSplit[j]));
                }
            }
        }

        return className.toString();
    }

    public String changedFirstUpperCase(String target) {
        return target.substring(0, 1).toUpperCase() + target.substring(1);
    }

    public String changedFirstLowerCase(String target) {
        return target.substring(0, 1).toLowerCase() + target.substring(1);
    }


}
