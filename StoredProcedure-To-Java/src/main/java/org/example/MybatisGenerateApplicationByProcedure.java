package org.example;

import com.mysql.cj.MysqlType;
import org.apache.commons.io.IOUtils;
import org.example.entity.TableInfo;
import org.example.util.Executor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 启动类
 *
 * @author hq
 * @date 2020-12-08
 */
public class MybatisGenerateApplicationByProcedure {

    private final String path = System.getProperty("user.dir") + "/generator-files/";

    /**
     * 实体类目录
     */
    private final String entityFilePath = path;

    /**
     * xml文件目录
     */
    private final String xmlFilePath = path;

    /**
     * 存储过程名称
     */
    private final String procedure = "p_map_garbage_front_stat_supervisor_record_details_info";

    private final String line = System.getProperty("line.separator");


    public static void main(String[] args) {
        new MybatisGenerateApplicationByProcedure().execute();
    }

    private void execute() {
        List<TableInfo> entityInfoList = new Executor().getEntityList(procedure);

        System.out.println(entityInfoList);

        outPutFile(entityInfoList);
    }

    private void outPutFile(List<TableInfo> entityInfoList) {
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
    private void outPutEntityFile(List<TableInfo> entityInfoList) throws IOException {
        for (TableInfo entityInfo : entityInfoList) {
            String path = entityFilePath + entityInfo.getClassName() + ".txt";
            FileWriter fileWriter = new FileWriter(createFile(path));
            List<String> fieldNames = entityInfo.getFieldNames();
            Map<String, MysqlType> mysqlType = entityInfo.getFieldRelationMysqlType();
            for (String fieldName : fieldNames) {
                MysqlType type = mysqlType.get(fieldName);
                String className = type.getClassName();
                int index = className.lastIndexOf(".");
                String javaType = className.substring(index + 1);

                fileWriter.write("@ApiModelProperty(\"\");" + line);
                fileWriter.write("private " + javaType + " " + fieldName + line + line);
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
    private void outPutMapperFile(List<TableInfo> entityInfoList) throws IOException {
        for (TableInfo entityInfo : entityInfoList) {
            String path = xmlFilePath + entityInfo.getClassName() + "Mapper" + ".xml";
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
