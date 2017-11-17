package vn.altalab.app.crmvietpack.transaction.object;

public class Transaction_Setget {

    String TRANSACTION_ID;
    String REG_USER;
    String STATUS;
    String PRIORITY;
    String START_DATE;
    String END_DATE;
    String REG_DTTM;
    String CHECK_SHARE;
    String TRANSACTION_TYPE_ID;

    public String getTRANSACTION_ID() {
        return TRANSACTION_ID;
    }

    public void setTRANSACTION_ID(String TRANSACTION_ID) {
        this.TRANSACTION_ID = TRANSACTION_ID;
    }

    public String getREG_USER() {
        return REG_USER;
    }

    public void setREG_USER(String REG_USER) {
        this.REG_USER = REG_USER;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getPRIORITY() {
        return PRIORITY;
    }

    public void setPRIORITY(String PRIORITY) {
        this.PRIORITY = PRIORITY;
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

    public String getREG_DTTM() {
        return REG_DTTM;
    }

    public void setREG_DTTM(String REG_DTTM) {
        this.REG_DTTM = REG_DTTM;
    }

    public String getCHECK_SHARE() {
        return CHECK_SHARE;
    }

    public void setCHECK_SHARE(String CHECK_SHARE) {
        this.CHECK_SHARE = CHECK_SHARE;
    }

    public String getTRANSACTION_TYPE_ID() {
        return TRANSACTION_TYPE_ID;
    }

    public void setTRANSACTION_TYPE_ID(String TRANSACTION_TYPE_ID) {
        this.TRANSACTION_TYPE_ID = TRANSACTION_TYPE_ID;
    }
}
