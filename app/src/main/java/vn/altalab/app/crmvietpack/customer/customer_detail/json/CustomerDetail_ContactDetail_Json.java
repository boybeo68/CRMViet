package vn.altalab.app.crmvietpack.customer.customer_detail.json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import vn.altalab.app.crmvietpack.customer.customer_detail.object.CustomerDetail_ContactDetail_Setget;

public class CustomerDetail_ContactDetail_Json {

    JSONObject jsonObject, jsonObject1;
    JSONArray jsonArray;
    Boolean isStatus = false;

    CustomerDetail_ContactDetail_Setget SETGET;

    public CustomerDetail_ContactDetail_Json(JSONObject jsonObject){
        this.jsonObject = jsonObject;

        try {

            if (jsonObject.getString("messages").equals("success")){
                isStatus = true;
            }

            jsonArray = jsonObject.getJSONArray("contact");
            jsonObject1 = (JSONObject) jsonArray.get(0);
            SETGET = new CustomerDetail_ContactDetail_Setget();

            String CONTACT_ID = "";
            String BIRTHDAY = "";
            String CONTACT_EMAIL = "";
            String CONTACT_FULL_NAME = "";
            String CONTACT_MOBIPHONE = "";
            String CONTACT_WORKPHONE = "";
            String GENDER = "";
            String POSITION_ID = "";
            String CUSTOMER_ID = "";
            String CUSTOMER_NAME="";
            try {
                CONTACT_ID = jsonObject1.getString("CONTACT_ID");
            } catch (Exception e){
                e.printStackTrace();
            }

            try {
                CUSTOMER_NAME = jsonObject1.getString("CUSTOMER_NAME");
            } catch (Exception e){
                e.printStackTrace();
            }

            try {
                BIRTHDAY = jsonObject1.getString("BIRTHDAY");
            } catch (Exception e){
                e.printStackTrace();
            }

            try{
                CONTACT_EMAIL = jsonObject1.getString("CONTACT_EMAIL");
            } catch (Exception e){
                e.printStackTrace();
            }

            try {
                CONTACT_FULL_NAME = jsonObject1.getString("CONTACT_FULL_NAME");
            } catch (Exception e){
                e.printStackTrace();
            }

            try{
                CONTACT_MOBIPHONE = jsonObject1.getString("CONTACT_MOBIPHONE");
            } catch (Exception e){
                e.printStackTrace();
            }

            try{
                CONTACT_WORKPHONE = jsonObject1.getString("CONTACT_WORKPHONE");
            } catch (Exception e){
                e.printStackTrace();
            }

            try {
                GENDER = jsonObject1.getString("GENDER");
            } catch (Exception e){
                e.printStackTrace();
            }

            try {
                POSITION_ID = jsonObject1.getString("POSITION_ID");
            } catch (Exception e){
                e.printStackTrace();
            }

            try {
                CUSTOMER_ID = jsonObject1.getString("CUSTOMER_ID");
            } catch (Exception e){
                e.printStackTrace();
            }

            if (CONTACT_ID != null && !CONTACT_ID.equals("null"))
                SETGET.setCONTACT_ID(CONTACT_ID);

            if (BIRTHDAY != null && !BIRTHDAY.equals("null")) {
                if (BIRTHDAY.length() > 11) {

                    String a = BIRTHDAY.substring(0, 10).trim();

                    SETGET.setBIRTHDAY(getStringDate(a));
                }
            }

            if (CONTACT_EMAIL != null && !CONTACT_EMAIL.equals("null"))
                SETGET.setCONTACT_EMAIL(CONTACT_EMAIL);

            if (CONTACT_FULL_NAME != null && !CONTACT_FULL_NAME.equals("null"))
                SETGET.setCONTACT_FULL_NAME(CONTACT_FULL_NAME);

            if (CUSTOMER_NAME != null && !CUSTOMER_NAME.equals("null"))
                SETGET.setCUSTOMER_NAME(CUSTOMER_NAME);

            if (CONTACT_MOBIPHONE != null && !CONTACT_MOBIPHONE.equals("null"))
                SETGET.setCONTACT_MOBIPHONE(CONTACT_MOBIPHONE);

            if (CONTACT_WORKPHONE != null && !CONTACT_WORKPHONE.equals("null"))
                SETGET.setCONTACT_WORKPHONE(CONTACT_WORKPHONE);

            if (GENDER != null && !GENDER.equals("null"))
                SETGET.setGENDER(GENDER);

            if (POSITION_ID != null && !POSITION_ID.equals("null"))
                SETGET.setPOSITION_ID(POSITION_ID);

            if (CUSTOMER_ID != null && !CUSTOMER_ID.equals("null"))
                SETGET.setCUSTOMER_ID(CUSTOMER_ID);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public CustomerDetail_ContactDetail_Setget get_SETGET(){
        return SETGET;
    }

    public Boolean get_STATUS(){
        return isStatus;
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
