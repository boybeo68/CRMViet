package vn.altalab.app.crmvietpack.customer.setget;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tung on 7/27/2017.
 */

public class ChildrenType implements Serializable {
    public ChildrenType(){

    }
    List <ChildrenType> childrenTypeList;

    public List<ChildrenType> getChildrenTypeList() {
        return childrenTypeList;
    }

    public void setChildrenTypeList(List<ChildrenType> childrenTypeList) {
        this.childrenTypeList = childrenTypeList;
    }

    private int CUSTOMER_GROUP_ID;
    private String CUSTOMER_GROUP_NAME="";

    public int getCUSTOMER_GROUP_ID() {
        return CUSTOMER_GROUP_ID;
    }

    public void setCUSTOMER_GROUP_ID(int CUSTOMER_GROUP_ID) {
        this.CUSTOMER_GROUP_ID = CUSTOMER_GROUP_ID;
    }

    public String getCUSTOMER_GROUP_NAME() {
        return CUSTOMER_GROUP_NAME;
    }

    public void setCUSTOMER_GROUP_NAME(String CUSTOMER_GROUP_NAME) {
        this.CUSTOMER_GROUP_NAME = CUSTOMER_GROUP_NAME;
    }
}
