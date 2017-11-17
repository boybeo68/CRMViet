package vn.altalab.app.crmvietpack.report.chart;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.object.Users;
import vn.altalab.app.crmvietpack.report.adapter.UserAdapter;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class Price_Activity extends AppCompatActivity {
    private PullToRefreshListView lstUser;
    public static List<Users> userses;
    UserAdapter adapter;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;
    List<chartRevenue> chartRevenues;
    static long staf_id;
    public static Toolbar toolbar;
    Calendar calendar, calendar1;
    SimpleDateFormat simpleDateFormat;
    static int year, month_to, month_from, year_from;
    Button btTime;
    LinearLayout tangtruong, kpi;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (settings == null) {
            settings = getSharedPreferences(PREFS_NAME, 0);

        }
        calendar1 = Calendar.getInstance();
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("M/yyyy", Locale.getDefault());
        String c = simpleDateFormat.format(calendar.getTime());
        String d[] = c.split("/");

        month_to = Integer.parseInt(d[0]);
        year = Integer.parseInt(d[1]);

        // 3 tháng

        simpleDateFormat = new SimpleDateFormat("M/yyyy", Locale.getDefault());
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 2);

        String c1 = simpleDateFormat.format(calendar.getTime());
        String d1[] = c1.split("/");

        month_from = Integer.parseInt(d1[0]);
        year_from = Integer.parseInt(d1[1]);
        //bieu do cot
        chartRevenues = new ArrayList<>();
        staf_id = -1;
        doGetRev();

        btTime = (Button) findViewById(R.id.btTimeRp);
        btTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Price_Activity.this);
                builder.create();
                List<String> listOptions = new ArrayList<>();
                listOptions.add("3 tháng");
                listOptions.add("6 tháng");
                listOptions.add("9 tháng");
                listOptions.add("12 tháng");
                ListAdapter listAdapter = new ArrayAdapter<>(Price_Activity.this, android.R.layout.simple_list_item_1, listOptions);
                builder.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            btTime.setText("3 tháng");
                            simpleDateFormat = new SimpleDateFormat("M/yyyy", Locale.getDefault());
                            calendar1.set(Calendar.MONTH, calendar1.get(Calendar.MONTH) - 2);

                            String a = simpleDateFormat.format(calendar1.getTime());
                            String b[] = a.split("/");

                            month_from = Integer.parseInt(b[0]);
                            year_from = Integer.parseInt(b[1]);

                            doGetRev();


                        }
                        if (which == 1) {
                            btTime.setText("6 tháng");
                            simpleDateFormat = new SimpleDateFormat("M/yyyy", Locale.getDefault());
                            calendar1.set(Calendar.MONTH, calendar1.get(Calendar.MONTH) - 5);

                            String a = simpleDateFormat.format(calendar1.getTime());
                            String b[] = a.split("/");

                            month_from = Integer.parseInt(b[0]);
                            year_from = Integer.parseInt(b[1]);
                            doGetRev();

                        }
                        if (which == 2) {
                            btTime.setText("9 tháng");

                            simpleDateFormat = new SimpleDateFormat("M/yyyy", Locale.getDefault());
                            calendar1.set(Calendar.MONTH, calendar1.get(Calendar.MONTH) - 8);

                            String a = simpleDateFormat.format(calendar1.getTime());
                            String b[] = a.split("/");

                            month_from = Integer.parseInt(b[0]);
                            year_from = Integer.parseInt(b[1]);

                            doGetRev();
                        }
                        if (which == 3) {
                            btTime.setText("12 tháng");
                            simpleDateFormat = new SimpleDateFormat("M/yyyy", Locale.getDefault());
                            calendar1.set(Calendar.MONTH, calendar1.get(Calendar.MONTH) - 11);

                            String a = simpleDateFormat.format(calendar1.getTime());
                            String b[] = a.split("/");

                            month_from = Integer.parseInt(b[0]);
                            year_from = Integer.parseInt(b[1]);

                            doGetRev();

                        }

                    }
                });
                builder.show();
            }
        });


        // list user

        lstUser = (PullToRefreshListView) findViewById(R.id.lstUser);
        userses = new ArrayList<>();


        doGetUsers();
        adapter = new UserAdapter(this, R.layout.item_user, userses);

        lstUser.setAdapter(adapter);
        lstUser.setMode(PullToRefreshBase.Mode.BOTH);

        lstUser.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if ("".equals(settings.getString("api_server", ""))) {
                    Toast.makeText(refreshView.getContext(), refreshView.getContext().getResources().getString(R.string.login_empty_api_server), Toast.LENGTH_SHORT).show();
                    return;
                }
                userses.clear();
                adapter.notifyDataSetChanged();
                doGetUsers();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if ("".equals(settings.getString("api_server", ""))) {
                    Toast.makeText(refreshView.getContext(), refreshView.getContext().getResources().getString(R.string.login_empty_api_server), Toast.LENGTH_SHORT).show();
                    return;
                }
                doGetUsers();
            }
        });
        lstUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                staf_id = userses.get(position - 1).getUserId();
                doGetRev();
            }
        });


        tangtruong = (LinearLayout) findViewById(R.id.tangtruong);
        kpi = (LinearLayout) findViewById(R.id.kpi);
        tangtruong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Price_Activity.this, RevActivity.class);
                startActivity(intent);
                finish();
            }
        });

        kpi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    // list danh sách user
    public void doGetUsers() {

        String url = settings.getString("api_server", "") + "/api/v1/users";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {

                    JSONArray array = jsonObject.optJSONArray("users");
                    for (int i = 0; i < array.length(); i++) {
                        final JSONObject object = array.optJSONObject(i);
                        String userId = object.optString("USER_ID");
                        boolean check = false;
                        for (Users user : userses) {
                            if (userId.equals(String.valueOf(user.getUserId()))) {
                                check = true;
                                break;
                            }
                        }
                        if (!check) {
                            Users users = null;
                            users = new Users();
                            users.setUserId(object.optLong("USER_ID"));
//                            users.setUserName(object.optString("USER_NAME"));
                            users.setUserFullName(object.optString("USER_FULL_NAME"));


                            userses.add(users);


                        }
                    }
                    Users users = null;
                    users = new Users();
                    users.setUserId(-1);
                    users.setUserFullName("Tất cả");
                    userses.add(0, users);
                    adapter.notifyDataSetChanged();

                    if (lstUser.isRefreshing()) {
                        lstUser.onRefreshComplete();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (lstUser.isRefreshing()) {
                    lstUser.onRefreshComplete();
                }
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
    /// bieu do cot

    public void doGetRev() {
        chartRevenues.clear();
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        String url = settings.getString("api_server", "") + "/api/v1/report/revenue?USER_ID="
                + settings.getString(getResources().getString(R.string.user_id_object), "") + "&month_from=" + month_from + "&year_from=" + year_from +
                "&month_to=" + month_to + "&year_to=" + year + "&staff_id=" + staf_id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {


                    JSONArray array = jsonObject.optJSONArray("chartRevenue");
                    for (int i = 0; i < array.length(); i++) {
                        final JSONObject object = array.optJSONObject(i);


                        chartRevenue chartRevenue = new chartRevenue();
                        chartRevenue.setMonth(object.optInt("month_int"));
                        chartRevenue.setYear(object.optString("year_int"));
                        chartRevenue.setREVENUE(object.optString("REVENUE"));


                        chartRevenues.add(chartRevenue);

                    }


                    ArrayList<BarEntry> entries = new ArrayList<>();

                    for (int i = 0; i < chartRevenues.size(); i++) {

                        entries.add(new BarEntry(Float.parseFloat(chartRevenues.get(i).getREVENUE()), i));
                    }
                    BarChart barChart = (BarChart) findViewById(R.id.chartDoanhthu);
                    barChart.removeAllViews();


                    BarDataSet dataset = new BarDataSet(entries, "doanh thu");
                    ArrayList<String> labels = new ArrayList<String>();

                    for (int i = 0; i < chartRevenues.size(); i++) {
                        labels.add(chartRevenues.get(i).getMonth() + "");
                    }

                    //xét label chục dưới x
                    barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    // bỏ cột bên phải
                    YAxis yAxisRight = barChart.getAxisRight();
                    yAxisRight.setEnabled(false);
                    dataset.setColor(Color.parseColor("#3366CC"));
                    dataset.setDrawValues(false);

                    dataset.setBarSpacePercent(30f);
                    barChart.setDescription("");

                    BarData data = new BarData(labels, dataset);


                    barChart.setData(data);
                    barChart.animateY(2000);
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (chartRevenues.size() == 0) {

                        Toast.makeText(getApplicationContext(), "Không có dữ liệu để hiển thị", Toast.LENGTH_LONG).show();
                    }

                }


            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Log.e("loadall", volleyError.toString());
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
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
