package vn.altalab.app.crmvietpack.home.Deathline;

/**
 * Created by mac2 on 2/16/17.
 */

public class Deathline_Setget {


    String TRANSACTION_ID = "";
    String TRANSACTION_NAME_TEXT = "";
    String CUSTOMER_NAME = "";
    String TELEPHONE = "";
    String TRANSACTION_USER_NAME = "";
    String END_DATE = "";
    String TRANSACTION_DESCRIPTION = "";
    String NUM_OUT_DATE = "";
    String CUSTOMER_ID = "";
    String OFFICE_ADDRESS = "";
    int contactPhone = 1;
    int contactEmail = 1;

    public int getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(int contactPhone) {
        this.contactPhone = contactPhone;
    }

    public int getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(int contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getOFFICE_ADDRESS() {
        return OFFICE_ADDRESS;
    }

    public void setOFFICE_ADDRESS(String OFFICE_ADDRESS) {
        this.OFFICE_ADDRESS = OFFICE_ADDRESS;
    }

    public String getCUSTOMER_ID() {
        return CUSTOMER_ID;
    }

    public void setCUSTOMER_ID(String CUSTOMER_ID) {
        this.CUSTOMER_ID = CUSTOMER_ID;
    }

    public String getNUM_OUT_DATE() {
        return NUM_OUT_DATE;
    }

    public void setNUM_OUT_DATE(String NUM_OUT_DATE) {
        this.NUM_OUT_DATE = NUM_OUT_DATE;
    }

    public String getTRANSACTION_NAME_TEXT() {
        return TRANSACTION_NAME_TEXT;
    }

    public void setTRANSACTION_NAME_TEXT(String TRANSACTION_NAME_TEXT) {
        this.TRANSACTION_NAME_TEXT = TRANSACTION_NAME_TEXT;
    }

    public String getCUSTOMER_NAME() {
        return CUSTOMER_NAME;
    }

    public void setCUSTOMER_NAME(String CUSTOMER_NAME) {
        this.CUSTOMER_NAME = CUSTOMER_NAME;
    }

    public String getTELEPHONE() {
        return TELEPHONE;
    }

    public void setTELEPHONE(String TELEPHONE) {
        this.TELEPHONE = TELEPHONE;
    }

    public String getTRANSACTION_USER_NAME() {
        return TRANSACTION_USER_NAME;
    }

    public void setTRANSACTION_USER_NAME(String TRANSACTION_USER_NAME) {
        this.TRANSACTION_USER_NAME = TRANSACTION_USER_NAME;
    }

    public String getEND_DATE() {
        return END_DATE;
    }

    public void setEND_DATE(String END_DATE) {
        this.END_DATE = END_DATE;
    }

    public String getTRANSACTION_DESCRIPTION() {
        return TRANSACTION_DESCRIPTION;
    }

    public void setTRANSACTION_DESCRIPTION(String TRANSACTION_DESCRIPTION) {
        this.TRANSACTION_DESCRIPTION = TRANSACTION_DESCRIPTION;
    }

    public String getTRANSACTION_ID() {
        return TRANSACTION_ID;
    }

    public void setTRANSACTION_ID(String TRANSACTION_ID) {
        this.TRANSACTION_ID = TRANSACTION_ID;
    }
}
