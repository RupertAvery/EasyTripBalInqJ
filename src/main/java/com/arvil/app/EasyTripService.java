package com.arvil.app;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.arvil.app.models.*;
import java.io.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.bouncycastle.crypto.InvalidCipherTextException;;

public class EasyTripService {
    private static String deviceId = "123456789012345"; // Uses IMEI but any value will do
    private static String passPhrase = "OAUNEW5EHNAU25Z9Z1T0A3R67YS0ZH2JGV89HSD2MDNN0HN6U90KJMDVOLO37O0ZMV4BKA9M5I5SISA0XZMYGEWE6CJAKYFQG1WC6KCOTA737E02LVHAPOX28KMD1UKD";
    private static String passPhrase2 = "C2TH1KWTLLK3ICLUCTYGLO3WILVOPYYRADUYCQSTT58WQBZIZBXEW6FVJ9OODMZJ36KUD3T6Y25HQOMOO0OLOONGVU1KN9IOA8XHWRWAFNOM7H6517BFZPGYRFYBQCEZ";
    private String easyTripServiceUrl = "http://www.easytripcustomer.ph/ESCCB_WS/ESCCB.aspx/P01";


    /**
     * Retrieves the CustomerInfo items associated with the account specifiec by accountNo
     * @param accountNo
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws InvalidCipherTextException
     * @throws MalformedURLException
     * @throws ProtocolException
     * @throws IOException
     */
    public List<CustomerInfo> GetCustomerInfos(String accountNo) throws InvalidKeyException, NoSuchAlgorithmException,
            InvalidCipherTextException, MalformedURLException, ProtocolException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Create the request, encrypt it and send it
        AccountObuIDInfo accountInfo = new AccountObuIDInfo();
        accountInfo.AccountObuID = accountNo;
        String acctJson = objectMapper.writeValueAsString(accountInfo);
        String content = StringCipher.Encrypt(acctJson, passPhrase2);
        String jsonMessage = "{\"Content\":\"" + content + "\"}";

        String id = StringCipher.Encrypt("02:00:00:00:00:00" + deviceId.toString(), passPhrase);

        String response = PostRequest(jsonMessage, id);

        ReturnResponse returnVal = objectMapper.readValue(response, ReturnResponse.class);

        String userInfo = StringCipher.Decrypt(returnVal.Return.Result, passPhrase2);

        ResponseContent responseContent = objectMapper.readValue(userInfo, ResponseContent.class);
        return responseContent.CustomerInfo;
    }

    /**
     * Posts a request to the web service and retrieves the response
     * @param content
     * @param id
     * @return
     * @throws MalformedURLException
     * @throws ProtocolException
     * @throws IOException
     */
    private String PostRequest(String content, String id) throws MalformedURLException, ProtocolException, IOException {
        URL url = new URL(easyTripServiceUrl);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Content-Length", Integer.toString(content.length(), 10));
        con.setRequestMethod("POST");
        con.setConnectTimeout(300000);
        con.setReadTimeout(300000);
        con.setRequestProperty("ID", id);
        con.setRequestProperty("AppVersion", "2.0");

        con.setDoOutput(true);

        OutputStream wr = null;
        try {
            wr = con.getOutputStream();
            wr.write(content.getBytes(UTF_8));
            wr.flush();
        } finally {
            wr.close();
        }

        StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
        InputStream is = con.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;

        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();

        return response.toString();
    }
}