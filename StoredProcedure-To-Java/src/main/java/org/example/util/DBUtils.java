package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author hq
 * @date 2020-12-07
 */
public class DBUtils {

    /**
     * 数据库连接信息
     */
    private final String dbUrl = "jdbc:mysql://localhost:3306/my_database?useUnicode=true&zeroDateTimeBehavior=convertToNull&autoReconnect=true&characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=GMT%2b8";

    private final String driver = "com.mysql.cj.jdbc.Driver";

    private final String database = "my_database";

    private final String userName = "username";

    private final String password = "password";

    private static final DBUtils INSTANCE = new DBUtils();

    private DBUtils() {
    }

    public static DBUtils getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(dbUrl, userName, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public String getDatabase() {
        return database;
    }
}
