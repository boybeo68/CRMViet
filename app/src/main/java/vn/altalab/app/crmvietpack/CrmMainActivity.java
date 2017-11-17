package vn.altalab.app.crmvietpack;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.realm.Realm;
import me.leolin.shortcutbadger.ShortcutBadger;
import vn.altalab.app.crmvietpack.contract.ContractFragment;
import vn.altalab.app.crmvietpack.customer.Customer_MainFragment;
import vn.altalab.app.crmvietpack.customer.SearchAdressCus;
import vn.altalab.app.crmvietpack.hanghoa.GoodsFragment;
import vn.altalab.app.crmvietpack.home.Home_Fragment;
import vn.altalab.app.crmvietpack.model.UsersModel;
import vn.altalab.app.crmvietpack.notification.Notification_Activity;
import vn.altalab.app.crmvietpack.object.Transaction;
import vn.altalab.app.crmvietpack.object.Users;
import vn.altalab.app.crmvietpack.object.UsersGroup;
import vn.altalab.app.crmvietpack.orders_fragment.OrderFragment;
import vn.altalab.app.crmvietpack.report.ReportFragment;
import vn.altalab.app.crmvietpack.service.MyServiceLocation;
import vn.altalab.app.crmvietpack.service.RegistrationIntentService;
import vn.altalab.app.crmvietpack.transaction.Transaction_MainFragment;
import vn.altalab.app.crmvietpack.utility.Config;
import vn.altalab.app.crmvietpack.utility.MySingleton;
import vn.altalab.app.crmvietpack.warehouse.Warehouse_Fragment;

import static vn.altalab.app.crmvietpack.MapActivity.REQUEST_ID_ACCESS_COURSE_FINE_LOCATION;


public class CrmMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    private LocationManager locationManager;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private static final String PREFS_GCM_NAME = "CRMVietGCMPrefs";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String JARGON = "jargon";
    private SharedPreferences settings;
    public static int a, a1, a2, a3, a4, a5;

    private BroadcastReceiver mBroadcastReceiver;
    private TextView txtTitle, txtSubTitle, tvVersion;
    public static Toolbar toolbar;
    public static boolean alreadyRunning = true;
    private List<Transaction> transactions;
    private GoogleCloudMessaging gcm;
    private String regId;
    private Realm realm;
    private int mInterval = 5000;
    private Runnable mStatusChecker;
    private Handler handler = new Handler();
    public static TextView tvNotification, tvReport;
    Button btTimeRp;
    public static Fragment fragment = null;
    ProgressDialog pDialog;
    public static FragmentTransaction ft;
    RadioGroup radioGroup1, radioGroup2;
    public static String from = "";
    public static String to = "";
    TextView fromTv, toTv;
    TextView tvBell;
    ImageView SearchAdress;
    LinearLayout linearLayout;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    private Locale locale;
    Thread t;
    public static int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crm_main);
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && isOnline()) {
        askPermissionsAndShowMyLocation();
//        }

        if (settings == null) {
            settings = getSharedPreferences(PREFS_NAME, 0);
        }
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        if (realm == null) {
            Realm.init(this);
            realm = Realm.getDefaultInstance();
        }

        if (transactions == null) {
            transactions = new ArrayList<>();
        }


//        doGetUsers();
        doGetCustomersAPI();

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean sentToken = settings.getBoolean(Config.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                } else {
                    String message = intent.getStringExtra("message");
                    Toast.makeText(context, "Push: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        };

//         TRANSACTION TYPE
//        Intent TRANSACTIONTYPE_intent = new Intent(this, TRANSACTIONTYPE_service.class);
//        startService(TRANSACTIONTYPE_intent);

        if (checkPlayServices()) {

            gcm = GoogleCloudMessaging.getInstance(this);
            regId = getRegistrationId(this);

            if (regId.isEmpty()) {
                registerInBackground(this);
            }
            sendRegistrationIdToBackend();
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);

        } else {

            Log.i("== Play Services ==", "No valid Google Play Services APK found.");

        }
        ft = getSupportFragmentManager().beginTransaction();
        fragment = new Home_Fragment();
        fragment.setArguments(getIntent().getExtras());

        ft.replace(R.id.fragment_container, fragment).commit();


        try {
            getToolbar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // hỏi người dùng cho phép lấy vị trí
    private void askPermissionsAndShowMyLocation() {


        // Với API >= 23, phải hỏi người dùng cho phép xem vị trí của họ.
        if (Build.VERSION.SDK_INT >= 23) {
            int accessCoarsePermission
                    = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
            int accessFinePermission
                    = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);


            if (accessCoarsePermission != PackageManager.PERMISSION_GRANTED
                    || accessFinePermission != PackageManager.PERMISSION_GRANTED) {

                // Các quyền cần người dùng cho phép.
                String[] permissions = new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION};

                // Hiển thị một Dialog hỏi người dùng cho phép các quyền trên.
                ActivityCompat.requestPermissions(this, permissions,
                        REQUEST_ID_ACCESS_COURSE_FINE_LOCATION);

                return;
            }
        }

        startService(new Intent(getBaseContext(), MyServiceLocation.class));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_ACCESS_COURSE_FINE_LOCATION: {


                // Chú ý: Nếu yêu cầu bị bỏ qua, mảng kết quả là rỗng.
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "đã từ chối !", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, "đã được cho phép !", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
// lấy thông báo

    public void doGetNocationCount() {

        String url = settings.getString("api_server", "") + "/api/v1/notif?USER_ID=" + settings.getString(getResources().getString(R.string.user_id_object), "");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {
                    try {
                        JSONArray array = jsonObject.optJSONArray("notifications");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.optJSONObject(i);
                            count = object.optInt("NOTIFICATION_COUNT");
                            if (count == 0) {
                                ShortcutBadger.removeCount(CrmMainActivity.this);
                            } else {
                                ShortcutBadger.applyCount(CrmMainActivity.this, count);
                            }

                        }
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


    // update khi kích vào chuông notifi
    public void notifyUpdate() {
        String url = settings.getString("api_server", "") + "/api/v1/notifyUpdate";
        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("USER_ID", settings.getString(getResources().getString(R.string.user_id_object), ""));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        count = 0;
                        ShortcutBadger.removeCount(CrmMainActivity.this);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("doLogin", volleyError.toString());
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


    // khai báo :
    public void getToolbar() {

        LinearLayout llBell = (LinearLayout) findViewById(R.id.llBell);
//        RelativeLayout rlSetting = (RelativeLayout) findViewById(R.id.rlSetting);
        tvBell = (TextView) findViewById(R.id.tvBell);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvNotification = (TextView) findViewById(R.id.tvNotification);
        tvReport = (TextView) findViewById(R.id.tvReport);
        SearchAdress = (ImageView) findViewById(R.id.SearchCusAdress);
        btTimeRp = (Button) findViewById(R.id.btTimeRp);
        doGetNocationCount();
        t = new Thread() {

            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        Thread.sleep(2000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (count == 0) {
                                tvBell.setText("");
                            } else {
                                tvBell.setText("" + count);
                            }
                        }
                    });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        t.start();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_crm_main);
        navigationView.setNavigationItemSelectedListener(this);

        tvVersion = (TextView) headerLayout.findViewById(R.id.tvVersion);
        tvVersion.setText("version: 1.50");
        txtTitle = (TextView) headerLayout.findViewById(R.id.txtTitle);
        txtSubTitle = (TextView) headerLayout.findViewById(R.id.txtSubTitle);
        tvNotification.setText(getResources().getString(R.string.home));

        if (!"".equals(settings.getString(getResources().getString(R.string.user_name_object), ""))) {
            txtTitle.setText(getResources().getString(R.string.welcome).concat(" ").concat(settings.getString(getResources().getString(R.string.user_name_object), "")));
            txtSubTitle.setText(settings.getString(getResources().getString(R.string.user_email_object), ""));
        }

        llBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyUpdate();
                Intent intent = new Intent(CrmMainActivity.this, Notification_Activity.class);
                intent.putExtra("type_object", 1);
                startActivity(intent);
            }
        });

        SearchAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CrmMainActivity.this, SearchAdressCus.class);
                startActivity(intent);
            }
        });
        btTimeRp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CrmMainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_time, null);
                radioGroup1 = (RadioGroup) dialogView.findViewById(R.id.group1);
//                radioGroup2 = (RadioGroup) dialogView.findViewById(R.id.group2);

                linearLayout = (LinearLayout) dialogView.findViewById(R.id.lntime);
                fromTv = (TextView) dialogView.findViewById(R.id.fromTv);
                toTv = (TextView) dialogView.findViewById(R.id.toTv);

                calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);

                fromTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                calendar.set(year, monthOfYear, dayOfMonth);
                                fromTv.setText(simpleDateFormat.format(calendar.getTime()));

                            }
                        }, year, month, day);
                        datePickerDialog.show();
                    }
                });
                toTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                calendar.set(year, monthOfYear, dayOfMonth);
                                toTv.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        }, year, month, day);
                        datePickerDialog.show();
                    }
                });
                dialogBuilder.setView(dialogView);
                dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int isChecked = radioGroup1.getCheckedRadioButtonId();
                        radioGroup1 = (RadioGroup) findViewById(isChecked);
                        switch (isChecked) {
                            case R.id.day7:
                                btTimeRp.setText("7 ngày");
                                Calendar date7 = Calendar.getInstance();
                                date7.set(Calendar.DAY_OF_MONTH, date7.get(Calendar.DAY_OF_MONTH) - 7);
                                from = simpleDateFormat.format(date7.getTime());
                                to = "";
                                ft = getSupportFragmentManager().beginTransaction();
                                fragment = new ReportFragment();
                                fragment.setArguments(getIntent().getExtras());
                                ft.replace(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.month:
                                btTimeRp.setText("tháng này");
                                Calendar month = Calendar.getInstance();
                                month.set(Calendar.DAY_OF_MONTH, month.getActualMinimum(Calendar.DAY_OF_MONTH));
                                from = simpleDateFormat.format(month.getTime());
                                to = "";
                                ft = getSupportFragmentManager().beginTransaction();
                                fragment = new ReportFragment();
                                fragment.setArguments(getIntent().getExtras());
                                ft.replace(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.month3:
                                btTimeRp.setText("3 tháng");
                                Calendar month3 = Calendar.getInstance();
                                month3.set(Calendar.DAY_OF_MONTH, month3.get(Calendar.DAY_OF_MONTH) - 90);
                                from = simpleDateFormat.format(month3.getTime());
                                to = "";
                                ft = getSupportFragmentManager().beginTransaction();
                                fragment = new ReportFragment();
                                fragment.setArguments(getIntent().getExtras());
                                ft.replace(R.id.fragment_container, fragment).commit();
                                break;

//                        }

//                        int isChecked2 = radioGroup2.getCheckedRadioButtonId();
//                        radioGroup2 = (RadioGroup) findViewById(isChecked2);
//                        switch (isChecked2) {
                            case R.id.day14:
                                btTimeRp.setText("14 ngày");
                                Calendar date14 = Calendar.getInstance();
                                date14.set(Calendar.DAY_OF_MONTH, date14.get(Calendar.DAY_OF_MONTH) - 14);
                                from = simpleDateFormat.format(date14.getTime());
                                to = "";
                                ft = getSupportFragmentManager().beginTransaction();
                                fragment = new ReportFragment();
                                fragment.setArguments(getIntent().getExtras());
                                ft.replace(R.id.fragment_container, fragment).commit();

                                break;
                            case R.id.oldmonth:
                                btTimeRp.setText("tháng trước");
                                Calendar oldmonth = Calendar.getInstance();
                                oldmonth.set(Calendar.DAY_OF_MONTH, oldmonth.getActualMinimum(Calendar.MONTH) - 1);
                                to = simpleDateFormat.format(oldmonth.getTime());
                                String b[] = to.split("/");
                                from = "01/" + b[1] + "/" + b[2];
                                ft = getSupportFragmentManager().beginTransaction();
                                fragment = new ReportFragment();
                                fragment.setArguments(getIntent().getExtras());
                                ft.replace(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.customDay:

                                if (!fromTv.getText().toString().equals("") & !toTv.getText().toString().equals("")) {
                                    btTimeRp.setText(fromTv.getText().toString() + "\n-" + toTv.getText().toString());
                                    from = fromTv.getText().toString();
                                    to = toTv.getText().toString();
                                    ft = getSupportFragmentManager().beginTransaction();
                                    fragment = new ReportFragment();
                                    fragment.setArguments(getIntent().getExtras());
                                    ft.replace(R.id.fragment_container, fragment).commit();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Nhập khoảng thời gian thiếu !", Toast.LENGTH_LONG).show();
                                }
                                break;
                        }

                    }
                });
                dialogBuilder.create();
                dialogBuilder.show();


            }
        });


        mStatusChecker = new Runnable() {
            @Override
            public void run() {
                try {
                    settings.getString("bellNotif", "");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    handler.postDelayed(mStatusChecker, mInterval);
                }
            }
        };

        handler = new Handler();
        mStatusChecker.run();

    }

    // click nút tùy chọn trong radio button tùy chọn của dialog
    public void ok6(View view) {
        linearLayout.setVisibility(View.VISIBLE);
    }

    public void ok5(View view) {
        linearLayout.setVisibility(View.GONE);
    }

    public void ok4(View view) {
        linearLayout.setVisibility(View.GONE);
    }

    public void ok3(View view) {
        linearLayout.setVisibility(View.GONE);
    }

    public void ok2(View view) {
        linearLayout.setVisibility(View.GONE);
    }

    public void ok1(View view) {
        linearLayout.setVisibility(View.GONE);
    }

    public void registerReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            startService(new Intent(getBaseContext(), MyServiceLocation.class));
        }
        super.onPause();
    }


    @Override
    public void onBackPressed() {

        if (pDialog != null)
            if (pDialog.isShowing())
                pDialog.dismiss();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /// check conect NetWork
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    /**
     * Đổi ngôn ngữ
     *
     * @param langval ký hiệu ngôn ngữ
     */
    public void setLangRecreate(String langval) {
        Configuration config = getBaseContext().getResources().getConfiguration();
        locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        recreate();
    }

    // chuyển ngôn ngữ
    public void changeLanguage(String language) {

        String url = settings.getString("api_server", "") + "/api/v1/users/language";
        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("USER_ID", settings.getString(getResources().getString(R.string.user_id_object), ""));
        jsonParams.put("language", language);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("doLogin", volleyError.toString());
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
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.

        int id = item.getItemId();

        ft = getSupportFragmentManager().beginTransaction();
        if (id == R.id.nav_setting) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CrmMainActivity.this);
            builder.create();
            List<String> listOptions = new ArrayList<>();
            listOptions.add("Tiếng Việt");
            listOptions.add("English");

            ListAdapter listAdapter = new ArrayAdapter<>(CrmMainActivity.this, android.R.layout.simple_list_item_1, listOptions);
            builder.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "hi").commit();
                        setLangRecreate("vi");
                        String language = "VN";
                        changeLanguage(language);
                    }
                    if (which == 1) {
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "en").commit();
                        setLangRecreate("en");

                        String language = "EN";
                        changeLanguage(language);
                    }
                }
            });
            builder.show();

        } else if (id == R.id.nav_home) {
            tvNotification.setText(getResources().getString(R.string.home));
            tvNotification.setVisibility(View.VISIBLE);
            btTimeRp.setVisibility(View.GONE);
            tvReport.setText("");
            SearchAdress.setVisibility(View.GONE);

            fragment = new Home_Fragment();
            fragment.setArguments(getIntent().getExtras());

        } else if (id == R.id.report) {
            SearchAdress.setVisibility(View.GONE);
            tvNotification.setVisibility(View.GONE);
            btTimeRp.setVisibility(View.VISIBLE);
            tvReport.setText(getResources().getString(R.string.report));

            fragment = new ReportFragment();
            fragment.setArguments(getIntent().getExtras());
        } else if (id == R.id.nav_customer) {
            tvNotification.setText(getResources().getString(R.string.customer));
            tvNotification.setVisibility(View.VISIBLE);
            btTimeRp.setVisibility(View.GONE);
            tvReport.setText("");
            SearchAdress.setVisibility(View.VISIBLE);

            fragment = new Customer_MainFragment();
            fragment.setArguments(getIntent().getExtras());
        } else if (id == R.id.check_in) {
//            tvNotification.setText("Định vị");
            Intent intent = new Intent(CrmMainActivity.this, MapActivity.class);
            startActivity(intent);
        } else if (id == R.id.transaction) {
            tvNotification.setText(getResources().getString(R.string.transaction));
            tvNotification.setVisibility(View.VISIBLE);
            btTimeRp.setVisibility(View.GONE);
            tvReport.setText("");
            SearchAdress.setVisibility(View.GONE);

            fragment = new Transaction_MainFragment();
            fragment.setArguments(getIntent().getExtras());
        } else if (id == R.id.nav_order) {
            tvNotification.setText(getResources().getString(R.string.order));
            tvNotification.setVisibility(View.VISIBLE);
            btTimeRp.setVisibility(View.GONE);
            tvReport.setText("");
            SearchAdress.setVisibility(View.GONE);

            fragment = new OrderFragment();
            fragment.setArguments(getIntent().getExtras());

        } else if (id == R.id.goods) {
            tvNotification.setVisibility(View.VISIBLE);
            btTimeRp.setVisibility(View.GONE);
            tvReport.setText("");
            SearchAdress.setVisibility(View.GONE);

            tvNotification.setText("Hàng hóa");
            fragment = new GoodsFragment();
            fragment.setArguments(getIntent().getExtras());
        } else if (id == R.id.contract) {
            tvNotification.setVisibility(View.VISIBLE);
            btTimeRp.setVisibility(View.GONE);
            tvReport.setText("");
            SearchAdress.setVisibility(View.GONE);

            tvNotification.setText(getResources().getString(R.string.contract));
            fragment = new ContractFragment();
            fragment.setArguments(getIntent().getExtras());
        } else if (id == R.id.nav_warehouse) {
            tvNotification.setText(getResources().getString(R.string.warehouse));
            tvNotification.setVisibility(View.VISIBLE);
            btTimeRp.setVisibility(View.GONE);
            tvReport.setText("");
            SearchAdress.setVisibility(View.GONE);

            fragment = new Warehouse_Fragment();
            fragment.setArguments(getIntent().getExtras());
        } else if (id == R.id.nav_log_out) {

            AlertDialog.Builder builder = new AlertDialog.Builder(CrmMainActivity.this);
            builder.create();
            builder.setTitle(getResources().getString(R.string.logout));
            builder.setMessage(getResources().getString(R.string.do_logout));
            builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            }).setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Clear sharedpreferences
                    doUser();
                    String api_server = settings.getString("api_server", "");
                    SharedPreferences.Editor editor = settings.edit();
                    editor.clear();
                    editor.putString("api_server", api_server);
                    editor.apply();
                    Intent intent = new Intent(CrmMainActivity.this, CrmLoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
            builder.show();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (fragment != null) {
            drawer.closeDrawer(GravityCompat.START);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
                    ft.replace(R.id.fragment_container, fragment).commit();
//                }
//            }, 250);


        }
        return true;
    }

    // gửi dữ liệu lên khi logout
    public void doUser() {
        String url = settings.getString("api_server", "") + "/api/v1/logout";
        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("USER_ID", settings.getString(getResources().getString(R.string.user_id_object), ""));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("doLogin", volleyError.toString());
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
    protected void onResume() {
        super.onResume();
        alreadyRunning = true;
        doGetCustomersAPI();


    }

    @Override
    protected void onPause() {

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        alreadyRunning = false;
        super.onPause();

    }

    @Override
    protected void onDestroy() {

        alreadyRunning = false;
        super.onDestroy();

    }

    @Override
    protected void onStop() {

        alreadyRunning = false;
        super.onStop();

    }

    @Override
    protected void onStart() {

        alreadyRunning = true;
        super.onStart();

    }

    private boolean checkPlayServices() {

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();

        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {

            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("== Check Service ==", "This device is not supported");
            }
            return false;

        }

        return true;

    }

    private String getRegistrationId(Context context) {

        final SharedPreferences prefs = getGCMPreferences(context);

        String registrationId = prefs.getString(PROPERTY_REG_ID, "");

        if (registrationId.isEmpty()) {
            Log.i("== Reg Id ==", "Registration not found.");
            return "";
        }

        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i("== App version ==", "App version changed.");
            return "";
        }
        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context) {
        return this.getSharedPreferences(PREFS_GCM_NAME, 0);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void registerInBackground(final Context context) {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regId = gcm.register(Config.SENDER_ID);
                    sendRegistrationIdToBackend();
                    storeRegistrationId(context, regId);
                } catch (IOException ioe) {
                    Log.e("== Register Id ==", ioe.toString());
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }.execute();
    }

    private void sendRegistrationIdToBackend() {

        String url = settings.getString("api_server", "") + "/api/v1/users/token";
        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put(getResources().getString(R.string.user_id), settings.getString(getResources().getString(R.string.user_id_object), ""));
        jsonParams.put("device_token", regId);
        jsonParams.put("device_type", "1");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && "success".equals(jsonObject.optString(getResources().getString(R.string.messages)))) {

                } else if (jsonObject != null && "fails".equals(jsonObject.optString(getResources().getString(R.string.messages)))) {
                    Toast.makeText(CrmMainActivity.this, jsonObject.optString("details"), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("== Regist Token ==", volleyError.toString());
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

    private void storeRegistrationId(Context context, String regId) {

        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i("== Save Pref ==", "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.apply();

    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }


    public void doGetCustomersAPI() {
        String url = settings.getString("api_server", "") + "/api/v1/customer/potential?index=" + 0 +
                "&USER_ID=" + settings.getString(getResources().getString(R.string.user_id_object), "");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                a = jsonObject.optInt("total");
                a1 = jsonObject.optInt("total_news");
                a2 = jsonObject.optInt("total_care");
                a3 = jsonObject.optInt("total_signed");
                a4 = jsonObject.optInt("total_unfollow");
                a5 = jsonObject.optInt("total_other");
//                b.add(a);
//                Toast.makeText(getActivity(), "=========" + a, Toast.LENGTH_LONG).show();
                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
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



    public void doGetUsers() {

        String url = settings.getString("api_server", "") + "/api/v1/users";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {
                    try {

                        JSONArray array = jsonObject.optJSONArray(CrmMainActivity.this.getResources().getString(R.string.users));
                        UsersModel usersModel = new UsersModel();
                        List<Users> usersList = usersModel.getUsers(realm);
                        Users users = null;
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.optJSONObject(i);
                            String userId = object.optString(CrmMainActivity.this.getResources().getString(R.string.user_id));
                            boolean check = false;
                            for (Users user : usersList) {
                                if (userId.equals(String.valueOf(user.getUserId()))) {
                                    check = true;
                                    break;
                                }
                            }
                            if (!check) {
                                users = new Users();
                                users.setUserId(Long.parseLong(userId));
                                users.setUserName(object.optString(CrmMainActivity.this.getResources().getString(R.string.user_name)));
                                users.setUserFullName(object.optString(CrmMainActivity.this.getResources().getString(R.string.user_full_name)));
                                users.setUserSubset(object.optString(CrmMainActivity.this.getResources().getString(R.string.user_subset)));
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CrmMainActivity.this.getResources().getString(R.string.date_format), Locale.getDefault());

                                try {
                                    users.setUserBirthDay(simpleDateFormat.parse(object.optString(CrmMainActivity.this.getResources().getString(R.string.user_birthday))));
                                } catch (ParseException e) {
                                    Log.e("loginuser", e.getLocalizedMessage());
                                }

                                users.setUserEmail(object.optString(CrmMainActivity.this.getResources().getString(R.string.user_email)));
                                users.setUserOwner(object.optInt(CrmMainActivity.this.getResources().getString(R.string.owner)));
                                users.setUserPosition(object.optString(CrmMainActivity.this.getResources().getString(R.string.user_position)));
                                UsersGroup usersGroup = new UsersGroup();
                                usersGroup.setUserGroupId(object.optInt(CrmMainActivity.this.getResources().getString(R.string.group_id)));
                                users.setUsersGroup(usersGroup);

                                usersModel.createUsers(realm, users);
                            }
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



}