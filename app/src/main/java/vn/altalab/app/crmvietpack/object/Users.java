package vn.altalab.app.crmvietpack.object;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by HieuDT on 5/25/2016.
 */
public class Users extends RealmObject implements Serializable {

    @Index
    @PrimaryKey
    private long userId;

    private String userName;
    private String userFullName;
    private String personalId;
    private String userAddress;
    private String userEmail;
    private String userMobile;
    private String userTelephone;
    private Integer userOwner;
    private String userSubset;
    private UsersGroup usersGroup;
    private Integer userStatus;
    private Date userBirthDay;
    private String userPosition;
    private String userDescription;
    private String userFromEmailMailChimp;

    @Ignore
    private Boolean checked;


    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public Date getUserBirthDay() {
        return userBirthDay;
    }

    public void setUserBirthDay(Date userBirthDay) {
        this.userBirthDay = userBirthDay;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserFromEmailMailChimp() {
        return userFromEmailMailChimp;
    }

    public void setUserFromEmailMailChimp(String userFromEmailMailChimp) {
        this.userFromEmailMailChimp = userFromEmailMailChimp;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(Integer userOwner) {
        this.userOwner = userOwner;
    }

    public String getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(String userPosition) {
        this.userPosition = userPosition;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserSubset() {
        return userSubset;
    }

    public void setUserSubset(String userSubset) {
        this.userSubset = userSubset;
    }

    public String getUserTelephone() {
        return userTelephone;
    }

    public void setUserTelephone(String userTelephone) {
        this.userTelephone = userTelephone;
    }


    public UsersGroup getUsersGroup() {
        return usersGroup;
    }

    public void setUsersGroup(UsersGroup usersGroup) {
        this.usersGroup = usersGroup;
    }

    @Override
    public String toString() {
        return getUserName();
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
