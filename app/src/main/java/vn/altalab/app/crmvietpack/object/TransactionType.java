package vn.altalab.app.crmvietpack.object;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Simple on 6/9/2016.
 */
public class TransactionType extends RealmObject implements Serializable {
    @PrimaryKey
    private long transactionTypeId;

    private String transactionTypeName;
    private String transactionTypeDescription;

    public String getTransactionTypeName() {
        return transactionTypeName;
    }

    public void setTransactionTypeName(String transactionTypeName) {
        this.transactionTypeName = transactionTypeName;
    }

    public long getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(long transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    public String getTransactionTypeDescription() {
        return transactionTypeDescription;
    }

    public void setTransactionTypeDescription(String transactionTypeDescription) {
        this.transactionTypeDescription = transactionTypeDescription;
    }

    @Override
    public String toString() {
        return getTransactionTypeName();
    }
}
