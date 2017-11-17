package vn.altalab.app.crmvietpack.home.Overview.Json;

import org.json.JSONObject;

public class Overview_Customer_Json {

    String data = "";
    String nhanvienmoi = "";
    String tongnhanvien = "";
    String phantram = "";

    Boolean success = false;

    public Overview_Customer_Json(String data){
        this.data = data;
        try {

            JSONObject jsonObject = new JSONObject(data);

            if (jsonObject.getString("messages").equals("success")) {
                success = true ;
            }

            try {
                nhanvienmoi = jsonObject.getString("new_customers");
            } catch (Exception e){
                e.printStackTrace();
            }

            try {
                tongnhanvien = jsonObject.getString("customers");
            } catch (Exception e){
                e.printStackTrace();
            }

            try {
                phantram = jsonObject.getString("change_percent");
            } catch (Exception e){
                e.printStackTrace();
            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public String getNhanvienmoi(){
        return nhanvienmoi;
    }

    public String getTongnhanvien(){
        return tongnhanvien;
    }

    public String getPhantram(){
        return phantram;
    }

    public Boolean getStatus(){
        return success;
    }

}
