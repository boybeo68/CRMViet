package vn.altalab.app.crmvietpack.orders_fragment.order_detail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.object.Customer;
import vn.altalab.app.crmvietpack.presenter.CustomerAdapter;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class ChoseCusActivity extends AppCompatActivity {
    private PullToRefreshListView lstCustomer;
    public static List<Customer> customers;
    public static CustomerAdapter customerAdapter;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;
    private ProgressDialog progressDialog;
    public static int phone_view, email_view, user_id;

    Toolbar toolbar;
    EditText searchdt;
    Button btSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_cus);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            getSupportActionBar().setSubtitle("Chọn khách hàng");
        }
        if (settings == null) {
            settings = getSharedPreferences(PREFS_NAME, 0);
        }
        doGetFunctionUser();
        searchdt = (EditText) findViewById(R.id.edSearchOrder);
        btSearch = (Button) findViewById(R.id.btSearchOrder);


        lstCustomer = (PullToRefreshListView) findViewById(R.id.lstCustomer);
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading..");

        customers = new ArrayList<>();

        doGetCustomersAPI(0);
        customerAdapter = new CustomerAdapter(this, R.layout.new_list_item_customer, customers);
        lstCustomer.setAdapter(customerAdapter);
        lstCustomer.setMode(PullToRefreshBase.Mode.BOTH);

        lstCustomer.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if ("".equals(settings.getString("api_server", ""))) {
                    Toast.makeText(refreshView.getContext(), refreshView.getContext().getResources().getString(R.string.login_empty_api_server), Toast.LENGTH_SHORT).show();
                    return;
                }
                customers.clear();
                customerAdapter.notifyDataSetChanged();
                doGetCustomersAPI(0);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if ("".equals(settings.getString("api_server", ""))) {
                    Toast.makeText(refreshView.getContext(), refreshView.getContext().getResources().getString(R.string.login_empty_api_server), Toast.LENGTH_SHORT).show();
                    return;
                }
                doGetCustomersAPI(customers.size());
            }
        });

        lstCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {


                Intent intent = new Intent();
                intent.putExtra("customer_id", customers.get(position - 1).getCustomerId());
                intent.putExtra("customer_name", customers.get(position - 1).getCustomerName());
                setResult(1, intent);
                finish();
            }
        });

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                doMySearch(0);
            }
        });
        searchdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {


                    doMySearch(0);
                    //tắt bàn phím sau khi click
                    final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }


    public void doGetCustomersAPI(final int index) {

        if (index == 0) {
            customers.clear();
        }

        String url = settings.getString("api_server", "") + "/api/v1/customer/potential?index=" + index +
                "&USER_ID=" + settings.getString(getResources().getString(R.string.user_id_object), "");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {

                    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    JSONArray array = jsonObject.optJSONArray(getResources().getString(R.string.customers));
                    for (int i = 0; i < array.length(); i++) {
                        final JSONObject object = array.optJSONObject(i);
                        boolean check = false;
                        for (Customer cus : customers) {
                            if (cus.getCustomerId() == object.optLong(ChoseCusActivity.this.getResources().getString(R.string.customer_id_db))) {
                                check = true;
                                break;
                            }
                        }
                        Customer customer;
                        if (!check) {
                            customer = new Customer();
                            customer.setCustomerId(object.optLong(ChoseCusActivity.this.getResources().getString(R.string.customer_id_db)));
                            customer.setUsers(object.optInt(ChoseCusActivity.this.getResources().getString(R.string.customer_owner_db)));
                            customer.setCustomerName(object.optString(ChoseCusActivity.this.getResources().getString(R.string.customer_name_db)));
                            customer.setCustomerCode(object.optString(ChoseCusActivity.this.getResources().getString(R.string.customer_code_db)));
                            if (email_view != 0 || user_id == object.optInt(ChoseCusActivity.this.getResources().getString(R.string.customer_owner_db)) ||
                                    user_id == 1) {
                                customer.setCustomerEmail(object.optString(ChoseCusActivity.this.getResources().getString(R.string.customer_email_db)));
                            }else {
                                customer.setCustomerEmail("*********");
                            }
                            if (phone_view != 0 || user_id == object.optInt(ChoseCusActivity.this.getResources().getString(R.string.customer_owner_db)) ||
                                    user_id == 1) {
                                customer.setTelephone(object.optString(ChoseCusActivity.this.getResources().getString(R.string.customer_telephone_db)));
                            }else {
                                customer.setTelephone("*********");
                            }
                            customer.setCustomerAddress(object.optString(getResources().getString(R.string.customer_address_db)));
                            customer.setOfficeAddress(object.optString(getResources().getString(R.string.customer_office_db)));
                            customers.add(customer);
                        }
                    }

                    customerAdapter.notifyDataSetChanged();
                }
                if (jsonObject == null && "".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {
                    Toast.makeText(getApplication(), "Không tìm thấy khách hàng này ", Toast.LENGTH_SHORT).show();
                }


                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (lstCustomer.isRefreshing()) {
                    lstCustomer.onRefreshComplete();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (lstCustomer.isRefreshing()) {
                    lstCustomer.onRefreshComplete();
                }
                Log.e("loadallcustomer", volleyError.toString());
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

    public static String KeyAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }

    //Search
    private void doMySearch(final int index) {

        if (index == 0) {
            customers.clear();
            customerAdapter.notifyDataSetChanged();
        }
        progressDialog.show();
        String KeyWord = searchdt.getText().toString();

        if (KeyWord.contains(" ")) {
            KeyWord = KeyWord.replace(" ", "%20");

        }

        String url = settings.getString("api_server", "") + "/api/v1/find/customers?keyword=" + KeyAccent(KeyWord) + "&USER_ID=" + settings.getString(getResources().getString(R.string.user_id_object), "") + "&index=" + index;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {

                    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

                    JSONArray array = jsonObject.optJSONArray(getResources().getString(R.string.customers));
                    for (int i = 0; i < array.length(); i++) {
                        final JSONObject object = array.optJSONObject(i);


                        boolean check = false;
                        for (Customer cus : customers) {
                            if (cus.getCustomerId() == object.optLong(getResources().getString(R.string.customer_id_db))) {
                                check = true;
                                break;
                            }
                        }
                        if (!check) {
                            Customer customer = new Customer();
                            customer.setCustomerId(object.optLong(getResources().getString(R.string.customer_id_db)));
                            customer.setUsers(object.optInt(getResources().getString(R.string.customer_owner_db)));
                            customer.setCustomerName(object.optString(getResources().getString(R.string.customer_name_db)));
                            customer.setCustomerCode(object.optString(getResources().getString(R.string.customer_code_db)));
                            if (email_view != 0 || user_id == object.optInt(ChoseCusActivity.this.getResources().getString(R.string.customer_owner_db)) ||
                                    user_id == 1) {
                                customer.setCustomerEmail(object.optString(getResources().getString(R.string.customer_email_db)));
                            }
                            if (phone_view != 0 || user_id == object.optInt(ChoseCusActivity.this.getResources().getString(R.string.customer_owner_db)) ||
                                    user_id == 1) {
                                customer.setTelephone(object.optString(getResources().getString(R.string.customer_telephone_db)));
                            }
                            customer.setOfficeAddress(object.optString(getResources().getString(R.string.customer_office_db)));

                            try {
                                customer.setCustomerFounding(simpleDateFormat.parse(object.optString(getResources().getString(R.string.customer_founding_db))));
                                customer.setRegUser(object.optInt(getResources().getString(R.string.customer_reg_user_db)));
                                customer.setRegDttm(object.optString(getResources().getString(R.string.customer_reg_dttm_db)));
                            } catch (ParseException e) {
                                Log.e("parserdate", e.getMessage());
                            }
                            customers.add(customer);
                        }
                    }
                    if (customers.size() == 0) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(ChoseCusActivity.this, "Khách hàng không tồn tại !", Toast.LENGTH_LONG).show();
                    }
                    customerAdapter.notifyDataSetChanged();
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
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

        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        MySingleton.getInstance(this).addToRequestQueue(request);

    }

// check quyen xem sdt va email
public void doGetFunctionUser() {
    if(settings.getString(getResources().getString(R.string.user_id_object), "")!="") {
        user_id = Integer.parseInt(settings.getString(getResources().getString(R.string.user_id_object), ""));
        String url = settings.getString("api_server", "") + "/api/v1/customer/function?USER_ID=" + settings.getString(getResources().getString(R.string.user_id_object), "");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {
                    try {
                        JSONArray array = jsonObject.optJSONArray("phone_view");
                        phone_view = array.length();


                        JSONArray array1 = jsonObject.optJSONArray("email_view");
                        email_view = array1.length();


                    } catch (NullPointerException npe) {
                        Log.e("homeusers", npe.toString());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("gettransactiontype", volleyError.toString());
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

        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        MySingleton.getInstance(this).addToRequestQueue(request);
    }
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
