package vn.altalab.app.crmvietpack.home.Overview;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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
import vn.altalab.app.crmvietpack.home.Overview.Json.Overview_Job_Json;
import vn.altalab.app.crmvietpack.home.Overview.adapter.Overview_Job_Adapter;

public class Overview_Job_Fragment extends Fragment {

    ListView listView;

    View view;

    TextView tvTongsocongviec, tvDahoanthanh, tvQuahan;

    ImageView ivback, ivnext;

    Overview_Job_Adapter overviewJobCustom;

    Shared_Preferences sharedPreferences;
    Overview_Job_Json overviewJobJson;

    ProgressDialog pDialog;

    private int type = 1;
    private int index = 0;

    Spinner spinner;
    ArrayList<String> listNgay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.home_overview_job_fragment, container, false);
            findViewById();

            if (getActivity() != null){
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
        tvTongsocongviec = (TextView) view.findViewById(R.id.tvTongsocongviec);
        tvDahoanthanh = (TextView) view.findViewById(R.id.tvDahoanthanh);
        tvQuahan = (TextView) view.findViewById(R.id.tvQuahan);
        ivback = (ImageView) view.findViewById(R.id.ivback);
        ivnext = (ImageView) view.findViewById(R.id.ivnext);
        spinner = (Spinner) view.findViewById(R.id.spinner_ngay);
    }

    public void Action(){

        // Nhận cache

        if (getActivity() != null) {
            sharedPreferences = new Shared_Preferences(getActivity(), "overviewjobfragment");
            if (!sharedPreferences.getString("response").equals(""))
                setText(sharedPreferences.getString("response"));
        }

        // Nhận dữ liệu Volley

        getDataVolley(index, type);

        // Xử lý button

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index >= 5) {
                    index = index - 5;
                    getDataVolley(index, type);
                }
            }
        });

        ivnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (overviewJobJson.getListTongquan().size() == 5) {
                    index = index + 5;
                    getDataVolley(index, type);
                }
            }

        });
        // Nhận dữ liệu spinner

        try {
            getSpinner();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void getDataVolley(final int index, int type){

        if (index != 0) {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading ...");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        if (getActivity() != null) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());

            Link link = new Link(getActivity());
            String url = link.get_Home_Overview_Job_Link(index, type);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("OverviewJobFragment", "responseVolleyOverviewJob: " + response);
                            if (pDialog != null)
                                if (pDialog.isShowing())
                                    pDialog.dismiss();
                            try {
                                if (getActivity() != null)
                                    setText(response);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("OverviewJobFragment", "error: " + error);
                    if (pDialog != null)
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                }
            });
            queue.add(stringRequest);
        }
    }

    public void setText(String response){
        overviewJobJson = new Overview_Job_Json(response);

        if (overviewJobJson.getStatus() == true) {
            if (overviewJobJson.getListTongquan().size() <= 5 && overviewJobJson.getListTongquan().size() > 0) {
                if (index == 0 && type == 1)
                    sharedPreferences.putString("response", response);

                listView = (ListView) view.findViewById(R.id.listView);

                // set Text
                tvTongsocongviec.setText(overviewJobJson.getTotal());
                tvQuahan.setText(overviewJobJson.getTotalUnFinish());
                tvDahoanthanh.setText(overviewJobJson.getTotalFinish());

                // set Overview_Job_Adapter
                overviewJobCustom = new Overview_Job_Adapter(getActivity(), R.layout.home_overview_job_listview_custom, overviewJobJson.getListTongquan());
                overviewJobCustom.notifyDataSetChanged();
                listView.setAdapter(overviewJobCustom);

            } else {
                index = index - 5;
                return;
            }
        }
    }

    public void getSpinner(){

        // set listNgay
        listNgay = new ArrayList<>();
        listNgay.add(getResources().getString(R.string.ngay7));
        listNgay.add(getResources().getString(R.string.ngay30));
        listNgay.add(getResources().getString(R.string.ngay90));
        listNgay.add(getResources().getString(R.string.Thangnay));

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getActivity(), R.layout.home_overview_spinner_custom, listNgay);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                type = position + 1;
                getDataVolley(index, type);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                getDataVolley(index, 1);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pDialog != null)
            if (pDialog.isShowing())
                pDialog.dismiss();
    }
}
