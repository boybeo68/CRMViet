package vn.altalab.app.crmvietpack.customer;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.customer.adapter.Customer_Adapter;
import vn.altalab.app.crmvietpack.customer.setget.Customer_Setget;
import vn.altalab.app.crmvietpack.customer.setget.GEOGRAPHY;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class SearchAdressCus extends AppCompatActivity {
    PullToRefreshListView listView;
    Spinner spCountry, spProvince, spDistrict, spWard;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;
    EditText AdressCus;
    List<GEOGRAPHY> countryList, provinceList, districtList, wardList;
    List<Customer_Setget> customer_setgetList;
    Customer_Adapter customerAdapter;
    static String country, province, district, ward;
    Customer_MainFragment customer_mainFragment = new Customer_MainFragment();
    Button search;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_adress_cus);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            getSupportActionBar().setSubtitle("Tìm kiếm khách hàng nâng cao");

        }
        if (settings == null) {
            settings = getSharedPreferences(PREFS_NAME, 0);
        }
        search = (Button) findViewById(R.id.btSearchCus);
        AdressCus = (EditText) findViewById(R.id.edtAdressCus);
        listView = (PullToRefreshListView) findViewById(R.id.lstKH);
        spCountry = (Spinner) findViewById(R.id.spCountry);
        spProvince = (Spinner) findViewById(R.id.spProvince);
        spDistrict = (Spinner) findViewById(R.id.spDistrict);
        spWard = (Spinner) findViewById(R.id.spWard);
        countryList = new ArrayList<>();
        provinceList = new ArrayList<>();
        districtList = new ArrayList<>();
        wardList = new ArrayList<>();
        customer_setgetList = new ArrayList<>();
        doCountry();
        customerAdapter = new Customer_Adapter(SearchAdressCus.this, R.layout.customer_listview_adapter, (ArrayList<Customer_Setget>) customer_setgetList);
        customerAdapter.notifyDataSetChanged();
        listView.setAdapter(customerAdapter);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if ("".equals(settings.getString("api_server", ""))) {
                    Toast.makeText(refreshView.getContext(), refreshView.getContext().getResources().getString(R.string.login_empty_api_server), Toast.LENGTH_SHORT).show();
                    return;
                }
                customer_setgetList.clear();
                customerAdapter.notifyDataSetChanged();
                SearchAdressCus(0);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if ("".equals(settings.getString("api_server", ""))) {
                    Toast.makeText(refreshView.getContext(), refreshView.getContext().getResources().getString(R.string.login_empty_api_server), Toast.LENGTH_SHORT).show();
                    return;
                }
                SearchAdressCus(customer_setgetList.size());
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchAdressCus(0);
            }
        });

    }


    //danh sách phường/xã

    public void SpWard() {
        String Ward[] = new String[wardList.size()];
        final String ID[] = new String[wardList.size()];
        for (int i = 0; i < wardList.size(); i++) {
            Ward[i] = wardList.get(i).getGEOGRAPHY_NAME();
            ID[i] = wardList.get(i).getGEOGRAPHY_ID();
        }

        ArrayAdapter adapterContract_USER = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Ward);
        spWard.setAdapter(adapterContract_USER);
        spWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ward = ID[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // danh sách quận/ huyện
    public void SpDistrict() {
        String District[] = new String[districtList.size()];
        final String ID[] = new String[districtList.size()];
        for (int i = 0; i < districtList.size(); i++) {
            District[i] = districtList.get(i).getGEOGRAPHY_NAME();
            ID[i] = districtList.get(i).getGEOGRAPHY_ID();
        }

        ArrayAdapter adapterContract_USER = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, District);
        spDistrict.setAdapter(adapterContract_USER);
        spDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wardList.clear();
                doGeography2(ID[position]);
                district = ID[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // danh sách tỉnh/thành phố
    public void SpProvince() {
        String Province[] = new String[provinceList.size()];
        final String ID[] = new String[provinceList.size()];
        for (int i = 0; i < provinceList.size(); i++) {
            Province[i] = provinceList.get(i).getGEOGRAPHY_NAME();
            ID[i] = provinceList.get(i).getGEOGRAPHY_ID();
        }

        ArrayAdapter adapterContract_USER = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Province);
        spProvince.setAdapter(adapterContract_USER);
        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                districtList.clear();
                doGeography1(ID[position]);
                province = ID[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // danh sách quốc gia
    public void SpCountry() {
        String Country[] = new String[countryList.size()];
        final String ID[] = new String[countryList.size()];
        for (int i = 0; i < countryList.size(); i++) {
            Country[i] = countryList.get(i).getGEOGRAPHY_NAME();
            ID[i] = countryList.get(i).getGEOGRAPHY_ID();
        }

        ArrayAdapter adapterContract_USER = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Country);
        spCountry.setAdapter(adapterContract_USER);
        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                doGeography(ID[position]);
                country = ID[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //Search khach hang
    public void SearchAdressCus(int index) {
        if (index == 0) {
            customer_setgetList.clear();
            customerAdapter.notifyDataSetChanged();
        }
        progressDialog = new ProgressDialog(SearchAdressCus.this);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Đang tìm...");
        progressDialog.show();

        String url = settings.getString("api_server", "") + "/api/v1/find/customersAdress?USER_ID=" + settings.getString(getResources().getString(R.string.user_id_object), "")
                + "&keyOffice=" + AdressCus.getText().toString() + "&keyCountry=" + country + "&keyProvince=" + province + "&keyDistrict=" + district
                + "&keyWard=" + ward + "&index=" + index;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {
                    try {

                        JSONArray array = jsonObject.optJSONArray("customers");
                        for (int i = 0; i < array.length(); i++) {
                            Customer_Setget customer_setget = new Customer_Setget();
                            JSONObject object = array.optJSONObject(i);
                            customer_setget.setCUSTOMER_ID(object.optString("CUSTOMER_ID"));
                            customer_setget.setCUSTOMER_NAME(object.optString("CUSTOMER_NAME"));
                            customer_setget.setOFFICE_ADDRESS(object.optString("OFFICE_ADDRESS"));
                            if (customer_mainFragment.email_view != 0 || customer_mainFragment.user_id == object.optInt("CUSTOMER_OWNER") ||
                                    customer_mainFragment.user_id == 1) {
                                customer_setget.setCUSTOMER_EMAIL(object.optString("CUSTOMER_EMAIL"));
                            }
                            if (customer_mainFragment.phone_view != 0 || customer_mainFragment.user_id == object.optInt("CUSTOMER_OWNER") ||
                                    customer_mainFragment.user_id == 1) {
                                customer_setget.setTELEPHONE(object.optString("TELEPHONE"));

                            }

                            customer_setgetList.add(customer_setget);

                        }
                        if(customer_setgetList.size()==0){
                            Toasty.normal(getApplicationContext(), "Không tìm thấy khách hàng nào, theo địa chỉ này !", Toast.LENGTH_LONG).show();
                        }
                        customerAdapter.notifyDataSetChanged();
                        if (listView.isRefreshing()) {
                            listView.onRefreshComplete();
                        }
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                    } catch (NullPointerException npe) {
                        Log.e("homeusers", npe.toString());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("doGetUsers", volleyError.toString());
                if (listView.isRefreshing()) {
                    listView.onRefreshComplete();
                }
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

    // danh sách tỉnh/ thành phố
    public void doGeography(String GEOGRAPHY_ID) {
        provinceList.clear();
        String url = settings.getString("api_server", "") + "/api/v1/adress_customer?GEOGRAPHY_ID=" + GEOGRAPHY_ID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {
                    try {

                        JSONArray array = jsonObject.optJSONArray("adress_customer");
                        for (int i = 0; i < array.length(); i++) {
                            GEOGRAPHY province = new GEOGRAPHY();
                            JSONObject object = array.optJSONObject(i);
                            province.setGEOGRAPHY_NAME(object.optString("GEOGRAPHY_NAME"));
                            province.setGEOGRAPHY_ID(object.optString("GEOGRAPHY_ID"));
                            provinceList.add(province);


                        }
                        GEOGRAPHY first = new GEOGRAPHY();
                        first.setGEOGRAPHY_NAME("- - chưa chọn - -");
                        first.setGEOGRAPHY_ID("");
                        provinceList.add(0,first);
                        SpProvince();
                    } catch (NullPointerException npe) {
                        Log.e("homeusers", npe.toString());
                    }
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

    // danh sách huyện
    public void doGeography1(String GEOGRAPHY_ID) {
        String url = settings.getString("api_server", "") + "/api/v1/adress_customer?GEOGRAPHY_ID=" + GEOGRAPHY_ID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {
                    try {

                        JSONArray array = jsonObject.optJSONArray("adress_customer");
                        for (int i = 0; i < array.length(); i++) {
                            GEOGRAPHY district = new GEOGRAPHY();
                            JSONObject object = array.optJSONObject(i);
                            district.setGEOGRAPHY_NAME(object.optString("GEOGRAPHY_NAME"));
                            district.setGEOGRAPHY_ID(object.optString("GEOGRAPHY_ID"));
                            districtList.add(district);


                        }
                        GEOGRAPHY first = new GEOGRAPHY();
                        first.setGEOGRAPHY_NAME("- - chưa chọn - -");
                        first.setGEOGRAPHY_ID("");
                        districtList.add(0,first);
                        SpDistrict();
                    } catch (NullPointerException npe) {
                        Log.e("homeusers", npe.toString());
                    }
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

    // danh sách xã /phường
    public void doGeography2(String GEOGRAPHY_ID) {
        String url = settings.getString("api_server", "") + "/api/v1/adress_customer?GEOGRAPHY_ID=" + GEOGRAPHY_ID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {
                    try {

                        JSONArray array = jsonObject.optJSONArray("adress_customer");
                        for (int i = 0; i < array.length(); i++) {
                            GEOGRAPHY ward = new GEOGRAPHY();
                            JSONObject object = array.optJSONObject(i);
                            ward.setGEOGRAPHY_NAME(object.optString("GEOGRAPHY_NAME"));
                            ward.setGEOGRAPHY_ID(object.optString("GEOGRAPHY_ID"));
                            wardList.add(ward);


                        }
                        GEOGRAPHY first = new GEOGRAPHY();
                        first.setGEOGRAPHY_NAME("- - chưa chọn - -");
                        first.setGEOGRAPHY_ID("");
                        wardList.add(0,first);
                        SpWard();
                    } catch (NullPointerException npe) {
                        Log.e("homeusers", npe.toString());
                    }
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

    public void doCountry() {

        String url = settings.getString("api_server", "") + "/api/v1/country_customer";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {
                    try {

                        JSONArray array = jsonObject.optJSONArray("country_customer");

                        for (int i = 0; i < array.length(); i++) {
                            GEOGRAPHY country = new GEOGRAPHY();
                            JSONObject object = array.optJSONObject(i);
                            country.setGEOGRAPHY_NAME(object.optString("GEOGRAPHY_NAME"));
                            country.setGEOGRAPHY_ID(object.optString("GEOGRAPHY_ID"));
                            countryList.add(country);


                        }
                        SpCountry();
                    } catch (NullPointerException npe) {
                        Log.e("homeusers", npe.toString());
                    }
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
