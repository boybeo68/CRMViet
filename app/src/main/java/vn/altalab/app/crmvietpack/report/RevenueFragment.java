package vn.altalab.app.crmvietpack.report;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import vn.altalab.app.crmvietpack.CrmMainActivity;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.report.adapter.RevenueAdapter;
import vn.altalab.app.crmvietpack.report.object.ReportPrice;
import vn.altalab.app.crmvietpack.utility.MySingleton;


public class RevenueFragment extends Fragment {
    View view;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;
    ListView listView;
    public static List<ReportPrice> reportPrices;
    public static RevenueAdapter revenueAdapter;
   static String from_date, to_date;
    ProgressDialog progressDialog;
    Calendar todate, fromdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_revenue, container, false);
        }

        if (getActivity() != null) {
            if (settings == null) {
                settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
            }


            //chọn thời gian

            // lấy ngày hiện tại


            // lấy 7 ngày trước

            CrmMainActivity crmMainActivity = new CrmMainActivity();
            String a = crmMainActivity.from;
            String b = crmMainActivity.to;

            if (!a.equals("") & !b.equals("")) {
                from_date = a;
                to_date = b;

                reportPrices.clear();
                revenueAdapter.notifyDataSetChanged();
                doGetReportRev();
            } else if (!a.equals("") & b.equals("")) {
                from_date = a;
                todate = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                to_date = simpleDateFormat.format(todate.getTime());

                reportPrices.clear();
                revenueAdapter.notifyDataSetChanged();
                doGetReportRev();
            } else {
                fromdate = Calendar.getInstance();
                fromdate.set(Calendar.DAY_OF_MONTH, fromdate.get(Calendar.DAY_OF_MONTH) - 7);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                from_date = simpleDateFormat.format(fromdate.getTime());

                todate = Calendar.getInstance();
                to_date = simpleDateFormat.format(todate.getTime());
                doGetReportRev();
            }

            listView = (ListView) view.findViewById(R.id.lstRevenue);

            reportPrices = new ArrayList<>();

//            doGetReportRev();

            revenueAdapter = new RevenueAdapter(getActivity(), R.layout.item_revenue, reportPrices);
            listView.setAdapter(revenueAdapter);

        }

        return view;
    }

    public void doGetReportRev() {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        String url = settings.getString("api_server", "") + "/api/v1/report/kpi/summary?USER_ID="
                + settings.getString(getResources().getString(R.string.user_id_object), "") + "&from_date=" + from_date + "&to_date=" + to_date;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {


                    if (getActivity() != null) {
                        JSONArray array = jsonObject.optJSONArray("listKpi");
                        for (int i = 0; i < array.length(); i++) {
                            final JSONObject object = array.optJSONObject(i);


                            ReportPrice reportPrice = new ReportPrice();
                            reportPrice.setUserFullName(object.optString("USER_FULL_NAME"));
                            reportPrice.setPrice(object.optString("PRICE"));
                            reportPrice.setPayment(object.optString("PAYMENT_MONEY"));
                            reportPrice.setDebt(object.optString("DEBT"));


                            reportPrices.add(reportPrice);

                        }
                    }
                    revenueAdapter.notifyDataSetChanged();
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
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
