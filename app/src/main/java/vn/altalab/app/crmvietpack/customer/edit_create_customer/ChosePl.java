package vn.altalab.app.crmvietpack.customer.edit_create_customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.customer.adapter.SettingsListAdapter;
import vn.altalab.app.crmvietpack.customer.setget.ChildrenType;
import vn.altalab.app.crmvietpack.customer.setget.CustomerType;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class ChosePl extends AppCompatActivity {
    ExpandableListView expandableListView;
    ArrayList<CustomerType> customerTypes;
    public static ArrayList<ChildrenType> childrenTypes;

    ArrayList<ChildrenType> choseTypeOk;
    SettingsListAdapter settingsListAdapter;
    Toolbar toolbar;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;
    private ProgressDialog progressDialog;
    Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_pl);
        if (settings == null) {
            settings = getSharedPreferences(PREFS_NAME, 0);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        bt = (Button) findViewById(R.id.btChonPl);
        expandableListView = (ExpandableListView) findViewById(R.id.lvChosePl);
        customerTypes = new ArrayList<>();
        choseTypeOk = new ArrayList<>();

        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            getSupportActionBar().setSubtitle("Phân loại khách hàng");
        }


        settingsListAdapter = new SettingsListAdapter(this,
                customerTypes, expandableListView);
        doGetCustomerType();
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                CheckedTextView checkbox = (CheckedTextView) v.findViewById(R.id.list_item_text_child);
                checkbox.toggle();
                View parentView = expandableListView.findViewWithTag(customerTypes.get(groupPosition).getCUSTOMER_TYPE_NAME());
                if (parentView != null) {
                    final CustomerType customerType = customerTypes.get(groupPosition);
                    if (checkbox.isChecked()) {


                        // add child category to parent's selection list
                        customerType.choseType.add(customerType.getChildren().get(childPosition));
                        choseTypeOk.add(customerType.getChildren().get(childPosition));

                    } else {
                        // remove child category from parent's selection list
                        customerType.choseType.remove(customerType.getChildren().get(childPosition));
                        choseTypeOk.remove(customerType.getChildren().get(childPosition));
                    }


                }
                return true;

            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ChildrenType childrenType = new ChildrenType();

                childrenType.setChildrenTypeList(choseTypeOk);
                intent.putExtra("chosePl", childrenType);
                setResult(1, intent);
                finish();
            }
        });


    }


    public void doGetCustomerType() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        String url = settings.getString("api_server", "") + "/api/v1/customers/types";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages")))
                    try {

                        JSONArray array = jsonObject.optJSONArray("customer_types");
                        for (int i = 0; i < array.length(); i++) {
                            final CustomerType customerType = new CustomerType();
                            JSONObject object = array.optJSONObject(i);
                            customerType.setCUSTOMER_TYPE_ID(object.optInt("CUSTOMER_TYPE_ID"));
                            customerType.setCUSTOMER_TYPE_NAME(object.optString("CUSTOMER_TYPE_NAME"));
                            customerTypes.add(customerType);

                            String url = settings.getString("api_server", "") + "/api/v1/customers/group_customer/" + object.optInt("CUSTOMER_TYPE_ID");
                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages")))
                                        try {
                                            childrenTypes = new ArrayList<>();
                                            JSONArray array = jsonObject.optJSONArray("group_customer");
                                            for (int i = 0; i < array.length(); i++) {
                                                ChildrenType childrenType = new ChildrenType();

                                                JSONObject object = array.optJSONObject(i);
                                                childrenType.setCUSTOMER_GROUP_ID(object.optInt("CUSTOMER_GROUP_ID"));
                                                childrenType.setCUSTOMER_GROUP_NAME(object.optString("CUSTOMER_GROUP_NAME"));
                                                childrenTypes.add(childrenType);


                                            }
                                            customerType.setChildren(childrenTypes);
                                            if (progressDialog != null && progressDialog.isShowing()) {
                                                progressDialog.dismiss();
                                            }

                                        } catch (NullPointerException npe) {
                                            Log.e("homeusers", npe.toString());
                                        }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Log.e("doGetUsers", volleyError.toString());
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

                            int socketTimeout = 60000;
                            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                            request.setRetryPolicy(policy);
                            MySingleton.getInstance(getApplication()).addToRequestQueue(request);


                        }
                        expandableListView.setAdapter(settingsListAdapter);


                    } catch (NullPointerException npe) {
                        Log.e("homeusers", npe.toString());
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("doGetUsers", volleyError.toString());
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

        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                setResult(1, intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent();
            setResult(1, intent);

            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
