package vn.altalab.app.crmvietpack.orders_fragment.order_detail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.object.Product;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class ChoseProductActivity extends AppCompatActivity implements ChoseProductAdapter.customButtonListener {
    private PullToRefreshListView lstProduct;
    public static List<Product> products;
    public static ChoseProductAdapter choseProductAdapter;

    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;
    private ProgressDialog progressDialog;
    EditText searchPro;
    Button btSearchPro;
    Product product = new Product();
    ArrayList<Product> list;
    Button btXong;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_product);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            getSupportActionBar().setSubtitle("Chọn sản phẩm");
        }
        btXong = (Button) findViewById(R.id.btXong);
        if (settings == null) {
            settings = getSharedPreferences(PREFS_NAME, 0);
        }

        searchPro = (EditText) findViewById(R.id.edSearchPro);
        btSearchPro = (Button) findViewById(R.id.btSearchPro);


        lstProduct = (PullToRefreshListView) findViewById(R.id.lstProduct);
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading..");
        products = new ArrayList<>();

        doGetProductAPI(0);
        choseProductAdapter = new ChoseProductAdapter(this, R.layout.item_list_chose_product, products);
        choseProductAdapter.setCustomButtonListner(ChoseProductActivity.this);

        lstProduct.setAdapter(choseProductAdapter);
        lstProduct.setMode(PullToRefreshBase.Mode.BOTH);

        lstProduct.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if ("".equals(settings.getString("api_server", ""))) {
                    Toast.makeText(refreshView.getContext(), refreshView.getContext().getResources().getString(R.string.login_empty_api_server), Toast.LENGTH_SHORT).show();
                    return;
                }

                products.clear();
                choseProductAdapter.notifyDataSetChanged();
                doGetProductAPI(0);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if ("".equals(settings.getString("api_server", ""))) {
                    Toast.makeText(refreshView.getContext(), refreshView.getContext().getResources().getString(R.string.login_empty_api_server), Toast.LENGTH_SHORT).show();
                    return;
                }

                doGetProductAPI(products.size());
                Log.e("pro", "" + products.size());

            }
        });

        list = new ArrayList<>();
        btXong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < products.size(); i++) {
                    product = products.get(i);
                    if (product.isSelected()) {
                        product.setProductId(products.get(i).getProductId());
                        product.setProductName(products.get(i).getProductName());
                        product.setProductCode(products.get(i).getProductCode());
                        product.setDiscount(products.get(i).getDiscount());
                        product.setPrice(products.get(i).getPrice());
                        product.setProductManufactory(products.get(i).getProductManufactory());
                        product.setDescription(products.get(i).getDescription());
                        product.setQuantity(products.get(i).getQuantity());
                        product.setTax(products.get(i).getTax());
                        product.setProductUnit(products.get(i).getProductUnit());
                        product.setLength(products.get(i).getLength());
                        product.setHeight(products.get(i).getHeight());
                        product.setWidth(products.get(i).getWidth());
                        product.setPRODUCT_IMAGE(products.get(i).getPRODUCT_IMAGE());
                        product.setUrl_image(products.get(i).getUrl_image());


//                        if (product.getQuantity() == 0) {
//                            responseText.append("- " + product.getProductName() + " (1 sản phẩm)" + "\n");
//                        } else {
//                            responseText.append("- " + product.getProductName() + " (" + product.getQuantity() + " sản phẩm)" + "\n");
//                        }
                        list.add(product);

                    }
                }

                Intent intent = new Intent();
                Productlst product1 = new Productlst();
                product1.setProducts(list);
                intent.putExtra("listproductChose", product1);

                setResult(2, intent);
                finish();
            }
        });


        btSearchPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doGetProductSearch(0);
            }
        });
        searchPro.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchPro.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    doGetProductSearch(0);
                    //tắt bàn phím sau khi click
                    final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


    }

    public static String KeyAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }

    //Search
    public void doGetProductSearch(final int index) {

        if (index == 0) {
            products.clear();
            choseProductAdapter.notifyDataSetChanged();
        }
        progressDialog.show();
        String KeyWord = searchPro.getText().toString();
        if (KeyWord.contains(" ")) {
            KeyWord = KeyWord.replaceAll(" ", "%20");
        }
        String url = settings.getString("api_server", "") + "/api/v1/products/search?USER_ID=" + settings.getString(getResources().getString(R.string.user_id_object), "")
                + "&keyword=" + KeyAccent(KeyWord) + "&index=" + index;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {

                    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    JSONArray array = jsonObject.optJSONArray("productsList");
                    for (int i = 0; i < array.length(); i++) {
                        final JSONObject object = array.optJSONObject(i);

                        Product product = null;

                        product = new Product();
                        product.setDiscount(object.optDouble("DISCOUNT"));
                        product.setTax(object.optDouble("TAX"));
                        product.setPrice(object.optString("PRODUCT_PRICE"));
                        product.setProductId(object.optInt("PRODUCT_ID"));
                        product.setProductCode(object.optString("PRODUCT_CODE"));
                        product.setProductName(object.optString("PRODUCT_NAME"));
                        product.setDescription(object.optString("PRODUCT_DESCRIPTION"));
                        product.setProductManufactory(object.optString("PRODUCT_MANUFACTORY"));
                        product.setProductUnit(object.optString("PRODUCT_UNIT"));
                        product.setLength(object.optDouble("LENGTH"));
                        product.setWidth(object.optDouble("WIDTH"));
                        product.setHeight(object.optDouble("HEIGHT"));
                        product.setINVENTORY(object.optString("INVENTORY"));


                        product.setPRODUCT_IMAGE(object.optString("PRODUCT_IMAGE"));
                        product.setUrl_image(object.optString("LINK_IMAGE"));


                        products.add(0, product);

                    }

                    choseProductAdapter.notifyDataSetChanged();

                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }


                } else {

                    Toast.makeText(getApplicationContext(), "Không tìm thấy sản phẩm này !", Toast.LENGTH_LONG).show();
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
                if (lstProduct.isRefreshing()) {
                    lstProduct.onRefreshComplete();
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


    public void doGetProductAPI(final int index) {


        String url = settings.getString("api_server", "") + "/api/v1/products?USER_ID="
                + settings.getString(getResources().getString(R.string.user_id_object), "") + "&index=" + index;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {

                    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    JSONArray array = jsonObject.optJSONArray("productsList");
                    for (int i = 0; i < array.length(); i++) {
                        final JSONObject object = array.optJSONObject(i);


                        Product product = null;

                        product = new Product();
                        product.setDiscount(object.optDouble("DISCOUNT"));
                        product.setTax(object.optDouble("TAX"));
                        product.setPrice(object.optString("PRODUCT_PRICE"));
                        product.setProductId(object.optInt("PRODUCT_ID"));
                        product.setProductCode(object.optString("PRODUCT_CODE"));
                        product.setProductName(object.optString("PRODUCT_NAME"));
                        product.setDescription(object.optString("PRODUCT_DESCRIPTION"));
                        product.setProductManufactory(object.optString("PRODUCT_MANUFACTORY"));
                        product.setProductUnit(object.optString("PRODUCT_UNIT"));
                        product.setLength(object.optDouble("LENGTH"));
                        product.setWidth(object.optDouble("WIDTH"));
                        product.setHeight(object.optDouble("HEIGHT"));
                        product.setINVENTORY(object.optString("INVENTORY"));
                        product.setPRODUCT_IMAGE(object.optString("PRODUCT_IMAGE"));
                        product.setUrl_image(object.optString("LINK_IMAGE"));
                        products.add(product);

                    }

                    choseProductAdapter.notifyDataSetChanged();
                }

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (lstProduct.isRefreshing()) {
                    lstProduct.onRefreshComplete();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (lstProduct.isRefreshing()) {
                    lstProduct.onRefreshComplete();
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

    //Search


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                setResult(2, intent);
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
            setResult(2, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onButtonClickListner(int position, String value) {
        Dialog(position);
    }

    public void Dialog(final int position) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog, null);
        dialogBuilder.setView(dialogView);
        final EditText edtSoluong = (EditText) dialogView.findViewById(R.id.etComments);
        dialogBuilder.setTitle("Nhập số lượng :");
        dialogBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (edtSoluong.getText().toString().equals("")) {
                    products.get(position).setQuantity(0);
                } else {
//                    Toast.makeText(ChoseProductActivity.this, edtSoluong.getText().toString(), Toast.LENGTH_LONG).show();
                    products.get(position).setQuantity(Integer.parseInt(edtSoluong.getText().toString()));
                }

                product.setQuantity(products.get(position).getQuantity());
                choseProductAdapter.notifyDataSetChanged();
            }
        })
                .create();
        dialogBuilder.show();

    }
}