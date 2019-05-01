package com.arvil.app;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ListIterator;

import org.bouncycastle.crypto.InvalidCipherTextException;

import com.arvil.app.models.CustomerInfo;
import com.arvil.app.models.ResponseContent;
import com.arvil.app.models.ReturnResponse;
import com.fasterxml.jackson.databind.ObjectMapper;;

public class App {
    private static String deviceId = "123456789012345"; // Uses IMEI but any value will do
    private static String passPhrase = "OAUNEW5EHNAU25Z9Z1T0A3R67YS0ZH2JGV89HSD2MDNN0HN6U90KJMDVOLO37O0ZMV4BKA9M5I5SISA0XZMYGEWE6CJAKYFQG1WC6KCOTA737E02LVHAPOX28KMD1UKD";
    private static String passPhrase2 = "C2TH1KWTLLK3ICLUCTYGLO3WILVOPYYRADUYCQSTT58WQBZIZBXEW6FVJ9OODMZJ36KUD3T6Y25HQOMOO0OLOONGVU1KN9IOA8XHWRWAFNOM7H6517BFZPGYRFYBQCEZ";

    public static void main(String[] args) {
        String strAcct = args[0];

        String acctJson = "{\"AccountObuID\":\"" + strAcct + "\"}";

        try {
            System.out.println("Sending request...");
            BalInqClass balInqClass = new BalInqClass();
            String crpContent = StringCipher.Encrypt(acctJson, passPhrase2);
            String id = StringCipher.Encrypt("02:00:00:00:00:00" + deviceId.toString(), passPhrase);
            String response = balInqClass.SendWS(crpContent, id);
            
            ObjectMapper objectMapper = new ObjectMapper();

            ReturnResponse returnVal = objectMapper.readValue(response, ReturnResponse.class);

            String userInfo = StringCipher.Decrypt(returnVal.Return.Result, passPhrase2);

            ResponseContent responseContent = objectMapper.readValue(userInfo, ResponseContent.class);

            ListIterator<CustomerInfo> iterator = responseContent.CustomerInfo.listIterator();

            while (iterator.hasNext()) { 
                CustomerInfo customerInfo = iterator.next();
                System.out.println("Name: " + customerInfo.Name);
                System.out.println("ID: " + customerInfo.CustAccountID);
                System.out.println("Balance: " + customerInfo.Balance);
            } 

        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidCipherTextException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}