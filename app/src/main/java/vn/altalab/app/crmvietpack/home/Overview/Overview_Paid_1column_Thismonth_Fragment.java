package vn.altalab.app.crmvietpack.home.Overview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import vn.altalab.app.crmvietpack.Link;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.Shared_Preferences;
import vn.altalab.app.crmvietpack.home.Overview.Json.Overview_Paid_Json;

public class Overview_Paid_1column_Thismonth_Fragment extends Fragment {

    View view;

    TextView tvTien1, tvTien2, tvTien3, tvTien4;
    TextView tvNgay1;
    ProgressBar pg1;
    TextView tvTong;
    TextView tvDonvi;

    Overview_Paid_Json overviewPaidJson;
    LinearLayout llrow1;

    Shared_Preferences sharedPreferences;

    long tonglamtron;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.home_overview_paid_1column_thismonth_fragment, container, false);

        if (getActivity() != null) {

            findViewId();
            Action();

        }

        return view;
    }

    public void findViewId(){

        pg1 = (ProgressBar) view.findViewById(R.id.progressBar1);

        tvTien1 = (TextView) view.findViewById(R.id.tvTien1);
        tvTien2 = (TextView) view.findViewById(R.id.tvTien2);
        tvTien3 = (TextView) view.findViewById(R.id.tvTien3);
        tvTien4 = (TextView) view.findViewById(R.id.tvTien4);

        tvNgay1 = (TextView) view.findViewById(R.id.tvNgay1);
        tvTong = (TextView) view.findViewById(R.id.tvtong);
        tvDonvi = (TextView) view.findViewById(R.id.tvDonvi);
        llrow1 = (LinearLayout) view.findViewById(R.id.llrow1);

    }

    public void Action(){
        sharedPreferences = new Shared_Preferences(getActivity(), "overviewpaidthismonth");
        if (!sharedPreferences.getString("response").equals(""))
            getDataAndSetData(sharedPreferences.getString("response"));
        getDataVolley();
    }

    public void getDataVolley(){



        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Link link = new Link(getActivity());
        String url = link.get_Home_Overview_Paid_Link(4);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("OverviewPaid1column", "responseoverviewpaid1column: " + response);
                        try {
                            if (getActivity() != null)
                                getDataAndSetData(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("OverviewPaid1column", "error: " + error);
            }
        });
        queue.add(stringRequest);

    }


    public void getDataAndSetData(String response){

        overviewPaidJson = new Overview_Paid_Json(response);

        if (overviewPaidJson.getStatus() == true){

            sharedPreferences.putString("response", response);

            if (overviewPaidJson.getList().size() == 1) {

                tvDonvi.setText("("+ getResources().getString(R.string.unit)+" :1.000đ)");

                String strTong = "" + overviewPaidJson.getTong();

                String strTongFormated = DecimalFormat.getInstance().format(overviewPaidJson.getTong());

                tvTong.setText(strTongFormated);
                tonglamtron = xulyTong(strTong);
                String soTien = DecimalFormat.getInstance().format(tonglamtron / 4);
                tvTien1.setText(soTien);
                soTien = DecimalFormat.getInstance().format(tonglamtron / 2);
                tvTien2.setText(soTien);
                soTien = DecimalFormat.getInstance().format(tonglamtron * 3 / 4);
                tvTien3.setText(soTien);
                soTien = DecimalFormat.getInstance().format(tonglamtron);
                tvTien4.setText(soTien);
                // ---------- get Date and set Date

                try {
                    getDateAndsetDate();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                llrow1.setVisibility(View.INVISIBLE);
            }

        }

    }

    public void getDateAndsetDate(){

        tvNgay1.setText(getStringDate(overviewPaidJson.getList().get(0).getPAYMENT_DATE()));
        pg1.setProgress(0);

        // phan tu 0

        long payment = Long.parseLong(overviewPaidJson.getList().get(0).getTOTAL_MONEY());
        long percent = payment / 10 / tonglamtron;

        if (payment != 0 && percent == 0)
            pg1.setProgress(1);
        else pg1.setProgress((int) percent);

    }

    public String getStringDate(String response){

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(response);
            response = new SimpleDateFormat("dd/MM/yyyy").format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response != null && !response.equals("") && response.length() > 0){
            return response.substring(0,5);
        } else return "";

    }

    public Long xulyTong(String tong){

        String ketqua = "0";

        if (tong.length() == 1){

            String kytudau = String.valueOf(tong.charAt(0));
            if (Integer.valueOf(kytudau)%2 != 0) {
                ketqua = tong.replace(kytudau,  String.valueOf(Integer.valueOf(kytudau) + 1));
            } else {
                ketqua = tong.replace(kytudau, String.valueOf(Integer.valueOf(kytudau) + 2));
            }

        } else if (tong.length() > 1){

            String kytudau = String.valueOf(tong.charAt(0));

            if (!kytudau.equals("9") || !kytudau.equals("8")) {
                if (Integer.valueOf(kytudau) % 2 == 1) {
                    ketqua = "" + (Integer.valueOf(kytudau) + 1);
                } else {
                    ketqua = "" + (Integer.valueOf(kytudau) + 2);
                }
                for (int i = 1; i< tong.length(); i++){
                    ketqua = ketqua + "0";
                }
            } else {
                ketqua = "10";
                for (int i = 2; i< tong.length(); i++){
                    ketqua = ketqua + "0";
                }
            }
        }

        return Long.valueOf(ketqua);

    }

}
