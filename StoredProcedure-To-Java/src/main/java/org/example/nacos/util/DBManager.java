package org.example.nacos.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author hq
 * @date 2020-12-07
 */
public class DBManager {

    private final Properties properties;

    public DBManager(Properties properties) {
        this.properties = properties;
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            String driverValue = properties.getProperty("driver");
            String dbUrlValue = properties.getProperty("dbUrl");
            String userNameValue = properties.getProperty("userName");
            String passwordValue = properties.getProperty("password");
            Class.forName(driverValue);
            connection = DriverManager.getConnection(dbUrlValue, userNameValue, passwordValue);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public String getDatabase() {
        return properties.getProperty("database");
    }
}
