package vn.altalab.app.crmvietpack.home.Deathline;

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
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.Link;
import vn.altalab.app.crmvietpack.Shared_Preferences;

import java.util.ArrayList;

public class Deathline_Fragment extends Fragment {

    View view;

    View footer;

    Shared_Preferences sharedPreferences;

    ArrayList<Deathline_Setget> list;

    ListView listView;

    Deathline_Json jsonDeathline;

    int index = 0;
    int type = 1;

    Boolean sharedPreBoo = false;
    Boolean isLoading = false;

    TextView tvNODATA;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.home_deathline_fragment, container, false);

            findViewById();

            if (getActivity() != null){
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

    public void findViewById(){

        listView = (ListView) view.findViewById(R.id.listView);

        tvNODATA = (TextView) view.findViewById(R.id.tvNODATA);
        tvNODATA.setVisibility(View.INVISIBLE);

    }

    public void Action(){

        list = new ArrayList<>();
        sharedPreferences = new Shared_Preferences(getActivity(), "deathlinefragment");
        if (!sharedPreferences.getString("response").equals("")){
            setListView(sharedPreferences.getString("response"));
            sharedPreBoo = true;
        }
        list = new ArrayList<>();
        getDataVolley(index);

    }

    public void getDataVolley(int index){

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        Link link = new Link(getActivity());

        String url = link.get_Home_Deathline_Link(index);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        tvNODATA.setVisibility(View.INVISIBLE);
                        Log.e("deathlinefragment", "responseVolleyQuahan: " + response);
                        try {

                            if (getActivity() != null){
                                setListView(response);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                tvNODATA.setVisibility(View.VISIBLE);
                Log.e("deathlinefragment", "error: " + error);
            }
        });

        queue.add(stringRequest);
    }

    public void setListView(String response){

        listView.removeFooterView(footer);

        isLoading = false;

        jsonDeathline = new Deathline_Json(response);

        if (jsonDeathline.getStatus() == true) {

            if (index == 0) {
                sharedPreferences.putString("response", response);
            }

            for (int i = 0; i < jsonDeathline.getList().size(); i++) {
                    list.add(jsonDeathline.getList().get(i));
            }

            Deathline_Baseadapter_Adapter customBaseadapterDeathline = new Deathline_Baseadapter_Adapter(getActivity(), R.layout.home_deathline_listview_custom, list, response);
            customBaseadapterDeathline.notifyDataSetChanged();
            listView.setAdapter(customBaseadapterDeathline);

            listView.setSelection(index);

            listView.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {
                }

                @Override
                public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int last = firstVisibleItem + visibleItemCount;
                    Log.e("deathlinefrag", "list.size(): " + list.size() + "-" + "jsonDeathline.getList().size(): " + jsonDeathline.getList().size() + "-" + isLoading);
                    if (list.size() != 0)
                        if (jsonDeathline.getList().size() != 0)
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
