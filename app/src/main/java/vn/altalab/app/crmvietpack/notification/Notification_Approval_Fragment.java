package vn.altalab.app.crmvietpack.notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vn.altalab.app.crmvietpack.Link;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.notification.adapter.Notification_Adapter_Sig;
import vn.altalab.app.crmvietpack.notification.object.Notifications_Setget;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class Notification_Approval_Fragment extends Fragment {

    View view;
    SharedPreferences sharedPreferences;
    private String PREFS_NAME = "CRMVietPrefs";
    PullToRefreshListView listView;
    ArrayList<Notifications_Setget> listNotification;
    public static Notification_Adapter_Sig notificationAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null)
            view = inflater.inflate(R.layout.notification_approval_fragment, container, false);
        listView = (PullToRefreshListView) view.findViewById(R.id.lstNotifi);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            listNotification = new ArrayList<>();
            doGetApprovalNotification(0);
            notificationAdapter = new Notification_Adapter_Sig(getActivity(), R.layout.notification_listview_custom, listNotification);
            listView.setAdapter(notificationAdapter);

            listView.setMode(PullToRefreshBase.Mode.BOTH);

            listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                    if ("".equals(sharedPreferences.getString("api_server", ""))) {
                        Toast.makeText(refreshView.getContext(), refreshView.getContext().getResources().getString(R.string.login_empty_api_server), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    listNotification.clear();
                    notificationAdapter.notifyDataSetChanged();
                    doGetApprovalNotification(0);
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    if ("".equals(sharedPreferences.getString("api_server", ""))) {
                        Toast.makeText(refreshView.getContext(), refreshView.getContext().getResources().getString(R.string.login_empty_api_server), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    doGetApprovalNotification(listNotification.size());
                }
            });

//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    donotifyUpdateById(listNotification.get(position-1).getNotificationId());
//                }
//            });
        }

    }

    // update status view cho notifi
//    public void donotifyUpdateById (int id) {
//
//
//        String url = sharedPreferences.getString("api_server", "") + "/api/v1/notifyUpdateById/"+ id;
//        Map<String, String> jsonParams = new HashMap<>();
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams),
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject jsonObject) {
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Log.e("doLogin", volleyError.toString());
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/json; charset=UTF-8");
//                headers.put("User-agent", System.getProperty("http.agent"));
//                return headers;
//            }
//        };
//
//        int socketTimeout = 60000;
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        request.setRetryPolicy(policy);
//        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
//
//    }

    public void doGetApprovalNotification(final int index) {

        Link link = new Link(getActivity());
        String url = link.get_Notification_Approval_Link(index);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (jsonObject != null)
                    if ("success".equals(jsonObject.optString(getResources().getString(R.string.messages)))) {

                        JSONArray array = jsonObject.optJSONArray("appr_notif");
                        Notifications_Setget notificationsSetget;

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.optJSONObject(i);
                            notificationsSetget = new Notifications_Setget();
                            notificationsSetget.setNotificationId(object.optInt("NOTIFICATION_ID"));
                            notificationsSetget.setNotificationName(object.optString("NOTIFICATION_NAME"));
                            notificationsSetget.setUserChange(object.optInt("USERCHANGE"));
                            notificationsSetget.setObjectId(object.optInt("OBJECTS_ID"));
                            notificationsSetget.setTimeChange(object.optString("TIMECHANGE"));
                            notificationsSetget.setChangeType(object.optString("TYPE_CHANGE"));
                            notificationsSetget.setTypeObject(object.optInt("TYPE_OBJECT"));
                            notificationsSetget.setViewStatus(object.optInt("VIEWSTATUS"));
                            listNotification.add(notificationsSetget);
                        }


                        notificationAdapter.notifyDataSetChanged();


                    }
                if (listView.isRefreshing()) {
                    listView.onRefreshComplete();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Notif Appr", volleyError.toString());
                if (listView.isRefreshing()) {
                    listView.onRefreshComplete();
                }
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
