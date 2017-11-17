package vn.altalab.app.crmvietpack.object;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Simple on 6/3/2016.
 */
public class UsersGroup extends RealmObject implements Serializable {

    @Index
    @PrimaryKey
    private Integer userGroupId;

    private Integer userGroupFatherId;
    private String userGroupName;
    private String userGroupDescription;
    private Integer status;
    private Integer regUser;
    private Integer udpUser;
    private Date regDate;
    private Date udpDate;

    private RealmList<Users> users = new RealmList<>();

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    public Integer getUserGroupFatherId() {
        return userGroupFatherId;
    }

    public void setUserGroupFatherId(Integer userGroupFatherId) {
        this.userGroupFatherId = userGroupFatherId;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }

    public String getUserGroupDescription() {
        return userGroupDescription;
    }

    public void setUserGroupDescription(String userGroupDescription) {
        this.userGroupDescription = userGroupDescription;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRegUser() {
        return regUser;
    }

    public void setRegUser(Integer regUser) {
        this.regUser = regUser;
    }

    public Integer getUdpUser() {
        return udpUser;
    }

    public void setUdpUser(Integer udpUser) {
        this.udpUser = udpUser;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public Date getUdpDate() {
        return udpDate;
    }

    public void setUdpDate(Date udpDate) {
        this.udpDate = udpDate;
    }

    public RealmList<Users> getUsers() {
        return users;
    }

    public void setUsers(RealmList<Users> users) {
        this.users = users;
    }
}
