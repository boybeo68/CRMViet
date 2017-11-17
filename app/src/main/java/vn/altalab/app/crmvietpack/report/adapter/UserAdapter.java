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
import vn.altalab.app.crmvietpack.object.Users;

/**
 * Created by Tung on 4/19/2017.
 */

public class UserAdapter extends ArrayAdapter<Users> {
    private Context context;
    private LayoutInflater inflater;
    private int resource;
    private List<Users> usersList;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;

    public UserAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Users> objects) {
        super(context, resource, objects);
        this.context = context;
        this.usersList = objects;
        this.resource = resource;

        if (settings == null) {
            settings = context.getSharedPreferences(PREFS_NAME, 0);
        }
    }

    @Override
    public int getCount() {
        return usersList.size();
    }

    @Override
    public Users getItem(int position) {
        return usersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UserAdapter.ViewHolder holder = null;
        if (convertView == null) {
            inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
            holder = new UserAdapter.ViewHolder();

            holder.userName = (TextView) convertView.findViewById(R.id.userNametv);


            convertView.setTag(holder);
        } else {
            holder = (UserAdapter.ViewHolder) convertView.getTag();
        }
        final Users users = usersList.get(position);

        holder.userName.setText(users.getUserFullName());





        return convertView;
    }

    private static class ViewHolder {
        TextView userName;



    }

}