package vks.vpn.backend;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by upitis on 08.07.2016.
 */
public class MmservSqlConnection implements AutoCloseable {
    private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String CONNECTION_URL = "jdbc:mysql://10.128.2.123:3306/sms?autoReconnect=true&" +
            "useSSL=false&" +
            "serverTimezone=UTC&" +
            "characterEncoding=UTF-8";
    private static final String USERNAME = "www_tms_user";
//    private static final String USERNAME = "sc_user";
    private static final String PASSWORD = "xDMq4Tj827tUnDtB";
    private static Connection connection = null;

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        if (connection == null) {
                Class.forName(MYSQL_DRIVER);
                connection = DriverManager.getConnection(CONNECTION_URL, USERNAME, PASSWORD);
                connection.setAutoCommit(false);
        }
        return connection;
    }

    private MmservSqlConnection() {
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}
