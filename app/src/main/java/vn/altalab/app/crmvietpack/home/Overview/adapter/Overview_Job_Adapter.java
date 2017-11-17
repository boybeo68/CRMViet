package vn.altalab.app.crmvietpack.home.Overview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.home.Overview.Setget.Overview_Job_Setget;

import java.util.ArrayList;

public class Overview_Job_Adapter extends BaseAdapter {

    Context context;
    int layout;
    ArrayList<Overview_Job_Setget> List;

    public Overview_Job_Adapter(Context context, int layout, ArrayList<Overview_Job_Setget> list) {
        this.context = context;
        this.layout = layout;
        this.List = list;
    }

    public class view_Holder {
        TextView tvDateDone, tvDateExpired, tvName, tvCompany;
    }

    @Override
    public int getCount() {
        if (List.size() != 0)
        return List.size();
        else return 0;
    }

    @Override
    public Object getItem(int i) {
        return List.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Long.parseLong(List.get(i).getUSER_ID());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewrow = convertView;

        view_Holder viewHolder;

        if (viewrow == null) {
            viewrow = layoutInflater.inflate(layout, parent, false);

            viewHolder = new view_Holder();

            viewHolder.tvName = (TextView) viewrow.findViewById(R.id.tvNameTongquan4);
            viewHolder.tvCompany = (TextView) viewrow.findViewById(R.id.tvCompanyTongquan4);
            viewHolder.tvDateDone = (TextView) viewrow.findViewById(R.id.tvDateDoneTongquan4);
            viewHolder.tvDateExpired = (TextView) viewrow.findViewById(R.id.tvDateExpiredTongquan4);

            viewrow.setTag(viewHolder);
        }

        viewHolder = (view_Holder) viewrow.getTag();

        viewHolder.tvName.setText(List.get(position).getUSER_NAME().trim());
        viewHolder.tvCompany.setText(List.get(position).getGROUP_NAME().trim());
        viewHolder.tvDateDone.setText(List.get(position).getFINISHED().trim());
        viewHolder.tvDateExpired.setText(List.get(position).getUNFINISHED().trim());

        return viewrow;

    }

}
