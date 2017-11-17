package vn.altalab.app.crmvietpack.warehouse.json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.altalab.app.crmvietpack.warehouse.object.Warehouse_Setget;

public class Warehouse_Json {

    String data;
    Boolean messages = false;
    JSONObject jsonObject, jsonObject1;
    JSONArray jsonArray;

    Warehouse_Setget warehouseSetget;

    ArrayList<Warehouse_Setget> listWarehouse;

    public Warehouse_Json(String data) {

        this.data = data;

        listWarehouse = new ArrayList<>();

        try {

            jsonObject = new JSONObject(data);

            if (jsonObject.getString("messages").equals("success")) {
                messages = true;
            }

            jsonArray = jsonObject.getJSONArray("warehouses");

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject1 = (JSONObject) jsonArray.get(i);

                warehouseSetget = new Warehouse_Setget();

                warehouseSetget.setWAREHOUSE_ID(jsonObject1.getString("WAREHOUSE_ID"));

                warehouseSetget.setWAREHOUSE_NAME(jsonObject1.getString("WAREHOUSE_NAME"));

                listWarehouse.add(warehouseSetget);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Warehouse_Setget> get_Warehouse_List() {
        return listWarehouse;
    }

    public Boolean getStatus() {
        return messages;
    }

}
