package vn.altalab.app.crmvietpack.customer.edit_create_customer;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import vn.altalab.app.crmvietpack.Link;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.Shared_Preferences;
import vn.altalab.app.crmvietpack.customer.customer_detail.CustomerDetail_Contact_Fragment;
import vn.altalab.app.crmvietpack.customer.customer_detail.json.CustomerDetail_ContactDetail_Json;
import vn.altalab.app.crmvietpack.customer.json.Position_Json;
import vn.altalab.app.crmvietpack.notification.Notification_Activity;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class CustomerDetail_ContactEditation_Activity extends AppCompatActivity {

    private Shared_Preferences sharedPreferences;
    CustomerDetail_Contact_Fragment customerDetail_contact_fragment = new CustomerDetail_Contact_Fragment();

    private Spinner spPosition, spGender;
    private EditText etFullName, etMobiphone, etWorkphone, etEmail, etBirthday;
    private ImageButton ibBirthdayPicker;
    private Button btEdit;

    private ArrayAdapter adapterGender, adapterPosition;
    private static String email, workPhone, mobilePhone;
    private String[] Gender = {"--Chưa chọn--","Nam", "Nữ"};
    private String GenderCode;
    private String contact_id = "";
    private String customer_id = "";
    private String data_Json = "";
    private ArrayList<String> POSITION_ID, POSITION_NAME;
    private String position_Id = "";
    private Position_Json positionJson;
    private String str_PositionJson;
    private ProgressDialog pDialog;

    Link link;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customerdetail_contacteditation_activity);

        FINDVIEWBYID();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            try {
                contact_id = bundle.getString("contact_id");
                customer_id = bundle.getString("customer_id");
                data_Json = bundle.getString("json");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                ACTION();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public void ACTION() {

        link = new Link(this);

        getToolbar();

        sharedPreferences = new Shared_Preferences(this, Link.PREFS_NAME + "CustomerDetail_ContactDetail");

        try {
            str_PositionJson = sharedPreferences.getString("position");
        } catch (Exception e) {
            e.printStackTrace();
        }

        get_POSITION(str_PositionJson);
        adapterGender = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Gender);
        spGender.setAdapter(adapterGender);
        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GenderCode = "" + position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                GenderCode = "1";
            }
        });


        try {
            if (!data_Json.equals(""))
                show_DATA(data_Json);
            else get_DATA(contact_id);
        } catch (Exception e) {
            e.printStackTrace();
        }


        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post_DATA();
            }
        });

        ibBirthdayPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

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
    }

    public void FINDVIEWBYID() {

        spPosition = (Spinner) findViewById(R.id.spPosition);
        spGender = (Spinner) findViewById(R.id.spGender);

        etFullName = (EditText) findViewById(R.id.etFullName);
        etMobiphone = (EditText) findViewById(R.id.etMobiphone);
        etWorkphone = (EditText) findViewById(R.id.etWorkphone);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etBirthday = (EditText) findViewById(R.id.etBirthday);
        etBirthday.setEnabled(false);

        ibBirthdayPicker = (ImageButton) findViewById(R.id.ibBirthdayPicker);
        btEdit = (Button) findViewById(R.id.btEdit);

    }

    public void get_DATA(String contact_id) {

        Link link = new Link(this);
        String url = link.get_CUSTOMERDETAIL_CONTACTDETAIL_Link(contact_id);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("CONTACTEDITION", "JSONObject: " + jsonObject);
                if (jsonObject != null)
                    show_DATA(String.valueOf(jsonObject));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("CONTACTEDITION", "Error: " + volleyError);
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

    public void get_POSITION(String data_Json) {

        positionJson = new Position_Json(data_Json);

        POSITION_ID = new ArrayList<>();
        POSITION_NAME = new ArrayList<>();

        if (positionJson.isStatus() == true) {
            POSITION_ID.add(0,"");
            POSITION_NAME.add(0,"--Chưa chọn--");
            for (int i = 0; i < positionJson.getList().size(); i++) {
                try {
                    POSITION_ID.add(positionJson.getList().get(i).getPOSITION_ID());

                } catch (Exception e) {
                    e.printStackTrace();
                    POSITION_NAME.add("");
                }
                try {
                    POSITION_NAME.add(positionJson.getList().get(i).getPOSITION_NAME());
                } catch (Exception e) {
                    e.printStackTrace();
                    POSITION_NAME.add("");
                }
            }

            adapterPosition = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, POSITION_NAME);
            spPosition.setAdapter(adapterPosition);
            spPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    position_Id = POSITION_ID.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    position_Id = POSITION_ID.get(0);
                }
            });
        }
    }

    public Map<String, String> get_PARAM() {

        Map<String, String> jsonParams = new HashMap<>();

        jsonParams.put("CONTACT_ID", contact_id);
        jsonParams.put("USER_ID", link.getId());
        jsonParams.put("CUSTOMER_ID", customer_id);
        jsonParams.put("CONTACT_FULL_NAME", etFullName.getText().toString());
        if (customerDetail_contact_fragment.contactPhone == 0) {
            jsonParams.put("CONTACT_MOBIPHONE", mobilePhone);
            jsonParams.put("CONTACT_WORKPHONE", workPhone);
        } else {
            jsonParams.put("CONTACT_MOBIPHONE", etMobiphone.getText().toString());
            jsonParams.put("CONTACT_WORKPHONE", etWorkphone.getText().toString());
        }
        if (customerDetail_contact_fragment.contactEmail == 0) {
            jsonParams.put("CONTACT_EMAIL", email);

        } else {
            jsonParams.put("CONTACT_EMAIL", etEmail.getText().toString());
        }

        jsonParams.put("BIRTHDAY", etBirthday.getText().toString());
        jsonParams.put("POSITION_ID", position_Id);

            jsonParams.put("GENDER", GenderCode);



        return jsonParams;
    }

    public void post_DATA() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Xin chờ ...");
        pDialog.setCancelable(true);
        pDialog.show();

        String url = link.post_CUSTOMERDETAIL_ContactEditation_Link();

        Map<String, String> jsonParams = get_PARAM();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("CONTACTEDITTION", "Response: " + jsonObject);
                if (pDialog != null && pDialog.isShowing())
                    pDialog.dismiss();
                try {
                    if (jsonObject.getString("messages").equals("success")) {
                        Toasty.success(getApplication(),"Sửa liên hệ thành công",Toast.LENGTH_LONG).show();
                        setResult(2);
                        finish();
                    } else {
                        if (jsonObject.getString("details") != null && !jsonObject.getString("details").equals("null"))
                            Toasty.normal(CustomerDetail_ContactEditation_Activity.this, "Lỗi: " + jsonObject.getString("details"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("CONTACTEDITION", "Error: " + volleyError.toString());
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

    public void show_DATA(String data) {
        Link link = new Link();
        try {
            CustomerDetail_ContactDetail_Json customerDetailContactDetailJson = new CustomerDetail_ContactDetail_Json(new JSONObject(data));

            if (customerDetailContactDetailJson.get_STATUS() == true) {
                mobilePhone = customerDetailContactDetailJson.get_SETGET().getCONTACT_MOBIPHONE();
                workPhone = customerDetailContactDetailJson.get_SETGET().getCONTACT_WORKPHONE();
                etFullName.setText(customerDetailContactDetailJson.get_SETGET().getCONTACT_FULL_NAME());
                if (link.getId().equals("1")) {
                    etMobiphone.setText(mobilePhone);
                    etWorkphone.setText(workPhone);
                } else {
                    if (customerDetail_contact_fragment.contactPhone == 0) {
                        etMobiphone.setText("*********");
                        etWorkphone.setText("*********");
                        etMobiphone.setEnabled(false);
                        etWorkphone.setEnabled(false);
                    } else {
                        etMobiphone.setText(mobilePhone);
                        etWorkphone.setText(workPhone);
                    }
                }

                email = customerDetailContactDetailJson.get_SETGET().getCONTACT_EMAIL();
                if (link.getId().equals("1")) {
                    etEmail.setText(email);
                } else {
                    if (customerDetail_contact_fragment.contactEmail == 0) {
                        etEmail.setText("*********");
                        etEmail.setEnabled(false);
                    } else {
                        etEmail.setText(email);
                    }
                }

                etBirthday.setText(customerDetailContactDetailJson.get_SETGET().getBIRTHDAY());

                for (int i = 0; i < POSITION_ID.size(); i++) {
                    if (customerDetailContactDetailJson.get_SETGET().getPOSITION_ID().equals(POSITION_ID.get(i))){
                        spPosition.setSelection(i);
                    }
                    if (customerDetailContactDetailJson.get_SETGET().getPOSITION_ID().equals("")||
                            customerDetailContactDetailJson.get_SETGET().getPOSITION_ID().equals("null")){
                        spPosition.setSelection(0);
                    }
                }

                    spGender.setSelection(Integer.parseInt(customerDetailContactDetailJson.get_SETGET().getGENDER()));


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvNotification = (TextView) findViewById(R.id.tvNotification);
        LinearLayout llBell = (LinearLayout) findViewById(R.id.llBell);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvNotification.setText("Chỉnh sửa liên hệ");

        llBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerDetail_ContactEditation_Activity.this, Notification_Activity.class);
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

}
