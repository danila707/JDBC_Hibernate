package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class UserDaoHibernateImpl implements UserDao {
    private final Logger log = Logger.getLogger(UserDaoHibernateImpl.class.getName());
    Util util = new Util();
    private final SessionFactory sessionFactory = util.getSessionFactory();

    @Override
    public void createUsersTable() {
        String createTableSql =
                "CREATE TABLE IF NOT EXISTS users (" +
                        "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                        "name VARCHAR(50)," +
                        "lastName VARCHAR(50)," +
                        "age TINYINT)";
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery(createTableSql).executeUpdate();
            transaction.commit();
            log.info("таблица создана");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                log.severe("ошибка создания таблицы" + e.getMessage());
            }
        }
    }

    @Override
    public void dropUsersTable() {
        String dropTableSql = "DROP TABLE IF EXISTS users";
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery(dropTableSql).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                log.severe("Ошибка удаления таблицы" + e.getMessage());
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
            transaction.commit();
            log.info("Пользователь " + name + " Добавлен в таблицу");

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                log.severe("Ошибка добавления пользователя " + e.getMessage());
            }
        }

    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
            }
            transaction.commit();
            log.info("Пользователь с id " + id + " удален");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                log.severe("Ошибка удаления пользователя" + e.getMessage());
            }
        }

    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User", User.class).getResultList();
        } catch (Exception e) {
            log.severe("Ошибка получения списка пользователей " + e.getMessage());
            return null;
        }
    }

    @Override
    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE users";
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery(sql).executeUpdate();
            transaction.commit();
            log.info("Таблица удалена");
        } catch (Exception e) {
            log.severe("Ошибка удаления таблицы " + e.getMessage());
        }
    }
}
