package vn.altalab.app.crmvietpack.customer.customer_detail.json;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import vn.altalab.app.crmvietpack.customer.customer_detail.object.CustomerDetail_Transaction_Listview_Setget;

public class CustomerDetail_Transaction_Json {

    ArrayList<CustomerDetail_Transaction_Listview_Setget> list;

    Boolean success = false;
    String data;

    JSONObject jsonObject, jsonObject1;
    JSONArray jsonArray;

    CustomerDetail_Transaction_Listview_Setget SETGET;

    String TRANSACTION_ID = "";
    String START_DATE = "";
    String END_DATE = "";
    String TRANSACTION_NAME_TEXT = "";
    String ASSIGNER = "";
    String CUSTOMER_NAME = "";
    String STATUS = "";
    String TRANSACTION_TYPE_NAME = "";
    String ASSIGNED_USER_NAME = "";
    long CUSTOMER_ID = 0;

    public CustomerDetail_Transaction_Json(String data){
        this.data = data;
        list = new ArrayList<>();

        try {

            jsonObject = new JSONObject(data);

            if (jsonObject.getString("messages").equals("success")) {
                success = true;
            }

            jsonArray = jsonObject.getJSONArray("transactions");

            for (int i =0 ; i< jsonArray.length(); i++){
                jsonObject1 = (JSONObject) jsonArray.get(i);

                SETGET = new CustomerDetail_Transaction_Listview_Setget();

                try {
                    TRANSACTION_ID = jsonObject1.getString("TRANSACTION_ID");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    START_DATE = jsonObject1.getString("START_DATE");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    END_DATE = jsonObject1.getString("END_DATE");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    TRANSACTION_NAME_TEXT = jsonObject1.getString("TRANSACTION_NAME_TEXT");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    ASSIGNER = jsonObject1.getString("ASSIGNER");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    CUSTOMER_NAME = jsonObject1.getString("CUSTOMER_NAME");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    CUSTOMER_ID = jsonObject1.getLong("CUSTOMER_ID");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    STATUS = jsonObject1.getString("STATUS");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    TRANSACTION_TYPE_NAME = jsonObject1.getString("TRANSACTION_TYPE_NAME");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    ASSIGNED_USER_NAME = jsonObject1.getString("ASSIGNED_USER_NAME");
                } catch (Exception e){
                    e.printStackTrace();
                }

                if (TRANSACTION_ID != null && !TRANSACTION_ID.equals("null"))
                    SETGET.setTRANSACTION_ID(TRANSACTION_ID);

                if (START_DATE != null && !START_DATE.trim().equals("null")) {

                    if (START_DATE.length() > 11) {

                        String a = START_DATE.substring(0, 10).trim();

                        Log.e("neededdo_json", "START_DATE_A: "  + a);
                        String b = START_DATE.substring(11, START_DATE.length() - 3).trim();

                        Log.e("neededdo_json", "START_DATE_B: "  + b);
                        SETGET.setSTART_DATE(getStringDate(a) + " " + b);
                    }

                }

                if (END_DATE != null && !END_DATE.trim().equals("null")) {
                    if (END_DATE.length() > 11) {

                        String a = END_DATE.substring(0, 10).trim();

                        Log.e("NEEDEDDO", "END_DATE_A: "  + a);
                        String b = END_DATE.substring(11, END_DATE.length() - 3).trim();

                        Log.e("NEEDEDDO", "END_DATE_B: "  + b);
                        SETGET.setEND_DATE(getStringDate(a) + " " + b);
                    }
                }

                if (TRANSACTION_NAME_TEXT != null && !TRANSACTION_NAME_TEXT.equals("null"))
                    SETGET.setTRANSACTION_NAME_TEXT(TRANSACTION_NAME_TEXT);

                if (ASSIGNER != null && !ASSIGNER.equals("null"))
                    SETGET.setASSIGNER(ASSIGNER);

                if (CUSTOMER_NAME != null && !CUSTOMER_NAME.equals("null"))
                    SETGET.setCUSTOMER_NAME(CUSTOMER_NAME);

                if (CUSTOMER_ID != 0)
                    SETGET.setCUSTOMER_ID(CUSTOMER_ID);


                if (STATUS != null && !STATUS.equals("null"))
                    SETGET.setSTATUS(STATUS);

                if (TRANSACTION_TYPE_NAME != null && !TRANSACTION_TYPE_NAME.equals("null"))
                    SETGET.setTRANSACTION_TYPE_NAME(TRANSACTION_TYPE_NAME);

                if (ASSIGNED_USER_NAME != null && !ASSIGNED_USER_NAME.equals("null"))
                    SETGET.setASSIGNED_USER_NAME(ASSIGNED_USER_NAME);

                list.add(SETGET);

            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<CustomerDetail_Transaction_Listview_Setget> get_CustomerDetail_Transaction_Json_List(){
        return list;
    }

    public Boolean getStatus(){
        return success;
    }

    public String getStringDate(String response){

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(response);
            response = new SimpleDateFormat("dd/MM/yyyy").format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;

    }

}
