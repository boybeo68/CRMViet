package vn.altalab.app.crmvietpack.service;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

import vn.altalab.app.crmvietpack.notification.object.Notifications_Setget;

/**
 * Created by Simple on 12/13/2016.
 */

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListener";
    private static final String GROUP_KEY_TOPIC = "crmviet_notification";
    private int numMessages = 0;

    @Override
    public void onMessageReceived(String s, Bundle bundle) {

//        String message = bundle.getString("data");

        Notifications_Setget notifications = new Notifications_Setget();
        notifications.setNotificationId(bundle.getInt("notification_id"));
        notifications.setNotificationName(bundle.getString("notification_name"));
        notifications.setTimeChange(bundle.getString("timechange"));
        notifications.setUserChange(bundle.getInt("userchange"));
        notifications.setViewStatus(bundle.getInt("viewstatus"));
        notifications.setChangeType(bundle.getString("changetype"));
        notifications.setTypeObject(Integer.parseInt(bundle.getString("typeobject")));

//        sendNotification(notifications);
    }

}
