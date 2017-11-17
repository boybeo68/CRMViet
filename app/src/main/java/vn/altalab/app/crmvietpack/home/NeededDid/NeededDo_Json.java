package vn.altalab.app.crmvietpack.home.NeededDid;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.altalab.app.crmvietpack.DATE_CUSTOM;
import vn.altalab.app.crmvietpack.home.Home_Fragment;

public class NeededDo_Json {

    String data;
    JSONObject jsonObject, jsonObject1, jsonObject2;
    JSONArray jsonArray, jsonArray1;
    NeededDo_Setget SETGET;
    ArrayList<NeededDo_Setget> LIST;
    Boolean isStatus = false;
    Home_Fragment home_fragment = new Home_Fragment();
    String TRANSACTION_ID;
    String TRANSACTION_NAME_TEXT;
    String CUSTOMER_NAME;
    String ASSIGNED_USER_NAME;
    String TELEPHONE;
    String START_DATE;
    String END_DATE;
    String TRANSACTION_DESCRIPTION;
    String CUSTOMER_ID;
    String OFFICE_ADDRESS;

    public NeededDo_Json(String data) {
        this.data = data;
        LIST = new ArrayList<>();

        try {
            jsonObject = new JSONObject(data);

            if (jsonObject.getString("messages").equals("success")) {
                isStatus = true;
            }

            jsonArray = jsonObject.getJSONArray("transactions");

            for (int i = 0; i < jsonArray.length(); i++) {

                jsonObject1 = (JSONObject) jsonArray.get(i);

                String DATE = "0000-00-00";

                try {
                    DATE = jsonObject1.getString("date");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String WORK_COURT = "";

                try {
                    WORK_COURT = jsonObject1.getString("work_count");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                jsonArray1 = jsonObject1.getJSONArray("data");

                for (int j = 0; j < jsonArray1.length(); j++) {
                    jsonObject2 = (JSONObject) jsonArray1.get(j);
                    SETGET = new NeededDo_Setget();

                    TRANSACTION_ID = "";
                    TRANSACTION_NAME_TEXT = "";
                    CUSTOMER_NAME = "";
                    ASSIGNED_USER_NAME = "";
                    TELEPHONE = "";
                    START_DATE = "";
                    END_DATE = "";
                    TRANSACTION_DESCRIPTION = "";
                    CUSTOMER_ID = "";
                    OFFICE_ADDRESS = "";

                    try {
                        CUSTOMER_ID = jsonObject2.getString("CUSTOMER_ID");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        TRANSACTION_ID = jsonObject2.getString("TRANSACTION_ID");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        TRANSACTION_NAME_TEXT = jsonObject2.getString("TRANSACTION_NAME_TEXT");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        CUSTOMER_NAME = jsonObject2.getString("CUSTOMER_NAME");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        OFFICE_ADDRESS = jsonObject2.getString("OFFICE_ADDRESS");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        if (home_fragment.phone_view == 0 && home_fragment.user_id != jsonObject2.getInt("CUSTOMER_OWNER") &&
                                home_fragment.user_id != 1) {
                            TELEPHONE = "*********";
                            SETGET.setContactPhone(0);
                        } else {
                            TELEPHONE = jsonObject2.getString("TELEPHONE");
                        }

                        if (home_fragment.email_view == 0 && home_fragment.user_id != jsonObject2.getInt("CUSTOMER_OWNER") &&
                                home_fragment.user_id != 1) {
                            SETGET.setContactEmail(0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        ASSIGNED_USER_NAME = jsonObject2.getString("ASSIGNED_USER_NAME");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        START_DATE = jsonObject2.getString("START_DATE");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        END_DATE = jsonObject2.getString("END_DATE");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        TRANSACTION_DESCRIPTION = jsonObject2.getString("TRANSACTION_DESCRIPTION");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (CUSTOMER_ID != null && !CUSTOMER_ID.equals("null")) {
                        SETGET.setCUSTOMER_ID(CUSTOMER_ID);
                    }

                    if (TRANSACTION_ID != null && !TRANSACTION_ID.trim().equals("null"))
                        SETGET.setTRANSACTION_ID(TRANSACTION_ID);

                    if (TRANSACTION_NAME_TEXT != null && !TRANSACTION_NAME_TEXT.trim().equals("null"))
                        SETGET.setTRANSACTION_NAME_TEXT(TRANSACTION_NAME_TEXT);

                    if (CUSTOMER_NAME != null && !CUSTOMER_NAME.trim().equals("null"))
                        SETGET.setCUSTOMER_NAME(CUSTOMER_NAME);


                    if (OFFICE_ADDRESS != null && !OFFICE_ADDRESS.trim().equals("null"))
                        SETGET.setOFFICE_ADDRESS(OFFICE_ADDRESS);

                    if (TELEPHONE != null && !TELEPHONE.trim().equals("null"))
                        SETGET.setTELEPHONE(TELEPHONE);

                    if (ASSIGNED_USER_NAME != null && !ASSIGNED_USER_NAME.trim().equals("null"))
                        SETGET.setASSIGNED_USER_NAME(ASSIGNED_USER_NAME);

                    if (START_DATE != null && !START_DATE.trim().equals("null")) {

                        if (START_DATE.length() > 11) {

                            String a = START_DATE.substring(0, 10).trim();

                            String b = START_DATE.substring(11, START_DATE.length() - 3).trim();

                            SETGET.setSTART_DATE(DATE_CUSTOM.getStringDate(a) + " " + b);

                        }

                    }

                    if (END_DATE != null && !END_DATE.trim().equals("null")) {

                        if (END_DATE.length() > 11) {

                            String a = END_DATE.substring(0, 10).trim();

                            String b = END_DATE.substring(11, END_DATE.length() - 3).trim();

                            SETGET.setEND_DATE(DATE_CUSTOM.getStringDate(a) + " " + b);

                        }

                    }

                    if (TRANSACTION_DESCRIPTION != null && !TRANSACTION_DESCRIPTION.trim().equals("null"))
                        SETGET.setTRANSACTION_DESCRIPTION(TRANSACTION_DESCRIPTION);

                    if (DATE != null && !DATE.equals("null"))
                        SETGET.setDATE(DATE);

                    if (j == 0) {
                        SETGET.setWORK_COURT(WORK_COURT);
                    } else SETGET.setWORK_COURT("");

                    LIST.add(SETGET);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ArrayList<NeededDo_Setget> GET_LIST() {
        return LIST;
    }

    public Boolean GET_STATUS() {
        return isStatus;
    }

}
