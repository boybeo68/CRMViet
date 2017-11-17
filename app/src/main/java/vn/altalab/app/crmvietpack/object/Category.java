package vn.altalab.app.crmvietpack.object;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Luongnv on 10/25/2016.
 */

public class Category extends RealmObject implements Serializable {
    @Index
    @PrimaryKey
    private Integer categoryId;

    private String categoryCode;
    private String categoryName;
    private String description;
    private Integer regUser;
    private Integer updUser;
    private Date regDate;
    private Date updDate;
    private Integer categoryFatherId;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRegUser() {
        return regUser;
    }

    public void setRegUser(Integer regUser) {
        this.regUser = regUser;
    }

    public Integer getUpdUser() {
        return updUser;
    }

    public void setUpdUser(Integer updUser) {
        this.updUser = updUser;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public Date getUpdDate() {
        return updDate;
    }

    public void setUpdDate(Date updDate) {
        this.updDate = updDate;
    }

    public Integer getCategoryFatherId() {
        return categoryFatherId;
    }

    public void setCategoryFatherId(Integer categoryFatherId) {
        this.categoryFatherId = categoryFatherId;
    }
}
