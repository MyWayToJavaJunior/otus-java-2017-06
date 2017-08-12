package hw9;

import hw9.dataset.User;
import hw9.jdbs.Executor;
import org.junit.Before;
import org.junit.Test;
import org.postgresql.Driver;

import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ExecutorTest {
    private Executor executor;
    private Connection connection;

    @Before
    public void setup() throws SQLException {
        DriverManager.registerDriver(new Driver());

        String url = "jdbc:postgresql://" +
                "localhost:" +
                "5432/" +
                "test?" +
                "user=homestead&" +
                "password=secret";

        connection = DriverManager.getConnection(url);
        executor = new Executor(connection);
    }

    @Test
    public void test() throws SQLException, InstantiationException, IllegalAccessException {
        try {
            connection.setAutoCommit(false);

            User expected;
            User actual;

            expected = User.builder().id(100).name("User1").age(1).build();
            executor.save(expected);
            actual = executor.load(100, User.class);
            assertEquals(actual, expected);

            expected = User.builder().id(101).name("User2").build();
            executor.save(expected);
            actual = executor.load(101, User.class);
            assertEquals(actual, expected);

            expected = User.builder().id(102).age(1).build();
            executor.save(expected);
            actual = executor.load(102, User.class);
            assertEquals(actual, expected);

            expected = User.builder().id(103).build();
            executor.save(expected);
            actual = executor.load(103, User.class);
            assertEquals(actual, expected);

        } catch (SQLException | IllegalAccessException | InstantiationException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.rollback();
        }
    }
}
