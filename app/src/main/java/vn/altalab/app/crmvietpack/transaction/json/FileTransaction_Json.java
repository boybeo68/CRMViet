package vn.altalab.app.crmvietpack.transaction.json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.altalab.app.crmvietpack.transaction.object.FileTransaction_Setget;

/**
 * Created by Tung on 8/8/2017.
 */

public class FileTransaction_Json {
    String data;
    Boolean isStatus = false;


    JSONArray jsonArray;
    ArrayList<FileTransaction_Setget> list;

    String FILE_TRANSACTION_NAME;
    int FILE_TRANSACTION_ID;

    public FileTransaction_Json(JSONObject jsonObject) {
        list = new ArrayList<>();

        try {

            if (jsonObject.getString("messages").equals("success")) {
                isStatus = true;
            }





            jsonArray = jsonObject.getJSONArray("files");


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                FileTransaction_Setget SETGET = new FileTransaction_Setget();


                try {
                    FILE_TRANSACTION_NAME = jsonObject1.getString("FILE_TRANSACTION_NAME");
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (FILE_TRANSACTION_NAME != "") {
                    SETGET.setFILE_TRANSACTION_NAME(FILE_TRANSACTION_NAME);
                }

                list.add(SETGET);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public ArrayList<FileTransaction_Setget> getList() {
        return list;
    }

    public Boolean isStatus() {
        return isStatus;
    }

}

