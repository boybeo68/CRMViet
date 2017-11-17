package vn.altalab.app.crmvietpack.warehouse;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import vn.altalab.app.crmvietpack.Link;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.Shared_Preferences;
import vn.altalab.app.crmvietpack.warehouse.adapter.Product_Listview_Adapter;
import vn.altalab.app.crmvietpack.warehouse.json.Warehouse_Json;
import vn.altalab.app.crmvietpack.warehouse.object.Product_Setget;

public class Warehouse_Fragment extends Fragment {

    View view;
    PullToRefreshListView listView;
    Spinner spinner;
    Button ibSearch;
    EditText etIdNameCustomer;
    private  static String id = "0";
    private String keyword = "";
    Shared_Preferences sharedPreferences;

    ProgressDialog pDialog;
    ArrayList<Product_Setget> listProducts;
    Product_Listview_Adapter productListviewAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.warehouse_fragment, container, false);
        }

        listView = (PullToRefreshListView) view.findViewById(R.id.listView);


        spinner = (Spinner) view.findViewById(R.id.spWareName);
        ibSearch = (Button) view.findViewById(R.id.ibSearch);
        etIdNameCustomer = (EditText) view.findViewById(R.id.edIdNameCustomer);

        if (getActivity() != null) {
            Action();
        }

        return view;

    }

    public void Action() {
        // load dữ liệu spinner
        get_SpinnerData_Volley();

        listProducts = new ArrayList<>();
        productListviewAdapter = new Product_Listview_Adapter(getActivity(), R.layout.warehouse_listview_adapter, listProducts);
        listView.setAdapter(productListviewAdapter);

        // load dữ liệu tìm kiếm sản phẩm
        post_FindedProductData_Volley(id, keyword, 0);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                listProducts.clear();
                productListviewAdapter.notifyDataSetChanged();
                post_FindedProductData_Volley(id, keyword, 0);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                post_FindedProductData_Volley(id, keyword, listProducts.size());

            }
        });


        // xử lý button tìm kiếm

        ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyword = etIdNameCustomer.getText().toString();

                    listProducts.clear();
                    productListviewAdapter.notifyDataSetChanged();

                post_FindedProductData_Volley(id, keyword, 0);
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }

        });
        etIdNameCustomer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    keyword = etIdNameCustomer.getText().toString();
                    listProducts.clear();
                    productListviewAdapter.notifyDataSetChanged();
                    post_FindedProductData_Volley(id, keyword, 0);
                    //tắt bàn phím sau khi click
                    final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

    public void get_SpinnerData_Volley() {


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Link link = new Link(getActivity());
        String url = link.get_Warehouse_Link();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("WarehouseFragment", "response: " + response);
                        try {
                            if (getActivity() != null)
                                setSpinner(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("WarehouseFragment", "error: " + error);

            }
        });
        queue.add(stringRequest);
    }

    public void post_FindedProductData_Volley(final String id, final String keyword, final int index1) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Xin chờ ...");
        pDialog.setCancelable(true);
        pDialog.show();


        final Link link = new Link(getActivity());

        String url = link.post_WAREHOUSEPRODUCT_link();

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("WarehouseFragment", "responseFindedProduct: " + response);

                        try {

                            try {
                                JSONObject jsonObject, jsonObject1;
                                JSONArray jsonArray;
                                jsonObject = new JSONObject(response);

                                if (jsonObject.getString("messages").equals("success")) {


                                    jsonArray = jsonObject.getJSONArray("productsList");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        jsonObject1 = (JSONObject) jsonArray.get(i);

                                        Product_Setget productSetget = new Product_Setget();

                                        productSetget.setPRODUCT_CODE(jsonObject1.getString("PRODUCT_CODE"));
                                        productSetget.setPRODUCT_NAME(jsonObject1.getString("PRODUCT_NAME"));
                                        productSetget.setINVENTORY(jsonObject1.getString("INVENTORY"));
                                        productSetget.setWAREHOUSE_NAME(jsonObject1.getString("WAREHOUSE_NAME"));
                                        productSetget.setLINK_IMAGE(jsonObject1.getString("LINK_IMAGE"));

                                        listProducts.add(productSetget);

                                    }
                                    if (listProducts.size() == 0) {
                                        Toasty.normal(getActivity(), "Hàng hóa không tồn tại trong kho !", Toast.LENGTH_LONG).show();
                                    }
                                    productListviewAdapter.notifyDataSetChanged();
                                    if (pDialog != null) {
                                        if (pDialog.isShowing())
                                            pDialog.dismiss();
                                    }
                                    if (listView.isRefreshing()) {
                                        listView.onRefreshComplete();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("WarehouseFragment", "error: " + error);
                if (pDialog != null)
                    if (pDialog.isShowing())
                        pDialog.dismiss();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<>();
                MyData.put("USER_ID", link.getId());
                MyData.put("keyword", keyword);
                MyData.put("WAREHOUSE_ID", id);
                MyData.put("index", String.valueOf(index1));
                Log.e("WarehouseFragment", "USER_ID: " + link.getId());
                return MyData;
            }
        };

        queue.add(stringRequest);
    }


    public void setSpinner(String response) {

        Warehouse_Json warehouseJson = new Warehouse_Json(response);

        if (warehouseJson.getStatus() == true) {

            final ArrayList<String> listNameSpinner = new ArrayList<>();
            final ArrayList<String> listIdSpinner = new ArrayList<>();
            listNameSpinner.add(0, "Tất cả");
            for (int i = 0; i < warehouseJson.get_Warehouse_List().size(); i++) {
                listNameSpinner.add(i + 1, warehouseJson.get_Warehouse_List().get(i).getWAREHOUSE_NAME());
            }
            listIdSpinner.add(0, "0");
            for (int i = 0; i < warehouseJson.get_Warehouse_List().size(); i++) {
                listIdSpinner.add(i + 1, warehouseJson.get_Warehouse_List().get(i).getWAREHOUSE_ID());
            }


            ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getActivity(), R.layout.home_overview_spinner_custom, listNameSpinner);
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    id = listIdSpinner.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    id = listIdSpinner.get(0);
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pDialog != null)
            if (pDialog.isShowing())
                pDialog.dismiss();
    }
}
