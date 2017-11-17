package vn.altalab.app.crmvietpack.customer.edit_create_customer;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import vn.altalab.app.crmvietpack.Link;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.customer.Customer_MainFragment;
import vn.altalab.app.crmvietpack.notification.Notification_Activity;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class Customer_Edit_Infomation_Activity extends AppCompatActivity {

    private static final String PREFS_NAME = "CRMVietPrefs";
    String userId;
    private SharedPreferences sharedPreferences;
    private EditText etName, etAddress, etTelephone, etBirthday, etEmail, etTaxCode, etDescription;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private ImageButton imBirthdayPicker;
    Customer_MainFragment customer_mainFragment = new Customer_MainFragment();
    private long customerId = 0;
    private static String CUSTOMER_EMAIL, CUSTOMER_PHONE;
    private static int CUSTOMER_OWNER;

    Button btnEditCustomer;
    String a = null;
    Toolbar toolbar;

    ProgressDialog pDialog;

    Link link;
    private Button buttonChoose, outChoose;

    ImageView imv;
    static String link_image;

    private int PICK_IMAGE_REQUEST = 1;

    private Bitmap bitmap;


    private String KEY_IMAGE = "image";
    static int clear = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_customer_edit);

        findViewById();
        Action();
    }

    public void findViewById() {
        imv = (ImageView) findViewById(R.id.imv);

        // Initiate Edit text
        etName = (EditText) findViewById(R.id.edtEditName);
        etAddress = (EditText) findViewById(R.id.edtEditAddress);
        etTelephone = (EditText) findViewById(R.id.edtEditTelephone);
        etBirthday = (EditText) findViewById(R.id.edtEditBirthday);

        // Initiate
        etEmail = (EditText) findViewById(R.id.edtEditEmail);
        etTaxCode = (EditText) findViewById(R.id.edtEditTaxCode);
        etDescription = (EditText) findViewById(R.id.edtEditDescription);
        imBirthdayPicker = (ImageButton) findViewById(R.id.btnEditBirthdayPicker);
        btnEditCustomer = (Button) findViewById(R.id.btnEditCustomer);

        etBirthday.setEnabled(false);
    }

    public void Action() {

        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }

        // cai dat sharedPreferences
        userId = sharedPreferences.getString(getResources().getString(R.string.user_id_object), "");
        link = new Link(this);

        // Nhận và cài đặt ToolBar
        getToolbar();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            customerId = bundle.getLong("customer_id");
        }

        bindingData();

        imBirthdayPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Initiate calendar
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

        btnEditCustomer.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if (etName == null || "".equals(etName.getText().toString().trim())) {
                    Toast.makeText(getApplication(), "Tên khách hàng không được để trống!", Toast.LENGTH_SHORT).show();
                } else if (etTelephone == null || "".equals(etTelephone.getText().toString().trim())) {
                    Toast.makeText(getApplication(), "Số điện thoại khách hàng không được để trống!", Toast.LENGTH_SHORT).show();
                } else {
                    doEditCustomer();
                }
            }
        });


        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        outChoose = (Button) findViewById(R.id.outChoose);

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
                if (!link_image.equals("")) {
                    Picasso.with(Customer_Edit_Infomation_Activity.this)
                            .load(link_image)
                            // chỉnh lại dung lượng của file ảnh cho giảm xuống.
                            .resize(512, 512)
                            .placeholder(R.drawable.ic_person)
                            .error(R.drawable.ic_person)
                            .into(imv);
                    clear = 0;
                    outChoose.setVisibility(View.GONE);
                }else {
                    imv.setImageResource(R.drawable.ic_person);
                    clear = 0;
                    outChoose.setVisibility(View.GONE);
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            outChoose.setVisibility(View.VISIBLE);
            clear = 1;
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap = Bitmap.createScaledBitmap(bitmap, 512, 512, false);
                //Setting the Bitmap to ImageView
                imv.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void showFileChooser() {

        imv.setVisibility(View.VISIBLE);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void bindingData() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Xin chờ ...");
        pDialog.setCancelable(true);
        pDialog.show();

        String url = link.get_CUSTOMERDETAIL_Infomation_Link(customerId);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (pDialog != null && pDialog.isShowing())
                    pDialog.dismiss();
                try {
                    Log.e("CUSTOMEREDIT", "Messages: " + (jsonObject.getString(getResources().getString(R.string.messages))));
                    setEdittext(jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("CUSTOMEREDIT", "Error: " + volleyError.getMessage());
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

        // Set timeout request to API
        request.setShouldCache(false);
        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void setEdittext(JSONObject jsonObject) {

        try {

            if (jsonObject.getString("messages").equals("success")) {
                JSONObject object = jsonObject.getJSONObject("customer");

                if (object.getString("CUSTOMER_NAME") != null && !object.getString("CUSTOMER_NAME").equals("null"))
                    etName.setText(object.getString(getResources().getString(R.string.customer_name_db)));

                if (object.getString("OFFICE_ADDRESS") != null && !object.getString("OFFICE_ADDRESS").equals("null"))
                    etAddress.setText(object.getString("OFFICE_ADDRESS"));

                CUSTOMER_OWNER = object.getInt("CUSTOMER_OWNER");

                CUSTOMER_PHONE = object.getString("TELEPHONE");

                if (customer_mainFragment.phone_view != 0 || customer_mainFragment.user_id == CUSTOMER_OWNER ||
                        customer_mainFragment.user_id == 1) {
                    if (CUSTOMER_PHONE != null && !CUSTOMER_PHONE.equals("null"))
                        etTelephone.setText(CUSTOMER_PHONE);
                } else {
                    etTelephone.setEnabled(false);
                    etTelephone.setText("*********");
                }
                if (object.getString("TAX_CODE") != null && !object.getString("TAX_CODE").equals("null"))
                    etTaxCode.setText(object.getString("TAX_CODE"));

//                Log.e("CustomerEdit", "CustomerEdit: " + object.getString("CUSTOMER_FOUNDING"));
                if (object.getString("CUSTOMER_FOUNDING") != null && !object.getString("CUSTOMER_FOUNDING").equals("null")) {
                    a = object.getString("CUSTOMER_FOUNDING");
                    if (a == null || a.equals("null") || a.equals("")) {
                        etBirthday.setText("");
                    } else {
                        String b[] = a.split("-");
                        String b2[] = b[2].split(" ");
                        etBirthday.setText(b2[0] + "/" + b[1] + "/" + b[0]);
                    }
                }

                CUSTOMER_EMAIL = object.getString("CUSTOMER_EMAIL");

                if (customer_mainFragment.email_view != 0 || customer_mainFragment.user_id == CUSTOMER_OWNER ||
                        customer_mainFragment.user_id == 1) {
                    if (CUSTOMER_EMAIL != null && !CUSTOMER_EMAIL.equals("null"))
                        etEmail.setText(CUSTOMER_EMAIL);
                } else {
                    etEmail.setEnabled(false);
                    etEmail.setText("*********");
                }

                if (object.getString("CUSTOMER_DESCRIPTION") != null && !object.getString("CUSTOMER_DESCRIPTION").equals("null"))
                    etDescription.setText(object.getString(getResources().getString(R.string.customer_description_db)));

                link_image = jsonObject.getString("link_image");
                if (!link_image.equals("")) {
                    Picasso.with(Customer_Edit_Infomation_Activity.this)
                            .load(link_image)
                            // chỉnh lại dung lượng của file ảnh cho giảm xuống.
                            .resize(300, 300)
                            .placeholder(R.drawable.ic_person)
                            .error(R.drawable.ic_person)
                            .into(imv);
                } else {
                    imv.setImageResource(R.drawable.ic_person);
                }


            } else {
                Toasty.normal(Customer_Edit_Infomation_Activity.this, jsonObject.getString("details"), Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public Map<String, Object> GET_EDITCUSTOMER_MAP() {
        Map<String, String> jscusED = new HashMap<>();

        jscusED.put("CUSTOMER_ID", String.valueOf(customerId));
        jscusED.put("CUSTOMER_NAME", etName.getText().toString().trim());
        jscusED.put("OFFICE_ADDRESS", etAddress.getText().toString().trim());
        if (customer_mainFragment.phone_view != 0 || customer_mainFragment.user_id == CUSTOMER_OWNER ||
                customer_mainFragment.user_id == 1) {
            jscusED.put("TELEPHONE", etTelephone.getText().toString().trim());

        } else {
            jscusED.put("TELEPHONE", CUSTOMER_PHONE);
        }
        jscusED.put("CUSTOMER_FOUNDING", etBirthday.getText().toString());
        jscusED.put("CUSTOMER_DESCRIPTION", etDescription.getText().toString().trim());
        jscusED.put("TAX_CODE", etTaxCode.getText().toString().trim());
        if (customer_mainFragment.email_view != 0 || customer_mainFragment.user_id == CUSTOMER_OWNER ||
                customer_mainFragment.user_id == 1) {
            jscusED.put("CUSTOMER_EMAIL", etEmail.getText().toString().trim());

        } else {
            jscusED.put("CUSTOMER_EMAIL", CUSTOMER_EMAIL);
        }

        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("USER_ID", userId);
        jsonParams.put("customer", jscusED);
        if (bitmap != null & clear != 0) {
            String image = getStringImage(bitmap);
            jsonParams.put(KEY_IMAGE, image);
            Log.e("image", image);
        }
        return jsonParams;
    }

    public void doEditCustomer() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Xin chờ ...");
        pDialog.setCancelable(true);
        pDialog.show();

        String url = link.post_CUSTOMERDETAIL_InfomationEditation_Link();
        Map<String, Object> jsonParams = GET_EDITCUSTOMER_MAP();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (pDialog != null)
                            if (pDialog.isShowing())
                                pDialog.dismiss();

                        // Return
                        try {
                            if (jsonObject.getString("messages").equals("success")) {
                                Toasty.success(getApplication(), "Sửa khách hàng thành công !", Toast.LENGTH_LONG).show();
                                setResult(0);
                                finish();
                            } else {
                                if (jsonObject.getString("details") != null && !jsonObject.getString("details").equals("null"))
                                    Toasty.normal(Customer_Edit_Infomation_Activity.this, jsonObject.getString("details"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("CustomerEdit", "Error: " + volleyError.toString());
                if (pDialog != null)
                    if (pDialog.isShowing())
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

        TextView tvNotification = (TextView) findViewById(R.id.tvNotification);
        LinearLayout llBell = (LinearLayout) findViewById(R.id.llBell);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvNotification.setText("Sửa khách hàng");

        llBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Customer_Edit_Infomation_Activity.this, Notification_Activity.class);
                intent.putExtra("type_object", 1);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (pDialog != null)
            if (pDialog.isShowing())
                pDialog.dismiss();
        setResult(0);
        finish();
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
