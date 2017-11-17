package vn.altalab.app.crmvietpack.customer.customer_detail.object;

/**
 * Created by mac2 on 3/3/17.
 */

public class CustomerDetail_Transaction_Listview_Setget {

    String TRANSACTION_NAME_TEXT;
    String ASSIGNER;
    String CUSTOMER_NAME;
    String START_DATE;
    String END_DATE;
    String STATUS;
    String TRANSACTION_ID;
    String TRANSACTION_TYPE_NAME;
    String ASSIGNED_USER_NAME;
    long CUSTOMER_ID = 0;

    public long getCUSTOMER_ID() {
        return CUSTOMER_ID;
    }

    public void setCUSTOMER_ID(long CUSTOMER_ID) {
        this.CUSTOMER_ID = CUSTOMER_ID;
    }

    public String getASSIGNED_USER_NAME() {
        return ASSIGNED_USER_NAME;
    }

    public void setASSIGNED_USER_NAME(String ASSIGNED_USER_NAME) {
        this.ASSIGNED_USER_NAME = ASSIGNED_USER_NAME;
    }

    public String getTRANSACTION_TYPE_NAME() {
        return TRANSACTION_TYPE_NAME;
    }

    public void setTRANSACTION_TYPE_NAME(String TRANSACTION_TYPE_NAME) {
        this.TRANSACTION_TYPE_NAME = TRANSACTION_TYPE_NAME;
    }

    public String getTRANSACTION_NAME_TEXT() {
        return TRANSACTION_NAME_TEXT;
    }

    public void setTRANSACTION_NAME_TEXT(String TRANSACTION_NAME_TEXT) {
        this.TRANSACTION_NAME_TEXT = TRANSACTION_NAME_TEXT;
    }

    public String getASSIGNER() {
        return ASSIGNER;
    }

    public void setASSIGNER(String ASSIGNER) {
        this.ASSIGNER = ASSIGNER;
    }

    public String getCUSTOMER_NAME() {
        return CUSTOMER_NAME;
    }

    public void setCUSTOMER_NAME(String CUSTOMER_NAME) {
        this.CUSTOMER_NAME = CUSTOMER_NAME;
    }

    public String getSTART_DATE() {
        return START_DATE;
    }

    public void setSTART_DATE(String START_DATE) {
        this.START_DATE = START_DATE;
    }

    public String getEND_DATE() {
        return END_DATE;
    }

    public void setEND_DATE(String END_DATE) {
        this.END_DATE = END_DATE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getTRANSACTION_ID() {
        return TRANSACTION_ID;
    }

    public void setTRANSACTION_ID(String TRANSACTION_ID) {
        this.TRANSACTION_ID = TRANSACTION_ID;
    }
}
