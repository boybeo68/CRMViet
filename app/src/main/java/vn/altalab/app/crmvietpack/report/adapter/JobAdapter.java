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
import vn.altalab.app.crmvietpack.report.object.ReportJob;

/**
 * Created by Tung on 4/15/2017.
 */

public class JobAdapter extends ArrayAdapter<ReportJob> {
    private Context context;
    private LayoutInflater inflater;
    private int resource;
    private List<ReportJob> reportJobList;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;

    public JobAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ReportJob> objects) {
        super(context, resource, objects);
        this.context = context;
        this.reportJobList = objects;
        this.resource = resource;

        if (settings == null) {
            settings = context.getSharedPreferences(PREFS_NAME, 0);
        }
    }

    @Override
    public int getCount() {
        return reportJobList.size();
    }

    @Override
    public ReportJob getItem(int position) {
        return reportJobList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        JobAdapter.ViewHolder holder = null;
        if (convertView == null) {
            inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
            holder = new JobAdapter.ViewHolder();

            holder.CvName = (TextView) convertView.findViewById(R.id.CvName);
            holder.num1 = (TextView) convertView.findViewById(R.id.number1);
            holder.num2 = (TextView) convertView.findViewById(R.id.number2);
            holder.num3 = (TextView) convertView.findViewById(R.id.number3);
            holder.num4 = (TextView) convertView.findViewById(R.id.number4);
            holder.num5 = (TextView) convertView.findViewById(R.id.number5);


            convertView.setTag(holder);
        } else {
            holder = (JobAdapter.ViewHolder) convertView.getTag();
        }
        final ReportJob reportJob = reportJobList.get(position);

        holder.CvName.setText(reportJob.getTRANSACTION_TYPE_NAME());


        holder.num1.setText(reportJob.getTransactions1());
        holder.num2.setText(reportJob.getTransactions2());
        holder.num3.setText(reportJob.getTransactions3());
        holder.num4.setText(reportJob.getTransactions4());
        holder.num5.setText(reportJob.getTransactions5());


        return convertView;
    }

    private static class ViewHolder {
        TextView CvName;
        TextView num1;
        TextView num2;
        TextView num3;
        TextView num4;
        TextView num5;


    }

}
