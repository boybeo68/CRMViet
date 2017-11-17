package vn.altalab.app.crmvietpack.hanghoa;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import vn.altalab.app.crmvietpack.Link;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.Shared_Preferences;
import vn.altalab.app.crmvietpack.model.UsersModel;
import vn.altalab.app.crmvietpack.notification.Notification_Activity;
import vn.altalab.app.crmvietpack.utility.MySingleton;
import vn.altalab.app.crmvietpack.utility.NumberTextWatcher;

public class Them_moi_hanghoa extends AppCompatActivity implements View.OnClickListener {
    private EditText edtProductName, edtManufactory, edtDescription, edtPrice, edtProductCode, edtUnit, edtTax, edtDiscount;
    private Button btnThem;
    private UsersModel usersModel = new UsersModel();
    private Realm realm;
    Shared_Preferences sharedPreferences;
    private SimpleDateFormat simpleDateFormat, simpleDateFormat2;
    Calendar calendar;

    String PREFS_NAME = "CRMVietPrefs";
    int statusPosition = 1;
    int transactionpoin;
    int priority = 1;
    StringBuilder userAssign;
    boolean[] selectedUser;
    String userId;
    long customerId;
    Integer[] priorities = {1, 2, 3, 4, 5};
    int year, month, day;
    int hour, minute;
    Boolean isLoading = false;
    ProgressDialog pDialog;
    Link link;

    private Button buttonChoose, outChoose;
    private ImageView imageView;


    private Bitmap bitmap;

    private int PICK_IMAGE_REQUEST = 1;

    private String KEY_IMAGE = "image";
    static int clear = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_moi_hanghoa);
        Action();
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        outChoose = (Button) findViewById(R.id.outChoose);


        imageView = (ImageView) findViewById(R.id.imageView);

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
                imageView.setVisibility(View.GONE);
                clear = 0;
                outChoose.setVisibility(View.GONE);

            }
        });

    }

    private void Action() {
        getToolbar();

        // Khai báo Id
        getfindViewById();

        // Khai báo SharedPreferences
        if (sharedPreferences == null) {
            sharedPreferences = new Shared_Preferences(this, PREFS_NAME);
        }

        btnThem.setOnClickListener(this);

    }


    private void getfindViewById() {
        edtProductName = (EditText) findViewById(R.id.edtProductName);
        edtManufactory = (EditText) findViewById(R.id.edtManufactory);
        edtDescription = (EditText) findViewById(R.id.edtDescription);
        edtPrice = (EditText) findViewById(R.id.edtPrice);
        edtPrice.addTextChangedListener(new NumberTextWatcher(  edtPrice));
        edtProductCode = (EditText) findViewById(R.id.edtProductCode);
        edtUnit = (EditText) findViewById(R.id.edtUnit);
        edtTax = (EditText) findViewById(R.id.edtTax);
        edtDiscount = (EditText) findViewById(R.id.edtDiscount);
        btnThem = (Button) findViewById(R.id.btnThem);

    }

    private void getToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvNotification = (TextView) findViewById(R.id.tvNotification);
        LinearLayout llBell = (LinearLayout) findViewById(R.id.llBell);

        // getSupport
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // findViewById
        tvNotification.setText("Thêm Sản phẩm");
        llBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Them_moi_hanghoa.this, Notification_Activity.class);
                intent.putExtra("type_object", 1);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnThem:

                if ("".equals(edtProductName.getText().toString().trim())) {
                    Toasty.normal(Them_moi_hanghoa.this, "Tên hàng hóa không được bỏ trống", Toast.LENGTH_SHORT).show();
                } else if ("".equals(edtProductCode.getText().toString().trim())) {
                    Toasty.normal(Them_moi_hanghoa.this, "Mã sản phẩm không được bỏ trống", Toast.LENGTH_SHORT).show();

                } else if ("".equals(edtPrice.getText().toString().trim())) {
                    Toasty.normal(Them_moi_hanghoa.this, "Giá hàng hóa không được bỏ trống", Toast.LENGTH_SHORT).show();

                } else if ("".equals(edtManufactory.getText().toString().trim())) {
                    Toasty.normal(Them_moi_hanghoa.this, "Nhà sản xuất không được bỏ trống", Toast.LENGTH_SHORT).show();

                } else if ("".equals(edtDescription.getText().toString().trim())) {
                    Toasty.normal(Them_moi_hanghoa.this, "Mô tả trống", Toast.LENGTH_SHORT).show();
                } else {
                    doGoodsAdd();
                }

                break;

        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            clear = 1;
            imageView.setVisibility(View.VISIBLE);
            outChoose.setVisibility(View.VISIBLE);
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap = Bitmap.createScaledBitmap(bitmap, 512, 512, false);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void doGoodsAdd() {
        Link link = new Link(this);
        String url = link.post_PRODUCT_CREATE_Link();
        //Showing the progress dialog
        Map<String, String> params = new Hashtable<String, String>();

        //Adding parameters
        if (bitmap != null & clear != 0) {
            String image = getStringImage(bitmap);
            params.put(KEY_IMAGE, image);
        }
        params.put("PRODUCT_NAME", edtProductName.getText().toString());
        params.put("PRODUCT_MANUFACTORY", edtManufactory.getText().toString());
        params.put("PRODUCT_DESCRIPTION", edtDescription.getText().toString());
        params.put("PRODUCT_PRICE", edtPrice.getText().toString().replace(".",""));
        params.put("PRODUCT_CODE", edtProductCode.getText().toString());
        params.put("PRODUCT_UNIT", edtUnit.getText().toString());
        params.put("TAX", edtTax.getText().toString());
        params.put("DISCOUNT", edtDiscount.getText().toString());


        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                //Disimissing the progress dialog

                //Showing toast message of the response
                try {
                    if (jsonObject.getString("messages").equals("success")) {
                        loading.dismiss();
                        setResult(0);
                        finish();
                        Toasty.success(Them_moi_hanghoa.this, "Thêm mới thành công !", Toast.LENGTH_LONG).show();

                    } else {
                        if (jsonObject.getString("details") != null && !jsonObject.getString("details").equals("null"))
                            loading.dismiss();
                        Toasty.normal(getApplication(), "Lỗi: " + jsonObject.getString("details"), Toast.LENGTH_SHORT).show();

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

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
