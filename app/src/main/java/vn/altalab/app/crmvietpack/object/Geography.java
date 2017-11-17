package vn.altalab.app.crmvietpack.object;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by HieuDT on 5/30/2016.
 */
public class Geography extends RealmObject implements Serializable {

    @Index
    @PrimaryKey
    private long geographyId;

    private long geographyFatherId;
    private Integer geographyLevel;
    private String geographyCode;
    private String geographyName;
    private String geographyDescription;
    private Integer regUser;
    private Integer udpUser;
    private Date regDate;
    private Date udpDate;

    public String getGeographyCode() {
        return geographyCode;
    }

    public void setGeographyCode(String geographyCode) {
        this.geographyCode = geographyCode;
    }

    public String getGeographyDescription() {
        return geographyDescription;
    }

    public void setGeographyDescription(String geographyDescription) {
        this.geographyDescription = geographyDescription;
    }

    public long getGeographyFatherId() {
        return geographyFatherId;
    }

    public void setGeographyFatherId(long geographyFatherId) {
        this.geographyFatherId = geographyFatherId;
    }

    public long getGeographyId() {
        return geographyId;
    }

    public void setGeographyId(long geographyId) {
        this.geographyId = geographyId;
    }

    public Integer getGeographyLevel() {
        return geographyLevel;
    }

    public void setGeographyLevel(Integer geographyLevel) {
        this.geographyLevel = geographyLevel;
    }

    public String getGeographyName() {
        return geographyName;
    }

    public void setGeographyName(String geographyName) {
        this.geographyName = geographyName;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public Integer getRegUser() {
        return regUser;
    }

    public void setRegUser(Integer regUser) {
        this.regUser = regUser;
    }

    public Date getUdpDate() {
        return udpDate;
    }

    public void setUdpDate(Date udpDate) {
        this.udpDate = udpDate;
    }

    public Integer getUdpUser() {
        return udpUser;
    }

    public void setUdpUser(Integer udpUser) {
        this.udpUser = udpUser;
    }

    @Override
    public String toString() {
        return getGeographyName();
    }
}
