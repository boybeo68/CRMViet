package vn.altalab.app.crmvietpack.transaction.json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.altalab.app.crmvietpack.transaction.object.TransactionType_Setget;

public class TransactionType_Json {

    String DATA;
    ArrayList<TransactionType_Setget> LIST;
    ArrayList<String> LIST_TRANSACTION_TYPE_ID, LIST_TRANSACTION_TYPE_NAME;
    TransactionType_Setget SETGET;

    JSONObject jsonObject, jsonObject1;
    JSONArray jsonArray;
    Boolean isStatus = false;

    String TRANSACTION_TYPE_ID ;
    String TRANSACTION_TYPE_NAME ;
    String TRANSACTION_TYPE_DESCRIPTION ;

    public TransactionType_Json(String DATA){
        this.DATA = DATA;
        LIST = new ArrayList<>();
        LIST_TRANSACTION_TYPE_ID = new ArrayList<>();
        LIST_TRANSACTION_TYPE_NAME = new ArrayList<>();

        try{
            jsonObject = new JSONObject(DATA);

            if (jsonObject.getString("messages").equals("success")){
                isStatus = true;
            }

            jsonArray = jsonObject.getJSONArray("transaction_types");

            for (int i=0; i< jsonArray.length(); i++){
                jsonObject1 = (JSONObject) jsonArray.get(i);
                SETGET = new TransactionType_Setget();

                TRANSACTION_TYPE_ID = "";
                TRANSACTION_TYPE_NAME = "";
                TRANSACTION_TYPE_DESCRIPTION = "";

                try {
                    TRANSACTION_TYPE_ID = jsonObject1.getString("TRANSACTION_TYPE_ID");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    TRANSACTION_TYPE_NAME = jsonObject1.getString("TRANSACTION_TYPE_NAME");
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    TRANSACTION_TYPE_DESCRIPTION = jsonObject1.getString("TRANSACTION_TYPE_DESCRIPTION");
                } catch (Exception e){
                    e.printStackTrace();
                }

                if (TRANSACTION_TYPE_ID != null && !TRANSACTION_TYPE_ID.equals("null")) {
                    SETGET.setTRANSACTION_TYPE_ID(TRANSACTION_TYPE_ID);
                    LIST_TRANSACTION_TYPE_ID.add(TRANSACTION_TYPE_ID);
                }

                if (TRANSACTION_TYPE_NAME != null && !TRANSACTION_TYPE_NAME.equals("null")) {
                    SETGET.setTRANSACTION_TYPE_NAME(TRANSACTION_TYPE_NAME);
                    LIST_TRANSACTION_TYPE_NAME.add(TRANSACTION_TYPE_NAME);
                }

                if (TRANSACTION_TYPE_DESCRIPTION != null && !TRANSACTION_TYPE_DESCRIPTION.equals("null"))
                SETGET.setTRANSACTION_TYPE_DESCRIPTION(TRANSACTION_TYPE_DESCRIPTION);

                LIST.add(SETGET);

            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public ArrayList<TransactionType_Setget> get_LIST(){
        return LIST;
    }

    public ArrayList<String> get_LIST_TRANSACTION_TYPE_ID(){
        return LIST_TRANSACTION_TYPE_ID;
    }

    public ArrayList<String> get_LIST_TRANSACTION_TYPE_NAME(){
        return LIST_TRANSACTION_TYPE_NAME;
    }

    public Boolean getIsStatus(){
        return isStatus;
    }

}
