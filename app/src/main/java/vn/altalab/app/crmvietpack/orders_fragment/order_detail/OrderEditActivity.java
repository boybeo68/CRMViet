package vn.altalab.app.crmvietpack.orders_fragment.order_detail;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.object.Product;
import vn.altalab.app.crmvietpack.object.Users;
import vn.altalab.app.crmvietpack.presenter.ProductAdapter;
import vn.altalab.app.crmvietpack.utility.MySingleton;
import vn.altalab.app.crmvietpack.utility.NumberTextWatcher;

public class OrderEditActivity extends AppCompatActivity {
    Spinner spOrder_USER;

    EditText edKhName;
    EditText edndh;
    EditText edngh;
    EditText sdtlH;
    EditText NlH;
    EditText Dcgh;
    EditText Des;
    Toolbar toolbar;
    Spinner Spstatus;
    Button BtKh, SuaDH, BtSp;
    long order_id;
    private long customerId;
    private static String cusName;
    private ListView lstProduct;
    static String choose_Order_USER = "1";
    static String order_user_name, order_user_fake;
    int statusPosition;
    private SharedPreferences settings;
    private static final String PREFS_NAME = "CRMVietPrefs";
    SimpleDateFormat simpleDateFormat;
    Calendar calendar;
    public static List<Product> products;
    ProductAdapter productAdapter;
    Productlst product1, product2, product3, product4;
    private ProgressDialog progressDialog;
    static int Status;
    static Date dateNd, dateNg;
    List<Users> userses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_edit);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            getSupportActionBar().setSubtitle("Sửa đơn hàng");
        }
        if (settings == null) {
            settings = getSharedPreferences(PREFS_NAME, 0);
        }
        init();


        BtKh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderEditActivity.this, ChoseCusActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            order_id = bundle.getLong("order_id");
            customerId = bundle.getLong("customer_id");
            edKhName.setText(bundle.getString("customer_name"));
            try {
                dateNd = simpleDateFormat.parse(bundle.getString("ndh"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            edndh.setText(bundle.getString("ndh"));
            edngh.setText(bundle.getString("ngh"));
            sdtlH.setText(bundle.getString("sdtlh"));
            NlH.setText(bundle.getString("nlh"));
            Dcgh.setText(bundle.getString("dcgh"));
            Des.setText(bundle.getString("description"));
            order_user_fake = bundle.getString("order_user_name");
            if (order_user_fake == "null" || order_user_fake == "" || order_user_fake == null) {
                order_user_name = "-- chưa chọn --";
            } else {
                order_user_name = order_user_fake;
            }
            Status = bundle.getInt("status");


        }

        //chọn spiner status
        String status[] = {"Chưa xác nhận", "Đã xác nhận", "Đã giao", "Đã hủy", "Đã trả về"};
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, status);
        Spstatus.setAdapter(adapterStatus);
        statusPosition = 1;
        Spstatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusPosition = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Spstatus.setSelection(Status - 1);

        products = new ArrayList<>();

        // nhận dữ liệu list object từ màn hình chi tiết gửi;

        product1 = (Productlst) bundle.get("listproduct");
        if (product1 != null) {
            products = product1.getProducts();
        }


        productAdapter = new ProductAdapter(this, R.layout.item_product, products);

        lstProduct.setAdapter(productAdapter);

        lstProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OrderEditActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_custom, null);
                dialogBuilder.setView(dialogView);
                final AlertDialog Optiondialog = dialogBuilder.create();
                final AlertDialog show = dialogBuilder.show();
                TextView tvSp = (TextView) dialogView.findViewById(R.id.txtNamedialog);
                tvSp.setText(products.get(position).getProductName());
                final Button btXoa = (Button) dialogView.findViewById(R.id.DeleteSP);

                final EditText edGia = (EditText) dialogView.findViewById(R.id.et_Gia);

                edGia.addTextChangedListener(new NumberTextWatcher(edGia));

                edGia.setText(DecimalFormat.getInstance().
                        format(Double.parseDouble(products.get(position).getPrice())));
                edGia.setSelection(edGia.getText().length());


                final EditText edSo = (EditText) dialogView.findViewById(R.id.et_Soluong);

                edSo.setText(String.valueOf(products.get(position).getQuantity()));

                btXoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        products.remove(position);
                        productAdapter.notifyDataSetChanged();
                        show.dismiss();
                    }
                });
                Button btSua = (Button) dialogView.findViewById(R.id.EditSP);
                btSua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edGia.getText().toString().trim().equals("")) {
                            products.get(position).setPrice("0");
                        } else {
                            products.get(position).setPrice(edGia.getText().toString().replace(".", ""));
                        }
                        if (edSo.getText().toString().trim().equals("")) {
                            products.get(position).setQuantity(0);
                        } else {
                            products.get(position).setQuantity(Integer.parseInt(edSo.getText().toString().trim()));
                        }
                        productAdapter.notifyDataSetChanged();
                        show.dismiss();
                    }
                });


//                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//                builder.create();
//                builder.setTitle("Sản phẩm : "+products.get(position).getProductName());
//                List<String> listOptions = new ArrayList<>();
//                listOptions.add("Xóa");
//                listOptions.add("Sửa");
//                ListAdapter listAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, listOptions);
//                builder.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (which == 0) {
//
//                            products.remove(position);
//                            productAdapter.notifyDataSetChanged();
//                        }
//
//                        if (which == 1) {
//
//                            Intent intent = new Intent(OrderEditActivity.this, EditProductActivity.class);
//                            Productlst product1 = new Productlst();
//                            product1.setProducts(products);
//                            intent.putExtra("listproduct", product1);
//
//                            Bundle bundle = new Bundle();
//                            bundle.putInt("position", position);
//                            intent.putExtras(bundle);
//
//                            startActivityForResult(intent, 4);
//                        }
//
//                    }
//                });
//                builder.show();
            }
        });

        BtSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderEditActivity.this, ChoseProductActivity.class);
                startActivityForResult(intent, 2);
            }
        });


        SuaDH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (products.size() == 0) {
                    Toast.makeText(getApplication(), "Chưa chọn hàng hóa !", Toast.LENGTH_LONG).show();
                } else {
                    doEditOrder();
                }

            }
        });


        doGetUsers();
    }


    public void init() {
        userses = new ArrayList<>();
        lstProduct = (ListView) findViewById(R.id.lstProductPull);
        spOrder_USER = (Spinner) findViewById(R.id.spnNguoiGh);

        edKhName = (EditText) findViewById(R.id.edtKH_ED);
        edKhName.setEnabled(false);
        edndh = (EditText) findViewById(R.id.edtNdh_ED);
        edndh.setEnabled(false);
        edngh = (EditText) findViewById(R.id.edtNgh_ED);
        edngh.setEnabled(false);
        NlH = (EditText) findViewById(R.id.edtnglh_ED);
        sdtlH = (EditText) findViewById(R.id.edtSdt_ED);
        Dcgh = (EditText) findViewById(R.id.edtAdress_ED);
        BtKh = (Button) findViewById(R.id.btKh_ED);
        BtSp = (Button) findViewById(R.id.btsp_ED);
        Spstatus = (Spinner) findViewById(R.id.statusOrder);
        SuaDH = (Button) findViewById(R.id.BtEdit);
        Des = (EditText) findViewById(R.id.edtDescription);


        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        ImageButton NdhPicker = (ImageButton) findViewById(R.id.ndhPicker_ED);
        ImageButton NghPickerContact = (ImageButton) findViewById(R.id.nghPicker_ED);

        NdhPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        dateNd = calendar.getTime();
                        edndh.setText(simpleDateFormat.format(dateNd));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        NghPickerContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        dateNg = calendar.getTime();
                        if (dateNg.after(dateNd)) {
                            edngh.setText(simpleDateFormat.format(dateNg));
                        } else {
                            Toast.makeText(getApplication(), "Ngày giao hàng phải sau ngày đặt hàng !", Toast.LENGTH_LONG).show();

                        }

                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
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
        spOrder_USER.setAdapter(adapterTRANSACTION_USER);
        spOrder_USER.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choose_Order_USER = UsersId[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        for (int i = 0; i < userses.size(); i++) {
            if (userses.get(i).getUserName().equals(order_user_name)) {
                spOrder_USER.setSelection(i);
            }

        }
    }

    // getUser lấy ra danh sách user
    public void doGetUsers() {

        String url = settings.getString("api_server", "") + "/api/v1/users";

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getLongExtra("customer_id", 0) != 0) {
            customerId = data.getLongExtra("customer_id", 0);
            cusName = data.getStringExtra("customer_name");
            edKhName.setText(cusName);
        }

        // nhận lại dữ liệu object từ màn hình chọn sản phẩm

        if (requestCode == 2) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                product2 = (Productlst) bundle.get("listproductChose");
                if (product2 != null) {
                    for (int i = 0; i < product2.getProducts().size(); i++) {
                        products.add(product2.getProducts().get(i));
                    }
                    productAdapter.notifyDataSetChanged();
                }
            }
        }


        //Nhận dữ liệu object từ màn hình Sửa sản phẩm
        if (requestCode == 4) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                product4 = (Productlst) bundle.get("listproductEdit");
                if (product4 != null) {
                    products.clear();

                    productAdapter.notifyDataSetChanged();
                    for (int i = 0; i < product4.getProducts().size(); i++) {
                        products.add(product4.getProducts().get(i));
                    }

                    productAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                setResult(7, intent);
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
            setResult(7, intent);

            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void doEditOrder() {
        progressDialog = new ProgressDialog(OrderEditActivity.this);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Đang sửa...");
        progressDialog.show();
        String url = settings.getString("api_server", "") + "/api/v1/order/edit?USER_ID=" + settings.getString("userId", "")
                + "&ORDER_ID=" + order_id;

        Map<String, String> order = new HashMap<>();
        order.put("QUALITY", NlH.getText().toString());
        order.put("PERIOD", sdtlH.getText().toString());
        order.put("ADDRESS_DELIVERY", Dcgh.getText().toString());
        order.put("ORDER_DATE", edndh.getText().toString());
        order.put("DATE_DELIVERY", edngh.getText().toString());
        order.put("CUSTOMER_ID", String.valueOf(customerId));
        order.put("STATUS", String.valueOf(statusPosition));
        order.put("DESCRIPTION", Des.getText().toString());
        order.put("ORDER_USER", choose_Order_USER);

        JSONArray arrayProduct = new JSONArray();

        for (int i = 0; i < products.size(); i++) {

            Map<String, String> product = new HashMap<>();
            product.put("PRODUCT_ID", String.valueOf(products.get(i).getProductId()));
            product.put("DISCOUNT", String.valueOf(products.get(i).getDiscount()));
            product.put("TAX", String.valueOf(products.get(i).getTax()));
            product.put("QUANTITY", String.valueOf(products.get(i).getQuantity()));
            product.put("PRICE", products.get(i).getPrice());
            product.put("PRODUCT_CODE", products.get(i).getProductCode());
            product.put("PRODUCT_NAME", products.get(i).getProductName());
            product.put("PRODUCT_DESCRIPTION", products.get(i).getDescription());
            product.put("PRODUCT_MANUFACTORY", products.get(i).getProductManufactory());
            product.put("PRODUCT_IMAGE", products.get(i).getPRODUCT_IMAGE());

            //ep du lieu 1 đối tượng prodct ve object
            JSONObject jsProduct = new JSONObject(product);
            //ep vè array object product
            arrayProduct.put(jsProduct);

        }
        // truyền hết kiểu dữ liệu của order và product
        Map<String, Object> jsonParams = new HashMap<>();

        jsonParams.put("order", order);
        jsonParams.put("products", arrayProduct);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Intent intent = new Intent();
                            setResult(7, intent);
                            finish();
                            Toast.makeText(OrderEditActivity.this, "Success", Toast.LENGTH_SHORT).show();


                        } else if (jsonObject != null && !"".equals(jsonObject.toString()) && "fails".equals(jsonObject.optString("messages"))) {
                            String details = jsonObject.optString(getResources().getString(R.string.details));
                            if (details != null && !"".equals(details)) {
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(OrderEditActivity.this, details, Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(OrderEditActivity.this, "Không thêm mới được đơn hàng !", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("createcustomer", volleyError.toString());
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
        MySingleton.getInstance(this).addToRequestQueue(request);


    }


}
