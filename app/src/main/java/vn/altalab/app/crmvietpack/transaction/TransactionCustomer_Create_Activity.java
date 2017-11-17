package vn.altalab.app.crmvietpack.transaction;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import vn.altalab.app.crmvietpack.Link;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.Shared_Preferences;
import vn.altalab.app.crmvietpack.model.UsersModel;
import vn.altalab.app.crmvietpack.notification.Notification_Activity;
import vn.altalab.app.crmvietpack.object.TransactionType;
import vn.altalab.app.crmvietpack.object.Users;
import vn.altalab.app.crmvietpack.orders_fragment.order_detail.ChoseCusActivity;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class TransactionCustomer_Create_Activity extends AppCompatActivity implements View.OnClickListener {
    int bytesRead, bytesAvailable, bufferSize;
    ProgressDialog pDialog;
    Link link;

    private Spinner spnEditTransType, spnTransStatus, spnTransPriority, spTRANSACTION_USER;
    private EditText edtTranName, edtTransDeadline, edtTransCompletedDate, edtDes, edtKh;
    private ImageButton btnTransDeadlinePicker, btnTransCompletedDatePicker;
    private Button btnCreateTransaction;

    private UsersModel usersModel = new UsersModel();
    private Realm realm;
    Shared_Preferences sharedPreferences;
    Shared_Preferences sharTRANSACTIONTYPE;
    private SimpleDateFormat simpleDateFormat, simpleDateFormat2;
    Calendar calendar;
    List<Users> userses;

    long customerId = 0;
    String customerName;
    String[] name_Priorities = {"1", "2", "3", "4", "5"};
    int year, month, day;
    int hour, minute;

    String PREFS_NAME = "CRMVietPrefs";
    String userId;
    String choose_TYPE = "1";
    String choose_STATUS = "1";
    String choose_PRIORITY = "1";
    String choose_TRANSACTION_USER = "1";


    String[] name_TRANSACTIONTYPE = {"Chưa thực hiện", "Đang thực hiện", "Đã giải quyết", "Đã hoàn thành"};
    String[] code_TRANSACTIONTYPE = {"1", "2", "4", "3"};
    List<TransactionType> transactionTypes;

    private Button buttonChoose, outChoose, btchooseKh;

    private EditText editTextName;
    LinearLayout lnFileName;
    TextView tvFileName;
    private int PICK_FILE_REQUEST = 2;
    private String selectedFilePath = "";
    private String fileName = "";
    String encodeFile = "";
    static boolean run = true;
    static int clear = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_transaction_customer_create);

        Action();

        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        outChoose = (Button) findViewById(R.id.outChoose);


        tvFileName = (TextView) findViewById(R.id.tv_file_name);
        lnFileName = (LinearLayout) findViewById(R.id.ln_file_name);

        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();

            }
        });
// clear ảnh khi bỏ chọn :
        outChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outChoose.setVisibility(View.GONE);
                lnFileName.setVisibility(View.GONE);
                clear = 0;
                run = true;
                selectedFilePath = "";
            }
        });

    }

    // lấy nguồn ảnh từ mobile
    private void showFileChooser() {

        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("*/*");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data == null) {
                    //no data present
                    return;
                }

                lnFileName.setVisibility(View.VISIBLE);
                clear = 1;
                run = true;
                outChoose.setVisibility(View.VISIBLE);
                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(this, selectedFileUri);


                if (selectedFilePath != null && !selectedFilePath.equals("")) {
                    String a[] = selectedFilePath.split("/");
                    fileName = a[a.length - 1];
                    tvFileName.setText(selectedFilePath);
                } else {
                    Toasty.normal(this, "Không thể upload file lên server", Toast.LENGTH_SHORT).show();
                    selectedFilePath = "";
                }
            }
        }

        if (requestCode == 1) {
            if (data.getLongExtra("customer_id", 0) != 0) {
                customerId = data.getLongExtra("customer_id", 0);
                customerName = data.getStringExtra("customer_name");
                edtKh.setText(customerName);
            }
        }
    }


    public void Action() {

        getToolbar();

        // Khai báo Id
        getfindViewById();

        // Khai báo SharedPreferences
        if (sharedPreferences == null) {
            sharedPreferences = new Shared_Preferences(this, PREFS_NAME);
        }

        userses = new ArrayList<>();

        transactionTypes = new ArrayList<>();
        doGetTransactionType();
//        set_SPINNER_TYPE();


        // nh?n UserId
        link = new Link(this);
        userId = link.getId();

        // Nhan Id
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            customerId = bundle.getLong("customer_id");
            customerName = bundle.getString("customer_name");
        }


        edtKh.setText(customerName);

        btchooseKh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionCustomer_Create_Activity.this, ChoseCusActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        Log.e("TranCreaCustomerId", "");



        // calendar
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        simpleDateFormat2 = new SimpleDateFormat("HH:mm", Locale.getDefault());

        // spinner Status Adapter
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, name_TRANSACTIONTYPE);
        spnTransStatus.setAdapter(adapterStatus);
        spnTransStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choose_STATUS = code_TRANSACTIONTYPE[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // spinner Priority adapter
        ArrayAdapter<String> adapterPriority = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, name_Priorities);
        spnTransPriority.setAdapter(adapterPriority);
        spnTransPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choose_PRIORITY = name_Priorities[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // spinner TRANSACTION_USER
        doGetUsers();

        edtTransDeadline.setText(simpleDateFormat.format(calendar.getTime()));
        edtTransCompletedDate.setText(simpleDateFormat2.format(calendar.getTime()));

        // S? ki?n click
        btnTransDeadlinePicker.setOnClickListener(this);
        btnTransCompletedDatePicker.setOnClickListener(this);
        btnCreateTransaction.setOnClickListener(this);


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
            if (userses.get(i).getUserId() == Long.parseLong(userId)) {
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

    // Transaction type
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
        spnEditTransType.setAdapter(adapterTYPE);

        spnEditTransType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
    }


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


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnTransDeadlinePicker:

                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        edtTransDeadline.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, year, month, day);
                datePickerDialog.show();

                break;

            case R.id.btnCreateTransaction:

                if ("".equals(edtTranName.getText().toString().trim())) {
                    Toasty.normal(TransactionCustomer_Create_Activity.this, this.getResources().getString(R.string.ctransaction_empty_transaction_name), Toast.LENGTH_SHORT).show();
                } else {
                    if (!selectedFilePath.equals("")) {
                        File selectedFile = new File(selectedFilePath);
                        encodeFile = encodeFileToBase64Binary(selectedFile);
                    }
                    if (run == true) {
                        btnCreateTransaction.setClickable(false);
                        doTransactionAdd();
                    }

                }

                break;

            case R.id.btnTransCompletedDatePicker:

                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                calendar.set(year, month, day, hourOfDay, minute);
                                edtTransCompletedDate.setText(simpleDateFormat2.format(calendar.getTime()));
                            }
                        }, hour, minute, false);
                timePickerDialog.show();

                break;

        }
    }

    public void getToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvNotification = (TextView) findViewById(R.id.tvNotification);
        LinearLayout llBell = (LinearLayout) findViewById(R.id.llBell);

        // getSupport
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // findViewById
        tvNotification.setText("Thêm giao dịch");
        llBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(TransactionCustomer_Create_Activity.this, Notification_Activity.class);
                intent.putExtra("type_object", 1);
                startActivity(intent);

            }

        });

    }

    public void getfindViewById() {

        // editText
        edtTranName = (EditText) findViewById(R.id.edtTranName);
        edtTransDeadline = (EditText) findViewById(R.id.edtTransDeadline);
        edtTransCompletedDate = (EditText) findViewById(R.id.edtTransCompletedDate);
        edtDes = (EditText) findViewById(R.id.edtTranDes);
        edtKh = (EditText) findViewById(R.id.edtKH);
        edtKh.setEnabled(false);

        // spinner
        spnEditTransType = (Spinner) findViewById(R.id.spnTransType);
        spnTransStatus = (Spinner) findViewById(R.id.spnTransStatus);
        spnTransPriority = (Spinner) findViewById(R.id.spnTransPriority);
        spTRANSACTION_USER = (Spinner) findViewById(R.id.spTRANSACTION_USER);

        // button
        btnTransDeadlinePicker = (ImageButton) findViewById(R.id.btnTransDeadlinePicker);
        btnTransCompletedDatePicker = (ImageButton) findViewById(R.id.btnTransCompletedDatePicker);
        btnCreateTransaction = (Button) findViewById(R.id.btnCreateTransaction);
        btchooseKh = (Button) findViewById(R.id.btKh);

        // set false a little edit
        edtTransDeadline.setEnabled(false);
        edtTransCompletedDate.setEnabled(false);

    }

    @Override
    public void onBackPressed() {

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

    private void doTransactionAdd() {


        Link link;
        String url;


        link = new Link(this);
        url = link.post_CUSTOMERDETAIL_TransactionAdditon_Link();
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        Map<String, String> jsonParams = new Hashtable<String, String>();
        //Adding parameters

        if (clear != 0) {
            jsonParams.put("uploaded_file", encodeFile);
            jsonParams.put("file_name", fileName);
        }
        if (customerId != 0) {
            jsonParams.put("CUSTOMER_ID", "" + customerId);

        }
        jsonParams.put("USER_ID", userId);

        jsonParams.put("TRANSACTION_NAME", edtTranName.getText().toString());

        jsonParams.put("TRANSACTION_USER", choose_TRANSACTION_USER);

        jsonParams.put("TRANSACTION_TYPE_ID", choose_TYPE);

        jsonParams.put("STATUS", choose_STATUS);

        jsonParams.put("END_DATE", simpleDateFormat.format(calendar.getTime()) + " " + simpleDateFormat2.format(calendar.getTime()));

        jsonParams.put("PRIORITY", choose_PRIORITY);

        jsonParams.put("TRANSACTION_DESCRIPTION", edtDes.getText().toString());


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                //Disimissing the progress dialog

                //Showing toast message of the response
                try {
                    if (jsonObject.getString("messages").equals("success")) {
                        loading.dismiss();
                        setResult(1);
                        finish();
                        btnCreateTransaction.setClickable(true);
                        Toasty.success(getApplication(), "Thêm mới giao dịch thành công !", Toast.LENGTH_LONG).show();

                    } else {
                        if (jsonObject.getString("details") != null && !jsonObject.getString("details").equals("null"))
                            Toasty.normal(getApplication(), "Lỗi: " + jsonObject.getString("details"), Toast.LENGTH_SHORT).show();
                        btnCreateTransaction.setClickable(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        Log.e("CONTACTEDITION", "Error: " + volleyError.toString());
                        loading.dismiss();
                        btnCreateTransaction.setClickable(true);

                        //Showing toast
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
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private String encodeFileToBase64Binary(File file) {
        String encodedfile = "";
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);


            if (file.length() > 13631488) {
//                Toast.makeText(getApplicationContext(), "File quá lớn, không thể upload !", Toast.LENGTH_LONG).show();
                Dialog();
                run = false;
            } else {
                byte[] bytes = new byte[(int) file.length()];
                fileInputStreamReader.read(bytes);
                encodedfile = Base64.encodeToString(bytes, Base64.DEFAULT);
            }

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }

        return encodedfile;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void Dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("File quá lớn !");
        builder.setMessage("Upload file dung lượng dưới 12 MB");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }

        });
        builder.create().show();
    }

}
