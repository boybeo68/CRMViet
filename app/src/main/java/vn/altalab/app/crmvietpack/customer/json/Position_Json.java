package vn.altalab.app.crmvietpack.customer.json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.altalab.app.crmvietpack.customer.setget.Position_Setget;

public class Position_Json {

    JSONObject jsonObject, jsonObject1;
    JSONArray jsonArray;;
    String data;

    Position_Setget SETGET;
    ArrayList<Position_Setget> LIST;

    Boolean isStatus = false;

    public Position_Json(String data){
        this.data = data;
        LIST = new ArrayList<>();

        try {
            jsonObject = new JSONObject(data);

            if (jsonObject.getString("messages").equals("success")){
                isStatus = true;
            }

            jsonArray = jsonObject.getJSONArray("position");

            for (int i=0 ; i< jsonArray.length(); i++) {
                jsonObject1 = (JSONObject) jsonArray.get(i);
                SETGET = new Position_Setget();

                String POSITION_ID = "";
                String POSITION_DESCRIPTION = "";
                String POSITION_NAME = "";

                try {
                    POSITION_ID = jsonObject1.getString("POSITION_ID");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    POSITION_DESCRIPTION = jsonObject1.getString("POSITION_DESCRIPTION");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    POSITION_NAME = jsonObject1.getString("POSITION_NAME");
                } catch (Exception e){
                    e.printStackTrace();
                }

                if (POSITION_ID != null && !POSITION_ID.equals("null")){
                    SETGET.setPOSITION_ID(POSITION_ID);
                }

                if (POSITION_DESCRIPTION != null && !POSITION_DESCRIPTION.equals("null")){
                    SETGET.setPOSITION_DESCRIPTION(POSITION_DESCRIPTION);
                }

                if (POSITION_NAME != null && !POSITION_NAME.equals("null")){
                    SETGET.setPOSITION_NAME(POSITION_NAME);
                }

                LIST.add(SETGET);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Position_Setget> getList(){
        return LIST;
    }

    public Boolean isStatus(){
        return isStatus;
    }
}
