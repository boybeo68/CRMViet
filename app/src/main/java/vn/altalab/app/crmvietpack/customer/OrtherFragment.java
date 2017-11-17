package vn.altalab.app.crmvietpack.customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
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
import vn.altalab.app.crmvietpack.customer.adapter.Customer_Adapter;
import vn.altalab.app.crmvietpack.customer.customer_detail.adapter.CustomerDetail_Contact_Listview_Adapter;
import vn.altalab.app.crmvietpack.customer.customer_detail.object.CustomerDetail_Contact_Listview_Setget;
import vn.altalab.app.crmvietpack.customer.json.Contact_Json;
import vn.altalab.app.crmvietpack.customer.json.CustomeOther_Json;
import vn.altalab.app.crmvietpack.customer.json.Customer_Json;
import vn.altalab.app.crmvietpack.customer.setget.Customer_Setget;
import vn.altalab.app.crmvietpack.utility.MySingleton;

/**
 * Created by Tung on 4/18/2017.
 */

public class OrtherFragment extends Fragment {
    public static ArrayList<Customer_Setget> list;

    private static final String PREFS_NAME = "CRMVietPrefs";
    private Shared_Preferences sharedPreferences;
    private ProgressDialog pDialog;
    View view;
    EditText edSearch;
    Button btSearch;
    public static ArrayList<Customer_Setget> ListOther;
    private ArrayList<CustomerDetail_Contact_Listview_Setget> List;
    ListView listView,lvLienHe,lvKhachHang;
    LinearLayout tablayout;
    TextView thongBao;
    Contact_Json contact_json;
    CustomeOther_Json customerOther_Json;
    Customer_Adapter customerAdapter;
    CustomerDetail_Contact_Listview_Adapter customerDetail_contact_listview_adapter;

    View footer;
    Boolean isLoading = false;
    private int index = 0;
    private int index2 = 0;
    Customer_Json customerJson;
    int max_item_in_page = 10;

    TextView tvNODATA;
    SwipeRefreshLayout swipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.customer_all_fragment, container, false);
        }

        footer = view.inflate(getActivity(), R.layout.home_listview_footer_loading_custom, null);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srlLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.backgroundGradientEnd);
        listView = (ListView) view.findViewById(R.id.listView);
        edSearch = (EditText) view.findViewById(R.id.edSearch);
        btSearch = (Button) view.findViewById(R.id.btSearch);
        tvNODATA = (TextView) view.findViewById(R.id.tvNODATA);
        tvNODATA.setVisibility(View.INVISIBLE);

        lvKhachHang= (ListView) view.findViewById(R.id.lvKhachHang);
        lvLienHe= (ListView) view.findViewById(R.id.lvLienHenContact);
        tablayout= (LinearLayout) view.findViewById(R.id.tablayout);
        thongBao= (TextView) view.findViewById(R.id.Thongbao);
        // Tab selector
        TabHost tabHost= (TabHost)view.findViewById(R.id.tabHost);//lấy tabHost Id
        tabHost.setup();
        TabHost.TabSpec tab1 =tabHost.newTabSpec("t1");
        tab1.setContent(R.id.tab1);
        tab1.setIndicator("Khách Hàng");
        tabHost.addTab(tab1);
        tabHost.setCurrentTab(0);


        TabHost.TabSpec tab2 =tabHost.newTabSpec("t2");
        tab2.setContent(R.id.tab2);
        tab2.setIndicator("Liên hệ");
        tabHost.addTab(tab2);

        if (getActivity() != null)
            Action();

        return view;
    }

    public void Action(){

        if (sharedPreferences == null) {
            sharedPreferences = new Shared_Preferences(getActivity(), PREFS_NAME + "CustomerSigned");
        }

        list = new ArrayList<>();
        ListOther=new ArrayList<>();
        List = new ArrayList<>();

//        if (!sharedPreferences.getString("response").equals("")){
//            setListView(sharedPreferences.getString("response"));
//        }

        list = new ArrayList<>();
        ListOther=new ArrayList<>();
        List = new ArrayList<>();

        loadList(0);
        // load mới lại danh sách khi kéo list danh sách từ trên xuống

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ListOther = new ArrayList<Customer_Setget>();
                list = new ArrayList<>();
                List = new ArrayList<>();
                thongBao.setVisibility(View.GONE);
                tablayout.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                index=0;
                loadList(index);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListOther = new ArrayList<Customer_Setget>();
                list = new ArrayList<>();
                List = new ArrayList<>();
                index = 0;
                index2 = 0;
                if (!edSearch.getText().toString().equals(""))

                    doMySearch(index, index2);

                else {
                    thongBao.setVisibility(View.GONE);
                    tablayout.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    loadList(index);

                }
                //tắt bàn phím sau khi click
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

            }

        });


        edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if( actionId == EditorInfo.IME_ACTION_SEARCH){
                    ListOther = new ArrayList<Customer_Setget>();
                    list = new ArrayList<>();
                    List = new ArrayList<>();
                    index = 0;
                    index2 = 0;
                    if (!edSearch.getText().toString().equals(""))

                        doMySearch(index, index2);

                    else {
                        thongBao.setVisibility(View.GONE);
                        tablayout.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        loadList(index);

                    }
                    //tắt bàn phím sau khi click
                    final InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

    }

    public void setListView(String data){

        listView.removeFooterView(footer);
        isLoading = false;

        customerJson = new Customer_Json(data);

        if (customerJson.getStatus() == true) {

            if (index == 0) {
                sharedPreferences.putString("response", String.valueOf(data));
            }

            for (int i = 0; i < customerJson.getList().size(); i++) {
                list.add(customerJson.getList().get(i));
            }

            Customer_Adapter customerAdapter = new Customer_Adapter(getActivity(), R.layout.customer_listview_adapter, list);
            customerAdapter.notifyDataSetChanged();
            listView.setAdapter(customerAdapter);

            listView.setSelection(index - 1);

            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {
                }
                @Override
                public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int last = firstVisibleItem + visibleItemCount;
                    Log.e("CUSTOMERSIGNED", "List.size(): " + list.size() + "-" + "customerJson.getList().size(): " + customerJson.getList().size() + "-" + isLoading);

                    if (list.size() != 0)
                        if (customerJson.getList().size() == max_item_in_page)
                            if (isLoading == false)
                                if (last == totalItemCount && totalItemCount != 0) {
                                    listView.addFooterView(footer);
                                    index = index + max_item_in_page;
                                    isLoading = true;
                                    loadList(index);
                                }

                }

            });

        }

    }

    public static String removeAccent(String s) {
//        s = s.replace("d","đ");
//        s = s.replace("D","Đ");
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }
    public void loadList(final int index) {

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Xin chờ giây lát ...");
        pDialog.setCancelable(true);
        pDialog.show();

        Link link = new Link(getActivity());
        String url = link.GET_CUSTOMER_LINK(index,"-1");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                tvNODATA.setVisibility(View.INVISIBLE);
                Log.e("CUSTOMERSIGNED", "Response: " + jsonObject);
                if (pDialog != null && pDialog.isShowing()){
                    pDialog.dismiss();
                }
                if (jsonObject != null){
                    setListView(String.valueOf(jsonObject));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                tvNODATA.setVisibility(View.VISIBLE);
                Log.e("CUSTOMERSIGNED", "Error: " + volleyError.toString());
                if (pDialog != null && pDialog.isShowing()){
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
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);

    }

    private void doMySearch(int index,int index2) {

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Xin chờ giây lát ...");
        pDialog.setCancelable(true);
        pDialog.show();

        Link link = new Link(getActivity());

        String KEYWORD = edSearch.getText().toString();

        if (KEYWORD.contains(" ")){
            KEYWORD = KEYWORD.replaceAll(" ", "%20");
        }


        String url = link.GET_SEARCH_LINK(removeAccent(KEYWORD),-1, index,index2);
        Log.e("CUSTOMERSIGNED", "URL: " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                tvNODATA.setVisibility(View.INVISIBLE);
                Log.e("CUSTOMERSIGNED", "Response: " + jsonObject);

                if (pDialog != null && pDialog.isShowing()){
                    pDialog.dismiss();
                }

                if (jsonObject != null){
//                    setListSearch(String.valueOf(jsonObject));
                    setListViewContact(jsonObject);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                tvNODATA.setVisibility(View.VISIBLE);

                Log.e("CUSTOMERSIGNED", "Error: " + volleyError.getMessage());

                if (pDialog != null && pDialog.isShowing()){
                    pDialog.dismiss();
                }

            }
        }){
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

    private void setListViewContact(JSONObject jsonObject) {

        // lam moi mang
//        listView.removeFooterView(footer);
//        isLoading = false;
        lvLienHe.removeFooterView(footer);
//        listView.removeFooterView(footer);
        isLoading = false;
        contact_json = new Contact_Json(jsonObject);
        customerJson = new Customer_Json(String.valueOf(jsonObject));
        customerOther_Json=new CustomeOther_Json(String.valueOf(jsonObject));

        if (contact_json.getStatus() == true && customerJson.getStatus() == true && customerOther_Json.getStatus()==true) {


            for (int i = 0; i < contact_json.get_CustomerDetail_Contact_Json_List().size(); i++) {
                List.add(contact_json.get_CustomerDetail_Contact_Json_List().get(i));
            }

            customerDetail_contact_listview_adapter = new CustomerDetail_Contact_Listview_Adapter(getActivity(), R.layout.customerdetail_contact_listview_adapter, List, String.valueOf(jsonObject));
            customerDetail_contact_listview_adapter.notifyDataSetChanged();
            lvLienHe.setAdapter(customerDetail_contact_listview_adapter);
            if (index2>= 1) {
                lvLienHe.setSelection(index2-1);
            };
            lvLienHe.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {
                }

                @Override
                public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int last2 = firstVisibleItem + visibleItemCount;
                    if (List.size() != 0)
                        if (contact_json.get_CustomerDetail_Contact_Json_List().size() == max_item_in_page)

                            if (isLoading == false)

                                if (last2 == totalItemCount && totalItemCount != 0) {

                                    lvLienHe.addFooterView(footer);
                                    index2 = index2 + max_item_in_page;
//                                    index=index+max_item_in_page;
                                    isLoading = true;
                                    doMySearch(index,index2);
                                }

                }

            });

            for (int j = 0; j < customerJson.getList().size(); j++) {
                list.add(customerJson.getList().get(j));
            }
            customerAdapter = new Customer_Adapter(getActivity(), R.layout.customer_listview_adapter, list);
            customerAdapter.notifyDataSetChanged();
            lvKhachHang.setAdapter(customerAdapter);
            for (int k = 0; k < customerOther_Json.getListOther().size(); k++) {
                ListOther.add(customerOther_Json.getListOther().get(k));
            }
            if (List.size()==0 &&ListOther.size()==0&&list.size()==0){
                thongBao.setVisibility(View.GONE);
                tablayout.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
                Toasty.normal(getActivity(), "Khách hàng và liên hệ không tồn tại", Toast.LENGTH_SHORT).show();
            }
            else if(List.size()==0 && ListOther.size()==0){
                int b = list.size();
                thongBao.setVisibility(View.GONE);
                tablayout.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                setListSearch(String.valueOf(jsonObject));
            }else if(List.size()==0&&ListOther.size()!=0){
                int a=ListOther.size();
                tablayout.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
                thongBao.setVisibility(View.VISIBLE);
                thongBao.setText("Đã tồn tại điện thoại: "+ListOther.get(0).getTELEPHONE()+"\nTrong khách hàng: "+ListOther.get(0).getCUSTOMER_NAME()+
                        "\nCủa chủ sở hữu: "+ListOther.get(0).getUSER_NAME());
            }
            else if(List.size()!=0){
                int c= list.size();
                thongBao.setVisibility(View.GONE);
                tablayout.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }

        }
        else {
            Toasty.normal(getActivity(),"Không tìm thấy khách hàng và liên hệ ",Toast.LENGTH_SHORT).show();
        }
    }

    public void setListSearch(String data){

        customerJson = new Customer_Json(data);
        if (customerJson.getStatus() == true) {


            Customer_Adapter customerAdapter = new Customer_Adapter(getActivity(), R.layout.customer_listview_adapter, list);
            customerAdapter.notifyDataSetChanged();
            listView.setAdapter(customerAdapter);

            if (index >= 1)
                listView.setSelection(index - 1);

            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {
                }

                @Override
                public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int last = firstVisibleItem + visibleItemCount;

                    Log.e("CUSTOMERSIGNED", "list.size(): " + list.size() + "-" + "customerJson.getList().size(): " + customerJson.getList().size() + "-" + isLoading);

                    if (list.size() != 0)
                        if (customerJson.getList().size() == max_item_in_page)
                            if (isLoading == false)
                                if (last == totalItemCount && totalItemCount != 0) {

                                    listView.addFooterView(footer);
                                    index = index + max_item_in_page;
                                    isLoading = true;
                                    loadList(index);

                                }

                }

            });

        }else {
            Toasty.normal(getActivity(), "Không tìm thấy khách hàng này !", Toast.LENGTH_LONG).show();
        }

    }
}
