package hw9.util;

import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper {
    public static Connection getConnection() {
        try {
            DriverManager.registerDriver(new Driver());

            String url = "jdbc:postgresql://" +       //db type
                    "localhost:" +               //host name
                    "5432/" +                    //port
                    "test?" +              //db name
                    "user=homestead&" +              //login
                    "password=secret";            //password

            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
