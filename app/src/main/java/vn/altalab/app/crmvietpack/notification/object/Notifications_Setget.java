package vn.altalab.app.crmvietpack.notification.object;

import android.text.Html;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Notifications_Setget implements Serializable {

    private Integer notificationId;
    private String notificationName;
    private Integer objectId;
    private String timeChange;
    private Integer userChange;
    private Integer viewStatus;
    private String changeType;
    private Integer typeObject;
    private Integer badge;

    public Integer getBadge() {
        return badge;
    }

    public void setBadge(Integer badge) {
        this.badge = badge;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationName() {
        return notificationName;
    }

    public void setNotificationName(String notificationName) {
        this.notificationName = notificationName;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getTimeChange() {
        return timeChange;
    }

    public void setTimeChange(String timeChange) {
        this.timeChange = timeChange;
    }

    public Integer getTypeObject() {
        return typeObject;
    }

    public void setTypeObject(Integer typeObject) {
        this.typeObject = typeObject;
    }

    public Integer getUserChange() {
        return userChange;
    }

    public void setUserChange(Integer userChange) {
        this.userChange = userChange;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public Integer getViewStatus() {
        return viewStatus;
    }

    public void setViewStatus(Integer viewStatus) {
        this.viewStatus = viewStatus;
    }

    public String getFieldChangeType() {
        try {
            JSONObject object = new JSONObject(this.getChangeType());
            String field = object.optString("fields");
            Integer type = object.optInt("type");
            return Html.fromHtml(field.trim()).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String toString() {
        return this.getNotificationName();
    }
}
