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

import java.util.List;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.report.object.ReportPrice;

/**
 * Created by Tung on 4/14/2017.
 */

public class KpiAdapter extends ArrayAdapter<ReportPrice> {

    private Context context;
    private LayoutInflater inflater;
    private int resource;
    private List<ReportPrice> ReportList;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;

    public KpiAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ReportPrice> objects) {
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
        KpiAdapter.ViewHolder holder = null;
        if (convertView == null) {
            inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
            holder = new KpiAdapter.ViewHolder();

            holder.txtKpiName = (TextView) convertView.findViewById(R.id.tvNameKpi);
            holder.txtKpiFORECASTS = (TextView) convertView.findViewById(R.id.tvFORECASTS);
            holder.txtKpiQUOTES = (TextView) convertView.findViewById(R.id.tvQUOTES);
            holder.txtKpiCONTRACTS = (TextView) convertView.findViewById(R.id.tvCONTRACTS);
            holder.txtKpiORDERS = (TextView) convertView.findViewById(R.id.tvORDERS);
            holder.txtKpiTRANSACTIONS = (TextView) convertView.findViewById(R.id.tvTRANSACTIONS);
            convertView.setTag(holder);
        } else {
            holder = (KpiAdapter.ViewHolder) convertView.getTag();
        }
        final ReportPrice reportPrice = ReportList.get(position);


        if (!reportPrice.getFORECASTS().equals("null")) {
            holder.txtKpiFORECASTS.setText(reportPrice.getFORECASTS());
        }else{
            holder.txtKpiFORECASTS.setText("0");
        }
        if (!reportPrice.getQUOTES().equals("null")) {
            holder.txtKpiQUOTES.setText(reportPrice.getQUOTES());

        }else {
            holder.txtKpiQUOTES.setText("0");
        }

        if (!reportPrice.getCONTRACTS().equals("null")) {
            holder.txtKpiCONTRACTS.setText(reportPrice.getCONTRACTS());

        }else {
            holder.txtKpiCONTRACTS.setText("0");
        }

        if (!reportPrice.getORDERS().equals("null")) {
            holder.txtKpiORDERS.setText(reportPrice.getORDERS());

        }else {
            holder.txtKpiORDERS.setText("0");
        }

        if (!reportPrice.getTRANSACTIONS().equals("null")) {
            holder.txtKpiTRANSACTIONS.setText(reportPrice.getTRANSACTIONS());

        }else {
            holder.txtKpiTRANSACTIONS.setText("0");
        }
        holder.txtKpiName.setText(reportPrice.getUserFullName());


        return convertView;
    }

    private static class ViewHolder {


        TextView txtKpiName;
        TextView txtKpiFORECASTS;
        TextView txtKpiQUOTES;
        TextView txtKpiCONTRACTS;
        TextView txtKpiORDERS;
        TextView txtKpiTRANSACTIONS;

    }
}
