package vn.altalab.app.crmvietpack.notification.json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.altalab.app.crmvietpack.notification.object.Notifications_Setget;

public class NotificationCustomer_Json {

    JSONObject jsonObject, jsonObject1;
    JSONArray jsonArray;
    Boolean isStatus = false;

    Notifications_Setget SETGET;
    ArrayList<Notifications_Setget> LIST;

    String NOTIFICATION_ID;
    String NOTIFICATION_NAME;
    String USERCHANGE;
    String OBJECTS_ID;
    String TIMECHANGE;
    String TYPE_CHANGE;
    String TYPE_OBJECT;
    String VIEWSTATUS;

    public NotificationCustomer_Json(JSONObject jsonObject){
        this.jsonObject = jsonObject;

        LIST = new ArrayList<>();

        try {

            if (jsonObject.getString("messages").equals("success")){
                isStatus = true;
            }

            jsonArray = jsonObject.getJSONArray("cus_notif");

            for (int i = 0; i< jsonArray.length(); i++){
                jsonObject1 = (JSONObject) jsonArray.get(i);

                SETGET = new Notifications_Setget();

                NOTIFICATION_ID = "";
                NOTIFICATION_NAME = "";
                USERCHANGE = "";
                OBJECTS_ID = "";
                TIMECHANGE = "";
                TYPE_CHANGE = "";
                TYPE_OBJECT = "";
                VIEWSTATUS = "";

                try {
                    NOTIFICATION_ID = jsonObject1.getString("NOTIFICATION_ID");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    NOTIFICATION_NAME = jsonObject1.getString("NOTIFICATION_NAME");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    USERCHANGE = jsonObject1.getString("USERCHANGE");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    OBJECTS_ID = jsonObject1.getString("OBJECTS_ID");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    TIMECHANGE = jsonObject1.getString("TIMECHANGE");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    TYPE_CHANGE = jsonObject1.getString("TYPE_CHANGE");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    TYPE_OBJECT = jsonObject1.getString("TYPE_OBJECT");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    VIEWSTATUS = jsonObject1.getString("VIEWSTATUS");
                } catch (Exception e){
                    e.printStackTrace();
                }

//                if (NOTIFICATION_ID != null && !NOTIFICATION_ID.equals("null")){
//                    SETGET.setNOTIFICATION_ID(NOTIFICATION_ID);
//                }
//
//                if (NOTIFICATION_NAME != null && !NOTIFICATION_NAME.equals("null")){
//                    SETGET.setNOTIFICATION_NAME(NOTIFICATION_NAME);
//                }
//
//                if (USERCHANGE != null && !USERCHANGE.equals("null")){
//                    SETGET.setUSERCHANGE(NOTIFICATION_NAME);
//                }
//
//                if (OBJECTS_ID != null && !OBJECTS_ID.equals("null")){
//                    SETGET.setOBJECTS_ID(OBJECTS_ID);
//                }
//
//                if (TIMECHANGE != null && !TIMECHANGE.equals("null")){
//                    SETGET.setTIMECHANGE(TIMECHANGE);
//                }
//
//                if (TYPE_CHANGE != null && !TYPE_CHANGE.equals("null")){
//                    SETGET.setTYPE_CHANGE(TYPE_CHANGE);
//                }
//
//                if (TYPE_OBJECT != null && !TYPE_OBJECT.equals("null")) {
//                    SETGET.setTYPE_OBJECT(TYPE_OBJECT);
//                }
//
//                if (VIEWSTATUS != null && !VIEWSTATUS.equals("null")) {
//                    SETGET.setVIEWSTATUS(VIEWSTATUS);
//                }

                LIST.add(SETGET);

            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public ArrayList<Notifications_Setget> get_LIST(){
        return LIST;
    }

    public Boolean getIsStatus(){
        return isStatus;
    }

}
