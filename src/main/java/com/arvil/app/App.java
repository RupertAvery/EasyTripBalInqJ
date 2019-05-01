package com.arvil.app;
import java.util.List;
import java.util.ListIterator;
import com.arvil.app.models.CustomerInfo;

public class App {

    public static void main(String[] args) {
        String accountNo = args[0];

        try {

            System.out.println("Sending request...");

            EasyTripService service = new EasyTripService();

            List<CustomerInfo> customerInfos = service.GetCustomerInfos(accountNo);

            ListIterator<CustomerInfo> iterator = customerInfos.listIterator();

            while (iterator.hasNext()) {
                CustomerInfo customerInfo = iterator.next();
                System.out.println("Name: " + customerInfo.Name);
                System.out.println("ID: " + customerInfo.CustAccountID);
                System.out.println("Balance: " + customerInfo.Balance);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}