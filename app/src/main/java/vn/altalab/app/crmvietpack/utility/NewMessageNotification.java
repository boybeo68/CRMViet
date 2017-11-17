package vn.altalab.app.crmvietpack.utility;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.text.Html;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.notification.object.Notifications_Setget;
import vn.altalab.app.crmvietpack.transaction.TransactionDetail_Activity;

public class NewMessageNotification {

    private static final String NOTIFICATION_TAG = "NewMessage";

    private static final int NOTIFICATION_POPUP = 9;

    public static void notify(final Context context, final Notifications_Setget notifications, final int number) {

        final Resources res = context.getResources();

        final Bitmap picture = BitmapFactory.decodeResource(res, R.drawable.logocrmviet);

        final String ticker = notifications.toString();

        final String title = res.getString(
                R.string.new_message_notification_title_template, notifications.toString());

        final String text = res.getString(
                R.string.new_message_notification_placeholder_text_template, Html.fromHtml(notifications.getFieldChangeType()).toString());


        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.logocrmviet)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setTicker(ticker);
//                .setWhen(SystemClock.uptimeMillis());

        long s = SystemClock.elapsedRealtime();

//        if (notifications.getTypeObject() == 1) {

        Bundle bundle = new Bundle();
        Intent intent = new Intent(context, TransactionDetail_Activity.class);
        bundle.putString("transaction_id", "" + notifications.getObjectId());
        bundle.putInt("typeobject", notifications.getTypeObject());
        bundle.putInt("from_activity", NOTIFICATION_POPUP);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.ic_action_view, res.getString(R.string.view), pendingIntent);

//        }

        builder.setStyle(new NotificationCompat.BigTextStyle()

                .bigText(text)

                .setBigContentTitle(title)

                .setSummaryText("http://crmviet.vn"))

                .addAction(
                        R.drawable.ic_action_stat_reply,
                        res.getString(R.string.action_reply),
                        null)

                .setAutoCancel(true);

        notify(context, builder.build());
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }

}
