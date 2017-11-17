package vn.altalab.app.crmvietpack.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.object.Customer;

/**
 * Created by HieuDT on 6/1/2016.
 */
public class CustomerAdapter extends ArrayAdapter<Customer> {

    private Context context;
    private LayoutInflater inflater;
    private int resource;
    private List<Customer> customersList;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;
    private SimpleDateFormat simpleDateFormat;

    public CustomerAdapter(Context context, int resource, List<Customer> data) {
        super(context, resource, data);
        this.context = context;
        this.customersList = data;
        this.resource = resource;

        if (settings == null) {
            settings = context.getSharedPreferences(PREFS_NAME, 0);
        }

        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        }
    }

    @Override
    public int getCount() {
        return customersList.size();
    }

    @Override
    public Customer getItem(int position) {
        return customersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return customersList.get(position).getCustomerId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;

        if (convertView == null) {
            inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.txtCusName = (TextView) convertView.findViewById(R.id.txtCusName);
            holder.txtCusEmail = (TextView) convertView.findViewById(R.id.txtCusEmail);
            holder.txtCusPhone = (TextView) convertView.findViewById(R.id.txtCusPhone);
            holder.txtCusAddress = (TextView) convertView.findViewById(R.id.txtCusAddress);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();

        Customer customer = customersList.get(position);
        holder.txtCusName.setText(customer.getCustomerName());
        if (!customer.getCustomerEmail().equals("null")) {
            holder.txtCusEmail.setText(customer.getCustomerEmail());
        }
        if (!customer.getTelephone().equals("null")) {
            holder.txtCusPhone.setText(customer.getTelephone());
        }
        if (!customer.getOfficeAddress().equals("null")&&!customer.getOfficeAddress().equals(null)&&!customer.getOfficeAddress().equals("")) {
            holder.txtCusAddress.setText(customer.getOfficeAddress());
        }


        return convertView;
    }

    private static class ViewHolder {
        TextView txtCusName;
        TextView txtCusPhone;
        TextView txtCusEmail;
        TextView txtCusAddress;
    }
}
