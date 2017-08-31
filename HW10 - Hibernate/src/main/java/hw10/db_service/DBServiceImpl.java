package hw10.db_service;

import hw10.dataset.AddressDataSet;
import hw10.dataset.PhoneDataSet;
import hw10.dataset.UserDataSet;
import hw10.db_service.dao.UserDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class DBServiceImpl implements DBService {
    private final SessionFactory sessionFactory;

    public DBServiceImpl() {
        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(UserDataSet.class);
        configuration.addAnnotatedClass(PhoneDataSet.class);
        configuration.addAnnotatedClass(AddressDataSet.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/test");
        configuration.setProperty("hibernate.connection.username", "homestead");
        configuration.setProperty("hibernate.connection.password", "secret");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.enable_lazy_load_no_trans", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");

        sessionFactory = createSessionFactory(configuration);
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    @Override
    public void save(UserDataSet user) {
        try (Session session = sessionFactory.openSession()) {
            new UserDAO(session).save(user);
        }
    }

    @Override
    public UserDataSet get(long id) {
        try (Session session = sessionFactory.openSession()) {
            return new UserDAO(session).get(id);
        }
    }

    @Override
    public UserDataSet get(String name) {
        try (Session session = sessionFactory.openSession()) {
            return new UserDAO(session).get(name);
        }
    }
}
