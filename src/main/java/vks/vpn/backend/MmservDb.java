package vks.vpn.backend;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by upitis on 08.07.2016.
 */
public class MmservDb {

    public static Connection getConnection() throws DbExceptions {

        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://10.128.2.123:3306/sms?autoReconnect=true&" +
                "useSSL=false&" +
                "serverTimezone=UTC&" +
                "characterEncoding=UTF-8";
        String username = "www_tms_user";
//        String username = "sc_user";
        String password = "xDMq4Tj827tUnDtB";
        Connection connection;

        try {
            Class.forName(driver).newInstance();
            connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(false);
        } catch (Exception ex) {
            throw new DbExceptions();
        }

        return connection;
    }

    public static void  closeConnection(Connection connection){
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
        }
    }

}
