package vn.altalab.app.crmvietpack.model;

import android.util.Log;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import vn.altalab.app.crmvietpack.object.Geography;

/**
 * Created by HieuDT on 5/30/2016.
 */
public class GeographyModel {

    public GeographyModel() {
    }

    public void createGeography(Realm realm) {
        Geography geography = new Geography();
        // Tao moi geography
        geography.setGeographyId(1);
        geography.setRegUser(1);
        Date date = new Date();
        geography.setRegDate(date);
        geography.setGeographyFatherId(-1);
        geography.setGeographyLevel(1);
        geography.setGeographyCode("VN");
        geography.setGeographyName("Việt Nam");
        geography.setGeographyDescription("Việt Nam");

        try {
            RealmResults<Geography> geographyList = realm.where(Geography.class).equalTo("geographyId", 1).findAll();
            if (geographyList.size() > 0) {
                Log.e("creategeography", geographyList.get(0).getGeographyName());
            } else {
                realm.beginTransaction();
                realm.copyToRealm(geography);
                realm.commitTransaction();
            }
        } catch (Exception e) {
            Log.d("ERROR", e.getLocalizedMessage());
        }

        geography.setGeographyId(10);
        geography.setRegUser(1);
        geography.setRegDate(date);
        geography.setGeographyFatherId(1);
        geography.setGeographyLevel(2);
        geography.setGeographyCode("VN.HN");
        geography.setGeographyName("Hà Nội");
        geography.setGeographyDescription("Hà Nội");

        try {
            final RealmResults<Geography> geographyList = realm.where(Geography.class).equalTo("geographyId", 1).findAll();
            if (geographyList.size() > 0) {
                Log.e("creategeography", geographyList.get(0).getGeographyName());
            } else {
                realm.beginTransaction();
                realm.copyToRealm(geography);
                realm.commitTransaction();
            }
        } catch (Exception e) {
            Log.d("ERROR", e.getLocalizedMessage());
        }

        geography.setGeographyId(19);
        geography.setRegUser(1);
        geography.setRegDate(date);
        geography.setGeographyFatherId(10);
        geography.setGeographyLevel(3);
        geography.setGeographyCode("VN.HN.QBD");
        geography.setGeographyName("Quận Ba Đình");
        geography.setGeographyDescription("Quận Ba Đình");

        try {
            final RealmResults<Geography> geographyList = realm.where(Geography.class).equalTo("geographyId", 19).findAll();
            if (geographyList.size() > 0) {
                Log.e("creategeography", geographyList.get(0).getGeographyName());
            } else {
                realm.beginTransaction();
                realm.copyToRealm(geography);
                realm.commitTransaction();
            }
        } catch (Exception e) {
            Log.d("ERROR", e.getLocalizedMessage());
        }

        geography.setGeographyId(31);
        geography.setRegUser(1);
        geography.setRegDate(date);
        geography.setGeographyFatherId(19);
        geography.setGeographyLevel(4);
        geography.setGeographyCode("VN.HN.QBD.PĐC");
        geography.setGeographyName("Phường Đội Cấn");
        geography.setGeographyDescription("Phường Đội Cấn");

        try {
            final RealmResults<Geography> geographyList = realm.where(Geography.class).equalTo("geographyId", 31).findAll();
            if (geographyList.size() > 0) {
                Log.e("creategeography", geographyList.get(0).getGeographyName());
            } else {
                realm.beginTransaction();
                realm.copyToRealm(geography);
                realm.commitTransaction();
            }
        } catch (Exception e) {
            Log.d("ERROR", e.getLocalizedMessage());
        }
    }

    public RealmResults<Geography> getGeographyCountry(Realm realm) {

        RealmResults<Geography> results = realm.where(Geography.class).equalTo("geographyLevel", 1).findAll();

        return results;
    }

    public RealmResults<Geography> getGeographyProvince(Realm realm, long fatherId) {

        RealmResults<Geography> results = realm.where(Geography.class).equalTo("geographyLevel", 2).equalTo("geographyFatherId", fatherId).findAll();

        return results;
    }

    public RealmResults<Geography> getGeographyDistrict(Realm realm, long fatherId) {

        RealmResults<Geography> results = realm.where(Geography.class).equalTo("geographyLevel", 3).equalTo("geographyFatherId", fatherId).findAll();

        return results;
    }

    public RealmResults<Geography> getGeographyWard(Realm realm, long fatherId) {

        RealmResults<Geography> results = realm.where(Geography.class).equalTo("geographyLevel", 4).equalTo("geographyFatherId", fatherId).findAll();

        return results;
    }

}
