package vn.altalab.app.crmvietpack.hanghoa;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.LinearLayout;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import vn.altalab.app.crmvietpack.CrmMainActivity;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.hanghoa.Adapter.HangHoaAdapter;
import vn.altalab.app.crmvietpack.hanghoa.object.Goods;
import vn.altalab.app.crmvietpack.utility.MySingleton;

/**
 * Created by boybe on 03/23/2017.
 */

public class GoodsFragment extends Fragment {
    private static final String PREFS_NAME = "CRMVietPrefs";
    public static List<Goods> dsgoods; // bên dưới xử lý
    public static HangHoaAdapter goodsAdapter;
    private PullToRefreshListView lvHangHoa; // trên giao diện
    private View mLayoutView;
    private Realm realm;
    private SharedPreferences settings;
    private ProgressDialog progressDialog;
    private Button btnTimkiem;
    private EditText edtTimkiem;
    private LinearLayout lnTimkiem;
//    public static Toolbar toolbar2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hanghoa, container, false);
        CrmMainActivity.toolbar.setSubtitle("Hàng hóa");
        if (settings == null) {
            settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        }

        if (realm == null) {
            realm = Realm.getDefaultInstance();
        }
        lnTimkiem = (LinearLayout) view.findViewById(R.id.lnTimkiem);
        if (dsgoods == null) {
            dsgoods = new ArrayList<>();
        }
        if (goodsAdapter == null) {
            goodsAdapter = new HangHoaAdapter(getActivity(), R.layout.new_list_hanghoa, dsgoods);
        }

        lvHangHoa = (PullToRefreshListView) view.findViewById(R.id.lstHangHoa);
        lvHangHoa.setAdapter(goodsAdapter);
        lvHangHoa.setMode(PullToRefreshBase.Mode.BOTH);
        lvHangHoa.getRefreshableView().setDividerHeight(1);
        // addControls Tìm kiếm
        btnTimkiem = (Button) view.findViewById(R.id.btnTimkiem);
        edtTimkiem = (EditText) view.findViewById(R.id.edtTimkiem);


        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
        }

        dogetGoodsAPI(0);

        lvHangHoa.setMode(PullToRefreshBase.Mode.BOTH);

        lvHangHoa.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                dogetGoodsAPI(0);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if ("".equals(settings.getString("api_server", ""))) {
                    Toast.makeText(refreshView.getContext(), refreshView.getContext().getResources().getString(R.string.login_empty_api_server), Toast.LENGTH_SHORT).show();
                    return;
                }
                dogetGoodsAPI(dsgoods.size());

            }
        });

        lvHangHoa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), Detail_product.class);
                Bundle bundle = new Bundle();
                bundle.putLong("PRODUCT_ID", dsgoods.get(position - 1).getProductId());
                bundle.putString("PRODUCT_NAME", dsgoods.get(position - 1).getProductName());
                bundle.putString("PRODUCT_MANUFACTORY", dsgoods.get(position - 1).getProductManufactory());
                bundle.putString("PRODUCT_PRICE", dsgoods.get(position - 1).getProductPrice());
                bundle.putString("PRODUCT_DESCRIPTION", dsgoods.get(position - 1).getProductDescription());
                bundle.putDouble("TAX", dsgoods.get(position - 1).getTax());
                bundle.putDouble("DISCOUNT", dsgoods.get(position - 1).getDiscount());
                bundle.putString("PRODUCT_CODE", dsgoods.get(position - 1).getProductCode());
                bundle.putString("LINK_IMAGE", dsgoods.get(position - 1).getImageUrl());
                intent.putExtras(bundle);
                startActivityForResult(intent,0);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabGoods);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Them_moi_hanghoa.class);
                startActivityForResult(intent, 0);
            }
        });
        btnTimkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doGetProductSearch(0);
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        });
        edtTimkiem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doGetProductSearch(0);
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

    public void dogetGoodsAPI(final int index) {
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        if (index == 0) {
            dsgoods.clear();
            goodsAdapter.notifyDataSetChanged();
        }
        String url = settings.getString("api_server", "") + "/api/v1/products?USER_ID=" +
                settings.getString(getResources().getString(R.string.user_id_object), "") +
                "&index=" + index;

        //JsonRequest(Method Post or Get -> Params -> listener )/ Error/ Header
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (jsonObject != null && !"".equals(jsonObject.toString())
                        && "success".equals(jsonObject.optString("messages"))) {
                    JSONArray array = jsonObject.optJSONArray("productsList");
                    if(array!=null){
                        for (int i = 0; i < array.length(); i++) {
                            final JSONObject object = array.optJSONObject(i);

                            Goods goods = new Goods();
                            goods.setProductId((int) object.optLong("PRODUCT_ID"));
                            goods.setProductId(object.optInt("PRODUCT_ID"));
                            goods.setProductName(object.optString("PRODUCT_NAME"));
                            goods.setProductDescription(object.optString("PRODUCT_DESCRIPTION"));
                            goods.setProductManufactory(object.optString("PRODUCT_MANUFACTORY"));
                            goods.setProductPrice(object.optString("PRODUCT_PRICE"));
                            goods.setTax(object.optDouble("TAX"));
                            goods.setDiscount(object.optDouble("DISCOUNT"));
                            goods.setProductCode(object.optString("PRODUCT_CODE"));


                            goods.setImageUrl(object.optString("LINK_IMAGE"));


                            dsgoods.add(goods);

                        }
                    }

                    goodsAdapter.notifyDataSetChanged();
                }
                if (lvHangHoa.isRefreshing()) {
                    lvHangHoa.onRefreshComplete();
                }
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (lvHangHoa.isRefreshing()) {
                    lvHangHoa.onRefreshComplete();
                }
                Log.e("getGood", volleyError.toString());
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

    public void doGetProductSearch(final int index) {
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        if (index == 0) {
            dsgoods.clear();
            goodsAdapter.notifyDataSetChanged();
        }

        String KEYWORD = edtTimkiem.getText().toString();
        if (KEYWORD.contains(" ")) {
            KEYWORD = KEYWORD.replaceAll(" ", "%20");
        }
        String url = settings.getString("api_server", "") + "/api/v1/products/search?USER_ID=" + settings.getString(getResources().getString(R.string.user_id_object), "")
                + "&keyword=" + KEYWORD + "&index=" + index;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {

                    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    JSONArray array = jsonObject.optJSONArray("productsList");
                    for (int i = 0; i < array.length(); i++) {
                        final JSONObject object = array.optJSONObject(i);
                        int productId = object.optInt("PRODUCT_ID");
                        Goods goods = new Goods();
                        goods.setProductId((int) object.optLong("PRODUCT_ID"));
                        goods.setProductId(productId);
                        goods.setProductName(object.optString("PRODUCT_NAME"));
                        goods.setProductDescription(object.optString("PRODUCT_DESCRIPTION"));
                        goods.setProductManufactory(object.optString("PRODUCT_MANUFACTORY"));
                        goods.setProductPrice(object.optString("PRODUCT_PRICE"));
                        goods.setTax(object.optDouble("TAX"));
                        goods.setDiscount(object.optDouble("DISCOUNT"));
                        goods.setProductCode(object.optString("PRODUCT_CODE"));
                        goods.setImageUrl(object.optString("LINK_IMAGE"));
                        dsgoods.add(goods);

                    }


                    goodsAdapter.notifyDataSetChanged();
                }else {
                    Toasty.normal(getContext(),"Sản phẩm không tồn tại !",Toast.LENGTH_LONG).show();
                }

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (lvHangHoa.isRefreshing()) {
                    lvHangHoa.onRefreshComplete();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (lvHangHoa.isRefreshing()) {
                    lvHangHoa.onRefreshComplete();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 0) {
            dogetGoodsAPI(0);
        }
    }
}
