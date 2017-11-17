package vn.altalab.app.crmvietpack.contract;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
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
import vn.altalab.app.crmvietpack.object.Product;
import vn.altalab.app.crmvietpack.orders_fragment.order_detail.ChoseCusActivity;
import vn.altalab.app.crmvietpack.orders_fragment.order_detail.ChoseProductActivity;
import vn.altalab.app.crmvietpack.orders_fragment.order_detail.ChoseProductAdapter;
import vn.altalab.app.crmvietpack.orders_fragment.order_detail.Productlst;
import vn.altalab.app.crmvietpack.presenter.ProductAdapter;
import vn.altalab.app.crmvietpack.transaction.FilePath;
import vn.altalab.app.crmvietpack.utility.MySingleton;
import vn.altalab.app.crmvietpack.utility.NumberTextWatcher;

public class AddContract extends AppCompatActivity {
    EditText ctKH, edtNbd, ctName, edtNkt, ctPaid, ctPrice, ctDebt;
    TextView ctOwner;
    Spinner spContract_USER;
    Button BtChonKh, BtChonSp, BtAddCT;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;
    SimpleDateFormat simpleDateFormat;
    Calendar calendar;
    Toolbar toolbar;
    String choose_Contract_USER = "1";
    private static long customerId;
    private static String cusName;
    private static String customer_name;
    TextView tv;
    public static Fragment fragment = null;
    public static List<Owner> dsOwner;
    ListView lstProductAdd;
    public static List<Product> products;
    public static ChoseProductAdapter choseProductAdapter;
    private ProgressDialog progressDialog;

    Product product = new Product();
    ProductAdapter productAdapter;
    Productlst productlst, product4;

    private Button buttonChoose, outChoose, btchooseKh;

    private EditText editTextName;
    LinearLayout lnFileName;
    TextView tvFileName;
    private int PICK_FILE_REQUEST = 2;
    private String selectedFilePath = "";
    private String fileName = "";
    String encodeFile = "";
    static boolean run = true;
    static int clear = 0;
    static String flag;

    static Date dateBd, dateKt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contract);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        if (settings == null) {
            settings = getSharedPreferences(PREFS_NAME, 0);
        }

        dsOwner = new ArrayList<>();
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        outChoose = (Button) findViewById(R.id.outChoose);


        tvFileName = (TextView) findViewById(R.id.tv_file_name);
        lnFileName = (LinearLayout) findViewById(R.id.ln_file_name);

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
                lnFileName.setVisibility(View.GONE);
                clear = 0;
                run = true;
                outChoose.setVisibility(View.GONE);
                selectedFilePath = "";
            }
        });


        lstProductAdd = (ListView) findViewById(R.id.lstProductCTPulladd);

        tv = (TextView) findViewById(R.id.soluong);
        ctKH = (EditText) findViewById(R.id.ctKH);
        ctKH.setEnabled(false);
        ctKH.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(ctKH.getWindowToken(), 0);
                }
            }
        });
        ctName = (EditText) findViewById(R.id.ctName);
        ctOwner = (TextView) findViewById(R.id.ctowner);
        ctPaid = (EditText) findViewById(R.id.ctPaid);

        ctPaid.addTextChangedListener(new NumberTextWatcher(ctPaid));


        BtChonKh = (Button) findViewById(R.id.btChonKH);
        BtChonSp = (Button) findViewById(R.id.btChonSp);
        BtAddCT = (Button) findViewById(R.id.BtAddCT);
        edtNbd = (EditText) findViewById(R.id.edtNbd);
        edtNbd.setEnabled(false);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        dateBd = new Date();
        edtNbd.setText(sdf.format(dateBd));
        edtNkt = (EditText) findViewById(R.id.edtNkt);
        edtNkt.setEnabled(false);

        Bundle bundle2 = getIntent().getExtras();

        if (bundle2 != null) {
            flag = bundle2.getString("contract");
            if (flag != "text") {
                customerId = bundle2.getLong("customer_id");
                customer_name = bundle2.getString("customer_name");
            }

        }
        if (customerId != 0) {
            ctKH.setText(customer_name);
        }

        spContract_USER = (Spinner) findViewById(R.id.spContract_USER);

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        ImageButton NbdPicker = (ImageButton) findViewById(R.id.nbdPicker);
        ImageButton NktPickerContact = (ImageButton) findViewById(R.id.nktPicker);

        // spinner lấy ra danh sách user
        dogetOwner();

        NbdPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        dateBd = calendar.getTime();
                        edtNbd.setText(simpleDateFormat.format(dateBd));
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
                        if (dateKt.after(dateBd)) {
                            edtNkt.setText(simpleDateFormat.format(dateKt));
                        } else {
                            Toast.makeText(getApplication(), "Ngày kết thúc phải sau ngày bắt đầu !", Toast.LENGTH_LONG).show();
                        }

                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        BtAddCT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ctKH == null || "".equals(ctKH.getText().toString())) {
                    Toast.makeText(getApplication(), "Chưa chọn Khách hàng ", Toast.LENGTH_SHORT).show();
                } else {

                    if (edtNbd == null || "".equals(edtNbd.getText().toString())) {
//                            edtNbd.setError("Vui lòng chọn ngày bắt đầu ");
                        Toast.makeText(getApplication(), "Chưa chọn ngày bắt đầu ", Toast.LENGTH_SHORT).show();
                    } else {
//                            doADDContract();
                        if (edtNkt == null || "".equals(edtNbd.getText().toString())) {
                            edtNkt.setText("");
                        } else {
                            if (!selectedFilePath.equals("")) {
                                File selectedFile = new File(selectedFilePath);
                                encodeFile = encodeFileToBase64Binary(selectedFile);
                            }
                            if (run == true) {
                                doADDContract();
                            }

                        }
                    }


                }

            }
        });

        BtChonKh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddContract.this, ChoseCusActivity.class);
                startActivityForResult(intent, 1);


            }
        });

        BtChonSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddContract.this, ChoseProductActivity.class);
                startActivityForResult(intent, 2);
            }
        });
        products = new ArrayList<>();
        productAdapter = new ProductAdapter(this, R.layout.item_product, products);
        lstProductAdd.setAdapter(productAdapter);
        lstProductAdd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddContract.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_custom, null);
                dialogBuilder.setView(dialogView);

                final AlertDialog Optiondialog = dialogBuilder.create();
                final AlertDialog show = dialogBuilder.show();
                final EditText edSoluong = (EditText) dialogView.findViewById(R.id.et_Soluong);
                final EditText edGia = (EditText) dialogView.findViewById(R.id.et_Gia);

                Button delete = (Button) dialogView.findViewById(R.id.DeleteSP);
                Button editSP = (Button) dialogView.findViewById(R.id.EditSP);
                TextView NameDialog = (TextView) dialogView.findViewById(R.id.txtNamedialog);
                NameDialog.setText(products.get(position).getProductName());
                edSoluong.setText(String.valueOf(products.get(position).getQuantity()));
                edGia.addTextChangedListener(new NumberTextWatcher(edGia));

                edGia.setText(DecimalFormat.getInstance().
                        format(Double.parseDouble(products.get(position).getPrice())));

                int textLength = edGia.getText().length();
                edGia.setSelection(textLength, textLength);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        products.remove(position);
                        productAdapter.notifyDataSetChanged();
                        show.dismiss();
                    }
                });
                editSP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(edGia.getText().toString().trim().equals("")){
                            products.get(position).setPrice("0");
                        }else {
                            products.get(position).setPrice(edGia.getText().toString().replace(".", ""));
                        }
                        if (edSoluong.getText().toString().trim().equals("")) {
                            products.get(position).setQuantity(0);
                        } else {
                            products.get(position).setQuantity(Integer.parseInt(edSoluong.getText().toString()));
                        }
                        productAdapter.notifyDataSetChanged();
                        show.dismiss();
                    }
                });
            }
        });


    }

    // lấy ra danh sách user
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
                                owner.setContractOwnerId(object.optInt("USER_ID"));
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


        ArrayAdapter ownerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, UsersName);
        ownerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spContract_USER.setAdapter(ownerAdapter);
        spContract_USER.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choose_Contract_USER = UsersId[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        for (int i = 0; i < dsOwner.size(); i++) {
            if (dsOwner.get(i).getContractOwnerName().equals(settings.getString("userName", ""))) {
                spContract_USER.setSelection(i);
            }


        }

    }

    private String encodeFileToBase64Binary(File file) {
        String encodedfile = "";
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);


            if (file.length() > 13631488) {
//                Toast.makeText(getApplicationContext(), "File quá lớn, không thể upload !", Toast.LENGTH_LONG).show();
                Dialog();
                run = false;
            } else {
                byte[] bytes = new byte[(int) file.length()];
                fileInputStreamReader.read(bytes);
                encodedfile = Base64.encodeToString(bytes, Base64.DEFAULT);
            }

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }

        return encodedfile;
    }

    public void Dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("File quá lớn !");
        builder.setMessage("Upload file dung lượng dưới 12 MB");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }

        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data == null) {
                    //no data present
                    return;
                }

                lnFileName.setVisibility(View.VISIBLE);
                outChoose.setVisibility(View.VISIBLE);
                clear = 1;
                run = true;
                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(this, selectedFileUri);


                if (selectedFilePath != null && !selectedFilePath.equals("")) {
                    String a[] = selectedFilePath.split("/");
                    fileName = a[a.length - 1];
                    tvFileName.setText(selectedFilePath);
                } else {
                    Toasty.normal(this, "Không thể upload file lên server", Toast.LENGTH_SHORT).show();
                    selectedFilePath = "";
                }
            }
        }

        if (requestCode == 1) {
            if (data.getLongExtra("customer_id", 0) != 0) {
                customerId = data.getLongExtra("customer_id", 0);
                cusName = data.getStringExtra("customer_name");
                ctKH.setText(cusName);
            }
        }


        // nhận lại dữ liệu object từ màn hình chọn sản phẩm

        if (requestCode == 2) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    productlst = (Productlst) bundle.get("listproductChose");
                    if (productlst != null) {
                        for (int i = 0; i < productlst.getProducts().size(); i++) {
                            products.add(productlst.getProducts().get(i));
                        }
                        productAdapter.notifyDataSetChanged();
                    }
                }
            }

        }

        if (requestCode == 4) {
            if (data != null) {
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


    }


    private void doADDContract() {

        progressDialog = new ProgressDialog(AddContract.this);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Đang thêm mới...");
        progressDialog.show();
        String url = settings.getString("api_server", "") + "/api/v1/contracts/add?USER_ID=" + settings.getString("userId", "");


        Map<String, String> contract = new HashMap<>();
        if (clear != 0) {
            contract.put("uploaded_file", encodeFile);
            contract.put("file_name", fileName);
        }
        contract.put("CONTRACT_NAME", ctName.getText().toString());
        contract.put("CUSTOMER_ID", String.valueOf(customerId));
        contract.put("CONTRACT_OWNER", String.valueOf(choose_Contract_USER));
        String b = ctPaid.getText().toString().replace(".", "");
        contract.put("PAID", b);
        contract.put("START_DATE", edtNbd.getText().toString());
        contract.put("END_DATE", edtNkt.getText().toString());

        JSONArray arrayProduct = new JSONArray();

        for (int i = 0; i < products.size(); i++) {

            Map<String, String> product = new HashMap<>();
            product.put("PRODUCT_ID", String.valueOf(products.get(i).getProductId()));
            product.put("DISCOUNT", String.valueOf(products.get(i).getDiscount()));
            product.put("TAX", String.valueOf(products.get(i).getTax()));
            if (products.get(i).getQuantity() == 0) {
                product.put("QUANTITY", "1");
            } else {
                product.put("QUANTITY", String.valueOf(products.get(i).getQuantity()));

            }
            product.put("PRICE", products.get(i).getPrice());
            product.put("PRODUCT_CODE", products.get(i).getProductCode());
            product.put("PRODUCT_NAME", products.get(i).getProductName());
            product.put("PRODUCT_DESCRIPTION", products.get(i).getDescription());
            product.put("PRODUCT_MANUFACTORY", products.get(i).getProductManufactory());
            product.put("PRODUCT_UNIT", products.get(i).getProductUnit());
            product.put("LENGTH", String.valueOf(products.get(i).getLength()));
            product.put("WIDTH", String.valueOf(products.get(i).getWidth()));
            product.put("HEIGHT", String.valueOf(products.get(i).getHeight()));
            product.put("PRODUCT_IMAGE", products.get(i).getPRODUCT_IMAGE());


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
                            Intent intent = new Intent();
                            setResult(4, intent);

                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            finish();
                            Toasty.success(AddContract.this, "Thêm mới hợp đồng thành công", Toast.LENGTH_SHORT).show();


                        } else if (jsonObject != null && !"".equals(jsonObject.toString()) && "fails".equals(jsonObject.optString("messages"))) {
                            String details = jsonObject.optString(getResources().getString(R.string.details));
                            if (details != null && !"".equals(details)) {
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                Toasty.normal(AddContract.this, details, Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Toasty.normal(AddContract.this, "Không thêm mới được hợp đồng !", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("createcontract", volleyError.toString());
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


    // lấy nguồn ảnh từ mobile
    private void showFileChooser() {

        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("*/*");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST);
    }


}

