package vn.altalab.app.crmvietpack.customer.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.customer.setget.ChildrenType;

/**
 * Created by Tung on 7/31/2017.
 */

public class ChosePl_Adapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;
    private int resource;
    private List<ChildrenType> childrenTypeList;
    public ChosePl_Adapter( Context context,  int resource,  List <ChildrenType> data) {
        super(context, resource, data);
        this.context = context;
        this.childrenTypeList = data;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return childrenTypeList.size();
    }

    @Override
    public ChildrenType getItem(int position) {
        return childrenTypeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ChosePl_Adapter.ViewHolder holder = null;
        if (convertView == null) {
            inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
            holder = new ChosePl_Adapter.ViewHolder();

            holder.tvPl = (TextView) convertView.findViewById(R.id.tvPl);



            convertView.setTag(holder);
        } else {
            holder = (ChosePl_Adapter.ViewHolder) convertView.getTag();
        }

        final ChildrenType childrenType = childrenTypeList.get(position);

        holder.tvPl.setText(childrenType.getCUSTOMER_GROUP_NAME());







        return convertView;
    }



    private static class ViewHolder {
        TextView tvPl;


    }
}
