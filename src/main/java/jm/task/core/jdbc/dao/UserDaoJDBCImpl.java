package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {
    private final Logger log = Logger.getLogger(UserDaoJDBCImpl.class.getName());
    private final Connection connection = Util.getConnection();
    @Override
    public void createUsersTable() {
        if (connection == null) {
            log.info("нет соединения");
        }

        try {
            if (connection.isClosed()) {
                log.info("Соединение с базой закрыто.");
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String createTableSql =
                "CREATE TABLE IF NOT EXISTS users (" +
                        "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                        "name VARCHAR(50)," +
                        "lastName VARCHAR(50)," +
                        "age TINYINT)";
        try (Statement statement = connection.prepareStatement(createTableSql)) {
            statement.execute(createTableSql);
            log.info("Таблица пользователей создана.");
        } catch (SQLException e) {
            log.severe("Ошибка при создании таблицы: " + e.getMessage());
        }

    }

    @Override
    public void dropUsersTable() {
        String dropTableSql = "DROP TABLE IF EXISTS users";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(dropTableSql);
            log.info("Таблица пользователей удалена.");
        } catch (SQLException e) {
            log.severe("Ошибка при удалении таблицы: " + e.getMessage());
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO users (name, lastName, age) VALUES(?, ?, ?)";
        try (PreparedStatement preparedStatement = Util.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            log.info("Пользователь с именем '{}' добавлен в базу данных.");
        } catch (SQLException e) {
            log.severe("Ошибка при добавлении пользователя: {}");
        }
    }

    @Override
    public void removeUserById(long id) {
        String sql = "DELETE FROM users WHERE id = " + id;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            log.info("Пользователь с id " + id + " удалён.");
        } catch (SQLException e) {
            log.severe("Ошибка при удалении пользователя: " + e.getMessage());
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }
        } catch (
                SQLException e) {
            log.severe("Ошибка при получении списка пользователей: " + e.getMessage());
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        String clean = "TRUNCATE TABLE users";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(clean);
            log.info("Таблица пользователей очищена.");
        } catch (SQLException e) {
            log.severe("Ошибка при очистке таблицы: " + e.getMessage());
        }
    }
}
