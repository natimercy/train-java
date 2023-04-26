package org.natimercy.generator;

import com.mysql.cj.MysqlType;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.natimercy.generator.entity.TableMetaData;
import org.natimercy.generator.platform.DMExecutor;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 启动类
 *
 * @author hq
 * @date 2020-12-08
 */
public class CustomizerGenerateApplicationByProcedure {

    private final String path = System.getProperty("user.dir") + "/generator-files/";

    /**
     * 实体类目录
     */
    private final String entityFilePath = path;

    /**
     * xml文件目录
     */
    private final String xmlFilePath = path;

    private final String classNamePrefix = "customize";

    private final String line = System.getProperty("line.separator");

    public static void main(String[] args) throws IOException {
        new CustomizerGenerateApplicationByProcedure().execute();
    }

    private void execute() throws IOException {
        Properties properties = getProperties();

        List<TableMetaData> entityInfoList = new DMExecutor(properties).getEntityList(properties.getProperty("procedure"));

        System.out.println(entityInfoList);

        outPutFile(entityInfoList);
    }

    private Properties getProperties() throws IOException {
        return PropertiesLoaderUtils.loadAllProperties("config.properties");
    }

    private void outPutFile(List<TableMetaData> entityInfoList) {
        try {
            outPutEntityFile(entityInfoList);
            outPutMapperFile(entityInfoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将实体类中的属性输出到文件中
     *
     * @param entityInfoList entityInfoList
     * @throws IOException IOException
     */
    private void outPutEntityFile(List<TableMetaData> entityInfoList) throws IOException {
        for (int i = 0; i < entityInfoList.size(); i++) {
            TableMetaData entityInfo = entityInfoList.get(i);
            String path = entityFilePath + generateClassName(entityInfo.getClassName(), i) + ".txt";
            FileWriter fileWriter = new FileWriter(createFile(path));
            List<String> fieldNames = entityInfo.getFieldNames();
            Map<String, MysqlType> mysqlType = entityInfo.getFieldRelationMysqlType();
            for (String fieldName : fieldNames) {
                MysqlType type = mysqlType.get(fieldName);
                String className = type.getClassName();
                int index = className.lastIndexOf(".");
                String javaType = className.substring(index + 1);

                fileWriter.write("@ApiModelProperty(\"\")" + line);
                fileWriter.write("private " + javaType + " " + fieldName + ";" + line + line);
            }
            IOUtils.closeQuietly(fileWriter);
        }
    }

    /**
     * <resultMap id="SupervisorRecordDetailVO" type="hw.topevery.baoan.vo.map.garbage.GarbageFrontSupervisorRecordDetailVO">
     *         <result column="c_supervisor_id" property="supervisorId" />
     * </resultMap>
     *
     * @param entityInfoList entityInfoList
     * @throws IOException IOException
     */
    private void outPutMapperFile(List<TableMetaData> entityInfoList) throws IOException {
        for (int i = 0; i < entityInfoList.size(); i++) {
            TableMetaData entityInfo = entityInfoList.get(i);
            String path = xmlFilePath + generateClassName(entityInfo.getClassName(), i) + "Mapper" + ".xml";
            FileWriter fileWriter = new FileWriter(createFile(path));
            String className = entityInfo.getClassName();
            fileWriter.write("<resultMap id=\"" + className + "\" type=\"" + className + "\">" + line);

            Map<String, String> relationColumns = entityInfo.getFieldRelationColumns();
            List<String> fieldNames = entityInfo.getFieldNames();
            for (String fieldName : fieldNames) {
                String column = relationColumns.get(fieldName);
                fileWriter.write("\t<result column=\"" + column + "\" property=\"" + fieldName + "\" />" + line);
            }
            fileWriter.write("</resultMap>");
            IOUtils.closeQuietly(fileWriter);
        }
    }

    public String generateClassName(String originalClassName, int index) {
        if (StringUtils.isNotEmpty(originalClassName)) {
            return originalClassName;
        }

        return classNamePrefix + index;
    }

    @SuppressWarnings("all")
    public File createFile(String outputFilePath) {
        File file = new File(outputFilePath);
        if (!file.getParentFile().exists()) {
            // 创建目录
            file.getParentFile().mkdirs();
        }
        return file;
    }

}
