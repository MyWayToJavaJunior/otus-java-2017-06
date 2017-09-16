package hw9.service;

import hw9.dataset.User;
import hw9.service.dao.UserDAO;
import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBServiceImpl implements DBService {
    Connection connection;

    public DBServiceImpl() throws SQLException {
        DriverManager.registerDriver(new Driver());

        String url = "jdbc:postgresql://" +
                "localhost:" +
                "5432/" +
                "test?" +
                "user=homestead&" +
                "password=secret";

        connection = DriverManager.getConnection(url);
    }

    @Override
    public void save(User user) {
        try {
            new UserDAO(connection).save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public User get(long id) {
        try {
            return new UserDAO(connection).get(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
