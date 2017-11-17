package vn.altalab.app.crmvietpack.object;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by HieuDT on 5/25/2016.
 */
public class Customer implements Serializable {

    private long customerId;


    private String customerCode;
    private String customerName;
    private String customerAddress;
    private String customerDescription;
    private String fax;
    private String textCode;
    private String telephone="";
    private String customerEmail="";
    private String customerWebsite;
    private String bankAccount;
    private String officeAddress;
    private String userOwner;
    private int total;
    private int total_news;
    private int total_care;
    private int total_signed;
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotal_news() {
        return total_news;
    }

    public void setTotal_news(int total_news) {
        this.total_news = total_news;
    }

    public int getTotal_care() {
        return total_care;
    }

    public void setTotal_care(int total_care) {
        this.total_care = total_care;
    }

    public int getTotal_signed() {
        return total_signed;
    }

    public void setTotal_signed(int total_signed) {
        this.total_signed = total_signed;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }


    private int regUser;

    public String getRegDttm() {
        return regDttm;
    }

    public void setRegDttm(String regDttm) {
        this.regDttm = regDttm;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    private String regDttm;
    private int updUser;
    private Date updDttm;

    private Date customerFounding;
    private Integer userId;
    private Double customerPresentDebt;
    private Double customerFirstDebt;
    private Integer geographyCountry;
    private Integer geographyProvince;
    private Integer geographyDistrict;
    private Integer geographyWard;
    private String customerImage;
    private long pointsPromotionsCurrent;
    private long pointsPromotionsTotal;
    private Date customerOwnerDate;                    //ngay chuyen chu so huu
    private Integer customerOwnerFlag;                //0 hoac null : da co chu so huu, 1 : la KH len man hinh chung
    private Date customerOwnerFlagDate;

//    private RealmList<Contract> contracts = new RealmList<>();
//    private RealmList<ContactCustomer> contactCustomers = new RealmList<>();

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerDescription() {
        return customerDescription;
    }

    public void setCustomerDescription(String customerDescription) {
        this.customerDescription = customerDescription;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Double getCustomerFirstDebt() {
        return customerFirstDebt;
    }

    public void setCustomerFirstDebt(Double customerFirstDebt) {
        this.customerFirstDebt = customerFirstDebt;
    }

    public Date getCustomerFounding() {
        return customerFounding;
    }

    public void setCustomerFounding(Date customerFounding) {
        this.customerFounding = customerFounding;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerImage() {
        return customerImage;
    }

    public void setCustomerImage(String customerImage) {
        this.customerImage = customerImage;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getCustomerOwnerDate() {
        return customerOwnerDate;
    }

    public void setCustomerOwnerDate(Date customerOwnerDate) {
        this.customerOwnerDate = customerOwnerDate;
    }

    public Integer getCustomerOwnerFlag() {
        return customerOwnerFlag;
    }

    public void setCustomerOwnerFlag(Integer customerOwnerFlag) {
        this.customerOwnerFlag = customerOwnerFlag;
    }

    public Date getCustomerOwnerFlagDate() {
        return customerOwnerFlagDate;
    }

    public void setCustomerOwnerFlagDate(Date customerOwnerFlagDate) {
        this.customerOwnerFlagDate = customerOwnerFlagDate;
    }

    public Double getCustomerPresentDebt() {
        return customerPresentDebt;
    }

    public void setCustomerPresentDebt(Double customerPresentDebt) {
        this.customerPresentDebt = customerPresentDebt;
    }

    public String getCustomerWebsite() {
        return customerWebsite;
    }

    public void setCustomerWebsite(String customerWebsite) {
        this.customerWebsite = customerWebsite;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Integer getGeographyCountry() {
        return geographyCountry;
    }

    public void setGeographyCountry(Integer geographyCountry) {
        this.geographyCountry = geographyCountry;
    }

    public Integer getGeographyDistrict() {
        return geographyDistrict;
    }

    public void setGeographyDistrict(Integer geographyDistrict) {
        this.geographyDistrict = geographyDistrict;
    }

    public Integer getGeographyProvince() {
        return geographyProvince;
    }

    public void setGeographyProvince(Integer geographyProvince) {
        this.geographyProvince = geographyProvince;
    }

    public Integer getGeographyWard() {
        return geographyWard;
    }

    public void setGeographyWard(Integer geographyWard) {
        this.geographyWard = geographyWard;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public long getPointsPromotionsCurrent() {
        return pointsPromotionsCurrent;
    }

    public void setPointsPromotionsCurrent(long pointsPromotionsCurrent) {
        this.pointsPromotionsCurrent = pointsPromotionsCurrent;
    }

    public long getPointsPromotionsTotal() {
        return pointsPromotionsTotal;
    }

    public void setPointsPromotionsTotal(long pointsPromotionsTotal) {
        this.pointsPromotionsTotal = pointsPromotionsTotal;
    }


    public int getRegUser() {
        return regUser;
    }

    public void setRegUser(int regUser) {
        this.regUser = regUser;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getTextCode() {
        return textCode;
    }

    public void setTextCode(String textCode) {
        this.textCode = textCode;
    }

    public Date getUpdDttm() {
        return updDttm;
    }

    public void setUpdDttm(Date updDttm) {
        this.updDttm = updDttm;
    }

    public int getUpdUser() {
        return updUser;
    }

    public void setUpdUser(int updUser) {
        this.updUser = updUser;
    }

    public String getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(String userOwner) {
        this.userOwner = userOwner;
    }

    public Integer getUsers() {
        return userId;
    }

    public void setUsers(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return getCustomerName();
    }
}
