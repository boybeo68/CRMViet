package vn.altalab.app.crmvietpack.home.Overview.Json;

import vn.altalab.app.crmvietpack.home.Overview.Setget.Overview_Paid_Customer_Setget;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Overview_PaidCustomer_Json {

    String data;
    JSONObject jsonObject, jsonObject1;
    JSONArray jsonArray;

    Overview_Paid_Customer_Setget setgetTongquan;
    ArrayList<Overview_Paid_Customer_Setget> list;

    long maxPayment = 0;
    long maxRevenue = 0;

    Boolean next = false;
    Boolean success = false;

    public Overview_PaidCustomer_Json(String data){
        this.data = data;

        try {
            list = new ArrayList<>();

            jsonObject = new JSONObject(data);

            if (jsonObject.getString("messages").equals("success")) {
                success = true ;
            }

            jsonArray = jsonObject.getJSONArray("payment");

            for (int i=0; i< jsonArray.length(); i++){

                jsonObject1 = (JSONObject) jsonArray.get(i);

                setgetTongquan = new Overview_Paid_Customer_Setget();

                String USER_ID = "";
                String REVENUE = "";
                String PAYMENT = "";
                String USER_FULL_NAME = "";

                try {
                    USER_ID = jsonObject1.getString("USER_ID");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    REVENUE = jsonObject1.getString("REVENUE");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    PAYMENT = jsonObject1.getString("PAYMENT");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    USER_FULL_NAME = jsonObject1.getString("USER_FULL_NAME");
                } catch (Exception e){
                    e.printStackTrace();
                }

                if (USER_ID != null && !USER_ID.equals("null"))
                setgetTongquan.setUSER_ID(USER_ID);

                if (REVENUE != null && !REVENUE.equals("null"))
                setgetTongquan.setREVENUE(REVENUE);

                if (PAYMENT != null && !PAYMENT.equals("null"))
                setgetTongquan.setPAYMENT(PAYMENT);

                if (USER_FULL_NAME != null && !USER_FULL_NAME.equals("null"))
                setgetTongquan.setUSER_FULL_NAME(USER_FULL_NAME);

                if (maxPayment < Long.parseLong(jsonObject1.getString("PAYMENT"))) {
                    maxPayment = Long.parseLong(jsonObject1.getString("PAYMENT"));
                }

                if (maxRevenue < Long.parseLong(jsonObject1.getString("REVENUE"))) {
                    maxRevenue = Long.parseLong(jsonObject1.getString("REVENUE"));
                }

                list.add(setgetTongquan);
            }

            if (list.size() == 5) {
                next = true;
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Overview_Paid_Customer_Setget> getListTongquan(){
        return list;
    }

    public long getMaxPayment(){
        if (maxPayment == 0)
            return 1;
        return maxPayment;
    }

    public long getMaxRevenue(){
        if (maxRevenue == 0)
            return 1;
        return maxRevenue;
    }

    public Boolean getNext(){
        return next;
    }

    public Boolean getStatus(){
        return success;
    }

}
