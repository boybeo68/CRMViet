package vn.altalab.app.crmvietpack.notification.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.notification.Notification_Transaction_Fragment;
import vn.altalab.app.crmvietpack.notification.object.Notifications_Setget;
import vn.altalab.app.crmvietpack.transaction.TransactionDetail_Activity;
import vn.altalab.app.crmvietpack.utility.MySingleton;

/**
 * Created by Simple on 12/16/2016.
 */

public class Notification_Adapter extends BaseAdapter {
    private static final String PREFS_NAME = "CRMVietPrefs";
    SharedPreferences settings;
    private Context context;
    private int layout;
    private LayoutInflater inflater;
    private ArrayList<Notifications_Setget> listNotification;

    public Notification_Adapter(Context context, int layout, ArrayList<Notifications_Setget> listNotification) {

        this.context = context;
        this.layout = layout;
        this.listNotification = listNotification;
        settings = context.getSharedPreferences(PREFS_NAME, 0);
    }

    private class ViewHolder {
        TextView tvTitle;
        TextView tvContent;
        TextView tvTime;
        LinearLayout linearLayout;
    }

    @Override
    public int getCount() {
        return listNotification.size();
    }

    @Nullable
    @Override
    public Notifications_Setget getItem(int position) {
        return listNotification.get(position);
    }

    @Override
    public long getItemId(int i) {
        return listNotification.get(i).getNotificationId();
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewrow = convertView;

        if (viewrow == null) {
            viewrow = layoutInflater.inflate(layout, parent, false);
            ViewHolder viewHolder = new ViewHolder();

            viewHolder.tvTitle = (TextView) viewrow.findViewById(R.id.tvTitle);
            viewHolder.tvContent = (TextView) viewrow.findViewById(R.id.tvContent);
            viewHolder.tvTime = (TextView) viewrow.findViewById(R.id.tvTime);
            viewHolder.linearLayout = (LinearLayout) viewrow.findViewById(R.id.notifLayout);

            viewrow.setTag(viewHolder);
        }

        final ViewHolder viewHolder = (ViewHolder) viewrow.getTag();
        if (listNotification.get(position).getViewStatus() == 2) {
            viewHolder.linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            viewHolder.linearLayout.setBackgroundColor(Color.parseColor("#DDDDDD"));

        }
        viewHolder.tvTitle.setText(listNotification.get(position).getNotificationName().toUpperCase());
        viewHolder.tvContent.setText(listNotification.get(position).getFieldChangeType());
        viewHolder.tvTime.setText(listNotification.get(position).getTimeChange());
        final int a = listNotification.get(position).getNotificationId();
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TransactionDetail_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("transaction_id", "" + listNotification.get(position).getObjectId());
                intent.putExtras(bundle);
                context.startActivity(intent);

                donotifyUpdateById(a, position);


            }
        });
        return viewrow;
    }


    public void donotifyUpdateById(int id, final int position) {


        String url = settings.getString("api_server", "") + "/api/v1/notifyUpdateById/" + id;
        Map<String, String> jsonParams = new HashMap<>();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            if (jsonObject.getString("messages").equals("success")) {


                                if (Notification_Transaction_Fragment.notificationAdapter != null) ;


                                listNotification.get(position).setViewStatus(2);
                                Notification_Transaction_Fragment.notificationAdapter.notifyDataSetChanged();



                            } else {
                                if (jsonObject.getString("details") != null && !jsonObject.getString("details").equals("null"))
                                    Toast.makeText(context, "" + jsonObject.getString("details"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("doLogin", volleyError.toString());
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
        MySingleton.getInstance(context).addToRequestQueue(request);

    }
}
