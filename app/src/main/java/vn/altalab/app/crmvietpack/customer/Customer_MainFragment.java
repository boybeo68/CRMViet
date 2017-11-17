package vn.altalab.app.crmvietpack.customer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.customer.adapter.Customer_TabAdapterFragment_Adapter;
import vn.altalab.app.crmvietpack.customer.edit_create_customer.Customer_Add_NewCustomer_Activity;
import vn.altalab.app.crmvietpack.object.Customer;
import vn.altalab.app.crmvietpack.presenter.CustomerAdapter;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class Customer_MainFragment extends android.support.v4.app.Fragment {
    private SharedPreferences settings;
    private static final String PREFS_NAME = "CRMVietPrefs";
    public static int phone_view, email_view, user_id;
    public static List<Customer> customers;
    public static CustomerAdapter customerAdapter;
    Context mContext;
    // String potential;
    private TabLayout tabLayout;
    ViewPager viewPager;
    View view;
    private FloatingActionButton floatingActionButton;
    Customer_TabAdapterFragment_Adapter customerTabAdapterFragmentCustom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

    if (view == null) {
        view = inflater.inflate(R.layout.customer_mainfragment, container, false);
    }
        if (settings == null) {
            settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        }
        doGetFunctionUser();
    tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
    viewPager = (ViewPager) view.findViewById(R.id.viewPager);
    floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingButton);

    return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Action();
    }

    public void Action(){

        // tablayout and viewpager
        Customer_TabAdapterFragment_Adapter customerTabAdapterFragmentCustom = new Customer_TabAdapterFragment_Adapter(getChildFragmentManager(),mContext);
        customerTabAdapterFragmentCustom.notifyDataSetChanged();
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(customerTabAdapterFragmentCustom);
        viewPager.setOffscreenPageLimit(customerTabAdapterFragmentCustom.getCount());

        // nhận hành động floatingActionButton
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Customer_Add_NewCustomer_Activity.class);
                startActivity(intent);
            }

        });

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext =context;

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == 0 || resultCode == 1 || resultCode == 2){
//            customerTabAdapterFragmentCustom.notifyDataSetChanged();
//        }
    }
    //check điều kiện cấp user có thể nhìn thấy sđt hoặc email của khách hàng cấp dưới

    public void doGetFunctionUser() {

            user_id = Integer.parseInt(settings.getString(getResources().getString(R.string.user_id_object), ""));
            String url = settings.getString("api_server", "") + "/api/v1/customer/function?USER_ID=" + user_id;

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {
                        try {
                            JSONArray array = jsonObject.optJSONArray("phone_view");
                            phone_view = array.length();


                            JSONArray array1 = jsonObject.optJSONArray("email_view");
                            email_view = array1.length();


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
            MySingleton.getInstance(getActivity()).addToRequestQueue(request);

    }
}
