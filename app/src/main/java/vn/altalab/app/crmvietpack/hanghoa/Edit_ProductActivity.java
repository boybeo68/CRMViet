package vn.altalab.app.crmvietpack.hanghoa;

import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
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
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.object.Product;
import vn.altalab.app.crmvietpack.utility.MySingleton;
import vn.altalab.app.crmvietpack.utility.NumberTextWatcher;

public class Edit_ProductActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText txtNameED, txtManufactoryED, txtCodeED, txtPriceED, txtDesED, txtTax, txtDiscount;
    private String productNsme, productDes, productMan, productPrice, productCode, link_image;
    private Double Tax, Discount;
    private long productID;
    Button btnDoneED;
    ImageView imv;
    public static List<Product> products;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;
    private ProgressDialog progressDialog;
    private Button buttonChoose, outChoose;


    private Bitmap bitmap;

    private int PICK_IMAGE_REQUEST = 1;

    private String KEY_IMAGE = "image";
    static int clear = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__product);
        Action();
        toolbar = (Toolbar) findViewById(R.id.toolbarr);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            getSupportActionBar().setSubtitle("Sửa hàng hóa ");
        }
        if (settings == null) {
            settings = getSharedPreferences(PREFS_NAME, 0);
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            productID = bundle.getLong("PRODUCT_ID");
            productCode = bundle.getString("PRODUCT_CODE");
            productNsme = bundle.getString("PRODUCT_NAME");
            productDes = bundle.getString("PRODUCT_DESCRIPTION");
            productMan = bundle.getString("PRODUCT_MANUFACTORY");
            productPrice = bundle.getString("PRODUCT_PRICE");
            Tax = bundle.getDouble("TAX");
            Discount = bundle.getDouble("DISCOUNT");
            link_image = bundle.getString("LINK_IMAGE");

        }
        txtNameED.setText(productNsme);
        txtManufactoryED.setText(productMan);
        txtCodeED.setText(productCode);
        txtDesED.setText(productDes);
        txtPriceED.setText(DecimalFormat.getInstance().format(Double.parseDouble(productPrice)));
        txtPriceED.addTextChangedListener(new NumberTextWatcher( txtPriceED));
        txtTax.setText("" + Tax);
        txtDiscount.setText("" + Discount);
        if(!link_image.equals("")){
            Picasso.with(this)
                    .load(link_image)
                    // chỉnh lại dung lượng của file ảnh cho giảm xuống.
                    .resize(512, 512)
                    .placeholder(R.drawable.product_box)
                    .error(R.drawable.product_box)
                    .into(imv);
        }else {
            imv.setImageResource(R.drawable.product_box);
        }

        btnDoneED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtManufactoryED.getText().toString().trim().equals("")) {
                    Toasty.normal(getApplicationContext(), "Lỗi. Chưa nhập nhà sản xuất", Toast.LENGTH_LONG).show();
                } else if (txtDesED.getText().toString().trim().equals("")) {
                    Toasty.normal(getApplicationContext(), "Lỗi. Chưa nhập mô tả", Toast.LENGTH_LONG).show();
                } else if (txtPriceED.getText().toString().trim().equals("")) {
                    Toasty.normal(getApplicationContext(), "Lỗi. Chưa nhập giá sản phẩm", Toast.LENGTH_LONG).show();
                } else if (txtCodeED.getText().toString().trim().equals("")) {
                    Toasty.normal(getApplicationContext(), "Lỗi. Chưa nhập mã sản phẩm", Toast.LENGTH_LONG).show();
                } else if (txtNameED.getText().toString().trim().equals("")) {
                    Toasty.normal(getApplicationContext(), "Lỗi. Chưa nhập tên sản phẩm", Toast.LENGTH_LONG).show();
                } else {
                    doEditProducts();
                }

            }
        });


        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        outChoose = (Button) findViewById(R.id.outChoose);
        outChoose.setVisibility(View.GONE);

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
                    Picasso.with(Edit_ProductActivity.this)
                            .load(link_image)
                            // chỉnh lại dung lượng của file ảnh cho giảm xuống.
                            .resize(512, 512)
                            .placeholder(R.drawable.product_box)
                            .error(R.drawable.product_box)
                            .into(imv);
                    clear = 0;
                    outChoose.setVisibility(View.GONE);
                } else {
                    imv.setImageResource(R.drawable.product_box);
                    clear = 0;
                    outChoose.setVisibility(View.GONE);
                }

            }
        });

    }

    public Map<String, String> post_PRODUCT_EDIT_hashmap() {

        Map<String, String> jsonParams = new HashMap<>();
        if (bitmap != null & clear != 0) {
            String image = getStringImage(bitmap);
            jsonParams.put(KEY_IMAGE, image);
        }
        jsonParams.put("PRODUCT_NAME", txtNameED.getText().toString());
        jsonParams.put("PRODUCT_MANUFACTORY", txtManufactoryED.getText().toString());
        jsonParams.put("PRODUCT_DESCRIPTION", txtDesED.getText().toString());
        jsonParams.put("PRODUCT_PRICE", txtPriceED.getText().toString().replace(".",""));
        jsonParams.put("PRODUCT_CODE", txtCodeED.getText().toString());
        jsonParams.put("TAX", txtTax.getText().toString());
        jsonParams.put("DISCOUNT", txtDiscount.getText().toString());
        return jsonParams;

    }

    private void doEditProducts() {
        progressDialog = new ProgressDialog(Edit_ProductActivity.this);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Đang sửa...");
        progressDialog.show();
        String url = settings.getString("api_server", "") + "/api/v1/products/edit?USER_ID=" + settings.getString("userId", "")
                + "&PRODUCT_ID=" + productID;
        Map<String, String> jsonParams = post_PRODUCT_EDIT_hashmap();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("CONTACTEDITTION", "Response: " + jsonObject);

                try {
                    if (jsonObject.getString("messages").equals("success")) {
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();

                        setResult(2);
                        finish();

                    } else {
                        if (jsonObject.getString("details") != null && !jsonObject.getString("details").equals("null"))
                            Toast.makeText(Edit_ProductActivity.this, "Lỗi: " + jsonObject.getString("details"), Toast.LENGTH_SHORT).show();
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("CONTACTEDITION", "Error: " + volleyError.toString());
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
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

    private void Action() {
        getfinViewById();
    }

    private void getfinViewById() {
        txtNameED = (EditText) findViewById(R.id.txtSP_NameED);
        txtCodeED = (EditText) findViewById(R.id.txtSp_codeED);
        txtDesED = (EditText) findViewById(R.id.txtSP_DesED);
        txtManufactoryED = (EditText) findViewById(R.id.txtSP_NsxED);
        txtPriceED = (EditText) findViewById(R.id.txtSP_PriceED);
        txtTax = (EditText) findViewById(R.id.txtSP_TaxED);
        txtDiscount = (EditText) findViewById(R.id.txtSP_DiscountED);
        imv = (ImageView) findViewById(R.id.imv);
        btnDoneED = (Button) findViewById(R.id.btDoneED);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
