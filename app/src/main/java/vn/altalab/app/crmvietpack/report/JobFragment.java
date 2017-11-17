package vn.altalab.app.crmvietpack.report;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import vn.altalab.app.crmvietpack.report.adapter.JobAdapter;
import vn.altalab.app.crmvietpack.report.object.ReportJob;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class JobFragment extends Fragment {
    ImageView back, next;
    View view;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;
    ListView listView;
    public static List<ReportJob> reportJobs, reportUSERNAME;
    public static JobAdapter jobAdapter;
    TextView Nv1, Nv2, Nv3, Nv4, Nv5;
    static String from_date, to_date;
    int index1 = 0;
    static int check;
    ProgressDialog progressDialog;

    Calendar todate, fromdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_job, container, false);
        }

        if (getActivity() != null) {
            if (settings == null) {
                settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
            }
            init();
            reportJobs = new ArrayList<>();
            reportUSERNAME = new ArrayList<>();

            CrmMainActivity crmMainActivity = new CrmMainActivity();
            String a = crmMainActivity.from;
            String b = crmMainActivity.to;

            if (!a.equals("") & !b.equals("")) {
                from_date = a;
                to_date = b;

                reportJobs.clear();
//                jobAdapter.notifyDataSetChanged();
                doGetReportJob(0);
            } else if (!a.equals("") & b.equals("")) {
                from_date = a;
                todate = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                to_date = simpleDateFormat.format(todate.getTime());

                reportJobs.clear();
//                jobAdapter.notifyDataSetChanged();
                doGetReportJob(0);
            } else {
                fromdate = Calendar.getInstance();
                fromdate.set(Calendar.DAY_OF_MONTH, fromdate.get(Calendar.DAY_OF_MONTH) - 7);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                from_date = simpleDateFormat.format(fromdate.getTime());

                todate = Calendar.getInstance();
                to_date = simpleDateFormat.format(todate.getTime());
                doGetReportJob(0);

            }


//            doGetReportJob(0);

            jobAdapter = new JobAdapter(getActivity(), R.layout.item_job, reportJobs);

            listView.setAdapter(jobAdapter);

        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nv1.setText("");
                Nv2.setText("");
                Nv3.setText("");
                Nv4.setText("");
                Nv5.setText("");
                reportUSERNAME.clear();
                reportJobs.clear();
                jobAdapter.notifyDataSetChanged();
                if (index1 > 0) {
                    index1 = index1 - 5;
                    doGetReportJob(index1);

                } else {
                    doGetReportJob(0);

                }


            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Nv1.setText("");
                Nv2.setText("");
                Nv3.setText("");
                Nv4.setText("");
                Nv5.setText("");
                reportUSERNAME.clear();
                reportJobs.clear();
                jobAdapter.notifyDataSetChanged();
                if (check == 1) {
                    index1 = index1 + 5;

                    doGetReportJob(index1);
                } else {
                    Toast.makeText(getActivity(), "Hết dữ liệu hiển thị", Toast.LENGTH_LONG).show();
                }


            }
        });
        return view;
    }

    // hàm khai báo
    public void init() {
        back = (ImageView) view.findViewById(R.id.backBt);
        next = (ImageView) view.findViewById(R.id.nextBt);

        Nv1 = (TextView) view.findViewById(R.id.Nv1);
        Nv2 = (TextView) view.findViewById(R.id.Nv2);
        Nv3 = (TextView) view.findViewById(R.id.Nv3);
        Nv4 = (TextView) view.findViewById(R.id.Nv4);
        Nv5 = (TextView) view.findViewById(R.id.Nv5);

        listView = (ListView) view.findViewById(R.id.lstJob);


    }

    public void doGetReportJob(final int index) {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        String url = settings.getString("api_server", "") + "/api/v1/report/transactions/summary?USER_ID="
                + settings.getString(getResources().getString(R.string.user_id_object), "") + "&from_date=" + from_date + "&to_date=" + to_date + "&index=" + index;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {


                    if (getActivity() != null) {
                        JSONArray array = jsonObject.optJSONArray("listUsers");
                        for (int i = 0; i < array.length(); i++) {
                            final JSONObject object = array.optJSONObject(i);


                            ReportJob reportJob = new ReportJob();
                            reportJob.setUSER_FULL_NAME(object.optString("USER_FULL_NAME"));


                            reportUSERNAME.add(reportJob);

                        }

                        if (0 == reportUSERNAME.size()) {
                            Toast.makeText(getActivity(), "Hết dữ liệu hiển thị", Toast.LENGTH_LONG).show();
                            check = 0;
                        } else {
                            if (1 <= reportUSERNAME.size()) {
                                Nv1.setText(reportUSERNAME.get(0).getUSER_FULL_NAME());
                            }
                            if (2 <= reportUSERNAME.size()) {
                                Nv2.setText(reportUSERNAME.get(1).getUSER_FULL_NAME());
                            }
                            if (3 <= reportUSERNAME.size()) {
                                Nv3.setText(reportUSERNAME.get(2).getUSER_FULL_NAME());
                            }
                            if (4 <= reportUSERNAME.size()) {
                                Nv4.setText(reportUSERNAME.get(3).getUSER_FULL_NAME());
                            }
                            if (5 <= reportUSERNAME.size()) {
                                Nv5.setText(reportUSERNAME.get(4).getUSER_FULL_NAME());
                            }

                            //loại giao dịch
                            JSONArray array1 = jsonObject.optJSONArray("listKpi");
                            for (int i = 0; i < array1.length(); i++) {
                                final JSONObject object = array1.optJSONObject(i);

                                ReportJob reportJob = new ReportJob();
                                reportJob.setTRANSACTION_TYPE_NAME(object.optString("transaction_type_name"));


                                JSONArray array2 = object.optJSONArray("users");
                                if (1 <= reportUSERNAME.size()) {

                                    JSONObject object1 = array2.optJSONObject(0);
                                    reportJob.setTransactions1(object1.optString("transactions"));

                                }
                                if (2 <= reportUSERNAME.size()) {

                                    JSONObject object2 = array2.optJSONObject(1);
                                    reportJob.setTransactions2(object2.optString("transactions"));
                                }
                                if (3 <= reportUSERNAME.size()) {

                                    JSONObject object3 = array2.optJSONObject(2);
                                    reportJob.setTransactions3(object3.optString("transactions"));
                                }
                                if (4 <= reportUSERNAME.size()) {

                                    JSONObject object4 = array2.optJSONObject(3);
                                    reportJob.setTransactions4(object4.optString("transactions"));
                                }
                                if (5 <= reportUSERNAME.size()) {

                                    JSONObject object5 = array2.optJSONObject(4);
                                    reportJob.setTransactions5(object5.optString("transactions"));
                                }


                                reportJobs.add(reportJob);

                            }
                            check = 1;
                        }

                    }


                }
                jobAdapter.notifyDataSetChanged();

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
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
