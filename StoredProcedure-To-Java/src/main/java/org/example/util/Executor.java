package org.example.util;

import com.mysql.cj.MysqlType;
import com.mysql.cj.jdbc.result.ResultSetImpl;
import com.mysql.cj.protocol.ColumnDefinition;
import com.mysql.cj.result.Field;
import org.apache.commons.lang3.StringUtils;
import org.example.entity.ProcedureParameter;
import org.example.entity.TableInfo;
import org.springframework.util.ReflectionUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author hq
 * @date 2020-12-08
 */
public class Executor {

    private final List<String> classes = new ArrayList<>();

    private final String procedureInformationSQL = "SELECT " +
            "       SPECIFIC_SCHEMA as specificSchema," +
            "       SPECIFIC_NAME   as specificName," +
            "       PARAMETER_MODE  as parameterMode," +
            "       PARAMETER_NAME  as parameterName," +
            "       DATA_TYPE       as dataType" +
            " from information_schema.PARAMETERS where SPECIFIC_SCHEMA = '%s' and SPECIFIC_NAME = '%s'; ";

    private final int defaultValue = 1;

    private final String mode = "IN";

    public Executor() {
        classes.add(Integer.class.getName());
        classes.add(int.class.getName());
        classes.add(Long.class.getName());
        classes.add(long.class.getName());

        classes.add(Float.class.getName());
        classes.add(float.class.getName());
        classes.add(Double.class.getName());
        classes.add(double.class.getName());
    }

    /**
     * 获取表信息
     *
     * @param procedure 存储过程名称
     * @return EntityInfo 集合
     */
    public List<TableInfo> getEntityList(String procedure) {
        List<TableInfo> entityList = new ArrayList<>();

        List<ProcedureParameter> parameters = getProcedureParameters(procedure);

        CallableStatement cs;
        String sql = builderSql(procedure, parameters);
        System.out.println(sql);
        try {
            cs = DBUtils.getInstance().getConnection().prepareCall(sql);
            cs.execute();

            ResultSetImpl resultSet = (ResultSetImpl) cs.getResultSet();
            while (resultSet != null) {
                TableInfo entityInfo = new TableInfo();
                ColumnDefinition metadata = resultSet.getMetadata();
                Field[] fields = metadata.getFields();
                if (fields.length == 0) {
                    break;
                }

                entityInfo.setClassName(tableNameConverterClassName(fields[0].getTableName()));
                Stream.of(fields).forEach(field -> {
                    entityInfo.getColumnNames().add(field.getColumnLabel());
                    String javaField = columnConverterField(field.getColumnLabel());
                    entityInfo.getFieldNames().add(javaField);
                    entityInfo.getFieldRelationMysqlType().put(javaField, field.getMysqlType());
                    entityInfo.getFieldRelationColumns().put(javaField, field.getColumnLabel());
                });
                entityList.add(entityInfo);

                resultSet = (ResultSetImpl) resultSet.getNextResultset();
            }
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
        String sql = String.format(procedureInformationSQL, DBUtils.getInstance().getDatabase(), procedure);
        PreparedStatement ps;
        try {
            ps = DBUtils.getInstance().getConnection().prepareStatement(sql);
            ResultSetImpl resultSet = (ResultSetImpl) ps.executeQuery();

            while (resultSet.next()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                java.lang.reflect.Field[] fields = ProcedureParameter.class.getDeclaredFields();

                ProcedureParameter parameter = new ProcedureParameter();
                for (int i = 1; i <= columnCount; i++) {
                    String columnLabel = metaData.getColumnLabel(i);
                    for (java.lang.reflect.Field field : fields) {
                        String fieldName = field.getName();
                        if (fieldName.equals(columnLabel)) {
                            String value = resultSet.getString(columnLabel);
                            ReflectionUtils.makeAccessible(field);
                            ReflectionUtils.setField(field, parameter, value);
                            break;
                        }
                    }
                }

                parameters.add(parameter);
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

        builder.append("Call ").append(procedure).append("(");
        procedureParameters.forEach(procedureParameter -> {
            String parameterMode = procedureParameter.getParameterMode();
            if (mode.equalsIgnoreCase(parameterMode)) {
                String dataType = procedureParameter.getDataType();
                MysqlType mysqlType = MysqlType.getByName(dataType);
                if (classes.contains(mysqlType.getClassName())) {
                    builder.append(defaultValue).append(",");
                } else {
                    builder.append(getNull()).append(",");
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
