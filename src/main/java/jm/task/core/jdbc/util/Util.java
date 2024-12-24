package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

public class Util {
    private SessionFactory sessionFactory = buildSessionFactory();

    SessionFactory buildSessionFactory() {
        try {
            sessionFactory = new Configuration()
                    .addProperties(getProperties())
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory();

        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
        return sessionFactory;

    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        properties.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        properties.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/users_db");
        properties.setProperty("hibernate.connection.username", "root");
        properties.setProperty("hibernate.connection.password", "root");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        return properties;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
