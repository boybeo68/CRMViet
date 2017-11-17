package vn.altalab.app.crmvietpack.home.Consigned;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import vn.altalab.app.crmvietpack.Link;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.Shared_Preferences;

public class Consigned_Fragment extends Fragment {

    Shared_Preferences sharedPreferences;
    ArrayList list;
    View view;
    ListView listView;

    Consigned_Json consignedJson;
    private int index = 0;
    Boolean isLoading = false;
    View footer;

    TextView tvNODATA;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    if (view == null) {
        view = inflater.inflate(R.layout.home_consigned_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        tvNODATA = (TextView) view.findViewById(R.id.tvNODATA);
        tvNODATA.setVisibility(View.INVISIBLE);
        if (getActivity() != null) {
            footer = view.inflate(getActivity(), R.layout.home_listview_footer_loading_custom, null);
            try {
                Action();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    return view;
    }

    public void Action(){

        list = new ArrayList<>();

        sharedPreferences = new Shared_Preferences(getActivity(), "consignedfragment");
        if (!sharedPreferences.getString("response").equals("")){
            setListView(sharedPreferences.getString("response"));
        }

        list = new ArrayList<>();

        getDataVolley(index);

    }

    public void getDataVolley(int index){
        // Run Volley

        Link link = new Link(getActivity());
        String url = link.get_Home_Consigned_Link(index);

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            tvNODATA.setVisibility(View.INVISIBLE);

                            Log.e("consignedfragment", "responseVolleyConsigned: " + response);

                            try {
                                setListView(response);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    tvNODATA.setVisibility(View.VISIBLE);
                    Log.e("consignedfragment", "error: " + error);
                }
            });

        queue.add(stringRequest);

    }

    public void setListView(String response){

        if (footer != null)
            listView.removeFooterView(footer);

            isLoading = false;
            consignedJson = new Consigned_Json(response);

        if (consignedJson.getStatus() == true) {

            if (index == 0)
                sharedPreferences.putString("response", response);

            for (int i = 0; i < consignedJson.getList().size(); i++) {
                list.add(consignedJson.getList().get(i));
            }

            Log.e("consignedfragment", "list.size(): " + list.size());

            Consigned_Baseadapter_Adapter consignedBaseadapterCustom = new Consigned_Baseadapter_Adapter(getActivity(), R.layout.home_consigned_listview_custom, list);
            consignedBaseadapterCustom.notifyDataSetChanged();
            listView.setAdapter(consignedBaseadapterCustom);
            listView.setSelection(index);

            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {
                }
                @Override
                public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int last = firstVisibleItem + visibleItemCount;

                    if (list.size() != 0)
                        if (consignedJson.getList().size() != 0)
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
