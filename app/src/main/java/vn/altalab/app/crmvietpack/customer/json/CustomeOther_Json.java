package vn.altalab.app.crmvietpack.customer.json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.altalab.app.crmvietpack.customer.setget.Customer_Setget;

/**
 * Created by boybe on 05/23/2017.
 */

public class CustomeOther_Json  { JSONObject jsonObject, jsonObject1;
    JSONArray jsonArray;

    String data = "";

    ArrayList<Customer_Setget> list;
    Boolean test = false;



    public CustomeOther_Json(String data){
        this.data = data;

        list = new ArrayList<>();

        try{

            jsonObject = new JSONObject(data);

            if (jsonObject.getString("messages").equals("success")){
                test = true;
            }

            jsonArray = jsonObject.getJSONArray("customerOther");

            for (int i=0 ; i< jsonArray.length(); i++){
                jsonObject1 = (JSONObject) jsonArray.get(i);
                Customer_Setget customerSetget = new Customer_Setget();

                String CUSTOMER_ID = "";
                String OFFICE_ADDRESS = "";
                String CUSTOMER_EMAIL = "";
                String CUSTOMER_NAME = "";
                String TELEPHONE = "";
                String USER_ID="";
                String USER_NAME="";
                try {
                    USER_NAME = jsonObject1.getString("USER_NAME");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    USER_ID = jsonObject1.getString("USER_ID");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    CUSTOMER_ID = jsonObject1.getString("CUSTOMER_ID");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    OFFICE_ADDRESS = jsonObject1.getString("OFFICE_ADDRESS");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    CUSTOMER_EMAIL = jsonObject1.getString("CUSTOMER_EMAIL");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    CUSTOMER_NAME = jsonObject1.getString("CUSTOMER_NAME");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    TELEPHONE = jsonObject1.getString("TELEPHONE");
                } catch (Exception e){
                    e.printStackTrace();
                }
                if (USER_ID != null && !USER_ID.equals("null"))
                    customerSetget.setUSER_ID(USER_ID);

                if (USER_NAME != null && !USER_NAME.equals("null"))
                    customerSetget.setUSER_NAME(USER_NAME);


                if (CUSTOMER_ID != null && !CUSTOMER_ID.equals("null"))
                    customerSetget.setCUSTOMER_ID(CUSTOMER_ID);

                if (OFFICE_ADDRESS != null && !OFFICE_ADDRESS.equals("null"))
                    customerSetget.setOFFICE_ADDRESS(OFFICE_ADDRESS);

                if (CUSTOMER_EMAIL != null && !CUSTOMER_EMAIL.equals("null"))
                    customerSetget.setCUSTOMER_EMAIL(CUSTOMER_EMAIL);

                if (CUSTOMER_NAME != null && !CUSTOMER_NAME.equals("null"))
                    customerSetget.setCUSTOMER_NAME(CUSTOMER_NAME);

                if (TELEPHONE != null && !TELEPHONE.equals("null"))
                    customerSetget.setTELEPHONE(TELEPHONE);

                list.add(customerSetget);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Customer_Setget> getListOther(){
        return list;
    }

    public Boolean getStatus(){
        return test;
    }
}
