package project.util;

import org.postgresql.Driver;

import java.sql.DriverManager;
import java.sql.SQLException;

public class ShopRunner {

    public static void main(String[] args) throws SQLException {
        Class<Driver> driverClass = Driver.class;
        try(var connection = ConnectionManager.get()) {
            System.out.println(connection.getTransactionIsolation());
        }
    }
}
