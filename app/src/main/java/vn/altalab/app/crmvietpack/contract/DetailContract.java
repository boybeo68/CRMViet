package vn.altalab.app.crmvietpack.contract;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import vn.altalab.app.crmvietpack.Link;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.contract.object.Contract;
import vn.altalab.app.crmvietpack.object.Product;
import vn.altalab.app.crmvietpack.orders_fragment.order_detail.Productlst;
import vn.altalab.app.crmvietpack.presenter.ProductAdapter;
import vn.altalab.app.crmvietpack.transaction.FilePath;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class DetailContract extends AppCompatActivity {
    TextView tvFileName, tvfileNumber, txtContractCode, txtContractName, txtStatusName, txtContractOwner, txtPaid, txtContractPrice, txtDebt, txtCustomerName, tv_bd, tv_kt;
    Button btnSua, btnXoa;
    Toolbar toolbar;
    public static List<Contract> contracts;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;
    private long contractId = 0;
    List<String> listFile;
    ListView ProductList;
    public static List<Product> products;
    ProductAdapter productAdapter;
    private ProgressDialog progressDialog;
    int position;

    long customer_Id;
    private String contractName, contractCode, statusName, contractOwner, paid, contractPrice, dept, customerName, nbd, nkt;


    private int PICK_FILE_REQUEST = 2;
    private String selectedFilePath = "";
    private String fileName = "";
    String encodeFile = "";
    LinearLayout lnFileName;
    Button btChose, btUpload, btOutChose;
    ProgressDialog loading;
    static boolean toast = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contract);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            getSupportActionBar().setSubtitle("Chi tiết hợp đồng");
        }
        if (settings == null) {
            settings = getSharedPreferences(PREFS_NAME, 0);
        }

        progressDialog = new ProgressDialog(DetailContract.this);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);


        ProductList = (ListView) findViewById(R.id.listProductCT);
        txtContractCode = (TextView) findViewById(R.id.txtContractCode);
        txtContractName = (TextView) findViewById(R.id.txtContractName);
        txtStatusName = (TextView) findViewById(R.id.txtStatusName);
        txtContractOwner = (TextView) findViewById(R.id.txtContractOwner);
        txtPaid = (TextView) findViewById(R.id.txtPaid);
        txtContractPrice = (TextView) findViewById(R.id.txtContractPrice);
        txtDebt = (TextView) findViewById(R.id.txtDebt);
        txtCustomerName = (TextView) findViewById(R.id.txtcustomerName);
        tvfileNumber = (TextView) findViewById(R.id.tvFileNumber);
        tvFileName = (TextView) findViewById(R.id.tv_file_name);
        lnFileName = (LinearLayout) findViewById(R.id.ln_file_name);
        btChose = (Button) findViewById(R.id.btChoseFile);
        btOutChose = (Button) findViewById(R.id.btOutChose);
        btUpload = (Button) findViewById(R.id.btUploadFile);
        tvfileNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listFile.size() > 0) {
                    listFileDialog();
                }
            }
        });


        btnSua = (Button) findViewById(R.id.btSuaContract);
        btnXoa = (Button) findViewById(R.id.btXoaContract);
        tv_bd = (TextView) findViewById(R.id.tv_bd);
        tv_kt = (TextView) findViewById(R.id.tv_kt);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            contractId = bundle.getLong("CONTRACT_ID");
            contractCode = bundle.getString("CONTRACT_CODE");
            contractName = bundle.getString("CONTRACT_NAME");
            statusName = bundle.getString("STATUS_NAME");


            contractOwner = bundle.getString("CONTRACT_OWNER_NAME");

            paid = bundle.getString("PAID");
            contractPrice = bundle.getString("CONTRACT_PRICE");
            dept = bundle.getString("DEBT");
            customerName = bundle.getString("CUSTOMER_NAME");
            nbd = bundle.getString("startDate");
            nkt = bundle.getString("endDate");

            customer_Id = bundle.getLong("customer_id");

            position = bundle.getInt("position");

        }

        btChose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();


            }
        });

        btUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Uploading...");
                progressDialog.show();
                toast = true;
                btUpload.setClickable(false);
                doUploadFile(String.valueOf(contractId));
            }
        });
        btOutChose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btOutChose.setVisibility(View.GONE);
                btUpload.setVisibility(View.GONE);
                btChose.setVisibility(View.VISIBLE);
                lnFileName.setVisibility(View.GONE);
            }
        });


        txtContractCode.setText(contractCode);
        txtContractName.setText(contractName);
        txtStatusName.setText(statusName);
        txtContractOwner.setText(contractOwner);
        txtPaid.setText(DecimalFormat.getInstance().format(Double.parseDouble(paid)));
        txtContractPrice.setText(DecimalFormat.getInstance().format(Double.parseDouble(contractPrice)));
        txtDebt.setText(DecimalFormat.getInstance().format(Double.parseDouble(dept)));
        txtCustomerName.setText(customerName);
        tv_bd.setText(nbd);
        tv_kt.setText(nkt);
        listFile = new ArrayList<>();

        products = new ArrayList<>();

        doGetDetailContract();

        productAdapter = new ProductAdapter(DetailContract.this, R.layout.item_product, products);
        ProductList.setAdapter(productAdapter);
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailContract.this, EditContract.class);
                Bundle bundle = new Bundle();
                bundle.putLong("contract_id", contractId);
                bundle.putString("customer_name", txtCustomerName.getText().toString());
                bundle.putLong("customer_id", customer_Id);
                bundle.putString("contract_name", txtContractName.getText().toString());

                bundle.putString("CONTRACT_OWNER_NAME", txtContractOwner.getText().toString());

                bundle.putString("paid", paid);
                bundle.putString("startDate", tv_bd.getText().toString());
                bundle.putString("endDate", tv_kt.getText().toString());
                bundle.putString("StatusName", txtStatusName.getText().toString());

                intent.putExtras(bundle);

                //xử lý chuyển list object giữa 2 màn hình :
                Productlst product1 = new Productlst();
                product1.setProducts(products);
                intent.putExtra("listproduct", product1);

                startActivityForResult(intent, 1);
//                finish();
            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(DetailContract.this);
                alertBuilder.create();
                alertBuilder.setMessage("Bạn có muốn xóa hợp đồng này không ?");
                alertBuilder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doRemoveContract();
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST) {
            if (data == null) {
                //no data present
                return;
            }
            btChose.setVisibility(View.GONE);
            btOutChose.setVisibility(View.VISIBLE);
            lnFileName.setVisibility(View.VISIBLE);
            btUpload.setVisibility(View.VISIBLE);
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

        if (requestCode == 1) {
            products.clear();
            productAdapter.notifyDataSetChanged();
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            doGetDetailContract();
        }
    }

    private void doRemoveContract() {
        progressDialog = new ProgressDialog(DetailContract.this);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Đang xóa...");
        progressDialog.show();
        String url = settings.getString("api_server", "") + "/api/v1/contract/remove/" + contractId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && "success".equals(jsonObject.optString("messages"))) {
                    Toasty.success(DetailContract.this, "Xóa thành công !", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("position", position);
                    setResult(5, intent);
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    finish();
                } else if (jsonObject != null && "fails".equals(jsonObject.optString("messages"))) {
                    Toasty.normal(DetailContract.this, jsonObject.optString("details"), Toast.LENGTH_SHORT).show();
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

    private void doGetDetailContract() {

        String url = settings.getString("api_server", "") + "/api/v1/contracts/" + contractId;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {


                            JSONObject object = jsonObject.optJSONObject("contracts");
                            try {
                                customer_Id = object.getLong("CUSTOMER_ID");
                                paid = object.getString("PAID");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            txtContractCode.setText(object.optString("CONTRACT_CODE"));
                            txtContractName.setText(object.optString("CONTRACT_NAME"));
                            txtStatusName.setText(object.optString("STATUS_NAME"));
                            txtContractOwner.setText(object.optString("CONTRACT_OWNER_NAME"));
                            txtPaid.setText(DecimalFormat.getInstance().format(Double.parseDouble(object.optString("PAID"))));
                            txtContractPrice.setText(DecimalFormat.getInstance().format(Double.parseDouble(object.optString("CONTRACT_PRICE"))));
                            txtDebt.setText(DecimalFormat.getInstance().format(Double.parseDouble(object.optString("DEBT"))));
                            txtCustomerName.setText(object.optString("CUSTOMER_NAME"));
                            String a = object.optString("START_DATE");
                            if (a.equals(null) || a.equals("null") || a.equals("") || a.equals("0000-00-00 00:00:00")) {

                                tv_bd.setText("");
                            } else {
                                String b[] = a.split("-");
                                String b2[] = b[2].split(" ");
                                tv_bd.setText(b2[0] + "/" + b[1] + "/" + b[0]);
                            }

                            String a1 = object.optString("END_DATE");
                            if (a1.equals(null) || a1.equals("null") || a1.equals("") || a1.equals("0000-00-00 00:00:00")) {
                                tv_kt.setText("");
                            } else {
                                String b[] = a1.split("-");
                                String b2[] = b[2].split(" ");
                                tv_kt.setText(b2[0] + "/" + b[1] + "/" + b[0]);
                            }

                            products.clear();
                            productAdapter.notifyDataSetChanged();
                            JSONArray array = object.optJSONArray("products");

                            for (int i = 0; i < array.length(); i++) {
                                final JSONObject object1 = array.optJSONObject(i);
                                Product product = new Product();
                                product.setProductName(object1.optString("PRODUCT_NAME"));
                                product.setProductManufactory(object1.optString("PRODUCT_MANUFACTORY"));
                                product.setDescription(object1.optString("PRODUCT_DESCRIPTION"));
                                product.setPrice(object1.optString("PRICE"));
                                product.setQuantity(object1.optInt("QUANTITY"));
                                product.setProductId(object1.optInt("PRODUCT_ID"));
                                product.setProductCode(object1.optString("PRODUCT_CODE"));
                                product.setDiscount(object1.optDouble("DISCOUNT"));
                                product.setTax(object1.optDouble("TAX"));
                                product.setProductUnit(object1.optString("PRODUCT_UNIT"));
                                product.setLength(object1.optDouble("LENGTH"));
                                product.setWidth(object1.optDouble("WIDTH"));
                                product.setHeight(object1.optDouble("HEIGHT"));
                                product.setPRODUCT_IMAGE(object1.optString("PRODUCT_IMAGE"));
                                product.setUrl_image(object1.optString("LINK_IMAGE"));

                                products.add(product);
                            }
                            listFile.clear();
                            JSONArray array1 = object.optJSONArray("files_contract");
                            for (int i = 0; i < array1.length(); i++) {
                                final JSONObject object2 = array1.optJSONObject(i);
                                listFile.add(object2.optString("FILE_NAME"));
                            }
                            tvfileNumber.setText(listFile.size() + " file");
                        }

                        productAdapter.notifyDataSetChanged();
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            // ẩn dòng hiển thị file upload
                            lnFileName.setVisibility(View.GONE);
                            btOutChose.setVisibility(View.GONE);
                            btUpload.setVisibility(View.GONE);
                            btUpload.setClickable(true);
                            btChose.setVisibility(View.VISIBLE);
                            if (toast == true) {
                                Toasty.success(getApplication(), "Upload file thành công !", Toast.LENGTH_LONG).show();
                                toast = false;
                            }
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

    //hiển thị tên file
    public void listFileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create();
        builder.setTitle("Tên các file đính kèm");


        ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listFile);
        builder.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }

        });
        builder.show();
    }

    public void doUploadFile(String contractId) {
        Link link;
        String url;

        File selectedFile = new File(selectedFilePath);
        encodeFile = encodeFileToBase64Binary(selectedFile);
        link = new Link(this);
        url = link.files_upload_detailContract();
        //Showing the progress dialog

        Map<String, String> jsonParams = new Hashtable<String, String>();
        //Adding parameters


        jsonParams.put("uploaded_file", encodeFile);
        jsonParams.put("file_name", fileName);
        jsonParams.put("USER_ID", link.getId());
        jsonParams.put("CONTRACT_ID", contractId);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                //Disimissing the progress dialog

                //Showing toast message of the response
                try {
                    if (jsonObject.getString("messages").equals("success")) {


                        doGetDetailContract();


                    } else {
                        if (jsonObject.getString("details") != null && !jsonObject.getString("details").equals("null")) {
                            Toasty.normal(getApplication(), "Lỗi: " + jsonObject.getString("details"), Toast.LENGTH_SHORT).show();
                        }
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            btUpload.setClickable(true);
                        }
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
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            btUpload.setClickable(true);
                        }
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

    private String encodeFileToBase64Binary(File file) {
        String encodedfile = "";
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);


            if (file.length() > 13631488) {
//                Toast.makeText(getApplicationContext(), "File quá lớn, không thể upload !", Toast.LENGTH_LONG).show();
                Dialog();
                toast = false;
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

    //chọn file từ mobile
    private void showFileChooser() {

        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("*/*");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST);
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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(4);
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

            setResult(4);

            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
