package vn.altalab.app.crmvietpack.hanghoa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.object.Product;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class Detail_product extends AppCompatActivity {
    TextView tv_pdName, tv_pdPrice, tv_pdManufactory, tv_pdId, tv_pdDescription, tv_tax, tv_discount;
    Button btnSua, btnXoa;
    ImageView imageView;
    Toolbar toolbar;
    public static List<Product> products;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;
    private long productID;
    private ProgressDialog progressDialog;
    int position;
    private Double tax, discount;
    private static String productNsme, productDes, productMan, productPrice, productCode, productImv, link_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            getSupportActionBar().setSubtitle("Chi tiết Hàng Hóa");
        }
        if (settings == null) {
            settings = getSharedPreferences(PREFS_NAME, 0);
        }
        progressDialog = new ProgressDialog(Detail_product.this);
        tv_pdName = (TextView) findViewById(R.id.tv_pdName);
        tv_pdPrice = (TextView) findViewById(R.id.tv_pdPrice);
        tv_pdManufactory = (TextView) findViewById(R.id.tv_pdManufactory);
        tv_pdId = (TextView) findViewById(R.id.tv_pdID);
        tv_pdDescription = (TextView) findViewById(R.id.tv_pdDescription);
        tv_tax = (TextView) findViewById(R.id.tv_tax);
        tv_discount = (TextView) findViewById(R.id.tv_discount);
        imageView = (ImageView) findViewById(R.id.imv);
        btnSua = (Button) findViewById(R.id.btnSuaSP);
//        btnXoa= (Button) findViewById(R.id.btnXoaSP);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            productID = bundle.getLong("PRODUCT_ID");
            productCode = bundle.getString("PRODUCT_CODE");
            productNsme = bundle.getString("PRODUCT_NAME");
            productDes = bundle.getString("PRODUCT_DESCRIPTION");
            productMan = bundle.getString("PRODUCT_MANUFACTORY");
            productPrice = bundle.getString("PRODUCT_PRICE");
            tax = bundle.getDouble("TAX");
            productImv = bundle.getString("PRODUCT_IMAGE");
            discount = bundle.getDouble("DISCOUNT");
            link_image = bundle.getString("LINK_IMAGE");


        }
        if (!link_image.equals("")) {
            Picasso.with(this)
                    .load(link_image)
                    // chỉnh lại dung lượng của file ảnh cho giảm xuống.
                    .resize(512, 512)
                    .placeholder(R.drawable.product_box)
                    .error(R.drawable.product_box)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.product_box);
        }


        tv_pdId.setText(productCode);

        tv_pdName.setSingleLine();
        tv_pdName.setSelected(true);
        tv_pdName.setText(productNsme);
        if (!productDes.equals("null")) {
            tv_pdDescription.setText(Html.fromHtml(productDes));
        }
        tv_pdPrice.setText(DecimalFormat.getInstance().format(Double.parseDouble(productPrice)));

        tv_pdManufactory.setSingleLine();
        tv_pdManufactory.setSelected(true);
        if (!productMan.equals("null")) {
            tv_pdManufactory.setText(Html.fromHtml(productMan));
        }


        tv_tax.setText("" + tax + " %");
        tv_discount.setText("" + discount + " %");

        products = new ArrayList<>();


        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Detail_product.this, Edit_ProductActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("PRODUCT_ID", productID);
                bundle.putString("PRODUCT_NAME", productNsme);
                bundle.putString("PRODUCT_MANUFACTORY", productMan);
                bundle.putString("PRODUCT_PRICE", productPrice);
                bundle.putString("PRODUCT_DESCRIPTION", productDes);
                bundle.putDouble("TAX", tax);
                bundle.putString("PRODUCT_CODE", productCode);
                bundle.putDouble("DISCOUNT", discount);
                bundle.putString("LINK_IMAGE", link_image);
                intent.putExtras(bundle);
                startActivityForResult(intent, 2);
            }
        });
//
//
//
//        btnXoa.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Detail_product.this);
//                alertBuilder.create();
//                alertBuilder.setMessage("Bạn có chắc sẽ xóa hàng hóa?");
//                alertBuilder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        doRemoveProduct();
//                        if (dialog != null) {
//                            dialog.dismiss();
//                        }
//                    }
//                }).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (dialog != null) {
//                            dialog.dismiss();
//                        }
//                    }
//                });
//                alertBuilder.show();
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            DetailProdcut();
        }
    }


    public void DetailProdcut() {
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        String url = settings.getString("api_server", "") + "/api/v1/products/" + productID;

        //JsonRequest(Method Post or Get -> Params -> listener )/ Error/ Header
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (jsonObject != null && !"".equals(jsonObject.toString())
                        && "success".equals(jsonObject.optString("messages"))) {
                    JSONArray array = jsonObject.optJSONArray("productsList");
                    for (int i = 0; i < array.length(); i++) {
                        final JSONObject object = array.optJSONObject(i);
                        productCode = object.optString("PRODUCT_CODE");
                        productNsme = object.optString("PRODUCT_NAME");
                        productDes = object.optString("PRODUCT_DESCRIPTION");
                        productPrice = object.optString("PRODUCT_PRICE");
                        productMan = object.optString("PRODUCT_MANUFACTORY");
                        tax = object.optDouble("TAX");
                        discount = object.optDouble("DISCOUNT");
                        link_image = object.optString("LINK_IMAGE");

                        tv_pdId.setText(productCode);

                        tv_pdName.setSingleLine();
                        tv_pdName.setSelected(true);
                        tv_pdName.setText(productNsme);
                        if (!productDes.equals("null")) {
                            tv_pdDescription.setText(Html.fromHtml(productDes));
                        }
                        tv_pdPrice.setText(DecimalFormat.getInstance().format(Double.parseDouble(productPrice)));

                        tv_pdManufactory.setSingleLine();
                        tv_pdManufactory.setSelected(true);
                        if (!productMan.equals("null")) {
                            tv_pdManufactory.setText(Html.fromHtml(productMan));
                        }
                        tv_tax.setText("" + tax + " %");
                        tv_discount.setText("" + discount + " %");

                        if (!link_image.equals("")) {
                            Picasso.with(Detail_product.this)
                                    .load(link_image)
                                    // chỉnh lại dung lượng của file ảnh cho giảm xuống.
                                    .resize(512, 512)
                                    .placeholder(R.drawable.product_box)
                                    .error(R.drawable.product_box)
                                    .into(imageView);
                        } else {
                            imageView.setImageResource(R.drawable.product_box);
                        }

                    }
                }

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Log.e("getGood", volleyError.toString());
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

//    private void doRemoveProduct() {
//
//        progressDialog.setIndeterminate(false);
//        progressDialog.setCancelable(true);
//        progressDialog.setMessage("Đang xóa...");
//        progressDialog.show();
//        String url = settings.getString("api_server", "") + "/api/v1/products/remove/" + productID;
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject jsonObject) {
//                if ("success".equals(jsonObject.optString("messages"))) {
//                    Toast.makeText(Detail_product.this, "Xóa thành công !", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent();
//                    intent.putExtra("position", position);
//                    setResult(1, intent);
//                    if (progressDialog != null && progressDialog.isShowing()) {
//                        progressDialog.dismiss();
//                    }
//                    finish();
//                } else if ("fails".equals(jsonObject.optString("messages"))) {
//                    Toast.makeText(Detail_product.this, jsonObject.optString("details"), Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent();
//                    intent.putExtra("position", position);
//                    setResult(1, intent);
//                    if (progressDialog != null && progressDialog.isShowing()) {
//                        progressDialog.dismiss();
//                    }
//                    finish();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                if (progressDialog != null && progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/json; charset=UTF-8");
//                headers.put("User-agent", System.getProperty("http.agent"));
//                return headers;
//            }
//        };
//        int socketTimeout = 60000;
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        request.setRetryPolicy(policy);
//        MySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(request);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(0);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            setResult(0);

            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
