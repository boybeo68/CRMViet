package vn.altalab.app.crmvietpack.customer.customer_detail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vn.altalab.app.crmvietpack.Link;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.Shared_Preferences;
import vn.altalab.app.crmvietpack.customer.customer_detail.adapter.CustomerDetail_Contact_Listview_Adapter;
import vn.altalab.app.crmvietpack.customer.customer_detail.json.CustomerDetail_Contact_Json;
import vn.altalab.app.crmvietpack.customer.customer_detail.object.CustomerDetail_Contact_Listview_Setget;
import vn.altalab.app.crmvietpack.customer.edit_create_customer.Customer_Contact_Create_Activity;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class CustomerDetail_Contact_Fragment extends Fragment {

    Shared_Preferences sharedPreferences;

    private View view;
    private ListView listView = null;
    private FloatingActionButton floatingActionButton;
    private View footer;

    private CustomerDetail_Contact_Json customerDetailContactJson;
    private CustomerDetail_Contact_Listview_Adapter customerDetailContactListviewAdapter;
    private ArrayList<CustomerDetail_Contact_Listview_Setget> List;

    private int index = 0;
    private Boolean isLoading = false;
    public static int contactPhone, contactEmail;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private long customerId = 0;
    private String customer_name = "";
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.customerdetail_contact_fragment, container, false);
            footer = view.inflate(getActivity(), R.layout.home_listview_footer_loading_custom, null);
        }

        listView = (ListView) view.findViewById(R.id.listView);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingButton);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            Action();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2){
            List.clear();
            customerDetailContactListviewAdapter.notifyDataSetChanged();
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Loading ..");
            progressDialog.show();
            getDataVolley(0);
        }
    }

    public void Action() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
        }
        // cai dat sharedPreferences
        if (List == null)
            List = new ArrayList<>();

        // cai dat sharedPreferences
        if (index == 0)
            sharedPreferences = new Shared_Preferences(getActivity(), PREFS_NAME);

        // nhan Id
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            customerId = bundle.getLong("customer_id");
            customer_name = bundle.getString("CUSTOMER_NAME");
            contactEmail = bundle.getInt("checkEmail");
            contactPhone = bundle.getInt("checkPhone");
        }

        // load du lieu
        getDataVolley(0);

        // hanh dong button
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Customer_Contact_Create_Activity.class);
                intent.putExtra("customer_id", customerId);
                startActivityForResult(intent, 2);

            }

        });

    }

    public void getDataVolley(int index) {

        Link link = new Link(getActivity());
        String url = link.get_CUSTOMERDETAIL_Contact_Link(customerId, index);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("CUSTOMERDETAILCONT", "Response: " + jsonObject);
                if (jsonObject != null) {
                    setListView(jsonObject);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("CUSTOMERDETAILCONT", "Error: " + volleyError.toString());
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

    public void setListView(JSONObject jsonObject) {

        // lam moi mang
        listView.removeFooterView(footer);
        isLoading = false;

        customerDetailContactJson = new CustomerDetail_Contact_Json(jsonObject);

        if (customerDetailContactJson.getStatus() == true) {

            for (int i = 0; i < customerDetailContactJson.get_CustomerDetail_Contact_Json_List().size(); i++) {
                List.add(customerDetailContactJson.get_CustomerDetail_Contact_Json_List().get(i));
            }

            customerDetailContactListviewAdapter = new CustomerDetail_Contact_Listview_Adapter(getActivity(), R.layout.item_contact_cus, List, String.valueOf(jsonObject));
            customerDetailContactListviewAdapter.notifyDataSetChanged();
            listView.setAdapter(customerDetailContactListviewAdapter);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            listView.setSelection(index);
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                }

                @Override
                public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int last = firstVisibleItem + visibleItemCount;

                    if (List.size() != 0)
                        if (customerDetailContactJson.get_CustomerDetail_Contact_Json_List().size() != 0)
                            if (isLoading == false)
                                if (last == totalItemCount && totalItemCount != 0) {
                                    listView.addFooterView(footer);
                                    index = index + 10;
                                    isLoading = true;
                                    getDataVolley(index);

                                }

                }

            });

        }

    }

}
