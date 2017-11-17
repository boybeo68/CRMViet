package vn.altalab.app.crmvietpack.customer.customer_detail.json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.altalab.app.crmvietpack.Link;
import vn.altalab.app.crmvietpack.customer.customer_detail.CustomerDetail_Contact_Fragment;
import vn.altalab.app.crmvietpack.customer.customer_detail.object.CustomerDetail_Contact_Listview_Setget;

public class CustomerDetail_Contact_Json {

    ArrayList<CustomerDetail_Contact_Listview_Setget> List;
    CustomerDetail_Contact_Listview_Setget SETGET;

    JSONObject jsonObject, jsonObject1;
    JSONArray jsonArray;
    CustomerDetail_Contact_Fragment customerDetail_contact_fragment = new CustomerDetail_Contact_Fragment();
    Boolean success = false;
    Link link = new Link();

    public CustomerDetail_Contact_Json(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        List = new ArrayList<>();

        try {

            if (jsonObject.getString("messages").equals("success")) {
                success = true;
            }

            jsonArray = jsonObject.getJSONArray("contact");

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject1 = (JSONObject) jsonArray.get(i);
                SETGET = new CustomerDetail_Contact_Listview_Setget();

                String CONTACT_ID = "";
                String CONTACT_ADDRESS = "";
                String CONTACT_EMAIL = "";
                String CONTACT_FULL_NAME = "";
                String CONTACT_MOBIPHONE = "";
                String GENDER = "";
                String POSITION_NAME = "";

                try {
                    CONTACT_ID = jsonObject1.getString("CONTACT_ID");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    CONTACT_ADDRESS = jsonObject1.getString("CONTACT_ADDRESS");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if(link.getId().equals("1")){
                        CONTACT_EMAIL = jsonObject1.getString("CONTACT_EMAIL");
                    }else {
                        if (customerDetail_contact_fragment.contactEmail == 0) {
                            CONTACT_EMAIL = "*********";
                        } else {
                            CONTACT_EMAIL = jsonObject1.getString("CONTACT_EMAIL");
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    CONTACT_FULL_NAME = jsonObject1.getString("CONTACT_FULL_NAME");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if(link.getId().equals("1")){
                        CONTACT_MOBIPHONE = jsonObject1.getString("CONTACT_MOBIPHONE");
                    }else {
                        if (customerDetail_contact_fragment.contactPhone == 0) {
                            CONTACT_MOBIPHONE = "*********";
                        } else {
                            CONTACT_MOBIPHONE = jsonObject1.getString("CONTACT_MOBIPHONE");
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    GENDER = jsonObject1.getString("GENDER");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    POSITION_NAME = jsonObject1.getString("POSITION_NAME");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (CONTACT_ID != null && !CONTACT_ID.equals("null"))
                    SETGET.setCONTACT_ID(CONTACT_ID);

                if (CONTACT_ADDRESS != null && !CONTACT_ADDRESS.equals("null"))
                    SETGET.setCONTACT_ADDRESS(CONTACT_ADDRESS);

                if (CONTACT_EMAIL != null && !CONTACT_EMAIL.equals("null"))
                    SETGET.setCONTACT_EMAIL(CONTACT_EMAIL);

                if (CONTACT_FULL_NAME != null && !CONTACT_FULL_NAME.equals("null"))
                    SETGET.setCONTACT_FULL_NAME(CONTACT_FULL_NAME);

                if (CONTACT_MOBIPHONE != null && !CONTACT_MOBIPHONE.equals("null"))
                    SETGET.setCONTACT_MOBIPHONE(CONTACT_MOBIPHONE);

                if (GENDER != null && !GENDER.equals("null"))
                    SETGET.setGENDER(GENDER);

                if (POSITION_NAME != null && !POSITION_NAME.equals("null"))
                    SETGET.setPOSITION_NAME(POSITION_NAME);

                List.add(SETGET);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<CustomerDetail_Contact_Listview_Setget> get_CustomerDetail_Contact_Json_List() {
        return List;
    }

    public Boolean getStatus() {
        return success;
    }


}
