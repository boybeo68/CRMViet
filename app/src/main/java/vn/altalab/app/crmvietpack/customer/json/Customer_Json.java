package vn.altalab.app.crmvietpack.customer.json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.altalab.app.crmvietpack.customer.Customer_MainFragment;
import vn.altalab.app.crmvietpack.customer.setget.Customer_Setget;

public class Customer_Json {

    JSONObject jsonObject, jsonObject1;
    JSONArray jsonArray;

    String data = "";

    ArrayList<Customer_Setget> list;
    Boolean isStatus = false;
    Customer_MainFragment customer_mainFragment = new Customer_MainFragment();


    public Customer_Json(String data) {
        this.data = data;

        list = new ArrayList<>();

        try {

            jsonObject = new JSONObject(data);

            if (jsonObject.getString("messages").equals("success")) {
                isStatus = true;
            }

            jsonArray = jsonObject.getJSONArray("customers");

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject1 = (JSONObject) jsonArray.get(i);
                Customer_Setget customerSetget = new Customer_Setget();

                String CUSTOMER_ID = "";
                String OFFICE_ADDRESS = "";
                String CUSTOMER_EMAIL = "";
                String CUSTOMER_NAME = "";
                String TELEPHONE = "";
                String LINK_IMAGE = "";
                try {
                    CUSTOMER_ID = jsonObject1.getString("CUSTOMER_ID");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    LINK_IMAGE = jsonObject1.getString("LINK_IMAGE");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    OFFICE_ADDRESS = jsonObject1.getString("OFFICE_ADDRESS");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (customer_mainFragment.email_view == 0 && customer_mainFragment.user_id != jsonObject1.getInt("CUSTOMER_OWNER") &&
                            customer_mainFragment.user_id != 1) {
                        CUSTOMER_EMAIL = "**********";
                        customerSetget.setContactEmail(0);
                    } else {
                        CUSTOMER_EMAIL = jsonObject1.getString("CUSTOMER_EMAIL");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    CUSTOMER_NAME = jsonObject1.getString("CUSTOMER_NAME");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (customer_mainFragment.phone_view == 0 && customer_mainFragment.user_id != jsonObject1.getInt("CUSTOMER_OWNER") &&
                            customer_mainFragment.user_id != 1) {
                        TELEPHONE = "**********";
                        customerSetget.setContactPhone(0);

                    } else {
                        TELEPHONE = jsonObject1.getString("TELEPHONE");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (CUSTOMER_ID != null && !CUSTOMER_ID.equals("null"))
                    customerSetget.setCUSTOMER_ID(CUSTOMER_ID);

                if (LINK_IMAGE != null && !LINK_IMAGE.equals("null")){

                        customerSetget.setLINK_IMAGE(LINK_IMAGE);


                }

                if (OFFICE_ADDRESS != null && !OFFICE_ADDRESS.equals("null"))
                    customerSetget.setOFFICE_ADDRESS(OFFICE_ADDRESS);

                if (CUSTOMER_EMAIL != null && !CUSTOMER_EMAIL.equals("null"))
                    customerSetget.setCUSTOMER_EMAIL(CUSTOMER_EMAIL);

                if (CUSTOMER_NAME != null && !CUSTOMER_NAME.equals("null"))
                    customerSetget.setCUSTOMER_NAME(CUSTOMER_NAME);

                if (TELEPHONE != null && !TELEPHONE.equals("null"))
                    customerSetget.setTELEPHONE(TELEPHONE);

                list.add(customerSetget);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public ArrayList<Customer_Setget> getList() {
        return list;
    }

    public Boolean getStatus() {
        return isStatus;
    }
}
