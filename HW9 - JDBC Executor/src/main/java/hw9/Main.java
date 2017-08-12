package hw9;

import hw9.dataset.User;
import hw9.jdbs.Executor;
import hw9.service.DBService;
import hw9.service.DBServiceImpl;

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
        DBService dbService = new DBServiceImpl();

        User admin = dbService.get(1);
        User user = dbService.get(3);

        dbService.save(User.builder().name("User1").age(10).build());
        dbService.save(User.builder().name("User2").build());
        dbService.save(User.builder().age(30).build());
        dbService.save(new User());
        dbService.save(User.builder().id(100).build());
        dbService.save(admin);
    }
}
