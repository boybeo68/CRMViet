package vn.altalab.app.crmvietpack.customer.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.customer.customer_detail.CustomerDetail_Activity;
import vn.altalab.app.crmvietpack.customer.setget.Customer_Setget;

public class Customer_Adapter extends BaseAdapter {

    Context context;
    int layout;
    ArrayList<Customer_Setget> list;
    ViewHolder viewHolder;

    public Customer_Adapter(Context context, int layout, ArrayList<Customer_Setget> list) {
        this.context = context;
        this.layout = layout;
        this.list = new ArrayList<>();
        this.list = list;

    }

    public class ViewHolder {
        TextView tvTitle, tvEmail, tvAddress, tvTelephone;
        ImageView img;
        RelativeLayout rlMain;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Long.parseLong(list.get(i).getCUSTOMER_ID());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewrow = convertView;

        if (viewrow == null) {
            viewrow = layoutInflater.inflate(layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = (TextView) viewrow.findViewById(R.id.tvTitle);
            viewHolder.tvTelephone = (TextView) viewrow.findViewById(R.id.tvTelephone);
            viewHolder.tvAddress = (TextView) viewrow.findViewById(R.id.tvAddress);
            viewHolder.tvEmail = (TextView) viewrow.findViewById(R.id.tvEmail);
            viewHolder.rlMain = (RelativeLayout) viewrow.findViewById(R.id.rlMain);
            viewHolder.img = (ImageView) viewrow.findViewById(R.id.iv);
            viewrow.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) viewrow.getTag();

        viewHolder.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("CustomerAdapter", "CustomerAdapter: " + list.get(position).getCUSTOMER_ID());
                Intent intent = new Intent(context, CustomerDetail_Activity.class);
                intent.putExtra("customer_id", Long.parseLong(list.get(position).getCUSTOMER_ID()));
                intent.putExtra("customer_name", list.get(position).getCUSTOMER_NAME());
                intent.putExtra("checkPhone", list.get(position).getContactPhone());
                intent.putExtra("checkEmail", list.get(position).getContactEmail());
                context.startActivity(intent);
            }
        });

        viewHolder.tvTitle.setText(list.get(position).getCUSTOMER_NAME());
        viewHolder.tvEmail.setText(list.get(position).getCUSTOMER_EMAIL());
        viewHolder.tvAddress.setText(list.get(position).getOFFICE_ADDRESS());
        viewHolder.tvTelephone.setText(list.get(position).getTELEPHONE());
        String link_image = list.get(position).getLINK_IMAGE();
        if(!link_image.equals("")){
            Picasso.with(context)
                    .load(link_image)
                    // chỉnh lại dung lượng của file ảnh cho giảm xuống.
                    .resize(200, 200)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .into(viewHolder.img);
        }else {
            viewHolder.img.setImageResource(R.drawable.ic_person);
        }


        return viewrow;
    }
}
