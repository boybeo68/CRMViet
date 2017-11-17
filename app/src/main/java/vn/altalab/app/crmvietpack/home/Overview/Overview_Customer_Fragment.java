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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import vn.altalab.app.crmvietpack.CrmMainActivity;
import vn.altalab.app.crmvietpack.Link;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.Shared_Preferences;
import vn.altalab.app.crmvietpack.customer.Customer_MainFragment;
import vn.altalab.app.crmvietpack.home.Overview.Json.Overview_Customer_Json;

public class Overview_Customer_Fragment extends Fragment {

    View view;
    TextView tvMoi, tvTong, tvPhantram;
    Shared_Preferences sharedPreferences;

    ArrayList<String> listNgay;
    Spinner spinner;
    Link link;
    private static int type = 1;

    ProgressDialog pDialog;
    Button btView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.home_overview_customer_fragment, container, false);
        }

        findViewId();

        if (getActivity() != null) {
            Action();
        }

        return view;
    }

    public void findViewId() {
        tvMoi = (TextView) view.findViewById(R.id.tvMoi);
        tvTong = (TextView) view.findViewById(R.id.tvTong);
        tvPhantram = (TextView) view.findViewById(R.id.tvPhantram);
        spinner = (Spinner) view.findViewById(R.id.spinner_ngay);
        btView = (Button) view.findViewById(R.id.btView);
    }

    public void Action() {
        sharedPreferences = new Shared_Preferences(getActivity(), "OverviewCustomer");

        if (getActivity() != null) {
//            if (!sharedPreferences.getString("response").equals(""))
//                setText(sharedPreferences.getString("response"));
            getDataVolley(type);
        }

        getSpinner();

        btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CrmMainActivity.tvNotification.setText(getResources().getString(R.string.customer));
                CrmMainActivity.fragment = new Customer_MainFragment();
                CrmMainActivity.fragment.setArguments(getActivity().getIntent().getExtras());
//                CrmMainActivity.ft.replace(R.id.fragment_container, CrmMainActivity.fragment);
//                CrmMainActivity.ft.addToBackStack(null);
//                CrmMainActivity.ft.commit();
            }
        });
    }

    public void getDataVolley(int type) {

        Link link = new Link(getActivity());
        String url = link.get_Home_Overview_Customer_Link(type);

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (pDialog != null)
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                        Log.e("OverviewCustomer", "Response: " + response);
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
                Log.e("OverviewCustomer", "Error: " + error);
                if (pDialog != null)
                    if (pDialog.isShowing())
                        pDialog.dismiss();
            }
        });
        queue.add(stringRequest);
    }

    public void setText(String response) {

        Overview_Customer_Json overviewCustomerJson = new Overview_Customer_Json(response);

        if (overviewCustomerJson.getStatus() == true) {
            if (type == 1)
//            sharedPreferences.putString("response", response);
                tvMoi.setText("0");
            tvPhantram.setText("0");
            tvTong.setText("0");
            try {
                tvMoi.setText(overviewCustomerJson.getNhanvienmoi().toString());
                tvPhantram.setText("+" + round(Double.parseDouble(overviewCustomerJson.getPhantram().toString()), 2) + "%");
                tvTong.setText(overviewCustomerJson.getTongnhanvien().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static double round(double value, int places) {

        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;

    }

    public void getSpinner() {

        // set list Ngay
        listNgay = new ArrayList<>();

        listNgay.add(getResources().getString(R.string.ngay7));
        listNgay.add(getResources().getString(R.string.ngay30));
        listNgay.add(getResources().getString(R.string.ngay90));
        listNgay.add(getResources().getString(R.string.Thangnay));

        // new Link
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getActivity(), R.layout.home_overview_spinner_custom, listNgay);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                type = position + 1;
                getDataVolley(type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                getDataVolley(type);
            }
        });
    }
}
