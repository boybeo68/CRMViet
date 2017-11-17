package vn.altalab.app.crmvietpack.home.NeededDid;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class NeededDo_Fragment extends Fragment {
    View view;
    Shared_Preferences sharedPreferences;
    ListView listView;
    NeededDo_Json neededDoJson;
    static int index = 0;
    Boolean isLoading = false;
    ArrayList<NeededDo_Setget> LIST;
    View footer;
    TextView tvNODATA;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.home_neededdid_fragment, container, false);

        footer = view.inflate(getActivity(), R.layout.home_listview_footer_loading_custom, null);

        listView = (ListView) view.findViewById(R.id.listView);
        tvNODATA = (TextView) view.findViewById(R.id.tvNODATA);
        tvNODATA.setVisibility(View.INVISIBLE);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Action();
    }

    public void getDataVolley(int index) {

        Link link = new Link(getActivity());
        String url = link.get_Home_Needed_Link(index);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                tvNODATA.setVisibility(View.INVISIBLE);
                try {
                    if (jsonObject != null) {
                        Log.e("NEEDEDDID", "Response: " + jsonObject);
                        SetListView(String.valueOf(jsonObject));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                tvNODATA.setVisibility(View.VISIBLE);
                Log.e("NEEDEDDID", "Error: " + volleyError.getMessage());
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

        // Set timeout request to API
        request.setShouldCache(false);
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    public void Action() {

        LIST = new ArrayList<>();

        sharedPreferences = new Shared_Preferences(getActivity(), "NEEDEDDID");

        if (!sharedPreferences.getString("response").equals("")) {
            SetListView(sharedPreferences.getString("response"));
        }

        LIST = new ArrayList<>();

        getDataVolley(0);

    }

    public void SetListView(String data) {

        listView.removeFooterView(footer);
        isLoading = false;
        neededDoJson = new NeededDo_Json(data);

        if (neededDoJson.GET_STATUS() == true) {

            if (index == 0)
                sharedPreferences.putString("response", data);

            for (int i = 0; i < neededDoJson.GET_LIST().size(); i++) {
                LIST.add(neededDoJson.GET_LIST().get(i));
            }

            NeededDo_Baseadapter_Adapter neededDoBaseadapterCustom = new NeededDo_Baseadapter_Adapter(getActivity(), R.layout.home_neededdid_listview_custom, LIST, data);
            neededDoBaseadapterCustom.notifyDataSetChanged();
            listView.setAdapter(neededDoBaseadapterCustom);

            if (index == 0 || index == 5 || index == 10)
            listView.setSelection(0);
            else
            listView.setSelection(index);

            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {
                }

                @Override
                public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (LIST.size() != 0)
                        if (neededDoJson.GET_LIST().size() != 0)
                            if (isLoading == false) {
                                int last = firstVisibleItem + visibleItemCount;
                                if (last == totalItemCount && totalItemCount != 0) {
                                    listView.addFooterView(footer);
                                    index = index + 5;
                                    isLoading = true;
                                    getDataVolley(index);
                                }
                            }
                }
            });
        }
    }
}