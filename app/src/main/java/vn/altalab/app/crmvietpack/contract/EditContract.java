package vn.altalab.app.crmvietpack.contract;

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

import es.dmoral.toasty.Toasty;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.contract.object.Owner;
import vn.altalab.app.crmvietpack.contract.object.Status;
import vn.altalab.app.crmvietpack.object.Product;
import vn.altalab.app.crmvietpack.orders_fragment.order_detail.ChoseCusActivity;
import vn.altalab.app.crmvietpack.orders_fragment.order_detail.ChoseProductActivity;
import vn.altalab.app.crmvietpack.orders_fragment.order_detail.Productlst;
import vn.altalab.app.crmvietpack.presenter.ProductAdapter;
import vn.altalab.app.crmvietpack.utility.MySingleton;
import vn.altalab.app.crmvietpack.utility.NumberTextWatcher;

public class EditContract extends AppCompatActivity {

    EditText ctedKH,ctednbd,ctednkt;
    EditText ctedName;
    TextView ctedOwner;
//    EditText ctedPaid;
    TextView ctedstatus;
    Toolbar toolbar;
    Spinner Spstatus , SpOwner;
    Button BtedChonKh, BtEDCT, BtedChonSp;

    long contract_id;
    private long customerId;
    private static String cusName;
    private ListView lstProduct;
    public static List<Status> dsStatus;
    public static ArrayAdapter statusAdapter;
    public static List<Owner> dsOwner;
    public static ArrayAdapter ownerAdapter;
    String choose_Status_ID = "1";
    String choose_Owner_ID="1";
   public static int size;
    int statusPosition;
   public static Integer a;
    private SharedPreferences settings;
    private static final String PREFS_NAME = "CRMVietPrefs";
    SimpleDateFormat simpleDateFormat;
    Calendar calendar;
    public static List<Product> products;
    ProductAdapter productAdapter;
    Productlst product1, product2, product3, product4;
    private ProgressDialog progressDialog;
        DetailContract detailContract;
    String contracOwnerName,paid, StatusName;

    static Date dateBd, dateKt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contract);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            getSupportActionBar().setSubtitle("Sửa hợp đồng");
        }
        if (settings == null) {
            settings = getSharedPreferences(PREFS_NAME, 0);
        }
        init();
        detailContract = new DetailContract();
        BtedChonKh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditContract.this, ChoseCusActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        // spinner Status
            dsStatus = new ArrayList<>();
            dsOwner = new ArrayList<>();

        dogetOwner();

        dogetStatus();

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            contract_id = bundle.getLong("contract_id");
            customerId = bundle.getLong("customer_id");
            ctedKH.setText(bundle.getString("customer_name"));
            ctedName.setText(bundle.getString("contract_name"));
            try {
                dateBd = simpleDateFormat.parse(bundle.getString("startDate"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ctednbd.setText(bundle.getString("startDate"));
            ctednkt.setText(bundle.getString("endDate"));

             StatusName = (bundle.getString("StatusName"));
            a= (bundle.getInt("CONTRACT_OWNER_ID")-1);
            contracOwnerName= bundle.getString("CONTRACT_OWNER_NAME");
            paid= bundle.getString("paid");
        }

        products = new ArrayList<>();

        // nhận dữ liệu list object từ màn hình chi tiết gửi;

        product1 = (Productlst) bundle.get("listproduct");
        if (product1 != null) {
            products = product1.getProducts();
        }


//        doGetProduct();
        productAdapter = new ProductAdapter(this, R.layout.item_product, products);

        lstProduct.setAdapter(productAdapter);

        lstProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//                builder.create();
//                List<String> listOptions = new ArrayList<>();
//                listOptions.add("Xóa sản phẩm");
//                listOptions.add("Sửa sản phẩm");
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
//                            Intent intent = new Intent(EditContract.this, EditProductActivity.class);
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
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditContract.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_custom, null);
                dialogBuilder.setView(dialogView);
                final AlertDialog Optiondialog = dialogBuilder.create();
                final AlertDialog show = dialogBuilder.show();
                TextView tvSp = (TextView) dialogView.findViewById(R.id.txtNamedialog);
                tvSp.setText(products.get(position).getProductName());
                final Button btXoa = (Button) dialogView.findViewById(R.id.DeleteSP);
                final EditText edGia = (EditText) dialogView.findViewById(R.id.et_Gia);

                //edGia.setText(products.get(position).getPrice());
                final EditText edSo = (EditText) dialogView.findViewById(R.id.et_Soluong);
                edSo.setText(String.valueOf(products.get(position).getQuantity()));
                edGia.addTextChangedListener(new NumberTextWatcher(edGia));

                edGia.setText(DecimalFormat.getInstance().
                        format(Double.parseDouble(products.get(position).getPrice())));
                int textLength = edGia.getText().length();
                edGia.setSelection(textLength, textLength);
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
                        //products.get(position).setPrice(edGia.getText().toString());
                        if(edGia.getText().toString().trim().equals("")){
                            products.get(position).setPrice("0");
                        }else {
                            products.get(position).setPrice(edGia.getText().toString().replace(".", ""));
                        }

                        if(edSo.getText().toString().trim().equals("")){
                            products.get(position).setQuantity(0);
                        }else {
                            products.get(position).setQuantity(Integer.parseInt(edSo.getText().toString()));
                        }
                        productAdapter.notifyDataSetChanged();
                        show.dismiss();
                    }
                });

            }
        });

        BtedChonSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditContract.this, ChoseProductActivity.class);
                startActivityForResult(intent, 2);
            }
        });



        BtEDCT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             doEditContract();


            }
        });
    }

    private void dogetOwner() {
        {
            String url = settings.getString("api_server", "") + "/api/v1/users";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                    url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {

                    if (jsonObject != null && !"".equals(jsonObject.toString())
                            && "success".equals(jsonObject.optString("messages"))) {
                        JSONArray array = jsonObject.optJSONArray("users");
                        for (int i = 0; i < array.length(); i++) {
                            final JSONObject object = array.optJSONObject(i);
                            boolean check = false;
                            int ownerId = object.optInt("USER_ID");
                            if (dsOwner.size() > 0) {
                                for (Owner cc : dsOwner) {
                                    if (cc != null && cc.getContractOwnerId() != null && !"".equals(cc.getContractOwnerName())) {
                                        if (cc.getContractOwnerId() == ownerId) {
                                            check = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (!check) {
                                Owner owner = new Owner();
                                owner.setContractOwnerId( object.optInt("USER_ID"));
                                owner.setContractOwnerName(object.optString("USER_NAME"));
                                dsOwner.add(owner);
                            }
                        }
                        getOwner();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
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
    }

    public void getOwner() {
        final String[] UsersName = new String[dsOwner.size()];
        final String[] UsersId = new String[dsOwner.size()];
        for (int i = 0; i < dsOwner.size(); i++) {
            UsersName[i] = dsOwner.get(i).getContractOwnerName();
        }

        for (int i = 0; i < dsOwner.size(); i++) {
            UsersId[i] = "" + dsOwner.get(i).getContractOwnerId();
        }


        ownerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, UsersName);
        ownerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpOwner.setAdapter(ownerAdapter);
        SpOwner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choose_Owner_ID = UsersId[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        for (int i = 0; i < dsOwner.size(); i++) {
            if (dsOwner.get(i).getContractOwnerName().equals(contracOwnerName)) {
                SpOwner.setSelection(i);
            }


        }
//        SpOwner.setSelection(a);

    }

    private void dogetStatus() {


        String url = settings.getString("api_server", "") + "/api/v1/contract/status";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (jsonObject != null && !"".equals(jsonObject.toString())
                        && "success".equals(jsonObject.optString("messages"))) {
                    JSONArray array = jsonObject.optJSONArray("contract_status");
                    for (int i = 0; i < array.length(); i++) {
                        final JSONObject object = array.optJSONObject(i);
                        boolean check = false;
                        int statusId = object.optInt("CONTRACT_STATUS_ID");
                        if (dsStatus.size() > 0) {
                            for (Status cc : dsStatus) {
                                if (cc != null && cc.getStatusId() != null && !"".equals(cc.getStatusName())) {
                                    if (cc.getStatusId() == statusId) {
                                        check = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (!check) {
                            Status status = new Status();
                            int a= object.optInt("CONTRACT_STATUS_ID");
                            status.setStatusId( object.optInt("CONTRACT_STATUS_ID"));
                            String b=object.optString("CONTRACT_STATUS_NAME");
                            status.setStatusName(object.optString("CONTRACT_STATUS_NAME"));
                            dsStatus.add(status);
                        }
                    }
                    getSpinner();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
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


    public void getSpinner(){
        final String[] UsersName = new String[dsStatus.size()];
        final String[] UsersId = new String[dsStatus.size()];
        for (int i = 0; i < dsStatus.size(); i++) {
            UsersName[i] = dsStatus.get(i).getStatusName();
        }

        for (int i = 0; i < dsStatus.size(); i++) {
            UsersId[i] = "" + dsStatus.get(i).getStatusId();
        }


        statusAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, UsersName);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spstatus.setAdapter(statusAdapter);
        Spstatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choose_Status_ID = UsersId[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        for (int i = 0; i < dsStatus.size(); i++) {
            if (dsStatus.get(i).getStatusName().equals(StatusName)) {
                Spstatus.setSelection(i);
            }


        }
    }


    private void doEditContract() {
        {
            progressDialog = new ProgressDialog(EditContract.this);
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Đang sửa...");
            progressDialog.show();
            String url = settings.getString("api_server", "") + "/api/v1/contracts/edit?USER_ID=" + settings.getString("userId", "")
                    + "&CONTRACT_ID=" + contract_id;

            Map<String, String> contract = new HashMap<>();
            contract.put("CONTRACT_NAME", ctedName.getText().toString());
            contract.put("START_DATE", ctednbd.getText().toString());
            contract.put("END_DATE", ctednkt.getText().toString());
            contract.put("CUSTOMER_ID", String.valueOf(customerId));
            contract.put("STATUS", choose_Status_ID);
            contract.put("CONTRACT_OWNER", choose_Owner_ID);
            contract.put("PAID",paid );
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
                product.put("PRODUCT_IMAGE",products.get(i).getPRODUCT_IMAGE());
                product.put("PRODUCT_UNIT",products.get(i).getProductUnit());
                product.put("LENGTH",String.valueOf(products.get(i).getLength()));
                product.put("WIDTH",String.valueOf(products.get(i).getWidth()));
                product.put("HEIGHT",String.valueOf(products.get(i).getHeight()));
                //ep du lieu 1 đối tượng prodct ve object
                JSONObject jsProduct = new JSONObject(product);
                //ep vè array object product
                arrayProduct.put(jsProduct);

            }
            // truyền hết kiểu dữ liệu của order và product
            Map<String, Object> jsonParams = new HashMap<>();

            jsonParams.put("contracts", contract);
            jsonParams.put("products", arrayProduct);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                setResult(1);
                                finish();

                                Toasty.success(EditContract.this, "Sửa hợp đồng thành công", Toast.LENGTH_SHORT).show();


                            } else if (jsonObject != null && !"".equals(jsonObject.toString()) && "fails".equals(jsonObject.optString("messages"))) {
                                String details = jsonObject.optString(getResources().getString(R.string.details));
                                if (details != null && !"".equals(details)) {
                                    if (progressDialog != null && progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                    Toasty.normal(EditContract.this, details, Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                Toasty.normal(EditContract.this, "Không sửa được hợp đồng !", Toast.LENGTH_SHORT).show();
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


    private void init() {
        lstProduct = (ListView) findViewById(R.id.lstProductCTPulled);

        ctedKH = (EditText) findViewById(R.id.ctedKH);
        ctedKH.setEnabled(false);
        ctedName = (EditText) findViewById(R.id.ctedName);
        ctednbd = (EditText) findViewById(R.id.ctedtNbd);
        ctednbd.setEnabled(false);
        ctednkt = (EditText) findViewById(R.id.ctedtNkt);
        ctednkt.setEnabled(false);
        BtedChonKh= (Button) findViewById(R.id.btedChonKH);
        BtedChonSp= (Button) findViewById(R.id.btedChonSp);
        BtEDCT= (Button) findViewById(R.id.BtEDCT);
        Spstatus= (Spinner) findViewById(R.id.spedContract_STATUS);
        SpOwner= (Spinner) findViewById(R.id.spedContract_Owner);

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        ImageButton NbdPicker = (ImageButton) findViewById(R.id.ctnbdPicker);
        ImageButton NktPickerContact = (ImageButton) findViewById(R.id.ctnktPicker);

        NbdPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        dateBd = calendar.getTime();
                        ctednbd.setText(simpleDateFormat.format(dateBd));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        NktPickerContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        dateKt = calendar.getTime();
                        if(dateKt.after(dateBd)){
                            ctednkt.setText(simpleDateFormat.format(dateKt));
                        }else {
                            Toasty.normal(getApplication(), "Ngày kết thúc phải sau ngày bắt đầu !", Toast.LENGTH_LONG).show();
                        }
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getLongExtra("customer_id", 0) != 0) {
            customerId = data.getLongExtra("customer_id", 0);
            cusName = data.getStringExtra("customer_name");
            ctedKH.setText(cusName);
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


            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
