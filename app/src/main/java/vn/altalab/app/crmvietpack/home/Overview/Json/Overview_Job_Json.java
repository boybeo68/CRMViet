package vn.altalab.app.crmvietpack.home.Overview.Json;

import vn.altalab.app.crmvietpack.home.Overview.Setget.Overview_Job_Setget;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mac2 on 2/14/17.
 */

public class Overview_Job_Json {

    String data;
    JSONObject jsonObject, jsonObject1, jsonObject2;
    JSONArray jsonArray;
    String total = "0";
    String unfinish = "", finish = "";

    Overview_Job_Setget setgetTongquan;
    ArrayList<Overview_Job_Setget> list;

    Boolean success = false;

    Boolean next = false;

    public Overview_Job_Json(String data){
        this.data = data;

        try {
            jsonObject = new JSONObject(data);
            list = new ArrayList<>();

            if (jsonObject.getString("messages").equals("success")) {
                success = true ;
            }

            try{
                total = jsonObject.getString("total");
            } catch (Exception e){
                e.printStackTrace();
            }
            try {
                unfinish = jsonObject.getString("total_unfinish");
            } catch (Exception e){
                e.printStackTrace();
            }

            try {
                finish = jsonObject.getString("total_finish");
            } catch (Exception e){
                e.printStackTrace();
            }

            try {
                jsonArray = jsonObject.getJSONArray("data");
            } catch (Exception e){
                e.printStackTrace();
            }

            for (int i=0; i< jsonArray.length(); i++){
                jsonObject1 = (JSONObject) jsonArray.get(i);
                setgetTongquan = new Overview_Job_Setget();

                String USER_NAME = "";
                String USER_ID = "";
                String GROUP_NAME = "";
                String FINISHED = "";
                String UNFINISHED = "";

                try {
                    USER_NAME = jsonObject1.getString("USER_NAME");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    USER_ID = jsonObject1.getString("USER_ID");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    GROUP_NAME = jsonObject1.getString("GROUP_NAME");
                } catch (Exception e){
                    e.printStackTrace();
                }

                if (USER_NAME != null && !USER_NAME.equals("null"))
                setgetTongquan.setUSER_NAME(USER_NAME);

                if (USER_ID != null && !USER_ID.equals("null"))
                setgetTongquan.setUSER_ID(jsonObject1.getString("USER_ID"));

                if (GROUP_NAME != null && !GROUP_NAME.equals("null"))
                setgetTongquan.setGROUP_NAME(jsonObject1.getString("GROUP_NAME"));

                try {
                    jsonObject2 = jsonObject1.getJSONObject("TRANSACTION");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    FINISHED = jsonObject2.getString("FINISHED");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    UNFINISHED = jsonObject2.getString("UNFINISHED");
                } catch (Exception e){
                    e.printStackTrace();
                }

                if (FINISHED != null && !FINISHED.equals("null"))
                setgetTongquan.setFINISHED(jsonObject2.getString("FINISHED"));

                if (UNFINISHED != null && !UNFINISHED.equals("null"))
                setgetTongquan.setUNFINISHED(jsonObject2.getString("UNFINISHED"));

                list.add(setgetTongquan);
            }

            if (list.size() == 5) {
                next = true;
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Overview_Job_Setget> getListTongquan(){
        return list;
    }

    public String getTotal(){
        return total;
    }

    public String getTotalFinish(){
        return finish;
    }

    public String getTotalUnFinish() {
        return unfinish;
    }

    public Boolean getNext(){
        return next;
    }

    public Boolean getStatus(){
        return success;
    }

}
