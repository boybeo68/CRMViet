package vn.altalab.app.crmvietpack.report.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.report.object.ReportPrice;

/**
 * Created by Tung on 4/14/2017.
 */

public class RevenueAdapter extends ArrayAdapter<ReportPrice> {
    private Context context;
    private LayoutInflater inflater;
    private int resource;
    private List<ReportPrice> ReportList;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;

    public RevenueAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ReportPrice> objects) {
        super(context, resource, objects);
        this.context = context;
        this.ReportList = objects;
        this.resource = resource;

        if (settings == null) {
            settings = context.getSharedPreferences(PREFS_NAME, 0);
        }
    }

    @Override
    public int getCount() {
        return ReportList.size();
    }

    @Override
    public ReportPrice getItem(int position) {
        return ReportList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }




    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RevenueAdapter.ViewHolder holder = null;
        if (convertView == null) {
            inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
            holder = new RevenueAdapter.ViewHolder();

            holder.txtReName = (TextView) convertView.findViewById(R.id.tvNameRev);
            holder.txtRePrice = (TextView) convertView.findViewById(R.id.tvPriceRev);
            holder.txtRePayment = (TextView) convertView.findViewById(R.id.tvPaymentRev);
            holder.txtReDebt = (TextView) convertView.findViewById(R.id.tvDebtRev);


            convertView.setTag(holder);
        } else {
            holder = (RevenueAdapter.ViewHolder) convertView.getTag();
        }
        final ReportPrice reportPrice = ReportList.get(position);

        if (!reportPrice.getPrice().equals("null")) {
            holder.txtRePrice.setText(DecimalFormat.getInstance().format(Double.parseDouble(reportPrice.getPrice())));
        }else {
            holder.txtRePrice.setText("0");
        }
        if (!reportPrice.getPayment().equals("null")) {
            holder.txtRePayment.setText(DecimalFormat.getInstance().format(Double.parseDouble(reportPrice.getPayment())));
        }else {
            holder.txtRePayment.setText("0");
        }
        if (!reportPrice.getDebt().equals("null")) {
            holder.txtReDebt.setText(DecimalFormat.getInstance().format(Double.parseDouble(reportPrice.getDebt())));
        }else {
            holder.txtReDebt.setText("0");
        }
        holder.txtReName.setText(reportPrice.getUserFullName());

        return convertView;
    }

    private static class ViewHolder {
        TextView txtReName;
        TextView txtRePrice;
        TextView txtRePayment;
        TextView txtReDebt;



    }
}
