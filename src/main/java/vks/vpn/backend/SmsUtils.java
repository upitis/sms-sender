package vks.vpn.backend;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


/**
 * Created by upitis on 08.07.2016.
 */
public class SmsUtils {
    private static final String MMSERV_API_URL = "http://mmserv2.sc.vpn/api/mailings/add";
    private static final String MMSERV_USER = "apilogin";
    private static final String MMSERV_PASSWORD = "apipassword";

    public static void sendSms(List<String> phoneNumbers, String smsText) {

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(MMSERV_API_URL);


        String authorizationCode = Base64.getEncoder().encodeToString((MMSERV_USER+":"+MMSERV_PASSWORD).getBytes());

        httpPost.setHeader("Authorization", "Basic " + authorizationCode);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        List<NameValuePair> nameValuePairs = new ArrayList<>();
        for (String phoneNumber : phoneNumbers) {
            if (phoneNumber.matches("[+][0-9]{11}")) {
                nameValuePairs.add(new BasicNameValuePair("numbers[]", phoneNumber));
            }
        }
        nameValuePairs.add(new BasicNameValuePair("message", smsText));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, Charset.forName("UTF-8")));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            System.out.println(httpResponse.getStatusLine());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}
