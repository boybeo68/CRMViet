package vn.altalab.app.crmvietpack.presenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.object.Order;
import vn.altalab.app.crmvietpack.orders_fragment.CancelledFragment;
import vn.altalab.app.crmvietpack.orders_fragment.ConfirmedFragment;
import vn.altalab.app.crmvietpack.orders_fragment.DeliveredFragment;
import vn.altalab.app.crmvietpack.orders_fragment.UnconfimredFragment;
import vn.altalab.app.crmvietpack.utility.MySingleton;

/**
 * Created by Tung on 1/20/2017.
 */

public class OderAdapter extends ArrayAdapter<Order> {
    private Context context;
    private LayoutInflater inflater;
    private int resource;
    private List<Order> orderList;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;
    private SimpleDateFormat simpleDateFormat;
    ProgressDialog pDialog;
    Integer[] STATUS_ID = {1, 2, 3, 4,5};

    public OderAdapter(Context context, int resource, List<Order> data) {

        super(context, resource, data);
        this.context = context;
        this.orderList = data;
        this.resource = resource;

        if (settings == null) {
            settings = context.getSharedPreferences(PREFS_NAME, 0);
        }

        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        }
    }


    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Order getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return orderList.get(position).getOderId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        OderAdapter.ViewHolder holder = null;
        if (convertView == null) {
            inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
            holder = new OderAdapter.ViewHolder();
            holder.nguoiGh = (TextView) convertView.findViewById(R.id.tv_NguoigiaoH);
            holder.txtdhCode = (TextView) convertView.findViewById(R.id.tv_dhCode);
            holder.txtndh = (TextView) convertView.findViewById(R.id.tv_datH);
            holder.txtngh = (TextView) convertView.findViewById(R.id.tv_giaoH);
            holder.txtCusName = (TextView) convertView.findViewById(R.id.tv_tkH);
            holder.sdtLh = (TextView) convertView.findViewById(R.id.tv_sdtlh);
            holder.dcgH = (TextView) convertView.findViewById(R.id.tv_dcgH);
            holder.monney = (TextView) convertView.findViewById(R.id.tv_monney);
            holder.ngLH = (TextView) convertView.findViewById(R.id.tv_tlH);
            holder.Img = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        } else {
            holder = (OderAdapter.ViewHolder) convertView.getTag();
        }

        final Order order = orderList.get(position);
        holder.txtdhCode.setText(order.getOderCode());
        if (order.getDayOder() != null) {
            holder.txtndh.setText(order.getDayOder());
        }
        if (order.getDeliveryDate() != null) {
            holder.txtngh.setText(order.getDeliveryDate());
        }

        holder.txtCusName.setText(order.getCustomerName());
        holder.sdtLh.setText(order.getCustomerPhone());
        if (!order.getCustomerAdress().equals("null")) {
            holder.dcgH.setText(order.getCustomerAdress());
        }

        holder.monney.setText(DecimalFormat.getInstance().format(Double.parseDouble(order.getMoneyOder())));
        holder.ngLH.setText(order.getQuality());
        if (order.getOrderUserName().equals("null")) {
            holder.nguoiGh.setText("");
        } else {
            holder.nguoiGh.setText(order.getOrderUserName());
        }

        int a = order.getStatus();
        if (a == 1) {
            holder.Img.setBackgroundResource(R.drawable.ic_chuaduyetweb);

        }
        if (a == 2) {
            holder.Img.setBackgroundResource(R.drawable.ic_daduyetweb);

        }
        if (a == 3) {
            holder.Img.setBackgroundResource(R.drawable.ic_dagiaoweb);

        }
        if (a == 4) {
            holder.Img.setBackgroundResource(R.drawable.ic_dahuyweb);

        }
        if (a == 5) {
            holder.Img.setBackgroundResource(R.drawable.back_order_bt);

        }

        holder.Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(order.getStatus()!=4||order.getStatus()!=5) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.create();
                    builder.setTitle("Mời chọn trạng thái");
                    List<String> listOptions = new ArrayList<>();
                    listOptions.add("Chưa xác nhận");
                    listOptions.add("Đã xác nhận");
                    listOptions.add("Đã giao");
                    listOptions.add("Đã hủy");
                    listOptions.add("Đã trả về");

                    ListAdapter listAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, listOptions);
                    builder.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                updateStatus(position, which);
                            }
                            if (which == 1) {
                                updateStatus(position, which);
                            }
                            if (which == 2) {
                                updateStatus(position, which);
                            }
                            if (which == 3) {
                                updateStatus(position, which);
                            }
                            if (which == 4) {
                                updateStatus(position, which);
                            }
                        }
                    });
                    builder.show();
                }
            }
        });
        return convertView;
    }

    public void updateStatus(final int position, final int which) {
        Map<String, String> jsonparam = new HashMap<>();
        jsonparam.put("USER_ID", settings.getString("userId", ""));
        jsonparam.put("ORDER_ID", String.valueOf(orderList.get(position).getOderId()));
        jsonparam.put("status", String.valueOf(STATUS_ID[which]));

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Xin chờ ...");
        pDialog.setCancelable(true);
        pDialog.show();

        String url = settings.getString("api_server", "") + "/api/v1/orders/status";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonparam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                if (pDialog != null)
                    if (pDialog.isShowing())
                        pDialog.dismiss();

                // Return
                try {
                    if (jsonObject.getString("messages").equals("success")) {

                        orderList.get(position).setStatus(STATUS_ID[which]);
                        Log.e("CUSTOMERTRANSACTION", "jsonObject " + jsonObject);

                        if (UnconfimredFragment.oderAdapter != null) ;
                        UnconfimredFragment.oderAdapter.notifyDataSetChanged();

                        if (ConfirmedFragment.oderAdapter != null)
                            ConfirmedFragment.oderAdapter.notifyDataSetChanged();

                        if (DeliveredFragment.oderAdapter != null)
                            DeliveredFragment.oderAdapter.notifyDataSetChanged();

                        if (CancelledFragment.oderAdapter != null)
                            CancelledFragment.oderAdapter.notifyDataSetChanged();

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
                Log.e("CUSTOMERTRANSACTION", "Error: " + volleyError.toString());
                if (pDialog != null)
                    if (pDialog.isShowing())
                        pDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=UTF-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };

        request.setShouldCache(false);
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        MySingleton.getInstance(context).addToRequestQueue(request);


    }

    private static class ViewHolder {
        TextView txtdhCode;
        TextView txtndh;
        TextView txtngh;
        TextView nguoiGh;
        TextView txtCusName;
        TextView sdtLh;
        TextView dcgH;
        TextView monney;
        TextView ngLH;
        ImageView Img;
    }
}
