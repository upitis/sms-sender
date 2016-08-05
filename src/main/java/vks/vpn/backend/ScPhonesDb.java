package vks.vpn.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by upitis on 01.06.2016.
 */
public class ScPhonesDb {

    public static  Connection getConnection() throws DbExceptions{

        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://mysql:3306/scphones_db?autoReconnect=true&" +
                "useSSL=false&" +
                "serverTimezone=UTC&" +
                "characterEncoding=UTF-8";
        String username = "root";
        String password = "P@$$w0rd";
        Connection connection;
        try {
            Class.forName(driver).newInstance();
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception ex) {
            throw new DbExceptions();
        }
        return connection;
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null ) {
                connection.close();
            }
        } catch (SQLException e) {
        }
    }
}
