package vn.altalab.app.crmvietpack.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
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
import java.util.Map;

import vn.altalab.app.crmvietpack.CrmMainActivity;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class Home_Fragment extends Fragment {
    public static int phone_view, email_view, user_id;
    private SharedPreferences settings;
    private static final String PREFS_NAME = "CRMVietPrefs";
    View view;
    ViewPager viewPager;
    TabLayout tabLayout;
    Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.home_activity, container, false);
            viewPager = (ViewPager) view.findViewById(R.id.viewPager);
            tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);


        }
        if (settings == null) {
            settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        }
        doGetFunctionUser();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext =context;

    }

    public void viewPager(){

        CrmMainActivity.toolbar.setTitle("Crm Viet");
        CrmMainActivity.toolbar.setSubtitle(getActivity().getResources().getString(R.string.home));

        Home_TabAdapterFragment_Custom fragmentAdapter = new Home_TabAdapterFragment_Custom(getChildFragmentManager(), mContext);
        fragmentAdapter.notifyDataSetChanged();

//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setAdapter(fragmentAdapter);
        viewPager.setOffscreenPageLimit(fragmentAdapter.getCount());

    }

    //check điều kiện cấp user có thể nhìn thấy sđt hoặc email của khách hàng cấp dưới

    public void doGetFunctionUser() {
        if(settings.getString(getResources().getString(R.string.user_id_object), "")!="") {
            user_id = Integer.parseInt(settings.getString(getResources().getString(R.string.user_id_object), ""));
            String url = settings.getString("api_server", "") + "/api/v1/customer/function?USER_ID=" + settings.getString(getResources().getString(R.string.user_id_object), "");

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
}
