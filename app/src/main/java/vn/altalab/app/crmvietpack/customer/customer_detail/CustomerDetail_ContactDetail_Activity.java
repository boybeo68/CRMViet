package vn.altalab.app.crmvietpack.customer.customer_detail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import vn.altalab.app.crmvietpack.Link;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.Shared_Preferences;
import vn.altalab.app.crmvietpack.customer.customer_detail.json.CustomerDetail_ContactDetail_Json;
import vn.altalab.app.crmvietpack.customer.edit_create_customer.CustomerDetail_ContactEditation_Activity;
import vn.altalab.app.crmvietpack.customer.json.Contact_Json;
import vn.altalab.app.crmvietpack.customer.json.Position_Json;
import vn.altalab.app.crmvietpack.notification.Notification_Activity;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class CustomerDetail_ContactDetail_Activity extends AppCompatActivity {

    TextView tvCONTACT_FULL_NAME, tvCONTACT_MOBIPHONE, tvCONTACT_WORKPHONE, tvCONTACT_EMAIL, tvGENDER, tvPOSITION, tvBIRTHDAY, tvCTCustomerName;
    Shared_Preferences sharedPreferences;
    FloatingActionButton floatingActionButton;
    CustomerDetail_Contact_Fragment customerDetail_contact_fragment = new CustomerDetail_Contact_Fragment();

    static String customer_id = "";
    String contact_id = "";
    String json = "";
    static String customer_name = "";
    ProgressDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customerdetail_contactdetail_activity);

        FINDVIEWBYID();

        try {
            ACTION();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void FINDVIEWBYID() {

        tvCONTACT_FULL_NAME = (TextView) findViewById(R.id.tvCONTACT_FULL_NAME);
        tvCONTACT_MOBIPHONE = (TextView) findViewById(R.id.tvCONTACT_MOBIPHONE);
        tvCONTACT_WORKPHONE = (TextView) findViewById(R.id.tvCONTACT_WORKPHONE);
        tvCONTACT_EMAIL = (TextView) findViewById(R.id.tvCONTACT_EMAIL);
        tvGENDER = (TextView) findViewById(R.id.tvGENDER);
        tvPOSITION = (TextView) findViewById(R.id.tvPOSITION);
        tvBIRTHDAY = (TextView) findViewById(R.id.tvBIRTHDAY);
        tvCTCustomerName = (TextView) findViewById(R.id.tvCTCustomerName);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingButton);

    }

    public void ACTION() {

        getToolbar();

        sharedPreferences = new Shared_Preferences(this, Link.PREFS_NAME + "CustomerDetail_ContactDetail");

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            contact_id = bundle.getString("contact_id");
            customer_id = bundle.getString("customer_id");
            customer_name = bundle.getString("CUSTOMER_NAME");
        }

        get_DATA(contact_id);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerDetail_ContactDetail_Activity.this, CustomerDetail_ContactEditation_Activity.class);
                intent.putExtra("contact_id", contact_id);
                intent.putExtra("customer_id", customer_id);
                intent.putExtra("CUSTOMER_NAME", customer_name);
                intent.putExtra("json", json);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("CustomerDetail", "resultCode: " + resultCode);
        if (resultCode == 2) {
            get_DATA(contact_id);
        }
    }

    public void get_DATA(String contact_id) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Xin chờ giây lát ...");
        pDialog.setCancelable(true);
        pDialog.show();

        Link link = new Link(this);
        String url = link.get_CUSTOMERDETAIL_CONTACTDETAIL_Link(contact_id);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("CustomerDetail", "JSONObject: " + jsonObject);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                if (jsonObject != null)
                    try {
                        setEditText(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("ContactDetail", "volleyError: " + volleyError);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=UTF-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };
        jsonObjectRequest.setShouldCache(false);
        int socketTimeout = 6000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void get_POSITION(final String POSITION_ID) {

        Link link = new Link(this);
        String url = link.get_POSITION_ID();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("CONTACTDETAIL", "Response: " + jsonObject);

                if (jsonObject != null)
                    setPOSITIONID(jsonObject, POSITION_ID);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("CONTACTDETAIL", "Error: " + volleyError);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=UTF-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };

        jsonObjectRequest.setShouldCache(false);
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void setPOSITIONID(JSONObject jsonObject, String POSITION_ID) {
        Position_Json positionJson = new Position_Json(String.valueOf(jsonObject));
        if (positionJson.isStatus() == true) {
            sharedPreferences.putString("position", String.valueOf(jsonObject));
            for (int i = 0; i < positionJson.getList().size(); i++) {
                if (positionJson.getList().get(i).getPOSITION_ID().equals(POSITION_ID))
                    tvPOSITION.setText(positionJson.getList().get(i).getPOSITION_NAME());
            }
        }
    }

    public void setEditText(JSONObject jsonObject) {
        Link link = new Link(this);
        CustomerDetail_ContactDetail_Json customerDetailContactDetailJson = new CustomerDetail_ContactDetail_Json(jsonObject);
        Contact_Json contact_json = new Contact_Json(jsonObject);

        if (customerDetailContactDetailJson.get_STATUS() == true) {

            json = String.valueOf(jsonObject);

            String CONTACT_FULL_NAME = "";
            String CONTACT_MOBIPHONE = "";
            String CONTACT_WORKPHONE = "";
            String CONTACT_EMAIL = "";
            String GENDER = "";
            String POSITION_ID = "";
            String BIRTHDAY = "";
            String CUSTOMER_NAME = "";

            CONTACT_FULL_NAME = customerDetailContactDetailJson.get_SETGET().getCONTACT_FULL_NAME();
            CONTACT_MOBIPHONE = customerDetailContactDetailJson.get_SETGET().getCONTACT_MOBIPHONE();
            CONTACT_WORKPHONE = customerDetailContactDetailJson.get_SETGET().getCONTACT_WORKPHONE();
            CONTACT_EMAIL = customerDetailContactDetailJson.get_SETGET().getCONTACT_EMAIL();
            GENDER = customerDetailContactDetailJson.get_SETGET().getGENDER();
            POSITION_ID = customerDetailContactDetailJson.get_SETGET().getPOSITION_ID();
            BIRTHDAY = customerDetailContactDetailJson.get_SETGET().getBIRTHDAY();
            CUSTOMER_NAME = customerDetailContactDetailJson.get_SETGET().getCUSTOMER_NAME();
            customer_id = customerDetailContactDetailJson.get_SETGET().getCUSTOMER_ID();
            customer_name = customerDetailContactDetailJson.get_SETGET().getCUSTOMER_NAME();

            tvCONTACT_FULL_NAME.setText(CONTACT_FULL_NAME);
            if (link.getId().equals("1")) {
                tvCONTACT_MOBIPHONE.setText(CONTACT_MOBIPHONE);
                tvCONTACT_WORKPHONE.setText(CONTACT_WORKPHONE);
            } else {
                if (customerDetail_contact_fragment.contactPhone == 0) {
                    tvCONTACT_MOBIPHONE.setText("**********");
                    tvCONTACT_WORKPHONE.setText("**********");
                } else {
                    tvCONTACT_MOBIPHONE.setText(CONTACT_MOBIPHONE);
                    tvCONTACT_WORKPHONE.setText(CONTACT_WORKPHONE);

                }
            }

            if (link.getId().equals("1")) {
                tvCONTACT_EMAIL.setText(CONTACT_EMAIL);
            } else {
                if (customerDetail_contact_fragment.contactEmail == 0) {
                    tvCONTACT_EMAIL.setText("**********");
                } else {
                    tvCONTACT_EMAIL.setText(CONTACT_EMAIL);

                }
            }
            tvCTCustomerName.setText(CUSTOMER_NAME);

            tvBIRTHDAY.setText(BIRTHDAY);

            if (GENDER.equals("1"))
                tvGENDER.setText("Nam");

            else if (GENDER.equals("2"))
                tvGENDER.setText("Nữ");

            if (!sharedPreferences.getString("response").equals("")) {
                try {
                    JSONObject jsonObject1 = new JSONObject(sharedPreferences.getString("response"));
                    setPOSITIONID(jsonObject1, POSITION_ID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                get_POSITION(POSITION_ID);
            }

        }

    }

    public void getToolbar() {

        if (getSupportActionBar() == null) {

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            TextView tvNotification = (TextView) findViewById(R.id.tvNotification);
            LinearLayout llBell = (LinearLayout) findViewById(R.id.llBell);

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            tvNotification.setText("Chi tiết liên hệ");
            llBell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(CustomerDetail_ContactDetail_Activity.this, Notification_Activity.class);
                    intent.putExtra("type_object", 1);
                    startActivity(intent);

                }

            });

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
