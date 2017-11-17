package vn.altalab.app.crmvietpack.customer.edit_create_customer;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import vn.altalab.app.crmvietpack.object.TblPosition;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class Customer_Contact_Create_Activity extends AppCompatActivity {

    private static final String PRESFS_NAME = "CRMVietPrefs";
    private Shared_Preferences sharedPreferences;
    ArrayAdapter<String> adapterPosition;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;

    private EditText etName, etEmail, etMobiphone, etWorkphone, etBirthday;
    private Spinner spnGender, spnPosition;
    private TextView tvStatus;

    private Toolbar toolbar;
    int PositonID = 1;
    private long customerId = 0;
    private int genderPosition = 1;
    Button btAddContact;
    ImageButton ibBirthdayPicker;
    List<TblPosition> tblPositions;
    ProgressDialog pDialog;
    String ChosePosition;
    Link link;
    ArrayAdapter<String> adapter;

    String Gender[] = {"Nam", "Nữ"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customerdetail_contactcreate_activity);

        findViewId();
        Action();

    }

    public void findViewId() {
        tblPositions = new ArrayList<>();
        tvStatus = (TextView) findViewById(R.id.tvStatus);

        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etBirthday = (EditText) findViewById(R.id.etBirthday);
        etMobiphone = (EditText) findViewById(R.id.etMobiPhone);
        etWorkphone = (EditText) findViewById(R.id.etWorkphone);

        spnGender = (Spinner) findViewById(R.id.spnGender);
        spnPosition = (Spinner) findViewById(R.id.spnPosition);

        btAddContact = (Button) findViewById(R.id.btnAddCustomerContact);
        ibBirthdayPicker = (ImageButton) findViewById(R.id.ibBirthdayPicker);

        etBirthday.setEnabled(false);

    }

    // Chức danh
    public void SpinerPosition() {
        String PositionName[] = new String[tblPositions.size()];
        final int PositionId[] = new int[tblPositions.size()];

        for (int i = 0; i < tblPositions.size(); i++) {
            PositionName[i] = tblPositions.get(i).getPositionName();
            PositionId[i] = tblPositions.get(i).getPositionId();
        }
        adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, PositionName);
        spnPosition.setAdapter(adapter);
        spnPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ChosePosition = "" + PositionId[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // lấy ra list chức danh
    public void doGetPosition() {
        String url = sharedPreferences.getString("api_server") + "/api/v1/position";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {
                    try {
                        JSONArray array = jsonObject.optJSONArray("position");
                        for (int i = 0; i < array.length(); i++) {
                            TblPosition tblPosition = new TblPosition();
                            JSONObject object = array.optJSONObject(i);
                            tblPosition.setPositionName(object.optString("POSITION_NAME"));
                            tblPosition.setPositionId(object.optInt("POSITION_ID"));
                            tblPositions.add(tblPosition);
                        }
                        SpinerPosition();

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

    public void Action() {

        // cai dat sharedpreferences
        if (sharedPreferences == null) {
            sharedPreferences = new Shared_Preferences(this, PRESFS_NAME);
        }

        link = new Link(this);

        getToolbar();

        // nhan birthday
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            customerId = bundle.getLong("customer_id");
        }

        Log.e("CUSTOMERCREATE", "customerId: " + customerId);


        doGetPosition();

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, Gender);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spnGender.setAdapter(adapter);
        spnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    genderPosition = 1;
                } else if (position == 1) {
                    genderPosition = 2;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                genderPosition = 1;
            }
        });

        //set action for btnBirthdayPicker
        ibBirthdayPicker.setOnClickListener(new View.OnClickListener() {
            // bật lên Dialog
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);

                simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        etBirthday.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        btAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etName.getText().toString().equals("")) {

                    String strError = "";

                    if (etName.getText().toString().equals("")) {
                        strError = strError + "\nTên.";
                    }

                    if (etName.getText().toString().equals("")) {
                        etName.requestFocus();
                    }

                    tvStatus.setText("Mời nhập dữ liệu: " + strError);

                } else {

                    tvStatus.setText("");

                    post_DATA();

                }

            }
        });

    }

    private Map<String, String> get_JSON() {

        Map<String, String> jsonParams = new HashMap<>();

        jsonParams.put("USER_ID", link.getId());
        jsonParams.put("CONTACT_FULL_NAME", etName.getText().toString());
        jsonParams.put("CUSTOMER_ID", String.valueOf(customerId));
        jsonParams.put("CONTACT_EMAIL", etEmail.getText().toString());
        jsonParams.put("GENDER", String.valueOf(genderPosition));
        jsonParams.put("CONTACT_MOBIPHONE", etMobiphone.getText().toString());
        jsonParams.put("CONTACT_WORKPHONE", etWorkphone.getText().toString());
        jsonParams.put("BIRTHDAY", etBirthday.getText().toString());
        jsonParams.put("POSITION_ID", ChosePosition);

        return jsonParams;

    }

    public void post_DATA() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Xin chờ ...");
        pDialog.setCancelable(true);
        pDialog.show();

        String url = link.post_CUSTOMERDETAIL_ContactAdditon_Link();
        Map<String, String> jsonParams = get_JSON();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("CUSTOMERCREATE", "Response: " + jsonObject);
                        if (pDialog != null && pDialog.isShowing())
                            pDialog.dismiss();

                        try {
                            if (jsonObject.getString("messages").equals("success")) {
                                Toasty.success(getApplication(),"Thêm mới liên hệ thành công",Toast.LENGTH_LONG).show();
                                setResult(2);
                                finish();
                            } else {
                                if (jsonObject.getString("details") != null && !jsonObject.getString("details").equals("null"))
                                    Toasty.normal(Customer_Contact_Create_Activity.this, "" + jsonObject.getString("details"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Log.e("CUSTOMERCREATE", "Error: " + volleyError.toString());

                if (pDialog != null && pDialog.isShowing())
                    pDialog.dismiss();

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

    public void getToolbar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tvNotification = (TextView) findViewById(R.id.tvNotification);
        LinearLayout llBell = (LinearLayout) findViewById(R.id.llBell);

        tvNotification.setText("Thêm liên hệ");

        llBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Customer_Contact_Create_Activity.this, Notification_Activity.class);
                intent.putExtra("type_object", 1);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(2);
        finish();
    }

}
