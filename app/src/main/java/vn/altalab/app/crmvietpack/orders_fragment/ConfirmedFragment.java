package vn.altalab.app.crmvietpack.orders_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.object.Order;
import vn.altalab.app.crmvietpack.orders_fragment.order_detail.DetailOrderActivity;
import vn.altalab.app.crmvietpack.presenter.OderAdapter;
import vn.altalab.app.crmvietpack.utility.MySingleton;


public class ConfirmedFragment extends Fragment {
    private final static int REQUEST_CODE = 1;

    private PullToRefreshListView lstOrder;
    public static List<Order> orders;
    public static OderAdapter oderAdapter;

    private View mProgressView;
    private View mLayoutView;
    //    String potential ;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;
    private ProgressDialog progressDialog;
    View view;
    EditText searchdt;
    Button btSearch;
    TextView nodata;

    public ConfirmedFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_fragment_2, container, false);
        }
        if (getActivity() != null) {

            searchdt = (EditText) view.findViewById(R.id.edSearchOrderList);
            btSearch = (Button) view.findViewById(R.id.btSearchOrderList);


            if (settings == null) {
                settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
            }
            nodata = (TextView) view.findViewById(R.id.nodataTv);
            lstOrder = (PullToRefreshListView) view.findViewById(R.id.lstOder);


            orders = new ArrayList<>();

            doGetOrdersAPI(0);

            oderAdapter = new OderAdapter(getActivity(), R.layout.list_item_oder, orders);
            lstOrder.setAdapter(oderAdapter);

            lstOrder.setMode(PullToRefreshBase.Mode.BOTH);

            lstOrder.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                    if ("".equals(settings.getString("api_server", ""))) {
                        Toast.makeText(refreshView.getContext(), refreshView.getContext().getResources().getString(R.string.login_empty_api_server), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    orders.clear();
                    oderAdapter.notifyDataSetChanged();
                    doGetOrdersAPI(0);
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    if ("".equals(settings.getString("api_server", ""))) {
                        Toast.makeText(refreshView.getContext(), refreshView.getContext().getResources().getString(R.string.login_empty_api_server), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    doGetOrdersAPI(orders.size());
                }
            });


            lstOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), DetailOrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("order_id", orders.get(position - 1).getOderId());
                    bundle.putString("order_code", orders.get(position - 1).getOderCode());
                    bundle.putString("customer_name", orders.get(position - 1).getCustomerName());
                    bundle.putString("ndh", orders.get(position - 1).getDayOder());
                    bundle.putString("ngh", orders.get(position - 1).getDeliveryDate());
                    bundle.putInt("status", orders.get(position - 1).getStatus());
                    bundle.putString("nglh", orders.get(position - 1).getQuality());
                    bundle.putString("adress", orders.get(position - 1).getCustomerAdress());
                    bundle.putString("phone", orders.get(position - 1).getCustomerPhone());
                    bundle.putLong("customer_id", orders.get(position - 1).getCustomerId());
                    bundle.putString("money", orders.get(position - 1).getMoneyOder());
                    bundle.putLong("order_user", orders.get(position - 1).getOrderUser());
                    bundle.putString("order_user_name", orders.get(position - 1).getOrderUserName());
                    bundle.putString("description", orders.get(position - 1).getDescription());
                    bundle.putInt("position", position);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);

                }
            });
        }

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                doGetOrdersSearch(0);
            }
        });
        searchdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doGetOrdersSearch(0);
                    //tắt bàn phím sau khi click
                    final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getIntExtra("position", 0) != 0) {
            int position = data.getIntExtra("position", 0);
            orders.remove(position - 1);
            oderAdapter.notifyDataSetChanged();
        }
    }

    public static String KeyAccent(String s) {
        s = s.replace("d", "đ");
        s = s.replace("D", "Đ");
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }

    //Search
    public void doGetOrdersSearch(final int index) {
        if (index == 0) {
            orders.clear();
        }
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Đang tìm...");
        progressDialog.show();

        String url = settings.getString("api_server", "") + "/api/v1/orders/search?index=" + index +
                "&USER_ID=" + settings.getString(getResources().getString(R.string.user_id_object), "") + "&keyword=" +
                KeyAccent(searchdt.getText().toString());


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {


                    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    if (getActivity() != null) {
                        JSONArray array = jsonObject.optJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            final JSONObject object = array.optJSONObject(i);


                            Order order = new Order();
                            order.setStatus(object.optInt("STATUS"));
                            order.setCustomerName(object.optString("CUSTOMER_NAME"));
                            order.setCustomerId(object.optLong("CUSTOMER_ID"));
                            order.setOderCode(object.optString("ORDER_CODE"));
                            order.setOderId(object.optLong("ORDER_ID"));
                            order.setOrderUser(object.optLong("ORDER_USER"));
                            order.setOrderUserName(object.optString("ORDER_USER_NAME"));

                            String a = object.optString("ORDER_DATE");
                            if (a.equals(null) || a.equals("null") || a.equals("") || a.equals("0000-00-00 00:00:00")) {
                                order.setDayOder("");
                            } else {
                                String b[] = a.split("-");
                                String b2[] = b[2].split(" ");
                                String b3[] = b2[1].split(":");
                                order.setDayOder(b2[0] + "/" + b[1] + "/" + b[0] + " " + b3[0] + ":" + b3[1]);
                            }

                            String a1 = object.optString("DATE_DELIVERY");
                            if (a1.equals(null) || a1.equals("null") || a1.equals("") || a1.equals("0000-00-00 00:00:00")) {
                                order.setDeliveryDate("");
                            } else {
                                String b[] = a1.split("-");
                                String b2[] = b[2].split(" ");
                                String b3[] = b2[1].split(":");
                                order.setDeliveryDate(b2[0] + "/" + b[1] + "/" + b[0] + " " + b3[0] + ":" + b3[1]);
                            }

                            order.setQuality(object.optString("QUALITY"));
                            order.setCustomerPhone(object.optString("PERIOD"));
                            order.setCustomerAdress(object.optString("ADDRESS_DELIVERY"));
                            order.setMoneyOder(object.optString("TOTAL_AMOUNT"));
                            order.setDescription(object.optString("DESCRIPTION"));


                            orders.add(order);

                        }
                    }
                    if (orders.size() == 0) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Toasty.normal(getActivity(), "Đơn hàng không tồn tại !", Toast.LENGTH_LONG).show();


                    }
                    oderAdapter.notifyDataSetChanged();


                }
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (lstOrder.isRefreshing()) {
                    lstOrder.onRefreshComplete();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (lstOrder.isRefreshing()) {
                    lstOrder.onRefreshComplete();
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
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    public void doGetOrdersAPI(final int index) {
        if (index == 0) {
            orders.clear();
        }
        String url = settings.getString("api_server", "") + "/api/v1/orders?index=" + index +
                "&USER_ID=" + settings.getString(getResources().getString(R.string.user_id_object), "") + "&STATUS=2";


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {


                    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    if (getActivity() != null) {
                        JSONArray array = jsonObject.optJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            final JSONObject object = array.optJSONObject(i);


                            Order order = new Order();
                            order.setStatus(object.optInt("STATUS"));
                            order.setOderId(object.optLong("ORDER_ID"));
                            order.setCustomerName(object.optString("CUSTOMER_NAME"));
                            order.setCustomerId(object.optLong("CUSTOMER_ID"));
                            order.setOderCode(object.optString("ORDER_CODE"));
                            order.setOrderUser(object.optLong("ORDER_USER"));
                            order.setOrderUserName(object.optString("ORDER_USER_NAME"));

                            String a = object.optString("ORDER_DATE");
                            if (a.equals(null) || a.equals("null") || a.equals("") || a.equals("0000-00-00 00:00:00")) {
                                order.setDayOder("");
                            } else {
                                String b[] = a.split("-");
                                String b2[] = b[2].split(" ");
                                String b3[] = b2[1].split(":");
                                order.setDayOder(b2[0] + "/" + b[1] + "/" + b[0] + " " + b3[0] + ":" + b3[1]);
                            }

                            String a1 = object.optString("DATE_DELIVERY");
                            if (a1.equals(null) || a1.equals("null") || a1.equals("") || a1.equals("0000-00-00 00:00:00")) {
                                order.setDeliveryDate("");
                            } else {
                                String b[] = a1.split("-");
                                String b2[] = b[2].split(" ");
                                String b3[] = b2[1].split(":");
                                order.setDeliveryDate(b2[0] + "/" + b[1] + "/" + b[0] + " " + b3[0] + ":" + b3[1]);
                            }
                            order.setQuality(object.optString("QUALITY"));
                            order.setCustomerPhone(object.optString("PERIOD"));
                            order.setCustomerAdress(object.optString("ADDRESS_DELIVERY"));
                            order.setMoneyOder(object.optString("TOTAL_AMOUNT"));
                            order.setDescription(object.optString("DESCRIPTION"));


                            orders.add(order);

                        }

                        if (orders.size() == 0) {
                            nodata.setVisibility(View.VISIBLE);
                            lstOrder.setVisibility(View.GONE);
                        } else {
                            nodata.setVisibility(View.GONE);
                            lstOrder.setVisibility(View.VISIBLE);
                        }
                    }
                    oderAdapter.notifyDataSetChanged();


                }
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (lstOrder.isRefreshing()) {
                    lstOrder.onRefreshComplete();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (lstOrder.isRefreshing()) {
                    lstOrder.onRefreshComplete();
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
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }


}
