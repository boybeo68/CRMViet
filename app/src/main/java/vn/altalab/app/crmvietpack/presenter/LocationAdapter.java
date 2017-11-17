package vn.altalab.app.crmvietpack.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.object.Location;

/**
 * Created by Tung on 12/19/2016.
 */

public class LocationAdapter extends ArrayAdapter<Location> {

    private Context context;
    private LayoutInflater inflater;
    private int resource;
    private List<Location> LocationList;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;
    private SimpleDateFormat simpleDateFormat;

    public LocationAdapter(Context context, int resource, List<Location> data) {
        super(context, resource, data);

        this.context = context;
        this.LocationList = data;
        this.resource = resource;


        if (settings == null) {
            settings = context.getSharedPreferences(PREFS_NAME, 0);
        }

        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        }
    }

    @Override
    public int getCount() {
        return LocationList.size();
    }

    @Nullable
    @Override
    public Location getItem(int position) {
        return LocationList.get(position);
    }

    @Override
    public int getPosition(Location item) {
        return LocationList.indexOf(item);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();

            holder.txtUser_id = (TextView) convertView.findViewById(R.id.user_IDtv);
            holder.txtlat = (TextView) convertView.findViewById(R.id.lattv);
//            holder.txtlng = (TextView) convertView.findViewById(R.id.lngtv);
            holder.txtDate = (TextView) convertView.findViewById(R.id.datetv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Location location = LocationList.get(position);
//        Realm realm = Realm.getDefaultInstance();
//        UsersModel usersModel = new UsersModel();
//        Users users = usersModel.getUserById(realm, location.getUserId());
//        holder.txtUser_id.setText(users.getUserFullName());
        holder.txtUser_id.setText(location.getUserFullname());

        holder.txtlat.setText(location.getAdressloca());
//        holder.txtlng.setText(String.valueOf(location.getLng()));
        holder.txtDate.setText(simpleDateFormat.format(location.getDate()));

        return convertView;


    }


    private static class ViewHolder {
        TextView txtUser_id;
        TextView txtlat;
        TextView txtlng;
        TextView txtDate;
    }


}
