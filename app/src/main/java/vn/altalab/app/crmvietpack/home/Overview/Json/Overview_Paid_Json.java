package vn.altalab.app.crmvietpack.home.Overview.Json;

import vn.altalab.app.crmvietpack.home.Overview.Setget.Overview_Paid_Setget;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Overview_Paid_Json {

    String data;
    JSONObject jsonObject;
    JSONObject jsonObject1;
    JSONArray jsonArray;

    Boolean success = false;

    ArrayList<Overview_Paid_Setget> listTongquan;
    Overview_Paid_Setget setgetTongquan;

    String tong = "0";

    public Overview_Paid_Json(String data){
        this.data = data;

        listTongquan = new ArrayList<>();

        try {
            jsonObject = new JSONObject(data);

            if (jsonObject.getString("messages").equals("success")) {
                success = true ;
            }

            tong = jsonObject.getString("total");
            jsonArray = jsonObject.getJSONArray("payments");

            for (int i = 0; i < jsonArray.length(); i++){
                jsonObject1 = (JSONObject) jsonArray.get(i);
                setgetTongquan = new Overview_Paid_Setget();

                String PAYMENT_DATE = "";
                String TOTAL_MONEY = "";

                try{
                    PAYMENT_DATE = jsonObject1.getString("PAYMENT_DATE");
                }catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    TOTAL_MONEY = jsonObject1.getString("TOTAL_MONEY");
                } catch (Exception e){
                    e.printStackTrace();
                }

                if (PAYMENT_DATE != null && !PAYMENT_DATE.equals("null"))
                setgetTongquan.setPAYMENT_DATE(PAYMENT_DATE);

                if (TOTAL_MONEY != null && !TOTAL_MONEY.equals("null"))
                setgetTongquan.setTOTAL_MONEY(String.valueOf(Long.parseLong(TOTAL_MONEY)));

                listTongquan.add(setgetTongquan);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Overview_Paid_Setget> getList(){
        return listTongquan;
    }

    public long getTong(){
        return Long.parseLong(tong)/1000;
    }

    public Boolean getStatus(){
        return success;
    }

}
