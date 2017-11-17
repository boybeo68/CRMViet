package vn.altalab.app.crmvietpack.transaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
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

import org.json.JSONObject;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import vn.altalab.app.crmvietpack.Link;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.Shared_Preferences;
import vn.altalab.app.crmvietpack.customer.customer_detail.adapter.CustomerDetail_Transaction_Listview_Adapter;
import vn.altalab.app.crmvietpack.customer.customer_detail.json.CustomerDetail_Transaction_Json;
import vn.altalab.app.crmvietpack.customer.customer_detail.object.CustomerDetail_Transaction_Listview_Setget;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class Transaction_MainFragment extends Fragment {

    private static final String PREFS_NAME = "CRMVietPrefs";
    private Shared_Preferences sharedPreferences;
    private ProgressDialog pDialog;

    private ListView listView;

    private FloatingActionButton fab;
    View footer;

    EditText edSEARCH;
    Button btSEARCH;

    View view;
    Boolean isLoading = false;
    CustomerDetail_Transaction_Json customerDetailTransactionJson;
    public static CustomerDetail_Transaction_Listview_Adapter customerDetailTransactionListviewAdapter;

    ArrayList<CustomerDetail_Transaction_Listview_Setget> LIST;
    int index = 0;

    TextView tvNODATA;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view == null)
            view = inflater.inflate(R.layout.transaction_mainfragment, container, false);

        footer = view.inflate(getActivity(), R.layout.home_listview_footer_loading_custom, null);

        if (getActivity() != null)
            Action();

        return view;

    }

    public void findViewById() {

        listView = (ListView) view.findViewById(R.id.listView);

        edSEARCH = (EditText) view.findViewById(R.id.edSEARCH);

        btSEARCH = (Button) view.findViewById(R.id.btSEARCH);

        tvNODATA = (TextView) view.findViewById(R.id.tvNODATA);

        fab = (FloatingActionButton) view.findViewById(R.id.fabTransaction);

        tvNODATA.setVisibility(View.INVISIBLE);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srlLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.backgroundGradientEnd);
    }

    public void Action() {

        // set Id
        findViewById();

        // nhận Shared
        sharedPreferences = new Shared_Preferences(getActivity(), PREFS_NAME);

        // nhận Transactions

        LIST = new ArrayList<>();

        if (!sharedPreferences.getString("response").equals(""))
            setListView(sharedPreferences.getString("response"));

        LIST = new ArrayList<>();
        if (pDialog == null) {
            pDialog = new ProgressDialog(getActivity());
        }
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.setMessage("Loading..");
//        pDialog.show();
        // load Transactions
        doGetTransactionCustomer(0);

        // floatingActionButton
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TransactionCustomer_Create_Activity.class);
                startActivityForResult(intent, 0);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LIST = new ArrayList<>();
                index = 0;
                doGetTransactionCustomer(index);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        btSEARCH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LIST = new ArrayList<>();
                index = 0;
                if (!edSEARCH.getText().toString().equals("")) {
                    doMySearch(index);
                } else {
                    doGetTransactionCustomer(index);
                }
                //tắt bàn phím sau khi click
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        });

        edSEARCH.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    LIST = new ArrayList<>();
                    index = 0;
                    if (!edSEARCH.getText().toString().equals("")) {
                        doMySearch(index);
                    } else {
                        doGetTransactionCustomer(index);
                    }
                    //tắt bàn phím sau khi click
                    final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

    }

    //search
    private void doMySearch(int index) {


        // run Dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Xin chờ giây lát ...");
        pDialog.setCancelable(true);
        pDialog.show();

        // link
        String KEYWORD = edSEARCH.getText().toString();

        if (KEYWORD.contains(" ")) {
            KEYWORD = KEYWORD.replaceAll(" ", "%20");
        }

        KEYWORD = removeAccent(KEYWORD);

        Link link = new Link(getActivity());
        String url = link.GET_TRANSACTIONSEARCH_LINK(KEYWORD, index);
        Log.e("CUSTOMERALL", "SEARCH_URL: " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("CUSTOMERALL", "RESPONSE: " + jsonObject);

                tvNODATA.setVisibility(View.INVISIBLE);

                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                if (jsonObject != null) {
                    setListView(String.valueOf(jsonObject));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                tvNODATA.setVisibility(View.VISIBLE);
                Log.e("CUSTOMERALL", "ERROR: " + volleyError.getMessage());
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
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(request);
    }

    public void doGetTransactionCustomer(final int index) {

//        pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("Xin chờ giây lát ...");
//        pDialog.setCancelable(true);
        pDialog.show();
        LIST.clear();
        Link link = new Link(getActivity());
        String url = link.get_Transaction_Link(index);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                tvNODATA.setVisibility(View.INVISIBLE);
                Log.e("TRANSACTION", "Response: " + jsonObject);

                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }

                if (jsonObject != null)
                    setListView(String.valueOf(jsonObject));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                tvNODATA.setVisibility(View.VISIBLE);
                Log.e("TRANSACTION", "Error: " + volleyError.toString());
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
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(request);

    }

    public void setListView(String data) {

        listView.removeFooterView(footer);
        isLoading = false;

        customerDetailTransactionJson = new CustomerDetail_Transaction_Json(data);

        if (customerDetailTransactionJson.getStatus() == true) {

            if (index == 0) {
                sharedPreferences.putString("response", String.valueOf(data));
            }
//            LIST.clear();
            for (int i = 0; i < customerDetailTransactionJson.get_CustomerDetail_Transaction_Json_List().size(); i++) {

                LIST.add(customerDetailTransactionJson.get_CustomerDetail_Transaction_Json_List().get(i));
            }

            customerDetailTransactionListviewAdapter = new CustomerDetail_Transaction_Listview_Adapter(getActivity(), R.layout.customerdetail_transaction_listview_adapter, LIST);
            customerDetailTransactionListviewAdapter.notifyDataSetChanged();
            listView.setAdapter(customerDetailTransactionListviewAdapter);

                listView.setSelection(index );



            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                }

                @Override
                public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int last = firstVisibleItem + visibleItemCount;
                    if (LIST.size() != 0)
                        if (customerDetailTransactionJson.get_CustomerDetail_Transaction_Json_List().size() == 10)
                            if (isLoading == false)
                                if (last == totalItemCount && totalItemCount != 0) {

                                    listView.addFooterView(footer);
                                    index = index + 10;
                                    isLoading = true;
                                    doGetTransactionCustomer(index);

                                }

                }

            });

        } else {
            Toasty.normal(getActivity(), "Không tìm thấy giao dịch này !", Toast.LENGTH_LONG).show();
        }

    }

    public static String removeAccent(String s) {

        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1) {
            LIST = new ArrayList<>();
            doGetTransactionCustomer(0);
        }
    }
}
