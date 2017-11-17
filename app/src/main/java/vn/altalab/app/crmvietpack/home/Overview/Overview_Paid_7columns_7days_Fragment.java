package vn.altalab.app.crmvietpack.home.Overview;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class Overview_Paid_7columns_7days_Fragment extends Fragment {

    View view;
    ProgressBar pg1, pg2, pg3, pg4, pg5, pg6, pg7;

    TextView tvTien1, tvTien2, tvTien3, tvTien4;
    TextView tvNgay1, tvNgay2, tvNgay3, tvNgay4, tvNgay5, tvNgay6, tvNgay7;
    TextView tvTong;

    Shared_Preferences sharedPreferences;
    Overview_Paid_Json jsonTongquanView1;

    long tonglamtron;

    ProgressDialog pDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.home_overview_paid_7columns_7days_fragment, container, false);
        }

        if (getActivity() != null) {
            findViewID();
            Action();
        }

        return view;

    }

    public void findViewID(){

        pg1 = (ProgressBar) view.findViewById(R.id.progressBar1);
        pg2 = (ProgressBar) view.findViewById(R.id.progressBar2);
        pg3 = (ProgressBar) view.findViewById(R.id.progressBar3);
        pg4 = (ProgressBar) view.findViewById(R.id.progressBar4);
        pg5 = (ProgressBar) view.findViewById(R.id.progressBar5);
        pg6 = (ProgressBar) view.findViewById(R.id.progressBar6);
        pg7 = (ProgressBar) view.findViewById(R.id.progressBar7);

        tvTien1 = (TextView) view.findViewById(R.id.tvTien1);
        tvTien2 = (TextView) view.findViewById(R.id.tvTien2);
        tvTien3 = (TextView) view.findViewById(R.id.tvTien3);
        tvTien4 = (TextView) view.findViewById(R.id.tvTien4);

        tvNgay1 = (TextView) view.findViewById(R.id.tvNgay1);
        tvNgay2 = (TextView) view.findViewById(R.id.tvNgay2);
        tvNgay3 = (TextView) view.findViewById(R.id.tvNgay3);
        tvNgay4 = (TextView) view.findViewById(R.id.tvNgay4);
        tvNgay5 = (TextView) view.findViewById(R.id.tvNgay5);
        tvNgay6 = (TextView) view.findViewById(R.id.tvNgay6);
        tvNgay7 = (TextView) view.findViewById(R.id.tvNgay7);

        tvTong = (TextView) view.findViewById(R.id.tvtong);

    }

    public void Action(){
        if (getActivity() != null) {
            sharedPreferences = new Shared_Preferences(getActivity(), "tongquanview17day");
            if (!sharedPreferences.getString("response").equals(""))
                getDataAndSetData(sharedPreferences.getString("response"));
        }

        getDataVolley();
    }

    public void getDataVolley() {

        if (getActivity() != null) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());

            Link link = new Link(getActivity());
            String url = link.get_Home_Overview_Paid_Link(1);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (pDialog != null)
                                if (pDialog.isShowing())
                                    pDialog.dismiss();
                            Log.e("OverviewPaid", "Response7days: " + response);
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
                    Log.e("OverviewPaid", "Error7days: " + error);
                    if (pDialog != null)
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                }

            });
            queue.add(stringRequest);
        }
    }

    public void getDataAndSetData(String response){

        jsonTongquanView1 = new Overview_Paid_Json(response);

        if (jsonTongquanView1.getStatus() == true){
            sharedPreferences.putString("response", response);

        String strTong = "" + jsonTongquanView1.getTong();

        String strTongFormated = DecimalFormat.getInstance().format(jsonTongquanView1.getTong());

        tvTong.setText(strTongFormated);

        tonglamtron = xulyTong(strTong);

        String soTien = DecimalFormat.getInstance().format(tonglamtron/4);
        tvTien1.setText(soTien);

        soTien = DecimalFormat.getInstance().format(tonglamtron/2);
        tvTien2.setText(soTien);

        soTien = DecimalFormat.getInstance().format(tonglamtron*3/4);
        tvTien3.setText(soTien);

        soTien = DecimalFormat.getInstance().format(tonglamtron);
        tvTien4.setText(soTien);

        // ---------- get Date and set Date

        try {
            getDateAndsetDate();
        } catch (Exception e){
            e.printStackTrace();
        }
        }
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

    public void getDateAndsetDate(){

        tvNgay1.setText(getStringDate(jsonTongquanView1.getList().get(0).getPAYMENT_DATE()));
        tvNgay2.setText(getStringDate(jsonTongquanView1.getList().get(1).getPAYMENT_DATE()));
        tvNgay3.setText(getStringDate(jsonTongquanView1.getList().get(2).getPAYMENT_DATE()));
        tvNgay4.setText(getStringDate(jsonTongquanView1.getList().get(3).getPAYMENT_DATE()));
        tvNgay5.setText(getStringDate(jsonTongquanView1.getList().get(4).getPAYMENT_DATE()));
        tvNgay6.setText(getStringDate(jsonTongquanView1.getList().get(5).getPAYMENT_DATE()));
        tvNgay7.setText(getStringDate(jsonTongquanView1.getList().get(6).getPAYMENT_DATE()));

        pg1.setProgress(0);
        pg2.setProgress(0);
        pg3.setProgress(0);
        pg4.setProgress(0);
        pg5.setProgress(0);
        pg6.setProgress(0);
        pg7.setProgress(0);

        // phan tu 0

        long payment = Long.parseLong(jsonTongquanView1.getList().get(0).getTOTAL_MONEY());
        long percent = payment / 10 / tonglamtron;

        if (payment != 0 && percent == 0)
            pg1.setProgress(1);
        else pg1.setProgress((int) percent);

        // phan tu 1

        payment = Long.parseLong(jsonTongquanView1.getList().get(1).getTOTAL_MONEY());
        percent = payment / 10 / tonglamtron;

        if (payment != 0 && percent == 0)
            pg2.setProgress(1);
        else pg2.setProgress((int) percent);

        // phan tu 2

        payment = Long.parseLong(jsonTongquanView1.getList().get(2).getTOTAL_MONEY());
        percent = payment / 10 / tonglamtron;

        if (payment != 0 && percent == 0)
            pg3.setProgress(1);
        else pg3.setProgress((int) percent);

        // phan tu 3

        payment = Long.parseLong(jsonTongquanView1.getList().get(3).getTOTAL_MONEY());
        percent = payment / 10 / tonglamtron;

        if (payment != 0 && percent == 0)
            pg4.setProgress(1);
        else pg4.setProgress((int) percent);

        // phan tu 4

        payment = Long.parseLong(jsonTongquanView1.getList().get(4).getTOTAL_MONEY());
        percent = payment / 10 / tonglamtron;

        if (payment != 0 && percent == 0)
            pg5.setProgress(1);
        else pg5.setProgress((int) percent);

        // phan tu 5
        payment = Long.parseLong(jsonTongquanView1.getList().get(5).getTOTAL_MONEY());
        percent = payment / 10 / tonglamtron;

        if (payment != 0 && percent == 0)
            pg6.setProgress(1);
        else pg6.setProgress((int) percent);

        // phan tu 6
        payment = Long.parseLong(jsonTongquanView1.getList().get(6).getTOTAL_MONEY());
        percent = payment / 10 / tonglamtron;

        if (payment != 0 && percent == 0)
            pg7.setProgress(1);
        else pg7.setProgress((int) percent);

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
}
