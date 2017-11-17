package vn.altalab.app.crmvietpack.customer.customer_detail;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import vn.altalab.app.crmvietpack.Link;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.customer.edit_create_customer.Customer_Edit_Infomation_Activity;
import vn.altalab.app.crmvietpack.transaction.FilePath;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class CustomerDetail_Info_Fragment extends Fragment {
    private TextView tvId, tvName, tvEmail, tvAddress, tvMainContact, tvPhone, tvDes, tvUserName, tvTaxCode;
    private FloatingActionButton floatingActionButton;
    private View view;
    private static long customerId = 0;
    public static int contactPhone, contactEmail;
    private String customer_name = "";
    Boolean isLoading = false;


    TextView tvFileName, tvfileNumber;
    private int PICK_FILE_REQUEST = 2;
    private String selectedFilePath = "";
    private String fileName = "";
    String encodeFile = "";
    LinearLayout lnFileName;
    Button btChose, btUpload, btOutChose;
    static boolean toast = false;
    List<String> listFile;
    private ProgressDialog progressDialog;

    ImageView imv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.customerdetail_info_fragment, container, false);

            if (getActivity() != null) {
                Action();
            }
        }

        return view;
    }

    public void findViewById() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Xin chờ ...");
        progressDialog.setCancelable(false);

        listFile = new ArrayList<>();
        imv = (ImageView) view.findViewById(R.id.imv);
        tvfileNumber = (TextView) view.findViewById(R.id.tvFileNumber);
        tvFileName = (TextView) view.findViewById(R.id.tv_file_name);
        lnFileName = (LinearLayout) view.findViewById(R.id.ln_file_name);
        btChose = (Button) view.findViewById(R.id.btChoseFile);
        btOutChose = (Button) view.findViewById(R.id.btOutChose);
        btUpload = (Button) view.findViewById(R.id.btUploadFile);
        tvfileNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listFile.size() > 0) {
                    listFileDialog();
                }
            }
        });

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
                doUploadFile(customerId);
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

        tvTaxCode = (TextView) view.findViewById(R.id.tvTaxCode);
        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        tvId = (TextView) view.findViewById(R.id.tvId);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvPhone = (TextView) view.findViewById(R.id.tvPhone);
        tvAddress = (TextView) view.findViewById(R.id.tvAddress);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        tvMainContact = (TextView) view.findViewById(R.id.tvMainContact);
        tvDes = (TextView) view.findViewById(R.id.tvDes);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingButton);
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

    public void doUploadFile(final long customerId) {
        Link link;
        String url;

        File selectedFile = new File(selectedFilePath);
        encodeFile = encodeFileToBase64Binary(selectedFile);
        link = new Link(getActivity());
        url = link.files_upload_detailCustomer();
        //Showing the progress dialog

        Map<String, String> jsonParams = new Hashtable<String, String>();
        //Adding parameters


        jsonParams.put("uploaded_file", encodeFile);
        jsonParams.put("file_name", fileName);
        jsonParams.put("USER_ID", link.getId());
        jsonParams.put("CUSTOMER_ID", "" + customerId);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                //Disimissing the progress dialog

                //Showing toast message of the response
                try {
                    if (jsonObject.getString("messages").equals("success")) {

                        doGetCustomerDetails(customerId);


                    } else {
                        if (jsonObject.getString("details") != null && !jsonObject.getString("details").equals("null"))
                            Toasty.normal(getActivity(), "Lỗi: " + jsonObject.getString("details"), Toast.LENGTH_SHORT).show();
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
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private String encodeFileToBase64Binary(File file) {
        String encodedfile = "";
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);


            if (file.length()> 13631488) {
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

    public void Dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

    public void Action() {

        // tim kiem Id
        findViewById();

        // nhan id
        final Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null)
            customerId = bundle.getLong("customer_id");
        contactEmail = bundle.getInt("checkEmail");
        contactPhone = bundle.getInt("checkPhone");
        // load du lieu
        if (customerId != 0 && isLoading == false)
            doGetCustomerDetails(customerId);

        // hanh dong button
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Customer_Edit_Infomation_Activity.class);
                intent.putExtra("customer_id", customerId);
                intent.putExtra("CUSTOMER_NAME", customer_name);
                startActivityForResult(intent, 0);
            }

        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            selectedFilePath = FilePath.getPath(getActivity(), selectedFileUri);


            if (selectedFilePath != null && !selectedFilePath.equals("")) {
                String a[] = selectedFilePath.split("/");
                fileName = a[a.length - 1];
                tvFileName.setText(selectedFilePath);
            } else {
                Toasty.normal(getActivity(), "Không thể upload file lên server", Toast.LENGTH_SHORT).show();
                selectedFilePath = "";
            }
        }
        if (requestCode == 0) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Xin chờ ...");
            progressDialog.setCancelable(true);
            progressDialog.show();
            doGetCustomerDetails(customerId);

        }
    }

    public void doGetCustomerDetails(long customerId) {

        isLoading = true;


        Link link = new Link(getActivity());
        String url = link.get_CUSTOMERDETAIL_Infomation_Link(customerId);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                setEditText(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Log.e("customerDetailInfo", "Error: " + volleyError.toString());
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
        int socketTimeout = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);

    }

    public void setEditText(JSONObject jsonObject) {
        Link link = new Link();
        try {
            Log.e("CustomerDetailInfo", "Messages: " + jsonObject.getString("messages"));

            if (jsonObject.getString("messages").equals("success")) {
                JSONObject object = jsonObject.getJSONObject("customer");

                String CUSTOMER_CODE = object.getString("CUSTOMER_CODE");
                if (!CUSTOMER_CODE.equals("null"))
                    tvId.setText(CUSTOMER_CODE);

                String USER_NAME = object.getString("USER_NAME");
                if (!USER_NAME.equals("null"))
                    tvUserName.setText(USER_NAME);

                String CUSTOMER_NAME = object.getString("CUSTOMER_NAME");
                if (!CUSTOMER_NAME.equals("null"))
                    tvName.setText(CUSTOMER_NAME);

                if (link.getId().equals("1")) {
                    String TELEPHONE = object.getString("TELEPHONE");
                    if (!TELEPHONE.equals("null"))
                        tvPhone.setText(TELEPHONE);
                } else {

                    if (contactPhone == 1) {
                        String TELEPHONE = object.getString("TELEPHONE");
                        if (!TELEPHONE.equals("null"))
                            tvPhone.setText(TELEPHONE);
                    } else {
                        tvPhone.setText("************");
                    }
                }
                if (link.getId().equals("1")) {
                    String CUSTOMER_EMAIL = object.getString("CUSTOMER_EMAIL");
                    if (!CUSTOMER_EMAIL.equals("null"))
                        tvEmail.setText(CUSTOMER_EMAIL);
                } else {
                    if (contactEmail == 1) {
                        String CUSTOMER_EMAIL = object.getString("CUSTOMER_EMAIL");
                        if (!CUSTOMER_EMAIL.equals("null"))
                            tvEmail.setText(CUSTOMER_EMAIL);
                    } else {
                        tvEmail.setText("************");
                    }
                }


                String OFFICE_ADDRESS = object.getString("OFFICE_ADDRESS");
                if (!OFFICE_ADDRESS.equals("null"))
                    tvAddress.setText(OFFICE_ADDRESS);

                String CUSTOMER_DESCRIPTION = object.getString("CUSTOMER_DESCRIPTION");
                if (!CUSTOMER_DESCRIPTION.equals("null"))
                    tvDes.setText(CUSTOMER_DESCRIPTION);

                String TAX_CODE = object.getString("TAX_CODE");
                if (!TAX_CODE.equals("null"))
                    tvTaxCode.setText(TAX_CODE);

                JSONArray array = jsonObject.getJSONArray("main_contact");

                for (int i = 0; i < array.length(); i++) {
                    final JSONObject object1 = array.optJSONObject(i);
                    if (link.getId().equals("1")) {
                        if (!object1.getString("CONTACT_MOBIPHONE").equals("") && !object1.getString("CONTACT_MOBIPHONE").equals("null")) {
                            tvMainContact.setText(object1.getString("CONTACT_FULL_NAME") + " ("
                                    + object1.getString("CONTACT_MOBIPHONE") + ")");
                        } else {
                            tvMainContact.setText(object1.getString("CONTACT_FULL_NAME"));

                        }
                    } else {
                        if (contactPhone == 1) {
                            if (!object1.getString("CONTACT_MOBIPHONE").equals("") && !object1.getString("CONTACT_MOBIPHONE").equals("null")) {
                                tvMainContact.setText(object1.getString("CONTACT_FULL_NAME") + " ("
                                        + object1.getString("CONTACT_MOBIPHONE") + ")");
                            } else {
                                tvMainContact.setText(object1.getString("CONTACT_FULL_NAME"));

                            }

                        } else {
                            tvMainContact.setText(object1.getString("CONTACT_FULL_NAME") + "(************)");
                        }
                    }


                }
                String link_image = jsonObject.getString("link_image");
                if(!link_image.equals("")){
                    Picasso.with(getActivity())
                            .load(link_image)
                            // chỉnh lại dung lượng của file ảnh cho giảm xuống.
                            .resize(300, 300)
                            .placeholder(R.drawable.ic_person)
                            .error(R.drawable.ic_person)
                            .into(imv);
                }else {
                    imv.setImageResource(R.drawable.ic_person);
                }


                listFile.clear();
                JSONArray array1 = jsonObject.getJSONArray("files");
                for (int i = 0; i < array1.length(); i++) {
                    final JSONObject object2 = array1.optJSONObject(i);
                    listFile.add(object2.optString("FILE_CUSTOMER_NAME"));

                }
                tvfileNumber.setText(listFile.size() + " file");
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();

                }
                // ẩn dòng hiển thị file upload
                lnFileName.setVisibility(View.GONE);
                btOutChose.setVisibility(View.GONE);
                btUpload.setVisibility(View.GONE);
                btUpload.setClickable(true);
                btChose.setVisibility(View.VISIBLE);
                if (toast == true) {
                    Toasty.success(getActivity(), "Upload file thành công !", Toast.LENGTH_LONG).show();
                    toast = false;
                }
            } else {
                Toasty.normal(getActivity(), jsonObject.getString("details"), Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //hiển thị tên file
    public void listFileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.create();
        builder.setTitle("Tên các file đính kèm");


        ListAdapter listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listFile);
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
}