package hw9;

import hw9.dataset.User;
import hw9.jdbs.Executor;
import hw9.util.ConnectionHelper;

import java.sql.SQLException;

/**
 * sudo -u postgres psql
 *
 * psql> CREATE DATABASE test;
 * psql> CREATE USER homestead WITH password 'secret';
 * psql> GRANT ALL ON DATABASE test TO homestead;
 *
 * sudo -u postgres psql test < dump.sql
 */

public class Main {
    public static void main(String[] args) throws IllegalAccessException, SQLException, InstantiationException {
        Executor executor = new Executor(ConnectionHelper.getConnection());

        User admin = executor.load(1, User.class);
        User user = executor.load(3, User.class);

        executor.save(User.builder().name("User1").age(10).build());
        executor.save(User.builder().name("User2").build());
        executor.save(User.builder().age(30).build());
        executor.save(new User());
        executor.save(User.builder().id(100).build());
        executor.save(admin);
    }
}
