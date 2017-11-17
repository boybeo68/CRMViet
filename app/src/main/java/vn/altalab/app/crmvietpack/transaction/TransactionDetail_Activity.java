package vn.altalab.app.crmvietpack.transaction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import vn.altalab.app.crmvietpack.Link;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.customer.customer_detail.CustomerDetail_Activity;
import vn.altalab.app.crmvietpack.notification.Notification_Activity;
import vn.altalab.app.crmvietpack.transaction.json.FileTransaction_Json;
import vn.altalab.app.crmvietpack.transaction.json.TransactionDetail_Json;
import vn.altalab.app.crmvietpack.transaction.object.TransactionDetail_Setget;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class TransactionDetail_Activity extends AppCompatActivity {
    ProgressDialog progressDialog;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private int PICK_FILE_REQUEST = 2;
    private String selectedFilePath = "";
    private String fileName = "";
    String encodeFile = "";
    private int REQUEST_CODE = 10;
    LinearLayout lnFileName;
    Button btChose, btUpload, btOutChose;
    private TextView
            tv_TRANSACTION_NAME_TEXT,
            tv_ASSIGNER,
            tv_ASSIGNED_USER_NAME,
            tv_CUSTOMER_NAME,
            tv_TRANSACTION_TYPE_NAME,
            tv_START_DATE,
            tv_END_DATE,
            tv_PRIORITY,
            tv_STATUS,
            tv_TRANSACTION_DESCRIPTION,
            tvFile,
            tvFileName;

    private FloatingActionButton FButton;
    ArrayList<String> list;
    ProgressDialog pDialog;

    private String JSON = "";
    String transactionId;
    String[] STATUS_ID = {"1", "2", "4", "3"};
    String[] STATUS = {"Chưa thực hiện", "Đang thực hiện", "Đã giải quyết", "Đã hoàn thành"};
    static boolean toast = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crm_transaction_details);

        FINDVIEWBYID();
        ACTION();

    }

    public void FINDVIEWBYID() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        tv_TRANSACTION_NAME_TEXT = (TextView) findViewById(R.id.tv_TRANSACTION_NAME_TEXT);
        tv_ASSIGNER = (TextView) findViewById(R.id.tv_ASSIGNER);
        tv_ASSIGNED_USER_NAME = (TextView) findViewById(R.id.tv_ASSIGNED_USER_NAME);
        tv_CUSTOMER_NAME = (TextView) findViewById(R.id.tv_CUSTOMER_NAME);
        tv_TRANSACTION_TYPE_NAME = (TextView) findViewById(R.id.tv_TRANSACTION_TYPE_NAME);
        tv_START_DATE = (TextView) findViewById(R.id.tv_START_DATE);
        tv_END_DATE = (TextView) findViewById(R.id.tv_END_DATE);
        tv_PRIORITY = (TextView) findViewById(R.id.tv_PRIORITY);
        tv_STATUS = (TextView) findViewById(R.id.tv_STATUS);
        tv_TRANSACTION_DESCRIPTION = (TextView) findViewById(R.id.tv_TRANSACTION_DESCRIPTION);
        tvFile = (TextView) findViewById(R.id.tvFile);
        tvFileName = (TextView) findViewById(R.id.tv_file_name);
        lnFileName = (LinearLayout) findViewById(R.id.ln_file_name);
        btChose = (Button) findViewById(R.id.btChoseFile);
        btOutChose = (Button) findViewById(R.id.btOutChose);
        btUpload = (Button) findViewById(R.id.btUploadFile);
        FButton = (FloatingActionButton) findViewById(R.id.FButton);

    }

    public void ACTION() {

        getToolbar();

        list = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            transactionId = bundle.getString("transaction_id");

            Log.e("TRANSACTIONDETAIL", "transaction_id: " + transactionId);

            get_DATA();
            get_ListFile(transactionId);
            tvFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.size() > 0) {
                        listFileDialog();
                    }
                }
            });
            FButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(TransactionDetail_Activity.this, TransactionDetail_Editation_Activity.class);
                    intent.putExtra("transaction_id", transactionId);
                    intent.putExtra("transaction_type", tv_TRANSACTION_TYPE_NAME.getText().toString());
                    intent.putExtra("transaction_user", tv_ASSIGNER.getText().toString());
                    intent.putExtra("json", JSON);
                    startActivityForResult(intent, REQUEST_CODE);
                }
            });
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
                doUploadFile(transactionId);
            }
        });
        btOutChose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btUpload.setVisibility(View.GONE);
                btOutChose.setVisibility(View.GONE);
                btChose.setVisibility(View.VISIBLE);
                lnFileName.setVisibility(View.GONE);
            }
        });
    }

    // lấy nguồn file từ mobile
    private void showFileChooser() {

        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("*/*");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST);
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
                    Toast.makeText(this, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                    selectedFilePath = "";
                }
            }
        }
        Log.e("TRANSACTIONDETAIL", "resultCode: " + resultCode);
        if (resultCode == 0) {
            get_DATA();
        }

    }

    public void doUploadFile(String transId) {
        Link link;
        String url;

        File selectedFile = new File(selectedFilePath);
        encodeFile = encodeFileToBase64Binary(selectedFile);
        link = new Link(this);
        url = link.files_upload_detailTransaction();
        //Showing the progress dialog

        Map<String, String> jsonParams = new Hashtable<String, String>();
        //Adding parameters


        jsonParams.put("uploaded_file", encodeFile);
        jsonParams.put("file_name", fileName);
        jsonParams.put("USER_ID", link.getId());
        jsonParams.put("TRANSACTION_ID", transId);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                //Disimissing the progress dialog

                //Showing toast message of the response
                try {
                    if (jsonObject.getString("messages").equals("success")) {


                        get_ListFile(transactionId);


                    } else {
                        if (jsonObject.getString("details") != null && !jsonObject.getString("details").equals("null"))
                            Toast.makeText(getApplication(), "Lỗi: " + jsonObject.getString("details"), Toast.LENGTH_SHORT).show();
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        btUpload.setClickable(true);
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
                        }
                        btUpload.setClickable(true);
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

    public void get_DATA() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Xin chờ giây lát ...");
        pDialog.setCancelable(true);
        pDialog.show();

        Link link = new Link(this);

        String url = link.get_TransactionDetail_GetLink(transactionId);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("TRANSACTIONDETAIL", "RESPONSE: " + jsonObject);

                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }

                try {
                    if (jsonObject != null)
                        set_VIEW(jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TRANSACTIONDETAIL", "ERROR: " + volleyError.toString());
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
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

        request.setShouldCache(false);
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        MySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(request);

    }


    public void get_ListFile(String id) {


        Link link = new Link(this);

        String url = link.get_files_transactions(id);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("TRANSACTIONDETAIL", "RESPONSE: " + jsonObject);

//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.dismiss();
//                }

                try {
                    if (jsonObject != null)
                        set_file(jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TRANSACTIONDETAIL", "ERROR: " + volleyError.toString());
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

        request.setShouldCache(false);
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        MySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(request);

    }

    public void set_file(JSONObject jsonObject) {
        list.clear();
        FileTransaction_Json fileTransaction_json = new FileTransaction_Json(jsonObject);
        if (fileTransaction_json.isStatus() == true) {
            for (int i = 0; i < fileTransaction_json.getList().size(); i++) {
                list.add(fileTransaction_json.getList().get(i).getFILE_TRANSACTION_NAME());
            }
            tvFile.setText(list.size() + " file");
            btUpload.setVisibility(View.GONE);
            btOutChose.setVisibility(View.GONE);
            btChose.setVisibility(View.VISIBLE);
            lnFileName.setVisibility(View.GONE);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            btUpload.setClickable(true);
            if (toast == true) {
                Toasty.success(getApplication(), "Thêm mới file thành công !", Toast.LENGTH_LONG).show();
                toast = false;
            }
        }

    }

    public void listFileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create();
        builder.setTitle("Tên các file đính kèm");


        ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
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

    public void set_VIEW(JSONObject jsonObject) {

        TransactionDetail_Json transactionDetailJson = new TransactionDetail_Json(jsonObject);

        if (transactionDetailJson.isStatus() == true) {

            JSON = String.valueOf(jsonObject);
            final TransactionDetail_Setget SETGET = transactionDetailJson.GET_SETGET();

            tv_TRANSACTION_NAME_TEXT.setText(SETGET.getTRANSACTION_NAME_TEXT());
            tv_ASSIGNER.setText(SETGET.getASSIGNER());
            tv_ASSIGNED_USER_NAME.setText(SETGET.getASSIGNED_USER_NAME());
            tv_CUSTOMER_NAME.setText(SETGET.getCUSTOMER_NAME());
            tv_TRANSACTION_TYPE_NAME.setText(SETGET.getTRANSACTION_TYPE_NAME());
            tv_START_DATE.setText(SETGET.getSTART_DATE());
            tv_END_DATE.setText(SETGET.getEND_DATE());
            tv_PRIORITY.setText(SETGET.getPRIORITY());

            if (SETGET.getSTATUS().equals(STATUS_ID[0])) {
                tv_STATUS.setText(STATUS[0]);
            } else if (SETGET.getSTATUS().equals(STATUS_ID[1])) {
                tv_STATUS.setText(STATUS[1]);
            } else if (SETGET.getSTATUS().equals(STATUS_ID[2])) {
                tv_STATUS.setText(STATUS[2]);
            } else if (SETGET.getSTATUS().equals(STATUS_ID[3])) {
                tv_STATUS.setText(STATUS[3]);
            }
            tv_CUSTOMER_NAME.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SETGET.getCUSTOMER_ID() != 0) {
                        Intent intent = new Intent(TransactionDetail_Activity.this, CustomerDetail_Activity.class);
                        Log.e("TRANSACTIONDETAIL", "getCUSTOMER_ID: " + SETGET.getCUSTOMER_ID());
                        intent.putExtra("customer_id", SETGET.getCUSTOMER_ID());
                        startActivityForResult(intent, REQUEST_CODE);
                    }

                }
            });

            try {
                Spanned spanned = Html.fromHtml(SETGET.getTRANSACTION_DESCRIPTION());
                tv_TRANSACTION_DESCRIPTION.setText(spanned);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.e("TRANDETAILSTATUS", "" + SETGET.getSTATUS());

        }
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

    public void getToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvNotification = (TextView) findViewById(R.id.tvNotification);
        LinearLayout llBell = (LinearLayout) findViewById(R.id.llBell);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvNotification.setText("Chi tiết giao dịch");

        llBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransactionDetail_Activity.this, Notification_Activity.class);
                intent.putExtra("type_object", 1);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(0);
        finish();
    }
}
