package vn.altalab.app.crmvietpack.transaction.json;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import vn.altalab.app.crmvietpack.DATE_CUSTOM;
import vn.altalab.app.crmvietpack.transaction.object.Transaction_Setget;

public class Transaction_Json {

    JSONObject jsonObject, jsonObject1;
    JSONArray jsonArray;
    Boolean isStatus = false;
    Transaction_Setget SETGET;
    ArrayList<Transaction_Setget> list;

    String TRANSACTION_ID = "";
    String CHECK_SHARE = "";
    String END_DATE = "";
    String PRIORITY = "";
    String REG_DTTM = "";
    String REG_USER = "";
    String START_DATE = "";
    String STATUS = "";
    String TRANSACTION_TYPE_ID = "";

    public Transaction_Json(JSONObject jsonObject){
        this.jsonObject = jsonObject;

        try {
            if (jsonObject.getString("messages").equals("success")){
                isStatus = true;
            }

            jsonArray = jsonObject.getJSONArray("transactions");

            for (int i = 0; i < jsonArray.length(); i++){
                jsonObject1 = (JSONObject) jsonArray.get(i);
                SETGET = new Transaction_Setget();

                try {
                    TRANSACTION_ID = jsonObject1.getString("TRANSACTION_ID");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    CHECK_SHARE = jsonObject1.getString("CHECK_SHARE");
                }catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    END_DATE = jsonObject1.getString("END_DATE");
                }catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    PRIORITY = jsonObject1.getString("PRIORITY");
                }catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    REG_DTTM = jsonObject1.getString("REG_DTTM");
                }catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    REG_USER = jsonObject1.getString("REG_USER");
                }catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    START_DATE = jsonObject1.getString("START_DATE");
                }catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    STATUS = jsonObject1.getString("STATUS");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    TRANSACTION_TYPE_ID = jsonObject1.getString("TRANSACTION_TYPE_ID");
                }catch (Exception e){
                    e.printStackTrace();
                }

                if (TRANSACTION_ID != null && !TRANSACTION_ID.equals("null"))
                    SETGET.setTRANSACTION_ID(TRANSACTION_ID);

                if (CHECK_SHARE != null && !CHECK_SHARE.equals("null"))
                    SETGET.setCHECK_SHARE(CHECK_SHARE);

                if (PRIORITY != null && !PRIORITY.equals("null"))
                    SETGET.setPRIORITY(PRIORITY);

                if (REG_DTTM != null && !REG_DTTM.equals("null"))
                    SETGET.setREG_DTTM(REG_DTTM);

                if (REG_USER != null && !REG_USER.equals("null"))
                    SETGET.setREG_USER(REG_USER);

                if (START_DATE != null && !START_DATE.trim().equals("null")) {

                    if (START_DATE.length() > 11) {

                        String a = START_DATE.substring(0, 10).trim();

                        Log.e("neededdo_json", "START_DATE_A: "  + a);
                        String b = START_DATE.substring(11, START_DATE.length() - 3).trim();

                        Log.e("neededdo_json", "START_DATE_B: "  + b);
                        SETGET.setSTART_DATE(DATE_CUSTOM.getStringDate(a) + " " + b);

                    }

                }

                if (END_DATE != null && !END_DATE.trim().equals("null")) {

                    if (END_DATE.length() > 11) {

                        String a = END_DATE.substring(0, 10).trim();

                        Log.e("NEEDEDDO", "END_DATE_A: "  + a);
                        String b = END_DATE.substring(11, END_DATE.length() - 3).trim();

                        Log.e("NEEDEDDO", "END_DATE_B: "  + b);
                        SETGET.setEND_DATE(DATE_CUSTOM.getStringDate(a) + " " + b);

                    }

                }

                if (STATUS != null && !STATUS.equals("null"))
                    SETGET.setSTATUS(STATUS);

                if (TRANSACTION_TYPE_ID != null && !TRANSACTION_TYPE_ID.equals("null"))
                    SETGET.setTRANSACTION_TYPE_ID(TRANSACTION_TYPE_ID);

                list.add(SETGET);

            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public ArrayList<Transaction_Setget> getList(){
        return list;
    }

    public Boolean getStatus(){
        return isStatus;
    }

}
