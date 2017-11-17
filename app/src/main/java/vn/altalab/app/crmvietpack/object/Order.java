package vn.altalab.app.crmvietpack.object;

import java.io.Serializable;

/**
 * Created by Tung on 1/20/2017.
 */

public class Order implements Serializable {

    private long oderId;

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    private long customerId;
    private String quality;

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    private String oderCode;
    private String orderUserName;
    private String oderName;
    private String dayOder;
    private String deliveryDate;
    private String customerName;
    private String customerPhone;
    private String customerAdress;
    private int status;
    private long orderUser;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getOrderUser() {
        return orderUser;
    }

    public void setOrderUser(long orderUser) {
        this.orderUser = orderUser;
    }

    public String getOrderUserName() {
        return orderUserName;
    }

    public void setOrderUserName(String orderUser) {
        this.orderUserName = orderUser;
    }

    public int getStatus() {
        return status;
    }

    public int setStatus(int status) {
        this.status = status;
        return status;
    }

    private String moneyOder;

    public long getOderId() {
        return oderId;
    }

    public void setOderId(long oderId) {
        this.oderId = oderId;
    }

    public String getOderCode() {
        return oderCode;
    }

    public void setOderCode(String oderCode) {
        this.oderCode = oderCode;
    }

    public String getOderName() {
        return oderName;
    }

    public void setOderName(String oderName) {
        this.oderName = oderName;
    }

    public String getDayOder() {
        return dayOder;
    }

    public void setDayOder(String dayOder) {
        this.dayOder = dayOder;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerAdress() {
        return customerAdress;
    }

    public void setCustomerAdress(String customerAdress) {
        this.customerAdress = customerAdress;
    }

    public String getMoneyOder() {
        return moneyOder;
    }

    public void setMoneyOder(String moneyOder) {
        this.moneyOder = moneyOder;
    }
}
