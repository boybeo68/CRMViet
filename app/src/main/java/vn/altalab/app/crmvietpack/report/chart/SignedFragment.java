package vn.altalab.app.crmvietpack.report.chart;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.utility.MySingleton;


public class SignedFragment extends Fragment {
    View view;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;
    List<chartCountNewCustomers> chartCountNewCustomerses;
    Calendar calendar, calendar1;
    SimpleDateFormat simpleDateFormat;
    static int year, month_to, month_from, year_from;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_signed, container, false);
        }
        if (getActivity() != null) {
            if (settings == null) {
                settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
            }
            RevActivity rev = new RevActivity();

            calendar = Calendar.getInstance();
            simpleDateFormat = new SimpleDateFormat("M/yyyy", Locale.getDefault());
            String a = simpleDateFormat.format(calendar.getTime());
            String b[] = a.split("/");

            month_to = Integer.parseInt(b[0]);
            year = Integer.parseInt(b[1]);

            //3 thang trước
            calendar1 = Calendar.getInstance();
            calendar1.set(Calendar.MONTH, calendar1.get(Calendar.MONTH) - 2);

            String a1 = simpleDateFormat.format(calendar1.getTime());
            String b1[] = a1.split("/");
            chartCountNewCustomerses = new ArrayList<>();

            if (rev.monthdate != 0 & rev.yearfrom != 0) {
                month_from = rev.monthdate;
                year_from = rev.yearfrom;
                doGetReportSign();
            }else {

                month_from = Integer.parseInt(b1[0]);
                year_from = Integer.parseInt(b1[1]);
                doGetReportSign();
            }

        }
        return view;
    }


    public void doGetReportSign() {

        chartCountNewCustomerses.clear();

        String url = settings.getString("api_server", "") + "/api/v1/report/customer/growth?USER_ID="
                + settings.getString(getResources().getString(R.string.user_id_object), "") + "&month_from=" + month_from + "&year_from=" + year_from +
                "&month_to=" + month_to + "&year_to=" + year + "&status=" + 1;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {


                    if (getActivity() != null) {
                        JSONArray array = jsonObject.optJSONArray("chartCountNewCustomers");
                        for (int i = 0; i < array.length(); i++) {
                            final JSONObject object = array.optJSONObject(i);


                            chartCountNewCustomers chartCountNewCustomers = new chartCountNewCustomers();
                            chartCountNewCustomers.setMonth(object.optInt("month_int"));
                            chartCountNewCustomers.setYear(object.optString("year_int"));
                            chartCountNewCustomers.setCustomerCount(object.optInt("customerCount"));


                            chartCountNewCustomerses.add(chartCountNewCustomers);

                        }
                        if (chartCountNewCustomerses.size() != 0) {

                            ArrayList<BarEntry> entries = new ArrayList<>();
                            for (int i = 0; i < chartCountNewCustomerses.size(); i++) {

                                entries.add(new BarEntry(chartCountNewCustomerses.get(i).getCustomerCount(), i));
                            }

                            BarChart barChart = (BarChart) view.findViewById(R.id.chart);
                            barChart.removeAllViews();
                            BarDataSet dataset = new BarDataSet(entries, "khách hàng");
                            ArrayList<String> labels = new ArrayList<String>();

                            for (int i = 0; i < chartCountNewCustomerses.size(); i++) {
                                labels.add(chartCountNewCustomerses.get(i).getMonth() + "");
                            }
                            //xétlabel chục dưới x
                            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                            // bỏ cột bên phải
                            YAxis yAxisRight = barChart.getAxisRight();
                            yAxisRight.setEnabled(false);

                            dataset.setColor(Color.parseColor("#3366CC"));
                            dataset.setDrawValues(false);

                            dataset.setBarSpacePercent(30f);
                            barChart.setDescription("");

                            BarData data = new BarData(labels, dataset);


                            barChart.setData(data);
                            barChart.animateY(2000);
                        }

                    }


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Log.e("loadall", volleyError.toString());
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

        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);

    }
}
