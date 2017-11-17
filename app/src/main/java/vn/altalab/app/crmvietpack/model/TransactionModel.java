package vn.altalab.app.crmvietpack.model;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import vn.altalab.app.crmvietpack.object.TransactionType;

/**
 * Created by Simple on 6/10/2016.
 */
public class TransactionModel {
    public TransactionModel() {
    }

    public void createTransactionType(Realm realm, TransactionType transactionType) {
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(transactionType);
            realm.commitTransaction();
        } catch (RealmException re) {
            Log.e("createtrantype", re.toString());
        }
    }


    public RealmResults<TransactionType> getTransactionType(Realm realm) {
        return realm.where(TransactionType.class).findAll();
    }

    public TransactionType getTransactionTypeById(Realm realm, long typeId) {
        return realm.where(TransactionType.class).equalTo("transactionTypeId", typeId).findFirst();
    }

}
