package vn.altalab.app.crmvietpack.model;

import android.util.Log;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import vn.altalab.app.crmvietpack.object.ContactCustomer;
import vn.altalab.app.crmvietpack.object.Users;

/**
 * Created by Luongnv on 10/11/2016.
 */
public class ContactCustomerModel {
    public ContactCustomerModel() {
    }

    public void createContactCustomer(Realm realm, ContactCustomer _contact) {
        try {
//            RealmResults<ContactCustomer> realmResults = realm.where(ContactCustomer.class).equalTo("contactId", _contact.getContactId()).findAll();
//            if (realmResults.size() > 0) {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(_contact);
            realm.commitTransaction();
//            }
        } catch (Exception e) {
            realm.cancelTransaction();
            Log.e("createcontactcustomer", e.toString());
        }
    }

    public RealmResults<ContactCustomer> getContactCustomer(Realm realm, long userId) {

//        Users users = realm.where(Users.class).equalTo("userId", userId).findFirst();

//        return realm.where(ContactCustomer.class).equalTo("customers.users.userId", userId).or().contains("customers.userOwner", users.getUserSubset()).findAll();
        return realm.where(ContactCustomer.class).findAll();
    }


    public List<ContactCustomer> getContactCustomerList(Realm realm, long userId, int first, int last) {
        Users users = realm.where(Users.class).equalTo("userId", userId).findFirst();

        return realm.where(ContactCustomer.class).equalTo("customers.users.userId", userId).or().contains("customers.userOwner", users.getUserSubset()).findAll().subList(first, last);
    }

    public RealmResults<ContactCustomer> getAllContactsCustomer(Realm realm) {
        return realm.where(ContactCustomer.class).findAll();
    }

    public ContactCustomer getContactCustomerById(Realm realm, int contactId) {
        return realm.where(ContactCustomer.class).equalTo("contactId", contactId).findFirst();
    }
}
