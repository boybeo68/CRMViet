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

import io.realm.Realm;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.object.ContactCustomer;

/**
 * Created by Luongnv on 10/11/2016.
 */
public class ContactCustomerAdapter extends ArrayAdapter<ContactCustomer> {
    private static final String PREFS_NAME = "CRMVietPrefs";
    private Context context;
    private LayoutInflater inflater;
    private int resource;
    private List<ContactCustomer> contactCustomersList;
    private Realm realm;
    private SharedPreferences setting;

    public ContactCustomerAdapter(Context context, int resource, List<ContactCustomer> data) {
        super(context, resource, data);
        this.context = context;
        this.contactCustomersList = data;
        this.resource = resource;

        setting = context.getSharedPreferences(PREFS_NAME, 0);
        if (realm == null) {

            realm = Realm.getDefaultInstance();
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();

            holder.txtContName = (TextView) convertView.findViewById(R.id.txtContName);
            holder.txtContReg = (TextView) convertView.findViewById(R.id.txtContReg);
            holder.txtContGender = (TextView) convertView.findViewById(R.id.txtContGender);
            holder.txtContPhone = (TextView) convertView.findViewById(R.id.txtContPhone);
            holder.txtContEmail = (TextView) convertView.findViewById(R.id.txtContEmail);
            holder.txtContAddress = (TextView) convertView.findViewById(R.id.txtContAddress);
//            holder.txtContYahoo = (TextView) convertView.findViewById(R.id.txtContYahoo);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ContactCustomer contactCustomer = contactCustomersList.get(position);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.txtContName.setText(contactCustomer.getContactFullName());
        if (contactCustomer.getContactEmail() != null) {
            holder.txtContEmail.setText(contactCustomer.getContactEmail());
        }
        if (contactCustomer.getGender() != null) {
            if (contactCustomer.getGender() == 1) {
                holder.txtContGender.setText(context.getResources().getString(R.string.male));
            } else if (contactCustomer.getGender() == 2) {
                holder.txtContGender.setText(context.getResources().getString(R.string.female));
            }
        }
        if (contactCustomer.getBirthday() != null) {
            holder.txtContReg.setText(format.format(contactCustomer.getBirthday()));
        }
//        if (contactCustomer.getNickChat() != null) {
//            holder.txtContYahoo.setText(contactCustomer.getNickChat());
//        }
        if (contactCustomer.getContactAddress() != null) {
            holder.txtContAddress.setText(contactCustomer.getContactAddress());
        }
        if (contactCustomer.getContactMobiphone() != null) {
            holder.txtContPhone.setText(contactCustomer.getContactMobiphone());
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return contactCustomersList.size();
    }

    @Override
    public ContactCustomer getItem(int position) {
        return contactCustomersList.get(position);
    }

    private static class ViewHolder {
        TextView txtContName;
        TextView txtContReg;
        TextView txtContGender;
        TextView txtContPhone;
        TextView txtContEmail;
        TextView txtContAddress;
//        TextView txtContYahoo;
    }
}
