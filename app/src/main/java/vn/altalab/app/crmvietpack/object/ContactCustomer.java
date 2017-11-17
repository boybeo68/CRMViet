package vn.altalab.app.crmvietpack.object;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by Luongnv on 10/10/2016.
 */
public class ContactCustomer extends RealmObject implements Serializable {

    private Integer contactId;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    private long customerId;
    private String contactFullName;
    private String contactEmail;
    private TblPosition position;
    private String contactMobiphone;
    private String contactWorkphone;
    private Date birthday;
    private String nickChat;
    private Integer gender;
    private String contactAddress;

    private int regUser;
    private Date regDttm;
    private int updUser;
    private Date updDttm;
    private Integer mainContact;
    @Ignore
    private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault());

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public String getContactFullName() {
        return contactFullName;
    }

    public void setContactFullName(String contactFullName) {
        this.contactFullName = contactFullName;
    }

    public TblPosition getPosition() {
        return position;
    }

    public void setPosition(TblPosition position) {
        this.position = position;
    }

    public String getContactMobiphone() {
        return contactMobiphone;
    }

    public void setContactMobiphone(String contactMobiphone) {
        this.contactMobiphone = contactMobiphone;
    }

    public String getContactWorkphone() {
        return contactWorkphone;
    }

    public void setContactWorkphone(String contactWorkphone) {
        this.contactWorkphone = contactWorkphone;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getRegUser() {
        return regUser;
    }

    public void setRegUser(int regUser) {
        this.regUser = regUser;
    }

    public Date getRegDttm() {
        return regDttm;
    }

    public void setRegDttm(Date regDttm) {
        this.regDttm = regDttm;
    }

    public int getUpdUser() {
        return updUser;
    }

    public void setUpdUser(int updUser) {
        this.updUser = updUser;
    }

    public Date getUpdDttm() {
        return updDttm;
    }

    public void setUpdDttm(Date updDttm) {
        this.updDttm = updDttm;
    }

    public String getNickChat() {
        return nickChat;
    }

    public void setNickChat(String nickChat) {
        this.nickChat = nickChat;
    }

    public Integer getMainContact() {
        return mainContact;
    }

    public void setMainContact(Integer mainContact) {
        this.mainContact = mainContact;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    @Override
    public String toString() {
        return getContactFullName();
    }

    public String getDateFormat() {
        return format.format(this.getBirthday());
    }

}
