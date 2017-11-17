package vn.altalab.app.crmvietpack.home.Consigned;

/**
 * Created by mac2 on 2/16/17.
 */

public class Consigned_Setget {

    String TRANSACTION_ID = "";
    String TRANSACTION_NAME_TEXT = "";
    String CUSTOMER_NAME = "";
    String TELEPHONE = "";
    String CUSTOMER_EMAIL = "";
    String ASSIGNED_USER_NAME = "";
    String START_DATE = "";
    String END_DATE = "";
    String TRANSACTION_DESCRIPTION = "";
    String CUSTOMER_ID = "";
    String ASSIGNER = "";
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

    public String getASSIGNER() {
        return ASSIGNER;
    }

    public void setASSIGNER(String ASSIGNER) {
        this.ASSIGNER = ASSIGNER;
    }

    public String getCUSTOMER_ID() {
        return CUSTOMER_ID;
    }

    public void setCUSTOMER_ID(String CUSTOMER_ID) {
        this.CUSTOMER_ID = CUSTOMER_ID;
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

    public String getCUSTOMER_EMAIL() {
        return CUSTOMER_EMAIL;
    }

    public void setCUSTOMER_EMAIL(String CUSTOMER_EMAIL) {
        this.CUSTOMER_EMAIL = CUSTOMER_EMAIL;
    }

    public String getASSIGNED_USER_NAME() {
        return ASSIGNED_USER_NAME;
    }

    public void setASSIGNED_USER_NAME(String ASSIGNED_USER_NAME) {
        this.ASSIGNED_USER_NAME = ASSIGNED_USER_NAME;
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
