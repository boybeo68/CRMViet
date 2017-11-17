package vn.altalab.app.crmvietpack.object;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Tung on 8/29/2017.
 */

public class AdressLocation extends RealmObject implements Serializable {
  private   String adress;

    public String getAdress(String formatted_address) {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
}
