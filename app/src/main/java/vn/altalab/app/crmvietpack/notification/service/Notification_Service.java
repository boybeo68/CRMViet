package vn.altalab.app.crmvietpack.notification.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import vn.altalab.app.crmvietpack.CrmLoginActivity;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.notification.json.Notification_Json;

/**
 * Created by mac2 on 3/1/17.
 */

public class Notification_Service extends Service {

    private int mInterval = 5000;
    private Runnable mStatusChecker;
    private Handler handler = new Handler();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String PREFS_NAME = "CRMVietPrefs";
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(Notification_Service.this)
                        .setSmallIcon(R.drawable.logocrmviet)
                        .setContentTitle("Thong bao!")
                        .setContentText("Ban co thong bao moi!");

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(Notification_Service.this, CrmLoginActivity.class);
        // The stack builder object will contain an artificial back stack for the
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(Notification_Service.this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(CrmLoginActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());

        Vibrator v = (Vibrator) Notification_Service.this.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        sleep(5000);
                        getDataVolley();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public void getDataVolley(){

        RequestQueue queue = Volley.newRequestQueue(this);
        Notification_Link link = new Notification_Link();
        String url = link.link;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.e("NotificationService", "responseNotificationService: " + response);

                            Notification_Json notificationJson = new Notification_Json(response);

                            if (notificationJson.getStatus() == true){
                                if (!response.equals(sharedPreferences.getString("bellNotif","")) && !sharedPreferences.getString("bellNotif","").equals("")) {
                                    NotificationCompat.Builder mBuilder =
                                            new NotificationCompat.Builder(Notification_Service.this)
                                                    .setSmallIcon(R.drawable.ic_clock)
                                                    .setContentTitle("Thong bao!")
                                                    .setContentText("Ban co thong bao moi!");
                                    // Creates an explicit intent for an Activity in your app
                                    Intent resultIntent = new Intent(Notification_Service.this, CrmLoginActivity.class);
                                    // The stack builder object will contain an artificial back stack for the
                                    // started Activity.
                                    // This ensures that navigating backward from the Activity leads out of
                                    // your application to the Home screen.
                                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(Notification_Service.this);
                                    // Adds the back stack for the Intent (but not the Intent itself)
                                    stackBuilder.addParentStack(CrmLoginActivity.class);
                                    // Adds the Intent that starts the Activity to the top of the stack
                                    stackBuilder.addNextIntent(resultIntent);

                                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                                    mBuilder.setContentIntent(resultPendingIntent);
                                    NotificationManager mNotificationManager =
                                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    // mId allows you to update the notification later on.
                                    mNotificationManager.notify(1, mBuilder.build());

                                    Vibrator v = (Vibrator) Notification_Service.this.getSystemService(Context.VIBRATOR_SERVICE);
                                    // Vibrate for 500 milliseconds
                                    v.vibrate(500);

                                    try {
                                        MediaPlayer player;
                                        player = MediaPlayer.create(Notification_Service.this, R.raw.bellmusic);
                                        player.setLooping(false);
                                        player.start();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                editor.putString("bellNotif", response);
                                editor.commit();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("deathlinefragment", "error: " + error);
            }
        });

        queue.add(stringRequest);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
