package vn.altalab.app.crmvietpack.service;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;
import vn.altalab.app.crmvietpack.CrmMainActivity;
import vn.altalab.app.crmvietpack.notification.object.Notifications_Setget;
import vn.altalab.app.crmvietpack.utility.NewMessageNotification;

/**
 * Created by Simple on 12/5/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFMService";
    private static final String GROUP_KEY_TOPIC = "crmviet_notification";
    private int numMessages;
    private SharedPreferences settings;
    private static final String PREFS_NAME = "CRMVietPrefs";
    CrmMainActivity crmMainActivity = new CrmMainActivity();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (settings == null) {
            settings = getSharedPreferences(PREFS_NAME, 0);
        }
        if (remoteMessage == null) return;

        if (remoteMessage.getNotification() != null) {
//            sendNotification(remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().size() > 0) {
            handleNotification(remoteMessage.getData());
            crmMainActivity.count =  crmMainActivity.count+1;
            ShortcutBadger.applyCount(MyFirebaseMessagingService.this, crmMainActivity.count);
        }
        Log.d(TAG, "FCM Message Id: " + remoteMessage.getMessageId());
        Log.d(TAG, "FCM Notification Message: " +
                remoteMessage.getNotification());
        Log.d(TAG, "FCM Data Message: " + remoteMessage.getData());

    }



    private void handleNotification(Map<String, String> messagesData) {

        Notifications_Setget notifications = new Notifications_Setget();
        notifications.setNotificationId(Integer.parseInt(messagesData.get("notification_id")));
        notifications.setNotificationName(messagesData.get("notification_name"));
        notifications.setTimeChange(messagesData.get("timechange"));
        notifications.setObjectId(Integer.parseInt(messagesData.get("object_id")));
        notifications.setUserChange(Integer.parseInt(messagesData.get("userchange")));
        notifications.setViewStatus(Integer.parseInt(messagesData.get("viewstatus")));
        notifications.setChangeType(messagesData.get("changetype"));
        notifications.setTypeObject(Integer.parseInt(messagesData.get("typeobject")));
        notifications.setUserChange(Integer.parseInt(messagesData.get("userchange")));
        notifications.setBadge(Integer.parseInt(messagesData.get("badge")));

        NewMessageNotification.notify(this, notifications, ++numMessages);
        Log.e("number", "" + numMessages);
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> taskInfoList = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : taskInfoList) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(1);
            ComponentName componentName = taskInfoList.get(0).topActivity;
            if (componentName.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }
}
