package vn.altalab.app.crmvietpack.report.object;

/**
 * Created by Tung on 4/14/2017.
 */

public class ReportPrice {

    private String UserFullName;
    private String UserName;
    private String Price;
    private String Payment;
    private String Debt;
    private String QUOTES;
    private String FORECASTS;
    private  String ORDERS;
    private  String CONTRACTS;

    public String getQUOTES() {
        return QUOTES;
    }

    public void setQUOTES(String QUOTES) {
        this.QUOTES = QUOTES;
    }

    public String getFORECASTS() {
        return FORECASTS;
    }

    public void setFORECASTS(String FORECASTS) {
        this.FORECASTS = FORECASTS;
    }

    public String getORDERS() {
        return ORDERS;
    }

    public void setORDERS(String ORDERS) {
        this.ORDERS = ORDERS;
    }

    public String getCONTRACTS() {
        return CONTRACTS;
    }

    public void setCONTRACTS(String CONTRACTS) {
        this.CONTRACTS = CONTRACTS;
    }

    public String getTRANSACTIONS() {
        return TRANSACTIONS;
    }

    public void setTRANSACTIONS(String TRANSACTIONS) {
        this.TRANSACTIONS = TRANSACTIONS;
    }

    private String TRANSACTIONS;

    public String getUserFullName() {
        return UserFullName;
    }

    public void setUserFullName(String userFullName) {
        UserFullName = userFullName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getPayment() {
        return Payment;
    }

    public void setPayment(String payment) {
        Payment = payment;
    }

    public String getDebt() {
        return Debt;
    }

    public void setDebt(String debt) {
        Debt = debt;
    }
}

