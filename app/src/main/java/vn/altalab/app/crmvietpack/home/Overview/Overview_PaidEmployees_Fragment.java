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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DecimalFormat;
import java.util.ArrayList;

import vn.altalab.app.crmvietpack.Link;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.Shared_Preferences;
import vn.altalab.app.crmvietpack.home.Overview.Json.Overview_PaidCustomer_Json;

public class Overview_PaidEmployees_Fragment extends Fragment implements View.OnClickListener {

    TextView tvTen1, tvTen2, tvTen3, tvTen4, tvTen5, tvTien1, tvTien2, tvTien3, tvTien4;
    ProgressBar pg1, pg2, pg3, pg4, pg5;
    ImageView ivBack, ivNext;
    TextView tvTitle;
    private Button btPayRevenue;
    View view;
    Shared_Preferences sharedPreferences;
    ProgressDialog pDialog;
    Spinner spinner;

    private int index = 0;

    Overview_PaidCustomer_Json overviewPaidCustomerJson;
    ArrayList<String> listNgay;
    private Boolean PaidEmployee = false;
    private int type = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null)
            view = inflater.inflate(R.layout.home_overview_paid_customer_fragment, container, false);

        findViewById();

        if (getActivity() != null)
            try {
                Action();
            } catch (Exception e) {
                e.printStackTrace();
            }

        return view;
    }

    public void findViewById(){

        ivBack = (ImageView) view.findViewById(R.id.ivback);
        ivNext = (ImageView) view.findViewById(R.id.ivnext);

        tvTen1 = (TextView) view.findViewById(R.id.tvTen1);
        tvTen2 = (TextView) view.findViewById(R.id.tvTen2);
        tvTen3 = (TextView) view.findViewById(R.id.tvTen3);
        tvTen4 = (TextView) view.findViewById(R.id.tvTen4);
        tvTen5 = (TextView) view.findViewById(R.id.tvTen5);

        pg1 = (ProgressBar) view.findViewById(R.id.pg1);
        pg2 = (ProgressBar) view.findViewById(R.id.pg2);
        pg3 = (ProgressBar) view.findViewById(R.id.pg3);
        pg4 = (ProgressBar) view.findViewById(R.id.pg4);
        pg5 = (ProgressBar) view.findViewById(R.id.pg5);

        tvTien1 =(TextView) view.findViewById(R.id.tvTien1);
        tvTien2 =(TextView) view.findViewById(R.id.tvTien2);
        tvTien3 =(TextView) view.findViewById(R.id.tvTien3);
        tvTien4 =(TextView) view.findViewById(R.id.tvTien4);

        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        spinner = (Spinner) view.findViewById(R.id.spinner_ngay);
        btPayRevenue = (Button) view.findViewById(R.id.btPayRevenue);
    }

    public void Action(){
        // Nhận lưu trữ và xử lý lưu trữ

        if (getActivity() != null) {
            sharedPreferences = new Shared_Preferences(getActivity(), "overviewpaidcustomer");
        }

        if (!sharedPreferences.getString("response").equals(""))
            try {
                setText(sharedPreferences.getString("response"));
            } catch (Exception e) {
                e.printStackTrace();
            }

        getSpinner();

        // Nhận dữ liệu Volley

        try {
            getDataVolley(index, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Xử lý Button

        ivBack.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        btPayRevenue.setOnClickListener(this);
    }

    public void getDataVolley(final int index, final int type){

        if (index != 0) {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading ...");
            pDialog.setCancelable(true);
            pDialog.show();
        }

            RequestQueue queue = Volley.newRequestQueue(getActivity());

            Link link = new Link(getActivity());
            String url = link.get_Home_Overview_PaidEmployees_Link(index, type);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("OverviewPaidCustomer", "responseOverviewPaidCustomer: " + response);
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
                    Log.e("ErOverviewPaidCustomer", "errorOverviewPaidCustomer: " + error);
                    if (pDialog != null)
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                }
            });
            queue.add(stringRequest);

    }

    public void setText(String response){
        overviewPaidCustomerJson = new Overview_PaidCustomer_Json(response);
        if (overviewPaidCustomerJson.getStatus() == true)
            if (overviewPaidCustomerJson.getListTongquan().size() <= 5 && overviewPaidCustomerJson.getListTongquan().size() > 0) {

                if (index == 0 && type == 1) sharedPreferences.putString("response", response);

                tvTen1.setText("");
                tvTen2.setText("");
                tvTen3.setText("");
                tvTen4.setText("");
                tvTen5.setText("");

                pg1.setProgress(0);
                pg2.setProgress(0);
                pg3.setProgress(0);
                pg4.setProgress(0);
                pg5.setProgress(0);

                try {
                    tvTen1.setText(overviewPaidCustomerJson.getListTongquan().get(0).getUSER_FULL_NAME());
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    tvTen2.setText(overviewPaidCustomerJson.getListTongquan().get(1).getUSER_FULL_NAME());
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    tvTen3.setText(overviewPaidCustomerJson.getListTongquan().get(2).getUSER_FULL_NAME());
                } catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    tvTen4.setText(overviewPaidCustomerJson.getListTongquan().get(3).getUSER_FULL_NAME());
                } catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    tvTen5.setText(overviewPaidCustomerJson.getListTongquan().get(4).getUSER_FULL_NAME());
                } catch (Exception e){
                    e.printStackTrace();
                }

                if (PaidEmployee == false) {
                    tvTitle.setText(getResources().getString(R.string.Thanhtoannhanvien));
                    btPayRevenue.setText(getResources().getString(R.string.Thanhtoan));

                    String strTong = "" + overviewPaidCustomerJson.getMaxPayment();
                    long a = xulyTong(strTong);

                    String soTien = DecimalFormat.getInstance().format(a / 4);
                    tvTien1.setText(soTien);

                    soTien = DecimalFormat.getInstance().format(a / 2);
                    tvTien2.setText(soTien);

                    soTien = DecimalFormat.getInstance().format(a * 3 / 4);
                    tvTien3.setText(soTien);

                    soTien = DecimalFormat.getInstance().format(a);
                    tvTien4.setText(soTien);

//                    for (int i = 0; i < overviewPaidCustomerJson.getListTongquan().size(); i++) {
////                        Log.e("parselong", "long.parseLong: " + Long.parseLong(overviewPaidCustomerJson.getListTongquan().get(i).getPAYMENT()));
////                        Log.e("parselong", "long.parseLong: " + a);
//                    }

                    long payment = Long.parseLong(overviewPaidCustomerJson.getListTongquan().get(0).getPAYMENT());
                    long percent = payment / 10 / a;

                    if (payment != 0 && percent == 0)
                        pg1.setProgress(1);
                    else try {
                        pg1.setProgress((int) percent);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    payment = Long.parseLong(overviewPaidCustomerJson.getListTongquan().get(1).getPAYMENT());
                    percent = payment / 10 / a;

                    if (payment != 0 && percent == 0)
                        pg2.setProgress(1);
                    else  try {
                        pg2.setProgress((int) percent);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    payment = Long.parseLong(overviewPaidCustomerJson.getListTongquan().get(2).getPAYMENT());
                    percent = payment / 10 / a;

                    if (payment != 0 && percent == 0)
                        pg3.setProgress(1);
                    else try {
                        pg3.setProgress((int) percent);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    payment = Long.parseLong(overviewPaidCustomerJson.getListTongquan().get(3).getPAYMENT());
                    percent = payment / 10 / a;

                    if (payment != 0 && percent == 0)
                        pg4.setProgress(1);
                    else  try {
                        pg4.setProgress((int) percent);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    payment = Long.parseLong(overviewPaidCustomerJson.getListTongquan().get(4).getPAYMENT());
                    percent = payment / 10 / a;

                    if (payment != 0 && percent == 0)
                        pg5.setProgress(1);
                    else  try {
                        pg5.setProgress((int) percent);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                } else {
                    tvTitle.setText(getResources().getString(R.string.Doanhthunhanvien));
                    btPayRevenue.setText(getResources().getString(R.string.Doanhthu));

                    String strTong = "" + overviewPaidCustomerJson.getMaxRevenue();
                    Long a = xulyTong(strTong);

                    String soTien = DecimalFormat.getInstance().format(a / 4);
                    tvTien1.setText(soTien);

                    soTien = DecimalFormat.getInstance().format(a / 2);
                    tvTien2.setText(soTien);

                    soTien = DecimalFormat.getInstance().format(a * 3 / 4);
                    tvTien3.setText(soTien);

                    soTien = DecimalFormat.getInstance().format(a);
                    tvTien4.setText(soTien);

                    long revenue = Long.parseLong(overviewPaidCustomerJson.getListTongquan().get(0).getREVENUE());
                    long percent = revenue / 10 / a;

                    if (revenue != 0 && percent == 0)
                        pg1.setProgress(1);
                            else  try {
                        pg1.setProgress((int) percent);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    revenue = Long.parseLong(overviewPaidCustomerJson.getListTongquan().get(1).getREVENUE());
                    percent = revenue / 10 / a;

                    if (revenue != 0 && percent == 0)
                        pg2.setProgress(1);
                    else  try {
                        pg2.setProgress((int) percent);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    revenue = Long.parseLong(overviewPaidCustomerJson.getListTongquan().get(2).getREVENUE());
                    percent = revenue / 10 / a;

                    if (revenue != 0 && percent == 0)
                        pg3.setProgress(1);
                    else  try {
                        pg3.setProgress((int) percent);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    revenue = Long.parseLong(overviewPaidCustomerJson.getListTongquan().get(3).getREVENUE());
                    percent = revenue / 10 / a;

                    if (revenue != 0 && percent == 0)
                        pg4.setProgress(1);
                    else  try {
                        pg4.setProgress((int) percent);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    revenue = Long.parseLong(overviewPaidCustomerJson.getListTongquan().get(4).getREVENUE());
                    percent = revenue / 10 / a;

                    if (revenue != 0 && percent == 0)
                        pg5.setProgress(1);
                    else  try {
                        pg5.setProgress((int) percent);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            } else {
                index = index - 5;
            }
    }

    public Long xulyTong(String tong){

        String ketqua = "0";

        if (tong.length() == 1){

            String kytudau = String.valueOf(tong.charAt(0));

            if (Integer.valueOf(kytudau)%2 == 1) {
                ketqua = tong.replace(kytudau,  String.valueOf(Long.valueOf(kytudau) + 1));
            } else {
                ketqua = tong.replace(kytudau, String.valueOf(Long.valueOf(kytudau) + 2));
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
            } else if (kytudau.equals("9") || kytudau.equals("8")){
                ketqua = "10";
                for (int i = 1; i< tong.length(); i++){
                    ketqua = ketqua + "0";
                }
            }
        }

        return Long.valueOf(ketqua)/1000;

    }

    public void getSpinner(){

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
//                Log.e("tongquanview2", "Day la day");
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivback:
                try {
                    if (index >= 5) {
                        index = index - 5;
                        getDataVolley(index, type);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ivnext:
                try {
                    if (overviewPaidCustomerJson.getListTongquan().size() == 5) {
                        index = index + 5;
                        getDataVolley(index,type);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.btPayRevenue:
                try {
                    if (PaidEmployee == false) {
                        PaidEmployee = true;
                    }
                    else {
                        PaidEmployee = false;
                    }
//                    Log.e("overviewPaid", "PaidEmployee: " + PaidEmployee);
                    getDataVolley(index,type);
                } catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }
}
