package vn.altalab.app.crmvietpack.model;

import android.util.Log;

import java.util.StringTokenizer;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import vn.altalab.app.crmvietpack.object.Users;
import vn.altalab.app.crmvietpack.object.UsersGroup;

/**
 * Created by HieuDT on 5/30/2016.
 */
public class UsersModel {

    public UsersModel() {
    }

    public void createUsers(Realm realm, Users users) {
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(users);
            realm.commitTransaction();
        } catch (RealmException re) {
            Log.e("ERROR", re.toString());
        }
    }

    public void createUserGroup(Realm realm, UsersGroup usersGroup) {
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(usersGroup);
            realm.commitTransaction();
        } catch (RealmException re) {
            Log.e("ERROR", re.toString());
        }
    }

    public RealmResults<Users> getUsers(Realm realm) {

        return realm.where(Users.class).findAll();

    }

    public Users getUserById(Realm realm, long userId) {

        return realm.where(Users.class).equalTo("userId", userId).findFirst();

    }

    public RealmResults<UsersGroup> getUsersGroup(Realm realm) {

        return realm.where(UsersGroup.class).findAll();

    }

    public UsersGroup getUsersGroupById(Realm realm, Integer id) {

        return realm.where(UsersGroup.class).equalTo("userGroupId", id).findFirst();

    }

    public StringBuilder getUserBuilder(Realm realm, String _userIds) {
        StringBuilder userIds = new StringBuilder();
        if (_userIds.trim().length() == 0) {
            return userIds;
        }
        if (",".equals(_userIds.substring(_userIds.length() - 1)))
            _userIds = _userIds.substring(0, _userIds.length() - 1);
        StringTokenizer tokenizer = new StringTokenizer(",".concat(_userIds).concat(","), ",");
        Users users = null;
        while (tokenizer.hasMoreElements()) {
            String userId = tokenizer.nextToken();
            if (userId != null && !"".equals(userId) && !"null".equals(userId)) {
                users = realm.where(Users.class).equalTo("userId", Integer.parseInt(userId)).findFirst();
                if (users != null) {
                    userIds.append(users.getUserName());
                    if (tokenizer.hasMoreTokens()) userIds.append(",");
                }
            }
        }
        return userIds;
    }
}
