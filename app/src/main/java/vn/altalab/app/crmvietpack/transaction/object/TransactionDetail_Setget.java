package vn.altalab.app.crmvietpack.transaction.object;

public class TransactionDetail_Setget {

    String TRANSACTION_NAME_TEXT;
    String CUSTOMER_NAME;
    String ASSIGNER;
    String ASSIGNED_USER_NAME;
    String TELEPHONE;
    String START_DATE;
    String END_DATE;
    String TRANSACTION_TYPE_ID;
    String TRANSACTION_TYPE_NAME;
    String TRANSACTION_USER;
    String TRANSACTION_DESCRIPTION;
    String PRIORITY;
    String STATUS;
    String TRANSACTION_ID;

    public String getTRANSACTION_ID() {
        return TRANSACTION_ID;
    }

    public void setTRANSACTION_ID(String TRANSACTION_ID) {
        this.TRANSACTION_ID = TRANSACTION_ID;
    }

    long CUSTOMER_ID ;

    public long getCUSTOMER_ID() {
        return CUSTOMER_ID;
    }

    public void setCUSTOMER_ID(long CUSTOMER_ID) {
        this.CUSTOMER_ID = CUSTOMER_ID;
    }

    public String getTRANSACTION_USER() {
        return TRANSACTION_USER;
    }

    public void setTRANSACTION_USER(String TRANSACTION_USER) {
        this.TRANSACTION_USER = TRANSACTION_USER;
    }

    public String getTRANSACTION_TYPE_ID() {
        return TRANSACTION_TYPE_ID;
    }

    public void setTRANSACTION_TYPE_ID(String TRANSACTION_TYPE_ID) {
        this.TRANSACTION_TYPE_ID = TRANSACTION_TYPE_ID;
    }

    public String getPRIORITY() {
        return PRIORITY;
    }

    public void setPRIORITY(String PRIORITY) {
        this.PRIORITY = PRIORITY;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
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

    public String getASSIGNER() {
        return ASSIGNER;
    }

    public void setASSIGNER(String ASSIGNER) {
        this.ASSIGNER = ASSIGNER;
    }

    public String getASSIGNED_USER_NAME() {
        return ASSIGNED_USER_NAME;
    }

    public void setASSIGNED_USER_NAME(String ASSIGNED_USER_NAME) {
        this.ASSIGNED_USER_NAME = ASSIGNED_USER_NAME;
    }

    public String getTELEPHONE() {
        return TELEPHONE;
    }

    public void setTELEPHONE(String TELEPHONE) {
        this.TELEPHONE = TELEPHONE;
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

    public String getTRANSACTION_TYPE_NAME() {
        return TRANSACTION_TYPE_NAME;
    }

    public void setTRANSACTION_TYPE_NAME(String TRANSACTION_TYPE_NAME) {
        this.TRANSACTION_TYPE_NAME = TRANSACTION_TYPE_NAME;
    }

    public String getTRANSACTION_DESCRIPTION() {
        return TRANSACTION_DESCRIPTION;
    }

    public void setTRANSACTION_DESCRIPTION(String TRANSACTION_DESCRIPTION) {
        this.TRANSACTION_DESCRIPTION = TRANSACTION_DESCRIPTION;
    }
}
