package vn.altalab.app.crmvietpack.customer.setget;

import java.util.ArrayList;

/**
 * Created by Tung on 7/27/2017.
 */

public class CustomerType {
  private   ArrayList<ChildrenType> children = new ArrayList<>();

    public ArrayList<ChildrenType> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<ChildrenType> children) {
        this.children = children;
    }


     public ArrayList<ChildrenType> choseType ;
    public  CustomerType(){
        choseType = new ArrayList<>();
    }



    private  String CUSTOMER_TYPE_NAME ="";
    private  int CUSTOMER_TYPE_ID ;

    public String getCUSTOMER_TYPE_NAME() {
        return CUSTOMER_TYPE_NAME;
    }

    public void setCUSTOMER_TYPE_NAME(String CUSTOMER_TYPE_NAME) {
        this.CUSTOMER_TYPE_NAME = CUSTOMER_TYPE_NAME;
    }

    public int getCUSTOMER_TYPE_ID() {
        return CUSTOMER_TYPE_ID;
    }

    public void setCUSTOMER_TYPE_ID(int CUSTOMER_TYPE_ID) {
        this.CUSTOMER_TYPE_ID = CUSTOMER_TYPE_ID;
    }
}
