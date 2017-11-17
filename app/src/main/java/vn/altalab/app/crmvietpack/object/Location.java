package vn.altalab.app.crmvietpack.object;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Tung on 12/19/2016.
 */

public class Location implements Serializable {
    public Location(double lat, double lng, Date date) {
        this.lat = lat;
        this.lng = lng;
        this.date = date;
    }

    //
    private long userId;
        private String userName;
    private String userFullname;
    private String adressloca;
    public String getAdressloca() {
        return adressloca;
    }

    public void setAdressloca(String adressloca) {
        this.adressloca = adressloca;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserFullname() {
        return userFullname;
    }

    public void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
    }

    private double lat;
    private double lng;
    private Date date;

    public Location() {

    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }



    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }


}
