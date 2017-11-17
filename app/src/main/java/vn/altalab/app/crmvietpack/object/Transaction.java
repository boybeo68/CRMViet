package vn.altalab.app.crmvietpack.object;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Simple on 6/9/2016.
 */
public class Transaction extends RealmObject implements Serializable {

    private long transactionId;

    private Integer customerId;
    private TransactionType transactionType;

    private String transactionName;
    private Users users;
    private Date startDate;
    private Date endDate;
    private Date finishDate;
    private String transactionDescription;
    private Integer regUser;
    private Integer updUser;
    private Date regDttm;
    private Date updDttm;
    private Integer status;
    private Integer dateAlert;
    private String checkShare;
    private String transactionResult;
    private Integer priority ;
    private String transactionNameText;
    private String transactionDescriptionText;
    private String transactionResultText;
    private Integer customerPrepresentativeId;
    private String customerPrepresentative;
    private String customerPrepresentativePosition;
    private String customerPrepresentativePhone;
    private String customerName;
    private int transactionTypeId;
    private String transactionAssigner;
    private String transactionUserAssigner;



    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public void setRegDttm(Date regDttm) {
        this.regDttm = regDttm;
    }

    public Integer getRegUser() {
        return regUser;
    }

    public void setRegUser(Integer regUser) {
        this.regUser = regUser;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public Integer getUpdUser() {
        return updUser;
    }

    public void setUpdUser(Integer updUser) {
        this.updUser = updUser;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getTransactionTypeId() {
        return transactionTypeId;
    }

}
