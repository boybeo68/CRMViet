package vn.altalab.app.crmvietpack.home.Deathline;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import vn.altalab.app.crmvietpack.home.Home_Fragment;

/**
 * Created by mac2 on 2/16/17.
 */

public class Deathline_Json {
    Home_Fragment home_fragment = new Home_Fragment();
    String data;

    JSONObject jsonObject, jsonObject1;
    JSONArray jsonArray;

    Deathline_Setget SETGET;

    ArrayList<Deathline_Setget> list;

    Boolean success = false;

    String TRANSACTION_NAME_TEXT;
    String CUSTOMER_NAME;
    String TELEPHONE;
    String TRANSACTION_USER_NAME;
    String END_DATE;
    String TRANSACTION_DESCRIPTION;
    String TRANSACTION_ID;
    String NUM_OUT_DATE;
    String CUSTOMER_ID;
    String OFFICE_ADDRESS;

    public Deathline_Json(String data) {
        this.data = data;

        list = new ArrayList<>();

        try {

            jsonObject = new JSONObject(data);
            if (jsonObject.getString("messages").equals("success")) {
                success = true;
            }
            jsonArray = jsonObject.getJSONArray("transactions");

            for (int i = 0; i < jsonArray.length(); i++) {

                jsonObject1 = (JSONObject) jsonArray.get(i);
                SETGET = new Deathline_Setget();

                TRANSACTION_NAME_TEXT = "";
                CUSTOMER_NAME = "";
                TELEPHONE = "";
                TRANSACTION_USER_NAME = "";
                END_DATE = "";
                TRANSACTION_DESCRIPTION = "";
                TRANSACTION_ID = "";
                NUM_OUT_DATE = "";
                CUSTOMER_ID = "";
                OFFICE_ADDRESS = "";

                try {
                    CUSTOMER_ID = jsonObject1.getString("CUSTOMER_ID");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    TRANSACTION_ID = jsonObject1.getString("TRANSACTION_ID");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    TRANSACTION_NAME_TEXT = jsonObject1.getString("TRANSACTION_NAME_TEXT");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    CUSTOMER_NAME = jsonObject1.getString("CUSTOMER_NAME");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (home_fragment.phone_view == 0 && home_fragment.user_id != jsonObject1.getInt("CUSTOMER_OWNER") &&
                            home_fragment.user_id != 1) {
                        TELEPHONE = "********";
                        SETGET.setContactPhone(0);
                    } else {
                        TELEPHONE = jsonObject1.getString("TELEPHONE");
                    }


                    if (home_fragment.email_view == 0 && home_fragment.user_id != jsonObject1.getInt("CUSTOMER_OWNER") &&
                            home_fragment.user_id != 1) {
                        SETGET.setContactEmail(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    OFFICE_ADDRESS = jsonObject1.getString("OFFICE_ADDRESS");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    TRANSACTION_USER_NAME = jsonObject1.getString("REG_USER_NAME");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    END_DATE = jsonObject1.getString("END_DATE");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    TRANSACTION_DESCRIPTION = jsonObject1.getString("TRANSACTION_DESCRIPTION");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    NUM_OUT_DATE = jsonObject1.getString("NUM_OUT_DATE");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (CUSTOMER_ID != null && !CUSTOMER_ID.equals("null")) {
                    SETGET.setCUSTOMER_ID(CUSTOMER_ID);
                }

                if (TRANSACTION_ID != null && !TRANSACTION_ID.equals("null"))
                    SETGET.setTRANSACTION_ID(TRANSACTION_ID);

                if (TRANSACTION_NAME_TEXT != null && !TRANSACTION_NAME_TEXT.equals("null"))
                    SETGET.setTRANSACTION_NAME_TEXT(TRANSACTION_NAME_TEXT);

                if (CUSTOMER_NAME != null && !CUSTOMER_NAME.equals("null"))
                    SETGET.setCUSTOMER_NAME(CUSTOMER_NAME);

                if (OFFICE_ADDRESS != null && !OFFICE_ADDRESS.equals("null"))
                    SETGET.setOFFICE_ADDRESS(OFFICE_ADDRESS);


                if (TELEPHONE != null && !TELEPHONE.equals("null"))
                    SETGET.setTELEPHONE(TELEPHONE);


                if (TRANSACTION_USER_NAME != null && !TRANSACTION_USER_NAME.equals("null"))
                    SETGET.setTRANSACTION_USER_NAME(TRANSACTION_USER_NAME);

                if (END_DATE != null && !END_DATE.trim().equals("null")) {
                    if (END_DATE.length() > 11) {

                        String a = END_DATE.substring(0, 10).trim();

                        Log.e("DEATHLINE", "END_DATE_A: " + a);
                        String b = END_DATE.substring(11, END_DATE.length() - 3).trim();


                        Log.e("DEATHLINE", "END_DATE_B: " + b);
                        SETGET.setEND_DATE(getStringDate(a) + " " + b);
                    }
                }


                if (TRANSACTION_DESCRIPTION != null && !TRANSACTION_DESCRIPTION.equals("null"))
                    SETGET.setTRANSACTION_DESCRIPTION(TRANSACTION_DESCRIPTION);


                if (NUM_OUT_DATE != null && !NUM_OUT_DATE.equals("null"))
                    SETGET.setNUM_OUT_DATE(NUM_OUT_DATE);


                list.add(SETGET);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Deathline_Setget> getList() {
        return list;
    }

    public Boolean getStatus() {
        return success;
    }

    public String getStringDate(String response) {

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(response);
            response = new SimpleDateFormat("dd/MM/yyyy").format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;

    }

}
