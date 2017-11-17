package vn.altalab.app.crmvietpack.object;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Luongnv on 10/10/2016.
 */
public class TblPosition extends RealmObject implements Serializable {
    @Index
    @PrimaryKey
    private Integer positionId;

    private String positionName;
    private String positionDescription;

    private RealmList<ContactCustomer> contactCustomerRealmList = new RealmList<>();

    public RealmList<ContactCustomer> getContactCustomerRealmList() {
        return contactCustomerRealmList;
    }

    public void setContactCustomerRealmList(RealmList<ContactCustomer> contactCustomerRealmList) {
        this.contactCustomerRealmList = contactCustomerRealmList;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPositionDescription() {
        return positionDescription;
    }

    public void setPositionDescription(String positionDescription) {
        this.positionDescription = positionDescription;
    }

    @Override
    public String toString() {
        return getPositionName();
    }
}
