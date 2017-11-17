package vn.altalab.app.crmvietpack.transaction.json;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.altalab.app.crmvietpack.DATE_CUSTOM;
import vn.altalab.app.crmvietpack.transaction.object.TransactionDetail_Setget;

public class TransactionDetail_Json {

    String data;
    Boolean isStatus = false;

    JSONObject jsonObject, jsonObject1;
    JSONArray jsonArray;

    TransactionDetail_Setget SETGET;
    ArrayList<TransactionDetail_Setget> LIST;

    String TRANSACTION_NAME_TEXT;
    String CUSTOMER_NAME;
    String ASSIGNER;
    String ASSIGNED_USER_NAME;
    String TELEPHONE;
    String START_DATE;
    String END_DATE;
    String TRANSACTION_TYPE_NAME;
    String TRANSACTION_DESCRIPTION;
    String STATUS;
    String PRIORITY;
    String TRANSACTION_TYPE_ID;
    String TRANSACTION_USER;
    String TRANSACTION_ID;
    long CUSTOMER_ID;

    public TransactionDetail_Json(JSONObject jsonObject) {

        this.jsonObject = jsonObject;

        try {

            if (jsonObject.getString("messages").equals("success")) {
                isStatus = true;
            }

            LIST = new ArrayList<>();

            SETGET = new TransactionDetail_Setget();

            jsonArray = jsonObject.getJSONArray("transaction");

            jsonObject1 = (JSONObject) jsonArray.get(0);

            TRANSACTION_NAME_TEXT = "";
            CUSTOMER_NAME = "";
            ASSIGNER = "";
            ASSIGNED_USER_NAME = "";
            TELEPHONE = "";
            START_DATE = "";
            END_DATE = "";
            TRANSACTION_TYPE_NAME = "";
            TRANSACTION_DESCRIPTION = "";
            STATUS = "";
            PRIORITY = "";
            TRANSACTION_TYPE_ID = "";
            TRANSACTION_USER = "";
            CUSTOMER_ID = 0;
            TRANSACTION_ID = "";

            try {
                TRANSACTION_TYPE_ID = jsonObject1.getString("TRANSACTION_TYPE_ID");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                TRANSACTION_ID = jsonObject1.getString("TRANSACTION_ID");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                CUSTOMER_ID = jsonObject1.getLong("CUSTOMER_ID");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                TRANSACTION_USER = jsonObject1.getString("TRANSACTION_USER");
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
                ASSIGNER = jsonObject1.getString("ASSIGNER");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                ASSIGNED_USER_NAME = jsonObject1.getString("ASSIGNED_USER_NAME");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                TELEPHONE = jsonObject1.getString("TELEPHONE");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                START_DATE = jsonObject1.getString("START_DATE");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                END_DATE = jsonObject1.getString("END_DATE");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                TRANSACTION_TYPE_NAME = jsonObject1.getString("TRANSACTION_TYPE_NAME");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                TRANSACTION_DESCRIPTION = jsonObject1.getString("TRANSACTION_DESCRIPTION");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                STATUS = jsonObject1.getString("STATUS");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                PRIORITY = jsonObject1.getString("PRIORITY");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (CUSTOMER_ID != 0) {
                SETGET.setCUSTOMER_ID(CUSTOMER_ID);
            }

            if (TRANSACTION_TYPE_ID != null && !TRANSACTION_TYPE_ID.equals("null")) {
                SETGET.setTRANSACTION_TYPE_ID(TRANSACTION_TYPE_ID);
            }

            if (TRANSACTION_ID != null && !TRANSACTION_ID.equals("null")) {
                SETGET.setTRANSACTION_ID(TRANSACTION_ID);
            }

            if (TRANSACTION_NAME_TEXT != null && !TRANSACTION_NAME_TEXT.equals("null")) {
                SETGET.setTRANSACTION_NAME_TEXT(TRANSACTION_NAME_TEXT);
            }

            if (CUSTOMER_NAME != null && !CUSTOMER_NAME.equals("null")) {
                SETGET.setCUSTOMER_NAME(CUSTOMER_NAME);
            }

            if (ASSIGNER != null && !ASSIGNER.equals("null")) {
                SETGET.setASSIGNER(ASSIGNER);
            }

            if (ASSIGNED_USER_NAME != null && !ASSIGNED_USER_NAME.equals("null")) {
                SETGET.setASSIGNED_USER_NAME(ASSIGNED_USER_NAME);
            }

            if (TELEPHONE != null && !TELEPHONE.equals("null")) {
                SETGET.setTELEPHONE(TELEPHONE);
            }

            if (TRANSACTION_USER != null && !TRANSACTION_USER.equals("null")) {
                SETGET.setTRANSACTION_USER(TRANSACTION_USER);
            }

            if (START_DATE != null && !START_DATE.trim().equals("null")) {
                if (START_DATE.length() > 11) {
                    String a = START_DATE.substring(0, 10).trim();

                    Log.e("neededdo_json", "START_DATE_A: " + a);
                    String b = START_DATE.substring(11, START_DATE.length() - 3).trim();

                    Log.e("neededdo_json", "START_DATE_B: " + b);
                    SETGET.setSTART_DATE(DATE_CUSTOM.getStringDate(a) + " " + b);
                }
            }

            if (END_DATE != null && !END_DATE.trim().equals("null")) {

                if (END_DATE.length() > 11) {

                    String a = END_DATE.substring(0, 10).trim();

                    Log.e("NEEDEDDO", "END_DATE_A: " + a);
                    String b = END_DATE.substring(11, END_DATE.length() - 3).trim();

                    Log.e("NEEDEDDO", "END_DATE_B: " + b);
                    SETGET.setEND_DATE(DATE_CUSTOM.getStringDate(a) + " " + b);

                }

            }

            if (TRANSACTION_TYPE_NAME != null && !TRANSACTION_TYPE_NAME.equals("null")) {
                SETGET.setTRANSACTION_TYPE_NAME(TRANSACTION_TYPE_NAME);
            }

            if (TRANSACTION_DESCRIPTION != null && !TRANSACTION_DESCRIPTION.equals("null")) {
                SETGET.setTRANSACTION_DESCRIPTION(TRANSACTION_DESCRIPTION);
            }

            if (STATUS != null && !STATUS.equals("STATUS")) {
                SETGET.setSTATUS(STATUS);
            }

            if (PRIORITY != null && !PRIORITY.equals("PRIORITY")) {
                SETGET.setPRIORITY(jsonObject1.getString("PRIORITY"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public TransactionDetail_Setget GET_SETGET() {
        return SETGET;
    }

    public Boolean isStatus() {
        return isStatus;
    }

}
