package vn.altalab.app.crmvietpack.presenter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import io.realm.Realm;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.model.UsersModel;
import vn.altalab.app.crmvietpack.object.Users;
import vn.altalab.app.crmvietpack.object.UsersGroup;

/**
 * Created by Simple on 6/13/2016.
 */
public class ListUsersSpinnerAdapter extends ArrayAdapter<Users> {

    private Context context;
    private LayoutInflater inflater;
    private int resource;
    private List<Users> usersList;
    private Realm realm;
    private UsersModel usersModel;

    public ListUsersSpinnerAdapter(Context context, int resource, List<Users> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.usersList = objects;

        usersModel = new UsersModel();

        realm = Realm.getDefaultInstance();
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
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();

            holder.txtUser = (TextView) convertView.findViewById(R.id.txtUser);
            holder.chkUser = (CheckBox) convertView.findViewById(R.id.chkUser);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Users users = usersList.get(position);

        UsersGroup group = usersModel.getUsersGroupById(realm, (int) users.getUserId());
        holder.txtUser.setText(users.getUserName() + " - " + group.getUserGroupName());

        if (users.getChecked()) {
            holder.chkUser.setChecked(true);
        } else {
            holder.chkUser.setChecked(false);
        }

        return convertView;
    }


    private static class ViewHolder {
        TextView txtUser;
        CheckBox chkUser;
    }
}
