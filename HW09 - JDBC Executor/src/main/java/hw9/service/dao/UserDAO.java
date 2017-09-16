package hw9.service.dao;

import hw9.dataset.User;
import hw9.jdbs.Executor;

import java.sql.Connection;
import java.sql.SQLException;

public class UserDAO {
    Executor executor;

    public UserDAO(Connection connection) {
        executor = new Executor(connection);
    }

    public void save(User user) throws SQLException {
        executor.save(user);
    }

    public User get(long id) throws IllegalAccessException, SQLException, InstantiationException {
        return executor.load(id, User.class);
    }
}
