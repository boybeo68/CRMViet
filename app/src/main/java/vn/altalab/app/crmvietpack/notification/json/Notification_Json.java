package vn.altalab.app.crmvietpack.notification.json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import vn.altalab.app.crmvietpack.home.Consigned.Consigned_Setget;
import vn.altalab.app.crmvietpack.notification.object.NotificationService_Setget;

public class Notification_Json {

    String data;

    JSONObject jsonObject, jsonObject1;
    JSONArray jsonArray;

    NotificationService_Setget notificationServiceSetget;

    ArrayList<NotificationService_Setget> list;

    Boolean success = false;

    public Notification_Json(String data){
        this.data = data;

        list = new ArrayList<>();

        try {

            jsonObject = new JSONObject(data);

            if (jsonObject.getString("messages").equals("success")) {
                success = true ;
            }

            jsonArray = jsonObject.getJSONArray("transactions");

            for (int i =0 ; i< jsonArray.length(); i++){
                jsonObject1 = (JSONObject) jsonArray.get(i);
                notificationServiceSetget = new NotificationService_Setget();

                String TRANSACTION_ID = "";
                String TRANSACTION_NAME_TEXT = "";

                try {
                    TRANSACTION_ID = jsonObject1.getString("TRANSACTION_ID");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    TRANSACTION_NAME_TEXT = jsonObject1.getString("TRANSACTION_NAME_TEXT");
                } catch (Exception e){
                    e.printStackTrace();
                }

                if (TRANSACTION_ID != null && !TRANSACTION_ID.equals("null"))
                notificationServiceSetget.setTRANSACTION_ID(TRANSACTION_ID);

                if (TRANSACTION_NAME_TEXT != null && !TRANSACTION_NAME_TEXT.equals("null"))
                notificationServiceSetget.setTRANSACTION_NAME_TEXT(TRANSACTION_NAME_TEXT);

                list.add(notificationServiceSetget);

            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<NotificationService_Setget> getList(){
        return list;
    }

    public Boolean getStatus(){
        return success;
    }

}
