package vks.vpn.backend;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by upitis on 13.07.2016.
 */
public class Authenticator {

    public static boolean isCredentialRight(String username, String password) {
        try {
            Properties loginProperties = new Properties();
            loginProperties.load(Authenticator.class.getResourceAsStream("passwd.properties"));
            String pass = loginProperties.getProperty(username);
            return (pass !=null && pass.equals(password)) ||
                    (
                            System.getenv("LOGIN") != null &&
                            username.equals(System.getenv("LOGIN")) &&
                            password.equals(System.getenv("PASSWORD"))
                    );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
