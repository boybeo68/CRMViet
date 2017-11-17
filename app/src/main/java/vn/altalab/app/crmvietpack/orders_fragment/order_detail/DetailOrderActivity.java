package vn.altalab.app.crmvietpack.orders_fragment.order_detail;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.object.Order;
import vn.altalab.app.crmvietpack.object.Product;
import vn.altalab.app.crmvietpack.presenter.ProductAdapter;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class DetailOrderActivity extends AppCompatActivity {
    TextView txtdhCode;
    TextView txtndh;
    TextView txtngh;
    TextView txtCusName;
    TextView sdtlH;
    TextView monney;
    TextView monneyAll;
    TextView NlH;
    TextView Dcgh;
    TextView NguoiGh;
    TextView labelGhichu;
    TextView Ghichu;
    Button btXoa, btSua;
    ImageView imageView;
    ListView ProductList;
    public static List<Product> products;
    ProductAdapter productAdapter;
    private long orderId = 0;
    private static String cusName, ndh, ngh, nglh, adress, phone, order_code, moneyall, nguoiGh, description;
    long customer_Id, order_user;
    int position;
    int Status;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;
    Toolbar toolbar;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            getSupportActionBar().setSubtitle("Chi tiết đơn hàng");
        }
        if (settings == null) {
            settings = getSharedPreferences(PREFS_NAME, 0);
        }
        ProductList = (ListView) findViewById(R.id.listProduct);
        NguoiGh = (TextView) findViewById(R.id.tv_NguoigiaoH);
        txtdhCode = (TextView) findViewById(R.id.tv_dhCode);
        txtndh = (TextView) findViewById(R.id.tv_datH);
        txtngh = (TextView) findViewById(R.id.tv_giaoH);
        txtCusName = (TextView) findViewById(R.id.tv_tkH);
        monney = (TextView) findViewById(R.id.tv_monney);
        monneyAll = (TextView) findViewById(R.id.monneyAll);
        NlH = (TextView) findViewById(R.id.tv_tlH);
        sdtlH = (TextView) findViewById(R.id.tv_sdtlh);
        Dcgh = (TextView) findViewById(R.id.tv_dcgH);
        imageView = (ImageView) findViewById(R.id.img);
        labelGhichu = (TextView) findViewById(R.id.ghichuLabel);
        Ghichu = (TextView) findViewById(R.id.ghichu);
        Ghichu.setSelected(true);
        btXoa = (Button) findViewById(R.id.btXoaOder);
        btSua = (Button) findViewById(R.id.btSuaOder);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderId = bundle.getLong("order_id");
            order_code = bundle.getString("order_code");
            cusName = bundle.getString("customer_name");
            ndh = bundle.getString("ndh");
            ngh = bundle.getString("ngh");
            customer_Id = bundle.getLong("customer_id");
            order_user = bundle.getLong("order_user");
            Status = bundle.getInt("status");
            adress = bundle.getString("adress");
            phone = bundle.getString("phone");
            nglh = bundle.getString("nglh");
            moneyall = bundle.getString("money");
            if(bundle.getString("order_user_name").equals("null")){
                nguoiGh = "";
            }else {
                nguoiGh = bundle.getString("order_user_name");
            }

            description = bundle.getString("description");
            position = bundle.getInt("position");


        }
        labelGhichu.setText("Ghi chú: ");
        if (description.equals("null")) {
            Ghichu.setText("");
        } else {
            Ghichu.setText(description);
        }

        txtCusName.setText(cusName);
        txtndh.setText(ndh);
        txtngh.setText(ngh);
        txtdhCode.setText(order_code);
        NlH.setText(nglh);
        sdtlH.setText(phone);
        if (adress.equals("null")) {
            Dcgh.setText("");
        } else {
            Dcgh.setText(adress);
        }

        NguoiGh.setText(nguoiGh);
        monney.setText(DecimalFormat.getInstance().format(Double.parseDouble(moneyall)));
        monneyAll.setText(DecimalFormat.getInstance().format(Double.parseDouble(moneyall)));
        if (Status == 1) {
            imageView.setBackgroundResource(R.drawable.ic_chuaduyetweb);

        }
        if (Status == 2) {
            imageView.setBackgroundResource(R.drawable.ic_daduyetweb);

        }
        if (Status == 3) {
            imageView.setBackgroundResource(R.drawable.ic_dagiaoweb);

        }
        if (Status == 4) {
            imageView.setBackgroundResource(R.drawable.ic_dahuyweb);

        }
        if (Status == 5) {
            imageView.setBackgroundResource(R.drawable.back_order_bt);

        }
        products = new ArrayList<>();

        doGetDetailOrder();
        productAdapter = new ProductAdapter(DetailOrderActivity.this, R.layout.item_product, products);
        ProductList.setAdapter(productAdapter);


// Xóa
        btXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(DetailOrderActivity.this);
                alertBuilder.create();
                alertBuilder.setMessage("Bạn có muốn xóa đơn hàng này không ?");
                alertBuilder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doRemoveOrder();
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                }).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
                alertBuilder.show();
            }
        });

        btSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailOrderActivity.this, OrderEditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("order_id", orderId);
                bundle.putString("customer_name", cusName);
                bundle.putLong("customer_id", customer_Id);
                bundle.putString("nlh", NlH.getText().toString());
                bundle.putString("sdtlh", sdtlH.getText().toString());
                bundle.putString("dcgh", Dcgh.getText().toString());
                bundle.putString("ndh", ndh);
                bundle.putString("ngh", ngh);
                bundle.putInt("status", Status);
                bundle.putString("order_user_name", nguoiGh);
                bundle.putString("description", description);
                intent.putExtras(bundle);

                //xử lý chuyển list object giữa 2 màn hình :
                Productlst product1 = new Productlst();
                product1.setProducts(products);
                intent.putExtra("listproduct", product1);

                startActivityForResult(intent, 7);
//                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7) {
            products.clear();
            productAdapter.notifyDataSetChanged();

            doGetDetailOrder();
        }
    }

    // Xóa
    public void doRemoveOrder() {
        progressDialog = new ProgressDialog(DetailOrderActivity.this);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Đang xóa...");
        progressDialog.show();
        String url = settings.getString("api_server", "") + "/api/v1/order/remove/" + orderId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && "success".equals(jsonObject.optString("messages"))) {
                    Toast.makeText(DetailOrderActivity.this, "Xóa thành công !", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("position", position);
                    setResult(1, intent);
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    finish();
                } else if (jsonObject != null && "fails".equals(jsonObject.optString("messages"))) {
                    Toast.makeText(DetailOrderActivity.this, jsonObject.optString("details"), Toast.LENGTH_SHORT).show();
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
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
        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        MySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(request);
    }

    public void doGetDetailOrder() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String url = settings.getString("api_server", "") + "/api/v1/orders/" + orderId;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {


                    JSONObject object = jsonObject.optJSONObject("orders");

                    Order order = new Order();
                    Status = order.setStatus(object.optInt("STATUS"));
                    cusName = object.optString("CUSTOMER_NAME");
                    customer_Id = object.optLong("CUSTOMER_ID");
                    order_code = object.optString("ORDER_CODE");
                    description = object.optString("DESCRIPTION");
                    nglh = object.optString("QUALITY");
                    if (object.optString("ORDER_USER_NAME").equals("null")) {
                        nguoiGh = "";
                    } else {
                        nguoiGh = object.optString("ORDER_USER_NAME");
                    }


                    String a = object.optString("ORDER_DATE");
                    if (a.equals(null) || a.equals("null") || a.equals("") || a.equals("0000-00-00 00:00:00")) {
                        order.setDayOder("");
                    } else {
                        String b[] = a.split("-");
                        String b2[] = b[2].split(" ");
                        String b3[] = b2[1].split(":");
                        ndh = b2[0] + "/" + b[1] + "/" + b[0] + " " + b3[0] + ":" + b3[1];
                    }

                    String a1 = object.optString("DATE_DELIVERY");
                    if (a1.equals(null) || a1.equals("null") || a1.equals("") || a1.equals("0000-00-00 00:00:00")) {
                        order.setDeliveryDate("");
                    } else {
                        String b[] = a1.split("-");
                        String b2[] = b[2].split(" ");
                        String b3[] = b2[1].split(":");
                        ngh = b2[0] + "/" + b[1] + "/" + b[0] + " " + b3[0] + ":" + b3[1];
                    }


                    phone = object.optString("PERIOD");
                    adress = object.optString("ADDRESS_DELIVERY");
                    moneyall = object.optString("TOTAL_AMOUNT");


                    JSONArray array = object.optJSONArray("products");

                    for (int i = 0; i < array.length(); i++) {
                        final JSONObject object1 = array.optJSONObject(i);
                        Product product = new Product();
                        product.setProductName(object1.optString("PRODUCT_ORDER_NAME"));
                        product.setProductManufactory(object1.optString("PRODUCT_MANUFACTORY"));
                        product.setDescription(object1.optString("PRODUCT_DESCRIPTION"));
                        product.setPrice(object1.optString("PRICE"));
                        product.setQuantity(object1.optInt("QUANTITY"));
                        product.setProductId(object1.optInt("PRODUCT_ID"));
                        product.setProductCode(object1.optString("PRODUCT_ORDER_CODE"));
                        product.setDiscount(object1.optDouble("DISCOUNT"));
                        product.setTax(object1.optDouble("TAX"));
                        product.setPRODUCT_IMAGE(object1.optString("PRODUCT_IMAGE"));
                        product.setUrl_image(object1.optString("LINK_IMAGE"));


                        products.add(product);
                    }
                    labelGhichu.setText("Ghi chú: ");
                    if (description.equals("null")) {
                        Ghichu.setText("");
                    } else {
                        Ghichu.setText(description);
                    }
                    txtCusName.setText(cusName);
                    txtndh.setText(ndh);
                    txtngh.setText(ngh);
                    txtdhCode.setText(order_code);
                    NlH.setText(nglh);
                    sdtlH.setText(phone);
                    if (adress.equals("null")) {
                        Dcgh.setText("");
                    } else {
                        Dcgh.setText(adress);
                    }

                    NguoiGh.setText(nguoiGh);
                    monney.setText(DecimalFormat.getInstance().format(Double.parseDouble(moneyall)));
                    monneyAll.setText(DecimalFormat.getInstance().format(Double.parseDouble(moneyall)));
                    if (Status == 1) {
                        imageView.setBackgroundResource(R.drawable.ic_chuaduyetweb);

                    }
                    if (Status == 2) {
                        imageView.setBackgroundResource(R.drawable.ic_daduyetweb);

                    }
                    if (Status == 3) {
                        imageView.setBackgroundResource(R.drawable.ic_dagiaoweb);

                    }
                    if (Status == 4) {
                        imageView.setBackgroundResource(R.drawable.ic_dahuyweb);

                    }
                    if (Status == 5) {
                        imageView.setBackgroundResource(R.drawable.back_order_bt);

                    }

                }

                productAdapter.notifyDataSetChanged();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                setResult(1, intent);
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

            Intent intent = new Intent();
            setResult(1, intent);

            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

