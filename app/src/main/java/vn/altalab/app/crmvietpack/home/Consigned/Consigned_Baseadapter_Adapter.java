package vn.altalab.app.crmvietpack.home.Consigned;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.home.Home_Consigned_Detail;

public class Consigned_Baseadapter_Adapter extends BaseAdapter {

    Context context;
    int layout;
    ArrayList<Consigned_Setget> list;

    public Consigned_Baseadapter_Adapter(Context context, int layout, ArrayList<Consigned_Setget> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    public class view_Holder {
        TextView tvTitle, tvKhachhang, tvSodienthoai, tvOffice, tvNguoithuchien, tvDate;
        RelativeLayout rlTouch;
    }

    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return  Long.parseLong(list.get(i).getTRANSACTION_ID());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewrow = convertView;

        if (viewrow == null) {

            viewrow = layoutInflater.inflate(layout, parent, false);
            view_Holder viewHolder = new view_Holder();

            viewHolder.tvTitle = (TextView) viewrow.findViewById(R.id.tvTitle);
            viewHolder.tvKhachhang = (TextView) viewrow.findViewById(R.id.tvKhachhang);
            viewHolder.tvSodienthoai = (TextView) viewrow.findViewById(R.id.tvSodienthoai);
            viewHolder.tvNguoithuchien = (TextView) viewrow.findViewById(R.id.tvNguoithuchien);
            viewHolder.tvOffice = (TextView) viewrow.findViewById(R.id.tvEmail);
            viewHolder.tvDate = (TextView) viewrow.findViewById(R.id.tvDate);
            viewHolder.rlTouch = (RelativeLayout) viewrow.findViewById(R.id.rlTouch);

            viewrow.setTag(viewHolder);

        }

        view_Holder viewHolder = (view_Holder) viewrow.getTag();
            viewHolder.rlTouch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,Home_Consigned_Detail.class);

                    intent.putExtra("TRANSACTION_ID", list.get(position).getTRANSACTION_ID());
                    intent.putExtra("TRANSACTION_NAME_TEXT", list.get(position).getTRANSACTION_NAME_TEXT());
                    intent.putExtra("CUSTOMER_NAME", list.get(position).getCUSTOMER_NAME());
                    intent.putExtra("TELEPHONE", list.get(position).getTELEPHONE());
                    intent.putExtra("CUSTOMER_EMAIL", list.get(position).getCUSTOMER_EMAIL());
                    intent.putExtra("ASSIGNED_USER_NAME", list.get(position).getASSIGNED_USER_NAME());
                    intent.putExtra("ASSIGNER", list.get(position).getASSIGNER());
                    intent.putExtra("START_DATE", list.get(position).getSTART_DATE());
                    intent.putExtra("END_DATE", list.get(position).getEND_DATE());
                    intent.putExtra("TRANSACTION_DESCRIPTION", list.get(position).getTRANSACTION_DESCRIPTION());
                    intent.putExtra("CUSTOMER_ID", list.get(position).getCUSTOMER_ID());
                    intent.putExtra("OFFICE_ADDRESS", list.get(position).getOFFICE_ADDRESS());
                    intent.putExtra("checkPhone", list.get(position).getContactPhone());
                    intent.putExtra("checkEmail", list.get(position).getContactEmail());
                    context.startActivity(intent);

                }

            });

        try {

                Spanned spanned = Html.fromHtml(list.get(position).getTRANSACTION_NAME_TEXT().trim());

                viewHolder.tvTitle.setText(spanned);
                viewHolder.tvKhachhang.setText(list.get(position).getCUSTOMER_NAME().trim());
                viewHolder.tvSodienthoai.setText(list.get(position).getTELEPHONE().trim());
                viewHolder.tvNguoithuchien.setText(list.get(position).getASSIGNER().trim());
                viewHolder.tvOffice.setText(list.get(position).getOFFICE_ADDRESS().trim());

                viewHolder.tvDate.setText(list.get(position).getSTART_DATE() + " -> " + list.get(position).getEND_DATE());

        } catch (Exception e){
            e.printStackTrace();
        }
        return viewrow;
    }
}
