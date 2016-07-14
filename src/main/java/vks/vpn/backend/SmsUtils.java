package vks.vpn.backend;


import vks.vpn.backend.MmservSqlConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;


/**
 * Created by upitis on 08.07.2016.
 */
public class SmsUtils {
    private static final String SMS_INSERT_SQL_QUERY = "INSERT INTO outbox (number,text,dreport) values(?,?,?)";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        sendSms(Arrays.asList(new String[]{"+79052748107"}),"TestMSG");
    }

    public static void sendSms(List<String> phoneNumbers, String smsText) throws ClassNotFoundException, SQLException {
        try (PreparedStatement statement = MmservSqlConnection.getConnection().prepareStatement(SMS_INSERT_SQL_QUERY)) {
            for (String ph: phoneNumbers) {
                ph = ph.trim();
                if (ph.matches("\\+7[\\d]{10}")) {
                    statement.setString(1, ph);
                    statement.setString(2, smsText);
                    statement.setInt(3, 1);
                    statement.executeUpdate();
                    MmservSqlConnection.getConnection().commit();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
