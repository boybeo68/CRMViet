package vn.altalab.app.crmvietpack.transaction;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import vn.altalab.app.crmvietpack.Link;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.Shared_Preferences;
import vn.altalab.app.crmvietpack.notification.Notification_Activity;
import vn.altalab.app.crmvietpack.object.TransactionType;
import vn.altalab.app.crmvietpack.object.Users;
import vn.altalab.app.crmvietpack.orders_fragment.order_detail.ChoseCusActivity;
import vn.altalab.app.crmvietpack.transaction.json.TransactionDetail_Json;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class TransactionDetail_Editation_Activity extends AppCompatActivity implements View.OnClickListener {
    String PREFS_NAME = "CRMVietPrefs";
    // View
    List<TransactionType> transactionTypes;
    List<Users> userses;

    EditText etNAME, etDATE, etTIME, etTRANSACTION_DESCRIPTION, edtKh;
    Spinner spTYPE, spSTATUS, spPRIORITY, spTRANSACTION_USER;
    Button btEDIT, btCHoseKh;
    ImageButton ibTIMEPICKER, ibDATEPICKER;
    Shared_Preferences sharedPreferences;
    TransactionDetail_Json transactionDetailJson;
    Link link;

    // String Int Array
    long customerId = 0;
    String customerName;
    String transactionId;
    String get_JSON = "";
    String[] STATUS_ID = {"1", "2", "4", "3"};
    String[] STATUS = {"Chưa thực hiện", "Đang thực hiện", "Đã giải quyết", "Đã hoàn thành"};
    String[] PRIORITY_ID = {"1", "2", "3", "4", "5"};
    String choose_STATUS = "1";
    String choose_PRIORITY = "1";
    String choose_TYPE = "1";
    String Type;
    String TransactionUser;
    String choose_TRANSACTION_USER = "1";
    ArrayList TYPE_LIST;
    String TRANSACTION_USER = "1";
    ProgressDialog pDialog;


    private int year1, month1, day1, hour1, minute1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crm_transaction_edit);

        FINDVIEWBYID();

        Action();
        transactionTypes = new ArrayList<>();
        doGetTransactionType();
        userses = new ArrayList<>();
        doGetUsers();

    }

    public void FINDVIEWBYID() {

        etNAME = (EditText) findViewById(R.id.etNAME);
        etDATE = (EditText) findViewById(R.id.etDATE);
        etTIME = (EditText) findViewById(R.id.etTIME);
        etTRANSACTION_DESCRIPTION = (EditText) findViewById(R.id.etTRANSACTION_DESCRIPTION);

        spTYPE = (Spinner) findViewById(R.id.spTYPE);
        spSTATUS = (Spinner) findViewById(R.id.spSTATUS);
        spPRIORITY = (Spinner) findViewById(R.id.spPRIORITY);
        spTRANSACTION_USER = (Spinner) findViewById(R.id.spTRANSACTION_USER);

        ibDATEPICKER = (ImageButton) findViewById(R.id.ibDATEPICKER);
        ibTIMEPICKER = (ImageButton) findViewById(R.id.ibTIMEPICKER);

        btEDIT = (Button) findViewById(R.id.btEDIT);
        btCHoseKh = (Button) findViewById(R.id.btKh);

        edtKh = (EditText) findViewById(R.id.edtKH);

        edtKh.setEnabled(false);

        etDATE.setEnabled(false);
        etTIME.setEnabled(false);

    }

    public void Action() {
        if (sharedPreferences == null) {
            sharedPreferences = new Shared_Preferences(this, PREFS_NAME);
        }

        getToolbar();

        btCHoseKh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionDetail_Editation_Activity.this, ChoseCusActivity.class);
                startActivityForResult(intent,1);
            }
        });

        link = new Link(this);

        TYPE_LIST = new ArrayList();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            transactionId = bundle.getString("transaction_id");
            Type = bundle.getString("transaction_type");
            TransactionUser = bundle.getString("transaction_user");

            Log.e("TransactionDetail", "transactionId: " + transactionId);

            try {

                get_JSON = bundle.getString("json");

                Log.e("TransactionDetail", "get_JSON: " + get_JSON);

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

        Log.e("editation", "transactionId: " + transactionId);

        if (!get_JSON.equals(""))
            set_VIEW(get_JSON);
        else
            get_DATA(transactionId);


        ibDATEPICKER.setOnClickListener(this);
        ibTIMEPICKER.setOnClickListener(this);
        btEDIT.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1){
            if (data.getLongExtra("customer_id", 0) != 0) {
                customerId = data.getLongExtra("customer_id", 0);
                customerName = data.getStringExtra("customer_name");
                edtKh.setText(customerName);
            }
        }
    }

    public void get_DATA(String transactionId) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Xin chờ giây lát ...");
        pDialog.setCancelable(true);
        pDialog.show();

        Link link = new Link(this);
        String url = link.get_TransactionDetail_GetLink(transactionId);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("TRANSACTIONDETAIL", "RESPONSE: " + jsonObject);

                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                try {
                    if (jsonObject != null)
                        set_VIEW(String.valueOf(jsonObject));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TRANSACTIONDETAIL", "ERROR: " + volleyError.toString());
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

        request.setShouldCache(false);
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        MySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(request);

    }

    public Map<String, String> get_PARAMS() {

        Map<String, String> jsonParams = new HashMap<>();
        if (customerId != 0) {
            jsonParams.put("CUSTOMER_ID", "" + customerId);

        }
        jsonParams.put("TRANSACTION_ID", transactionId);
        jsonParams.put("USER_ID", link.getId());
        jsonParams.put("TRANSACTION_NAME", etNAME.getText().toString());
        jsonParams.put("END_DATE", etDATE.getText().toString() + " " + etTIME.getText().toString());
        jsonParams.put("TRANSACTION_USER", choose_TRANSACTION_USER);
        jsonParams.put("TRANSACTION_TYPE_ID", choose_TYPE);
        jsonParams.put("STATUS", choose_STATUS);
        jsonParams.put("PRIORITY", choose_PRIORITY);
        jsonParams.put("TRANSACTION_DESCRIPTION", etTRANSACTION_DESCRIPTION.getText().toString());

        return jsonParams;
    }

    public void post_Data() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Xin chờ giây lát ...");
        pDialog.setCancelable(true);
        pDialog.show();

        String url = link.post_TRANSACTION_EDITION_Link();

        Map<String, String> jsonParams = get_PARAMS();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("TRANSACTIONEDITION", "JSONObject: " + jsonObject);

                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }

                try {
                    if (jsonObject != null) {
                        if (jsonObject.getString("messages").equals("success")) {
                            Toasty.success(getApplication(),"Sửa giao dịch thành công",Toast.LENGTH_LONG).show();
                            setResult(0);
                            finish();
                        } else {
                            if (jsonObject.getString("details") != null && !jsonObject.getString("details").equals("null"))
                                Toasty.normal(TransactionDetail_Editation_Activity.this, jsonObject.getString("details"), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TRANSACTIONEDITION", "Error: " + volleyError);
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
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }

    // Spinner danh sách loại giao dịch
    public void set_SPINNER_TYPE() {
        final String[] UsersName = new String[transactionTypes.size()];
        final String[] UsersId = new String[transactionTypes.size()];
        for (int i = 0; i < transactionTypes.size(); i++) {
            UsersName[i] = transactionTypes.get(i).getTransactionTypeName();
        }

        for (int i = 0; i < transactionTypes.size(); i++) {
            UsersId[i] = "" + transactionTypes.get(i).getTransactionTypeId();
        }
        ArrayAdapter adapterTYPE = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, UsersName);
        spTYPE.setAdapter(adapterTYPE);

        spTYPE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    choose_TYPE = UsersId[position];
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        for (int i = 0; i < transactionTypes.size(); i++) {
            if (transactionTypes.get(i).getTransactionTypeName().equals(Type)) {
                spTYPE.setSelection(i);
            }
        }
    }

    // lấy ra danh sách loại giao dịch
    public void doGetTransactionType() {


        String url = sharedPreferences.getString("api_server") + "/api/v1/transaction/type";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {
                    try {
                        JSONArray array = jsonObject.optJSONArray("transaction_types");
                        TransactionType transType = null;
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.optJSONObject(i);


                            transType = new TransactionType();

                            transType.setTransactionTypeId(Long.parseLong(object.optString("TRANSACTION_TYPE_ID")));
                            transType.setTransactionTypeName(object.optString("TRANSACTION_TYPE_NAME"));
                            transactionTypes.add(transType);
                        }
                        set_SPINNER_TYPE();

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

    private void set_VIEW(String data) {

        try {
            transactionDetailJson = new TransactionDetail_Json(new JSONObject(data));

            if (transactionDetailJson.isStatus() == true) {

                TRANSACTION_USER = transactionDetailJson.GET_SETGET().getTRANSACTION_USER();
                etNAME.setText(transactionDetailJson.GET_SETGET().getTRANSACTION_NAME_TEXT());
                edtKh.setText(transactionDetailJson.GET_SETGET().getCUSTOMER_NAME());
                customerId = transactionDetailJson.GET_SETGET().getCUSTOMER_ID();

                try {
                    etDATE.setText(transactionDetailJson.GET_SETGET().getEND_DATE().substring(0, 10).trim());
                    etTIME.setText(transactionDetailJson.GET_SETGET().getEND_DATE().substring(11, transactionDetailJson.GET_SETGET().getEND_DATE().length()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    Spanned spanned = Html.fromHtml(transactionDetailJson.GET_SETGET().getTRANSACTION_DESCRIPTION());
                    etTRANSACTION_DESCRIPTION.setText(spanned);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    year1 = Integer.valueOf(transactionDetailJson.GET_SETGET().getEND_DATE().substring(6, 10).trim());
                    month1 = Integer.valueOf(transactionDetailJson.GET_SETGET().getEND_DATE().substring(3, 5).trim());
                    day1 = Integer.valueOf(transactionDetailJson.GET_SETGET().getEND_DATE().substring(0, 2).trim());
                    hour1 = Integer.valueOf(transactionDetailJson.GET_SETGET().getEND_DATE().substring(11, 13).trim());
                    minute1 = Integer.valueOf(transactionDetailJson.GET_SETGET().getEND_DATE().substring(14, 16).trim());

                    Log.e("datetime", "year: " + transactionDetailJson.GET_SETGET().getEND_DATE().substring(6, 10) + "month: " + transactionDetailJson.GET_SETGET().getEND_DATE().substring(3, 5) + "day: " + transactionDetailJson.GET_SETGET().getEND_DATE().substring(0, 2) + "minutes: " + transactionDetailJson.GET_SETGET().getEND_DATE().substring(14, 16) + "hour: " + transactionDetailJson.GET_SETGET().getEND_DATE().substring(11, 13));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Spinner STATUS

                try {
                    ArrayAdapter adapterSTATUS = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, STATUS);
                    spSTATUS.setAdapter(adapterSTATUS);
                    spSTATUS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choose_STATUS = STATUS_ID[position];
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    Log.e("TRANSACTIONEDIT", "STATUS: " + transactionDetailJson.GET_SETGET().getSTATUS());
                    for (int i = 0; i < STATUS.length; i++) {
                        if (transactionDetailJson.GET_SETGET().getSTATUS().equals(STATUS_ID[i])) {
                            Log.e("TRANSACTIONEDIT", "STATUSSUCCESS");
                            spSTATUS.setSelection(i);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Spinner PRIORITY

                try {
                    ArrayAdapter adapterPRIORITY = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, PRIORITY_ID);
                    spPRIORITY.setAdapter(adapterPRIORITY);
                    spPRIORITY.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choose_PRIORITY = PRIORITY_ID[position];
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    Log.e("TRANSACTIONEDIT", "PRIORITY_ID: " + transactionDetailJson.GET_SETGET().getPRIORITY());
                    for (int i = 0; i < PRIORITY_ID.length; i++) {
                        if (transactionDetailJson.GET_SETGET().getPRIORITY().equals(PRIORITY_ID[i])) {
                            spPRIORITY.setSelection(i);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // spinner TRANSACTION_USER


//                if (!sharedPreferences.getString("type").equals("")){
//                    set_SPINNER_TYPE(sharedPreferences.getString("type"));
//                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // spinner danh sách user
    public void Spinner_USER() {

        final String[] UsersName = new String[userses.size()];
        final String[] UsersId = new String[userses.size()];

        for (int i = 0; i < userses.size(); i++) {
            UsersName[i] = userses.get(i).getUserName();
        }

        for (int i = 0; i < userses.size(); i++) {
            UsersId[i] = "" + userses.get(i).getUserId();
        }

        ArrayAdapter adapterTRANSACTION_USER = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, UsersName);
        spTRANSACTION_USER.setAdapter(adapterTRANSACTION_USER);
        spTRANSACTION_USER.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choose_TRANSACTION_USER = UsersId[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        for (int i = 0; i < userses.size(); i++) {
            if (userses.get(i).getUserName().equals(TransactionUser)) {
                spTRANSACTION_USER.setSelection(i);
            }

        }
    }

    // getUser lấy ra danh sách user
    public void doGetUsers() {

        String url = sharedPreferences.getString("api_server") + "/api/v1/users";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {
                    try {

                        JSONArray array = jsonObject.optJSONArray("users");

                        Users users = null;
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.optJSONObject(i);

                            users = new Users();
                            users.setUserId(object.optLong("USER_ID"));
                            users.setUserName(object.optString("USER_NAME"));

                            userses.add(users);


                        }
                        Spinner_USER();
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
    public void onClick(View view) {

        final Calendar calendar = Calendar.getInstance();

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm", Locale.getDefault());

        switch (view.getId()) {

            case R.id.ibDATEPICKER:

                Log.e("datetime", "year1: " + year1 + "month1: " + month1 + "day1: " + day1);

                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        calendar.set(year, monthOfYear, dayOfMonth);

                        etDATE.setText(simpleDateFormat.format(calendar.getTime()));

                    }

                }, year1, month1 - 1, day1);

                datePickerDialog.show();

                break;

            case R.id.ibTIMEPICKER:

                Log.e("datetime", "year1: " + year1 + "month1: " + month1 + "year1: " + year1 + "hour1: " + hour1 + "minute1: " + minute1);
                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar.set(year1, month1 - 1, day1, hourOfDay, minute);
                                etTIME.setText(simpleDateFormat2.format(calendar.getTime()));
                            }
                        }, hour1, minute1, false);
                timePickerDialog.show();
                break;

            case R.id.btEDIT:
                if(etNAME.getText().toString().trim().equals("")){
                    Toast.makeText(getApplication(),"Chưa nhập tên giao dịch !",Toast.LENGTH_LONG).show();
                }else {
                    post_Data();

                }
                break;
        }
    }

    public void getToolbar() {

        TextView tvNotification = (TextView) findViewById(R.id.tvNotification);
        LinearLayout llBell = (LinearLayout) findViewById(R.id.llBell);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvNotification.setText("Sửa giao dịch");

        llBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransactionDetail_Editation_Activity.this, Notification_Activity.class);
                intent.putExtra("type_object", 1);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(0);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}

