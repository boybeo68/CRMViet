package vn.altalab.app.crmvietpack.utility;

/**
 * Created by Simple on 12/12/2016.
 */

public class Config {

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";

    public static final String APP_ID = "1:896575330161:android:a46a621d3b45094f";
    public static final String PROJECT_ID = "crmviet-f355d";
    public static final String SENDER_ID = "896575330161";

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
}
