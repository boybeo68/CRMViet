package vn.altalab.app.crmvietpack.customer.edit_create_customer;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import vn.altalab.app.crmvietpack.Link;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.Shared_Preferences;
import vn.altalab.app.crmvietpack.customer.adapter.ChosePl_Adapter;
import vn.altalab.app.crmvietpack.customer.setget.ChildrenType;
import vn.altalab.app.crmvietpack.customer.setget.GEOGRAPHY;
import vn.altalab.app.crmvietpack.object.TblPosition;
import vn.altalab.app.crmvietpack.object.Users;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class Customer_Add_NewCustomer_Activity extends AppCompatActivity {

    static String country = "", province = "", district = "", ward = "";
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    int PositonID, gd;
    private long pressedTime = 0;
    ArrayAdapter<String> adapter, adapterGender;
    private EditText edtCustomerName, edtOfficeAddress, edtTelephone, edtBirthday, edtEmail, edtTaxCode, edtFax, edtDes,
            edtWeb, edtBankAccount, edtBankLocation, edtDelivery, edtDebt, edtĐKKD, edtShare;
    private EditText edtContactName, edtContactMobilePhone, edtContactBirthday, edtContactEmail, edtWorkPhone, edtContactAdress, edtNickChat;
    private Spinner spnPosition, spnGender;
    Toolbar toolbar;
    ProgressDialog pDialog;
    List<TblPosition> tblPositions;
    String ChosePosition = "0";
    // Get default instance of realm
    private Shared_Preferences sharedPreferences;
    Link link;
    ListView listView;
    ArrayList<ChildrenType> ListPl;
    ChosePl_Adapter chosePl_adapter;
    ChildrenType childrenType;
    Spinner spCustomer_USER, spCountry, spProvince, spDistrict, spWard;
    List<GEOGRAPHY> countryList, provinceList, districtList, wardList;
    String Gender[] = {"-- chưa chọn --", "Nam", "Nữ"};
    String choose_Contract_USER = "", chose_USER_OWNER = "";
    ImageButton btnBirthdayPicker;
    ImageButton btnBirthdayPickerContact, btnShare;
    Button btnAddCustomer, btnChosePl, btHienthi, btThugon;
    int check = 0;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;
    private boolean[] selectedUser;
    private String USER_OWNER = "";

    ArrayList<Integer> mUserItems = new ArrayList<>();
    String[] items;

    List<Users> userses;

    private Button buttonChoose, outChoose;
    private ImageView imageView;


    private Bitmap bitmap;

    private int PICK_IMAGE_REQUEST = 2;

    private String KEY_IMAGE = "image";
    static int clear = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_customer_create);

        GET_FINDVIEWBYID();
        ACTION();
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        outChoose = (Button) findViewById(R.id.outChoose);


        imageView = (ImageView) findViewById(R.id.imageView);

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
                imageView.setVisibility(View.GONE);
                clear = 0;
                outChoose.setVisibility(View.GONE);

            }
        });
    }

    // Chức danh
    public void SpinerPosition() {
        String PositionName[] = new String[tblPositions.size() + 1];
        final int PositionId[] = new int[tblPositions.size() + 1];
        PositionName[0] = "-- chưa chọn --";
        PositionId[0] = 0;
        for (int i = 0; i < tblPositions.size(); i++) {
            PositionName[i + 1] = tblPositions.get(i).getPositionName();
            PositionId[i + 1] = tblPositions.get(i).getPositionId();
        }
        adapter = new ArrayAdapter<>(this, R.layout.spinner_textview, PositionName);
        spnPosition.setAdapter(adapter);
        spnPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ChosePosition = "" + PositionId[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // chọn ảnh
    private void showFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            clear = 1;
            imageView.setVisibility(View.VISIBLE);
            outChoose.setVisibility(View.VISIBLE);
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap = Bitmap.createScaledBitmap(bitmap, 512, 512, false);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == 1) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                childrenType = (ChildrenType) bundle.get("chosePl");
                if (childrenType != null) {
//
                    for (int i = 0; i < childrenType.getChildrenTypeList().size(); i++) {
                        if (ListPl.size() != 0) {
                            for (int j = 0; j < ListPl.size(); j++) {
                                if (childrenType.getChildrenTypeList().get(i).getCUSTOMER_GROUP_NAME().equals(ListPl.get(j).getCUSTOMER_GROUP_NAME())) {
                                    // remove các phần tử đã có trong ChildrenTypeList trùng với các phần tử trong ListPl
                                    childrenType.getChildrenTypeList().remove(childrenType.getChildrenTypeList().get(i));

                                }
                            }
                            //add list ChildrenTypeList vào ListPl khi i chạy đến phần tử cuối cùng trong mảng
                            if (i == childrenType.getChildrenTypeList().size() - 1) {
                                ListPl.addAll(childrenType.getChildrenTypeList());
                                break;
                            }

                        } else {
                            ListPl.addAll(childrenType.getChildrenTypeList());
                            break;

                        }


//
                    }


                }


                chosePl_adapter.notifyDataSetChanged();
            }
        }

    }

    public void ACTION() {
        link = new Link(this);
        getToolbar();
        tblPositions = new ArrayList<>();

        if (sharedPreferences == null) {
            sharedPreferences = new Shared_Preferences(this, PREFS_NAME);
        }
        userses = new ArrayList<>();
        ListPl = new ArrayList<>();
        chosePl_adapter = new ChosePl_Adapter(this, R.layout.item_pl, ListPl);
        listView.setAdapter(chosePl_adapter);


        doCountry();
        // spinner
// danh sách chủ sở hữu và chia sẻ
        doGetUsers();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.create();
                List<String> listOptions = new ArrayList<>();
                listOptions.add("Xóa");
                ListAdapter listAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, listOptions);
                builder.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {

                            ListPl.remove(position);
                            chosePl_adapter.notifyDataSetChanged();
                        }
                    }
                });
                builder.show();
            }
        });


        btnChosePl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Customer_Add_NewCustomer_Activity.this, ChosePl.class);
                startActivityForResult(intent, 1);
            }
        });

        doGetPosition();


        calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());


        adapterGender = new ArrayAdapter<>(this, R.layout.spinner_textview, Gender);
        spnGender.setAdapter(adapterGender);
        spnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("-- chưa chọn --")) {
                    gd = 0;
                }

                if (selectedItem.equals("Nam")) {
                    gd = 1;
                }

                if (selectedItem.equals("Nữ")) {
                    gd = 2;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //      bindingData(this);

        btnBirthdayPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        edtBirthday.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        btnBirthdayPickerContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        edtContactBirthday.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        btnAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtCustomerName == null || edtCustomerName.getText().toString().trim().equals("")) {
//                    edtCustomerName.setError(getResources().getString(R.string.empty_customer_name));
                    Toasty.normal(getApplicationContext(), "Bạn phải nhập tên khách hàng", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (edtTelephone == null || "".equals(edtTelephone.getText().toString().trim())) {
//                    edtTelephone.setError(getResources().getString(R.string.empty_telephone));
                    Toasty.normal(getApplicationContext(), "Bạn phải nhập số điện thoại khách hàng", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ("".equals(sharedPreferences.getString("api_server"))) {
                    Toast.makeText(Customer_Add_NewCustomer_Activity.this, v.getContext().getResources().getString(R.string.login_empty_api_server), Toast.LENGTH_SHORT).show();
                    return;
                }

                doCreateCustomer();

            }
        });
    }

    // lấy ra list chức danh
    public void doGetPosition() {
        String url = sharedPreferences.getString("api_server") + "/api/v1/position";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {
                    try {
                        JSONArray array = jsonObject.optJSONArray("position");
                        for (int i = 0; i < array.length(); i++) {
                            TblPosition tblPosition = new TblPosition();
                            JSONObject object = array.optJSONObject(i);
                            tblPosition.setPositionName(object.optString("POSITION_NAME"));
                            tblPosition.setPositionId(object.optInt("POSITION_ID"));
                            tblPositions.add(tblPosition);
                        }
                        SpinerPosition();

                    } catch (NullPointerException npe) {
                        Log.e("homeusers", npe.toString());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("gettransactiontype", volleyError.toString());
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

        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        MySingleton.getInstance(this).addToRequestQueue(request);


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

        ArrayAdapter adapterTRANSACTION_USER = new ArrayAdapter<>(this, R.layout.spinner_textview, UsersName);
        spCustomer_USER.setAdapter(adapterTRANSACTION_USER);
        spCustomer_USER.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choose_Contract_USER = UsersId[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Link link = new Link();
        for (int i = 0; i < userses.size(); i++) {
            if (userses.get(i).getUserId() == Long.parseLong(link.getId())) {
                spCustomer_USER.setSelection(i);
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
// danh sách chia sẻ
                        items = new String[userses.size()];
                        for (int i = 0; i < userses.size(); i++) {
                            items[i] = userses.get(i).getUserName();
                        }
                        selectedUser = new boolean[items.length];

                        btnShare.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(Customer_Add_NewCustomer_Activity.this);

                                builder.setMultiChoiceItems(items, selectedUser, new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                        if (isChecked) {
                                            mUserItems.add(which);
                                        } else {
                                            mUserItems.remove((Integer.valueOf(which)));
                                        }
                                    }
                                });
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (dialog != null) {
                                            dialog.dismiss();
                                        }

                                        String a = "";
                                        USER_OWNER = "";
                                        for (int i = 0; i < mUserItems.size(); i++) {
                                            a = a + items[mUserItems.get(i)];
                                            USER_OWNER = USER_OWNER + userses.get(mUserItems.get(i)).getUserId();
                                            if (i != mUserItems.size() - 1) {
                                                a = a + ", ";
                                                USER_OWNER = USER_OWNER + ",";
                                            }
                                        }

                                        edtShare.setText(a);
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (dialog != null) {
                                            dialog.dismiss();
                                        }
                                    }
                                });
                                builder.setCancelable(false);
                                AlertDialog mDialog = builder.create();
                                mDialog.show();
                            }
                        });
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

    public void GET_FINDVIEWBYID() {
        if (settings == null) {
            settings = getSharedPreferences(PREFS_NAME, 0);
        }
        spCustomer_USER = (Spinner) findViewById(R.id.spCustomer_USER);
        spCountry = (Spinner) findViewById(R.id.spCountry);
        spProvince = (Spinner) findViewById(R.id.spProvince);
        spDistrict = (Spinner) findViewById(R.id.spDistrict);
        spWard = (Spinner) findViewById(R.id.spWard);
        btnShare = (ImageButton) findViewById(R.id.btShare);
        countryList = new ArrayList<>();
        provinceList = new ArrayList<>();
        districtList = new ArrayList<>();
        wardList = new ArrayList<>();

        listView = (ListView) findViewById(R.id.lv);
        // Initiate Edit text Customer
        edtShare = (EditText) findViewById(R.id.edtShare);
        edtCustomerName = (EditText) findViewById(R.id.edtCustomerName);
        edtFax = (EditText) findViewById(R.id.edtFaxCus);
        edtDes = (EditText) findViewById(R.id.edtDesCus);
        edtOfficeAddress = (EditText) findViewById(R.id.edtOfficeAddress);
        edtTelephone = (EditText) findViewById(R.id.edtTelephone);
        edtBirthday = (EditText) findViewById(R.id.edtBirthday);
        edtBirthday.setEnabled(false);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtTaxCode = (EditText) findViewById(R.id.edtTaxCodeCus);
        edtWeb = (EditText) findViewById(R.id.edtWebsite);
        edtBankAccount = (EditText) findViewById(R.id.edtBankAccount);
        edtBankLocation = (EditText) findViewById(R.id.edtBankLocation);
        edtDelivery = (EditText) findViewById(R.id.edtDeliveryPlace);
        edtDebt = (EditText) findViewById(R.id.edtDebt);
        edtĐKKD = (EditText) findViewById(R.id.edtĐKKD);


        // Initiate Edit text Contact
        edtContactName = (EditText) findViewById(R.id.edtContactName);
        edtContactMobilePhone = (EditText) findViewById(R.id.edtContactMobilePhone);
        edtWorkPhone = (EditText) findViewById(R.id.edtContactWorkPhone);
        edtContactAdress = (EditText) findViewById(R.id.edtContactAdress);
        edtNickChat = (EditText) findViewById(R.id.edtContactNick);
        edtContactEmail = (EditText) findViewById(R.id.edtContactEmail);
        edtContactBirthday = (EditText) findViewById(R.id.edtBirthdayContact);
        edtContactBirthday.setEnabled(false);

        spnPosition = (Spinner) findViewById(R.id.spnPosition);
        spnGender = (Spinner) findViewById(R.id.spnGender);

        // Initiate Button
        btnBirthdayPicker = (ImageButton) findViewById(R.id.btnBirthdayPicker);
        btnBirthdayPickerContact = (ImageButton) findViewById(R.id.btnBirthdayPickerContact);
        btnAddCustomer = (Button) findViewById(R.id.btnAddCustomer);
        btnChosePl = (Button) findViewById(R.id.chosePl);
        final LinearLayout lnHint = (LinearLayout) findViewById(R.id.LnHint);
        final LinearLayout lnPl = (LinearLayout) findViewById(R.id.LnPl);

        btHienthi = (Button) findViewById(R.id.btHienthithem);
        btHienthi.setText("Hiển thị thêm >>");
        btHienthi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = 1;
                btHienthi.setVisibility(View.GONE);
                btThugon.setVisibility(View.VISIBLE);
                lnHint.setVisibility(View.VISIBLE);
                lnPl.setVisibility(View.VISIBLE);

            }
        });
        btThugon = (Button) findViewById(R.id.btThugon);
        btThugon.setText("Thu gọn <<");
        btThugon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btHienthi.setVisibility(View.VISIBLE);
                btThugon.setVisibility(View.GONE);
                lnHint.setVisibility(View.GONE);
                lnPl.setVisibility(View.GONE);
            }
        });

    }

    public Map<String, Object> POST_CREATECUSTOMER_HASHMAP() {

//        userId = link.getId();
        //        customer
        Map<String, String> jsCus = new HashMap<>();
        jsCus.put(getResources().getString(R.string.customer_name_db), edtCustomerName.getText().toString().trim());
        jsCus.put("OFFICE_ADDRESS", edtOfficeAddress.getText().toString().trim());
        jsCus.put(getResources().getString(R.string.customer_telephone_db), edtTelephone.getText().toString().trim());
        jsCus.put(getResources().getString(R.string.customer_founding_db), edtBirthday.getText().toString());
        jsCus.put(getResources().getString(R.string.customer_email_db), edtEmail.getText().toString().trim());
        jsCus.put(getResources().getString(R.string.customer_tax_code_db), edtTaxCode.getText().toString().trim());
        jsCus.put("USER_OWNER", "" + USER_OWNER);
        if (!edtDes.getText().toString().trim().equals("")) {
            jsCus.put("CUSTOMER_DESCRIPTION", edtDes.getText().toString());
        }
        if (!edtFax.getText().toString().trim().equals("")) {
            jsCus.put("FAX", edtFax.getText().toString());
        }
        if (!edtWeb.getText().toString().trim().equals("")) {
            jsCus.put("CUSTOMER_WEBSITE", edtWeb.getText().toString());
        }
        if (!edtBankAccount.getText().toString().trim().equals("")) {
            jsCus.put("BANK_ACCOUNT", edtBankAccount.getText().toString());
        }
        if (!edtBankLocation.getText().toString().trim().equals("")) {
            jsCus.put("BANK_LOCATION", edtBankLocation.getText().toString());
        }
        if (!edtDelivery.getText().toString().trim().equals("")) {
            jsCus.put("DELIVERY_PLACE", edtDelivery.getText().toString());
        }
        if (!edtĐKKD.getText().toString().trim().equals("")) {
            jsCus.put("CUSTOMER_ADDRESS", edtĐKKD.getText().toString());
        }
        if (!edtDebt.getText().toString().trim().equals("")) {
            jsCus.put("CUSTOMER_FIRST_DEBT", edtDebt.getText().toString());
        }
        if (!country.equals("")) {
            jsCus.put("GEOGRAPHY_COUNTRY", country);
        }
        if (!province.equals("")) {
            jsCus.put("GEOGRAPHY_PROVINCE", province);
        }
        if (!district.equals("")) {
            jsCus.put("GEOGRAPHY_DISTRICT", district);
        }
        if (!ward.equals("")) {
            jsCus.put("GEOGRAPHY_WARD", ward);
        }
        //contact
        Map<String, String> jsContact = new HashMap<>();
        jsContact.put(getResources().getString(R.string.contact_customer_full_name_db), edtContactName.getText().toString().trim());
        jsContact.put(getResources().getString(R.string.contact_customer_mobiphone_db), edtContactMobilePhone.getText().toString().trim());
        jsContact.put("CONTACT_WORKPHONE", edtWorkPhone.getText().toString().trim());
        jsContact.put(getResources().getString(R.string.contact_customer_email_db), edtContactEmail.getText().toString().trim());
        jsContact.put(getResources().getString(R.string.contact_customer_birthday_db), edtContactBirthday.getText().toString());
        jsContact.put("GENDER", String.valueOf(gd));

        if (!ChosePosition.equals(0)) {
            jsContact.put("POSITION_ID", ChosePosition);
        }
        jsContact.put("CONTACT_ADDRESS", edtContactAdress.getText().toString().trim());
        jsContact.put("CONTACT_CHAT", edtNickChat.getText().toString().trim());


        JSONArray arrayListPl = new JSONArray();

        for (int i = 0; i < ListPl.size(); i++) {
            Map<String, String> Pl = new HashMap<>();
            Pl.put("CUSTOMER_GROUP_ID", "" + ListPl.get(i).getCUSTOMER_GROUP_ID());
            //ep du lieu ve object
            JSONObject jsPl = new JSONObject(Pl);
            //ep vè array object ListPl
            arrayListPl.put(jsPl);
        }


        Map<String, Object> jsonParams = new HashMap<>();
        if (check == 0) {
            jsonParams.put("USER_ID", sharedPreferences.getString("userId"));
        } else {
            jsonParams.put("USER_ID", choose_Contract_USER);

        }
        if (bitmap != null & clear != 0) {
            String image = getStringImage(bitmap);
            jsonParams.put(KEY_IMAGE, image);
        }
        jsonParams.put("customer", jsCus);
        if(!edtContactName.getText().toString().trim().equals("")||!edtContactMobilePhone.getText().toString().trim().equals("")||!edtWorkPhone.getText().toString().trim().equals("")||
        !edtContactEmail.getText().toString().trim().equals("") ||!edtContactBirthday.getText().toString().equals("")||!ChosePosition.equals("0")||gd!=0||!edtContactAdress.getText().toString().trim().equals("")||
                !edtNickChat.getText().toString().trim().equals("")){
            jsonParams.put("contact", jsContact);
        }

        jsonParams.put("customer_group_customers", arrayListPl);

        return jsonParams;
    }

    public void doCreateCustomer() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Xin chờ ...");
        pDialog.setCancelable(true);
        pDialog.show();


        String url = link.post_CUSTOMERCREATION_link();
        Map<String, Object> jsonParams = POST_CREATECUSTOMER_HASHMAP();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("CUSTOMER_ADD", "Response: " + jsonObject);

                        try {

                            if (pDialog != null)
                                if (pDialog.isShowing())
                                    pDialog.dismiss();

                            if (jsonObject != null && !jsonObject.equals("null") && "success".equals(jsonObject.getString("messages"))) {
                                Toasty.success(Customer_Add_NewCustomer_Activity.this, "Thêm mới khách hàng thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (jsonObject != null && !jsonObject.equals("null") && jsonObject.getString("messages").equals("fails")) {

                                String details = "";

                                try {
                                    details = jsonObject.getString("details");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (details != null && !details.equals("null")) {
                                    Toasty.normal(Customer_Add_NewCustomer_Activity.this, details, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toasty.normal(Customer_Add_NewCustomer_Activity.this, getResources().getString(R.string.create_customer_fails), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("CUSTOMER_ADD", "Error: " + volleyError.toString());
                if (pDialog != null)
                    if (pDialog.isShowing())
                        pDialog.dismiss();

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

        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    // danh sách quốc gia
    public void SpCountry() {
        String Country[] = new String[countryList.size()];
        final String ID[] = new String[countryList.size()];
        for (int i = 0; i < countryList.size(); i++) {
            Country[i] = countryList.get(i).getGEOGRAPHY_NAME();
            ID[i] = countryList.get(i).getGEOGRAPHY_ID();
        }

        ArrayAdapter adapterContract_USER = new ArrayAdapter<>(this, R.layout.spinner_textview, Country);
        spCountry.setAdapter(adapterContract_USER);
        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!ID[position].equals("")) {
                    doGeography(ID[position]);
                }

                country = ID[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void doCountry() {

        String url = settings.getString("api_server", "") + "/api/v1/country_customer";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {
                    try {

                        JSONArray array = jsonObject.optJSONArray("country_customer");

                        for (int i = 0; i < array.length(); i++) {
                            GEOGRAPHY country = new GEOGRAPHY();
                            JSONObject object = array.optJSONObject(i);
                            country.setGEOGRAPHY_NAME(object.optString("GEOGRAPHY_NAME"));
                            country.setGEOGRAPHY_ID(object.optString("GEOGRAPHY_ID"));
                            countryList.add(country);


                        }
                        GEOGRAPHY first = new GEOGRAPHY();
                        first.setGEOGRAPHY_NAME("- - chưa chọn - -");
                        first.setGEOGRAPHY_ID("");
                        countryList.add(0, first);
                        SpCountry();
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

    // danh sách tỉnh/thành phố
    public void SpProvince() {
        String Province[] = new String[provinceList.size()];
        final String ID[] = new String[provinceList.size()];
        for (int i = 0; i < provinceList.size(); i++) {
            Province[i] = provinceList.get(i).getGEOGRAPHY_NAME();
            ID[i] = provinceList.get(i).getGEOGRAPHY_ID();
        }

        ArrayAdapter adapterContract_USER = new ArrayAdapter<>(this, R.layout.spinner_textview, Province);
        spProvince.setAdapter(adapterContract_USER);
        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                doGeography1(ID[position]);
                province = ID[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void doGeography(String GEOGRAPHY_ID) {
        provinceList.clear();
        String url = settings.getString("api_server", "") + "/api/v1/adress_customer?GEOGRAPHY_ID=" + GEOGRAPHY_ID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {
                    try {

                        JSONArray array = jsonObject.optJSONArray("adress_customer");
                        for (int i = 0; i < array.length(); i++) {
                            GEOGRAPHY province = new GEOGRAPHY();
                            JSONObject object = array.optJSONObject(i);
                            province.setGEOGRAPHY_NAME(object.optString("GEOGRAPHY_NAME"));
                            province.setGEOGRAPHY_ID(object.optString("GEOGRAPHY_ID"));
                            provinceList.add(province);


                        }
                        GEOGRAPHY first = new GEOGRAPHY();
                        first.setGEOGRAPHY_NAME("- - chưa chọn - -");
                        first.setGEOGRAPHY_ID("");
                        provinceList.add(0, first);
                        SpProvince();
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

    // danh sách huyện
    public void SpDistrict() {
        String District[] = new String[districtList.size()];
        final String ID[] = new String[districtList.size()];
        for (int i = 0; i < districtList.size(); i++) {
            District[i] = districtList.get(i).getGEOGRAPHY_NAME();
            ID[i] = districtList.get(i).getGEOGRAPHY_ID();
        }

        ArrayAdapter adapterContract_USER = new ArrayAdapter<>(this, R.layout.spinner_textview, District);
        spDistrict.setAdapter(adapterContract_USER);
        spDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                doGeography2(ID[position]);
                district = ID[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void doGeography1(String GEOGRAPHY_ID) {
        districtList.clear();
        String url = settings.getString("api_server", "") + "/api/v1/adress_customer?GEOGRAPHY_ID=" + GEOGRAPHY_ID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {
                    try {

                        JSONArray array = jsonObject.optJSONArray("adress_customer");
                        for (int i = 0; i < array.length(); i++) {
                            GEOGRAPHY district = new GEOGRAPHY();
                            JSONObject object = array.optJSONObject(i);
                            district.setGEOGRAPHY_NAME(object.optString("GEOGRAPHY_NAME"));
                            district.setGEOGRAPHY_ID(object.optString("GEOGRAPHY_ID"));
                            districtList.add(district);


                        }
                        GEOGRAPHY first = new GEOGRAPHY();
                        first.setGEOGRAPHY_NAME("- - chưa chọn - -");
                        first.setGEOGRAPHY_ID("");
                        districtList.add(0, first);
                        SpDistrict();
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


    //danh sách phường/xã

    public void SpWard() {
        String Ward[] = new String[wardList.size()];
        final String ID[] = new String[wardList.size()];
        for (int i = 0; i < wardList.size(); i++) {
            Ward[i] = wardList.get(i).getGEOGRAPHY_NAME();
            ID[i] = wardList.get(i).getGEOGRAPHY_ID();
        }

        ArrayAdapter adapterContract_USER = new ArrayAdapter<>(this, R.layout.spinner_textview, Ward);
        spWard.setAdapter(adapterContract_USER);
        spWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ward = ID[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void doGeography2(String GEOGRAPHY_ID) {
        wardList.clear();
        String url = settings.getString("api_server", "") + "/api/v1/adress_customer?GEOGRAPHY_ID=" + GEOGRAPHY_ID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {
                    try {

                        JSONArray array = jsonObject.optJSONArray("adress_customer");
                        for (int i = 0; i < array.length(); i++) {
                            GEOGRAPHY ward = new GEOGRAPHY();
                            JSONObject object = array.optJSONObject(i);
                            ward.setGEOGRAPHY_NAME(object.optString("GEOGRAPHY_NAME"));
                            ward.setGEOGRAPHY_ID(object.optString("GEOGRAPHY_ID"));
                            wardList.add(ward);


                        }
                        GEOGRAPHY first = new GEOGRAPHY();
                        first.setGEOGRAPHY_NAME("- - chưa chọn - -");
                        first.setGEOGRAPHY_ID("");
                        wardList.add(0, first);
                        SpWard();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            finish();
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

    public void getToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            getSupportActionBar().setSubtitle("Thêm mới khách hàng");
        }


    }


}
