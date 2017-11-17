package vn.altalab.app.crmvietpack.customer.customer_detail.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.customer.customer_detail.CustomerDetail_Activity;
import vn.altalab.app.crmvietpack.customer.customer_detail.CustomerDetail_Info_Fragment;
import vn.altalab.app.crmvietpack.customer.customer_detail.object.CustomerDetail_Contact_Listview_Setget;
import vn.altalab.app.crmvietpack.customer.setget.Customer_Setget;

/**
 * Created by boybe on 06/06/2017.
 */

public class CustomerSearch_Contact_DetailCustomer_Adapter extends BaseAdapter {

    Context context;
    int layout;
    ArrayList<CustomerDetail_Contact_Listview_Setget> LIST;
    ArrayList<Customer_Setget> list;
    String json;
    String customer_id;

    public CustomerSearch_Contact_DetailCustomer_Adapter(Context context, int layout, ArrayList<CustomerDetail_Contact_Listview_Setget> LIST, String json){
        this.context = context;

        this.layout = layout;
        this.LIST = LIST;
        this.json = json;
        this.customer_id = customer_id;
    }

    public class view_Holder{
        TextView tvContactName, tvPhone, tvAddress, tvJobPosition, tvEmail, tvGender,tvCTTenKH;
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
        return Long.parseLong(LIST.get(position).getCONTACT_ID());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewRow = convertView;

        if (viewRow == null){
            viewRow = layoutInflater.inflate(layout,parent,false);
            view_Holder viewHolder=new view_Holder();

            viewHolder.tvContactName = (TextView) viewRow.findViewById(R.id.tvContactName);
            viewHolder.tvPhone = (TextView) viewRow.findViewById(R.id.tvPhone);
            viewHolder.tvAddress = (TextView) viewRow.findViewById(R.id.tvAddress);
            viewHolder.tvJobPosition = (TextView) viewRow.findViewById(R.id.tvJobPosition);
            viewHolder.tvEmail = (TextView) viewRow.findViewById(R.id.tvEmail);
            viewHolder.tvGender = (TextView) viewRow.findViewById(R.id.tvSex);
            viewHolder.tvCTTenKH= (TextView) viewRow.findViewById(R.id.tvCTTenKH);
            viewRow.setTag(viewHolder);
        }

        CustomerSearch_Contact_DetailCustomer_Adapter.view_Holder viewHolder = (CustomerSearch_Contact_DetailCustomer_Adapter.view_Holder) viewRow.getTag();


        String CONTACT_FULL_NAME = LIST.get(position).getCONTACT_FULL_NAME();
        String CONTACT_MOBIPHONE = LIST.get(position).getCONTACT_MOBIPHONE();
        String CONTACT_ADDRESS = LIST.get(position).getCONTACT_ADDRESS();
        String POSITION_NAME = LIST.get(position).getPOSITION_NAME();
        String CONTACT_EMAIL = LIST.get(position).getCONTACT_EMAIL();
        String GENDER = LIST.get(position).getGENDER();
        String TenKH=LIST.get(position).getCUSTOMER_NAME();

        if (CONTACT_FULL_NAME != null && !CONTACT_FULL_NAME.equals("null"))
            viewHolder.tvContactName.setText(CONTACT_FULL_NAME);

        if (TenKH != null && !TenKH.equals("null"))
            viewHolder.tvCTTenKH.setText(TenKH);

        if (CONTACT_MOBIPHONE != null && !CONTACT_MOBIPHONE.equals("null"))
            viewHolder.tvPhone.setText(CONTACT_MOBIPHONE);
        if (CONTACT_ADDRESS != null && !CONTACT_ADDRESS.equals("null"))
            viewHolder.tvAddress.setText(CONTACT_ADDRESS);
        if (POSITION_NAME != null && !POSITION_NAME.equals("null"))
            viewHolder.tvJobPosition.setText(POSITION_NAME);
        if (CONTACT_EMAIL != null && !CONTACT_EMAIL.equals("null"))
            viewHolder.tvEmail.setText(CONTACT_EMAIL);

        if (GENDER != null && !GENDER.equals("null"))
            if (GENDER.equals("1")) {
                viewHolder.tvGender.setText("Nam");
            } else {
                viewHolder.tvGender.setText("Nữ");
            }
        return viewRow;
    }

}
