package com.arvil.app;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.io.*;
import static java.nio.charset.StandardCharsets.UTF_8;

public class BalInqClass {
    public String SendWS(String content, String id) throws MalformedURLException, ProtocolException, IOException {
        String jsonMessage = "{\"Content\":\"" + content + "\"}";

        URL url = new URL("http://www.easytripcustomer.ph/ESCCB_WS/ESCCB.aspx/P01");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Content-Length", Integer.toString(jsonMessage.length(), 10));
        con.setRequestMethod("POST");
        con.setConnectTimeout(300000);
        con.setReadTimeout(300000);
        con.setRequestProperty("ID", id);
        con.setRequestProperty("AppVersion", "2.0");

        con.setDoOutput(true);

        OutputStream wr = con.getOutputStream();
        wr.write(jsonMessage.getBytes(UTF_8));
        wr.flush();
        wr.close();
        
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