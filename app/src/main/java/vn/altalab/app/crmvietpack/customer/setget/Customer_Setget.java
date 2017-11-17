package vn.altalab.app.crmvietpack.customer.setget;

/**
 * Created by mac2 on 3/17/17.
 */

public class Customer_Setget {
    String CUSTOMER_ID;
    String CUSTOMER_NAME;
    String USER_NAME;
    String TELEPHONE;
    String CUSTOMER_EMAIL;
    String OFFICE_ADDRESS;
    String USER_ID;
    String CUSTOMER_IMAGE;
    String LINK_IMAGE;

    public String getLINK_IMAGE() {
        return LINK_IMAGE;
    }

    public void setLINK_IMAGE(String LINK_IMAGE) {
        this.LINK_IMAGE = LINK_IMAGE;
    }

    public String getCUSTOMER_IMAGE() {
        return CUSTOMER_IMAGE;
    }

    public void setCUSTOMER_IMAGE(String CUSTOMER_IMAGE) {
        this.CUSTOMER_IMAGE = CUSTOMER_IMAGE;
    }

    Long ContractId;
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

    public Long getContractId() {
        return ContractId;
    }

    public void setContractId(Long contractId) {
        ContractId = contractId;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public Customer_Setget() {
    }


    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
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
}
