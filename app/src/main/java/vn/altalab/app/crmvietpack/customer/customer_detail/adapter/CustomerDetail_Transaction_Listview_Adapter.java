package vn.altalab.app.crmvietpack.customer.customer_detail.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import vn.altalab.app.crmvietpack.Link;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.customer.customer_detail.CustomerDetail_Transaction_Fragment;
import vn.altalab.app.crmvietpack.customer.customer_detail.object.CustomerDetail_Transaction_Listview_Setget;
import vn.altalab.app.crmvietpack.transaction.TransactionDetail_Activity;
import vn.altalab.app.crmvietpack.transaction.Transaction_MainFragment;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class CustomerDetail_Transaction_Listview_Adapter extends BaseAdapter{

    ArrayList<CustomerDetail_Transaction_Listview_Setget> LIST;
    private view_Holder viewHolder;
    Context context;
    int layout;

    String TRANSACTION_NAME_TEXT = "";
    String ASSIGNER = "";
    String ASSIGNED_USER_NAME = "";
    String TRANSACTION_TYPE_NAME = "";
    String START_DATE = "";
    String END_DATE = "";

    String[] STATUS = {"Chưa thực hiện", "Đang thực hiện", "Đã giải quyết", "Đã hoàn thành"};
    String[] STATUS_ID = {"1","2","4","3"};
    String[] STATUS_COLOR = {"#333333", "#3399FF", "#66FF66", "#FF0000"};

    int position_fake = 0;

    View viewrow;
    ArrayList listIDCHANGE;

    private int[] drawable = {R.drawable.loading1, R.drawable.loading2, R.drawable.loading3, R.drawable.loadingfinish};
    private Link link;
    ProgressDialog pDialog;

    public CustomerDetail_Transaction_Listview_Adapter(Context context, int layout, ArrayList<CustomerDetail_Transaction_Listview_Setget> LIST){
        this.context = context;
        this.layout = layout;
        this.LIST = LIST;

        if (context != null)
        link = new Link(context);

        listIDCHANGE = new ArrayList();
    }

    public class view_Holder{
        TextView tvTransactionName, tvAssigner, tvExecutor, tvJobType, tvTime, tvStatus;
        ImageView iv_STATUS;
    }

    @Override
    public int getCount() {
        return LIST.size();
    }

    @Override
    public Object getItem(int position) {
        return LIST.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (LIST.get(position).getTRANSACTION_ID() != null && !LIST.get(position).getTRANSACTION_ID().equals("null"))
        return Long.parseLong(LIST.get(position).getTRANSACTION_ID());
        else return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewrow = convertView;
        position_fake = position;
        Log.e("positionaa", "listIDCHANGE.size(): " + listIDCHANGE.size());

        if (viewrow == null) {
            viewrow = layoutInflater.inflate(layout, parent, false);
            viewHolder = new view_Holder();

            viewHolder.tvTransactionName = (TextView) viewrow.findViewById(R.id.tvTransactionName);
            viewHolder.tvAssigner = (TextView) viewrow.findViewById(R.id.tvAssigner);
            viewHolder.tvExecutor = (TextView) viewrow.findViewById(R.id.tvExecutor);
            viewHolder.tvJobType = (TextView) viewrow.findViewById(R.id.tvJobType);
            viewHolder.tvTime = (TextView) viewrow.findViewById(R.id.tvTime);
            viewHolder.iv_STATUS = (ImageView) viewrow.findViewById(R.id.iv_STATUS);
            viewHolder.tvStatus = (TextView) viewrow.findViewById(R.id.tvStatus);

            viewrow.setTag(viewHolder);
        }

        viewHolder = (view_Holder) viewrow.getTag();
        viewHolder.tvTransactionName.setSingleLine();
        viewHolder.tvTransactionName.setSelected(true);

        TRANSACTION_NAME_TEXT = LIST.get(position).getTRANSACTION_NAME_TEXT();
        ASSIGNER = LIST.get(position).getASSIGNER();
        ASSIGNED_USER_NAME = LIST.get(position).getASSIGNED_USER_NAME();
        TRANSACTION_TYPE_NAME = LIST.get(position).getTRANSACTION_TYPE_NAME();
        START_DATE = LIST.get(position).getSTART_DATE();
        END_DATE = LIST.get(position).getEND_DATE();

        viewHolder.tvTransactionName.setText(TRANSACTION_NAME_TEXT);
        viewHolder.tvAssigner.setText(ASSIGNED_USER_NAME);
        viewHolder.tvExecutor.setText(ASSIGNER);
        viewHolder.tvJobType.setText(TRANSACTION_TYPE_NAME);
        viewHolder.tvTime.setText(START_DATE + " -> " + END_DATE);

        for (int i=0; i< 4; i++) {
            if (LIST.get(position).getSTATUS().equals(STATUS_ID[i])) {
                viewHolder.iv_STATUS.setBackgroundResource(drawable[i]);
                viewHolder.tvStatus.setText(STATUS[i]);
                viewHolder.tvStatus.setTextColor(Color.parseColor(STATUS_COLOR[i]));
            }
        }

        viewrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TransactionDetail_Activity.class);
                intent.putExtra("transaction_id", "" + LIST.get(position).getTRANSACTION_ID());

                context.startActivity(intent);
            }
        });

        viewHolder.iv_STATUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Mời chọn trạng thái:")
                        .setItems(STATUS, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("CUSTOMER_DETAIL", "which: " + which + "position: "+ position);
                                if (context != null)
                                post_STATUS(position, which);
                            }
                        });
                builder.show();
            }
        });
        return viewrow;
    }

    private Map<String, String> get_PARAMS(int position, int which){
        Map<String, String> jsonParams = new HashMap<>();

        Log.e("position", "position: " + position);
        Log.e("position", "TRANSACTION_ID: " + LIST.get(position).getTRANSACTION_ID());

        jsonParams.put("USER_ID", link.getId());
        jsonParams.put("TRANSACTION_ID", LIST.get(position).getTRANSACTION_ID());


        jsonParams.put("status", STATUS_ID[which]);

        Log.e("CUSTOMERDETAILIDTRAN", LIST.get(position).getTRANSACTION_ID());
        Log.e("CUSTOMERDETAILIDTRAN", STATUS_ID[which]);

        return jsonParams;
    }

    private void post_STATUS(final int position, final int which){

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Xin chờ ...");
        pDialog.setCancelable(true);
        pDialog.show();

        String url = link.post_TRANSACTION_STATUS_link();
        Map<String, String> jsonParams = get_PARAMS(position, which);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject){

                Log.e("CUSTOMERTRANSACTION", "Response: " + jsonObject);

                if (pDialog != null)
                    if (pDialog.isShowing())
                        pDialog.dismiss();

                // Return
                try {
                    if (jsonObject.getString("messages").equals("success")) {

                        LIST.get(position).setSTATUS(STATUS_ID[which]);
                        Log.e("CUSTOMERTRANSACTION", "jsonObject " + jsonObject);

                        if (Transaction_MainFragment.customerDetailTransactionListviewAdapter != null);
                        Transaction_MainFragment.customerDetailTransactionListviewAdapter.notifyDataSetChanged();

                        if (CustomerDetail_Transaction_Fragment.customerDetailTransactionListviewAdapter != null)
                            CustomerDetail_Transaction_Fragment.customerDetailTransactionListviewAdapter.notifyDataSetChanged();

                    } else {
                        if (jsonObject.getString("details") != null && !jsonObject.getString("details").equals("null"))
                            Toast.makeText(context, "" + jsonObject.getString("details"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e){
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

}
