package vn.altalab.app.crmvietpack.contract.object;

import java.io.Serializable;

/**
 * Created by boybe on 04/28/2017.
 */

public class Contract implements Serializable {
    private Integer contractID;
    private String contractCode;
    private String contractName;
    private String contractPrice;
    private Integer status;
    private Integer contractOwnerId;
    private String debt;
    private String paid;
    private String contractTypeId;
    private String contractDescription;
    private String profitMoney;
    private String contractOwnerName;

    private String statusName;
    private String customerName;
    private String startDate;
    private String endDate;
    private long customerId;

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public Contract(Integer contractID, String contractCode, String contractName, String contractPrice, Integer status, Integer contractOwnerId, String debt, String paid, String contractTypeId, String contractDescription, String profitMoney, String contractOwnerName, String statusName, String customerName, String startDate, String endDate, long customerId) {
        this.contractID = contractID;
        this.contractCode = contractCode;
        this.contractName = contractName;
        this.contractPrice = contractPrice;
        this.status = status;
        this.contractOwnerId = contractOwnerId;
        this.debt = debt;
        this.paid = paid;
        this.contractTypeId = contractTypeId;
        this.contractDescription = contractDescription;
        this.profitMoney = profitMoney;
        this.contractOwnerName = contractOwnerName;
        this.statusName = statusName;
        this.customerName = customerName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.customerId = customerId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getContractOwnerId() {
        return contractOwnerId;
    }

    public void setContractOwnerId(Integer contractOwnerId) {
        this.contractOwnerId = contractOwnerId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Contract() {
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }


    public Integer getContractID() {
        return contractID;
    }

    public void setContractID(Integer contractID) {
        this.contractID = contractID;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getContractPrice() {
        return contractPrice;
    }

    public void setContractPrice(String contractPrice) {
        this.contractPrice = contractPrice;
    }


    public String getDebt() {
        return debt;
    }

    public void setDebt(String debt) {
        this.debt = debt;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getContractTypeId() {
        return contractTypeId;
    }

    public void setContractTypeId(String contractTypeId) {
        this.contractTypeId = contractTypeId;
    }

    public String getContractDescription() {
        return contractDescription;
    }

    public void setContractDescription(String contractDescription) {
        this.contractDescription = contractDescription;
    }

    public String getProfitMoney() {
        return profitMoney;
    }

    public void setProfitMoney(String profitMoney) {
        this.profitMoney = profitMoney;
    }

    public String getContractOwnerName() {
        return contractOwnerName;
    }

    public void setContractOwnerName(String contractOwnerName) {
        this.contractOwnerName = contractOwnerName;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
