package vn.altalab.app.crmvietpack.customer.customer_detail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

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
import vn.altalab.app.crmvietpack.transaction.TransactionCustomer_Create_Activity;
import vn.altalab.app.crmvietpack.customer.customer_detail.adapter.CustomerDetail_Transaction_Listview_Adapter;
import vn.altalab.app.crmvietpack.customer.customer_detail.json.CustomerDetail_Transaction_Json;
import vn.altalab.app.crmvietpack.customer.customer_detail.object.CustomerDetail_Transaction_Listview_Setget;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class CustomerDetail_Transaction_Fragment extends Fragment {

    private static final String PREFS_NAME = "CRMVietPrefs";
    private static final int RESULT_CODE_EDIT = 2;

    Shared_Preferences sharedPreferences;

    public static CustomerDetail_Transaction_Listview_Adapter customerDetailTransactionListviewAdapter;

    private View view, footer;
    TextView tvTransactionName;
    private ListView listView;

    private long customerId;
    private String customerName;
    private FloatingActionButton floatingActionButton;

    Boolean isLoading = false;
    private int index = 0;
    ProgressDialog progressDialog;

    ArrayList<CustomerDetail_Transaction_Listview_Setget> listTransaction;
    CustomerDetail_Transaction_Json customerDetailTransactionJson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.customerdetail_transaction_fragment, container, false);
        }
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
        }
        footer = view.inflate(getActivity(), R.layout.home_listview_footer_loading_custom, null);
        listView = (ListView) view.findViewById(R.id.listView);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingButton);
        tvTransactionName = (TextView) view.findViewById(R.id.tvTransactionName);

        if (getActivity() != null){
            Action();
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            listTransaction.clear();
            customerDetailTransactionListviewAdapter.notifyDataSetChanged();
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Loading..");
            progressDialog.show();
            get_DATA(0);
        }
    }

    public void Action(){

        listTransaction = new ArrayList<>();

        sharedPreferences = new Shared_Preferences(getActivity(), PREFS_NAME);

        // set customerId
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            customerId = bundle.getLong("customer_id");
            customerName = bundle.getString("customer_name");
        }

        // CustomerDetail_Activity.customer_Id;
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TransactionCustomer_Create_Activity.class);
                intent.putExtra("customer_id", customerId);
                intent.putExtra("customer_name", customerName);
                startActivityForResult(intent, 1);
            }
        });

        // set Listview
        get_DATA(index);
    }

    public void get_DATA(int index){

        Link link = new Link(getActivity());
        String url = link.get_CUSTOMERDETAIL_Transaction_Link(customerId, index);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject){
                Log.e("CUSTOMERTRANSACTION", "Response: " + jsonObject);
                if (jsonObject != null){
                    set_LISTVIEW(jsonObject);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("CUSTOMERTRANSACTION", "Error: " + volleyError.toString());
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

    public void set_LISTVIEW(JSONObject jsonObject){

        listView.removeFooterView(footer);
        isLoading = false;

        customerDetailTransactionJson = new CustomerDetail_Transaction_Json(String.valueOf(jsonObject));

        if (customerDetailTransactionJson.getStatus() == true) {

            for (int i = 0; i < customerDetailTransactionJson.get_CustomerDetail_Transaction_Json_List().size(); i++) {
                listTransaction.add(customerDetailTransactionJson.get_CustomerDetail_Transaction_Json_List().get(i));
            }

            customerDetailTransactionListviewAdapter = new CustomerDetail_Transaction_Listview_Adapter(getActivity(), R.layout.customerdetail_transaction_listview_adapter, listTransaction);
            customerDetailTransactionListviewAdapter.notifyDataSetChanged();
            listView.setAdapter(customerDetailTransactionListviewAdapter);
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

                    if (listTransaction.size() != 0)
                        if (customerDetailTransactionJson.get_CustomerDetail_Transaction_Json_List().size() == 10)
                            if (isLoading == false)
                                if (last == totalItemCount && totalItemCount != 0) {
                                    listView.addFooterView(footer);
                                    index = index + 10;
                                    isLoading = true;
                                    get_DATA(index);

                    }

                }

            });

        }

    }

}
