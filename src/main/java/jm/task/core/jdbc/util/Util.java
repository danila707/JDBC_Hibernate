package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Util {
    private static final Logger log = Logger.getLogger(Util.class.getName());

    private static final String URL = "jdbc:mysql://localhost:3306/users_db?useSSL=false&autoReconnect=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            log.info("Соединение с базой данных установлено!");
        } catch (SQLException e) {
            log.severe("Соединение с базой данных не установлено!");
        }
        return connection;
    }
}
