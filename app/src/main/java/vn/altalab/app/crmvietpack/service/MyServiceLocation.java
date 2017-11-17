package vn.altalab.app.crmvietpack.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class MyServiceLocation extends Service implements LocationListener {
    private LocationManager locationManager;
    private static double lat, lng;
    String userId;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;
    public static Timer timer;

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (settings == null) {
            settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1, this);


        } catch (SecurityException e) {
            e.printStackTrace();
        }

        if (timer == null) {
            timer = new Timer();
                timer.scheduleAtFixedRate(new MainTask(), 1000, 900000);
        }

        return START_STICKY;
    }

    private class MainTask extends TimerTask {
        @Override
        public void run() {
            Calendar c = Calendar.getInstance();
            long min = c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE);
            if (480 <= min && min <= 1080) {
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                    || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                toastHandler.sendEmptyMessage(0);
            }
            }
        }
    }

    private final Handler toastHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            doLocation();
        }
    };

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();

    }

    public void doLocation() {

        userId = settings.getString(getResources().getString(R.string.user_id_object), "");

        String url = settings.getString("api_server", "") + "/api/v1/checkin";
        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("USER_ID", userId);
        jsonParams.put(getResources().getString(R.string.latitude), String.valueOf(lat));
        jsonParams.put(getResources().getString(R.string.longitude), String.valueOf(lng));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("doLogin", volleyError.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=UTF-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };

        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        MySingleton.getInstance(this).addToRequestQueue(request);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);
    }

    @Override
    public void onProviderDisabled(String provider) {
//        doLocationFalse();
    }

    public void doLocationFalse() {

        userId = settings.getString(getResources().getString(R.string.user_id_object), "");

        String url = settings.getString("api_server", "") + "/api/v1/checkin";
        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("USER_ID", userId);
        jsonParams.put(getResources().getString(R.string.latitude), "" + 777);
        jsonParams.put(getResources().getString(R.string.longitude), "" + 777);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Log.e("doLogin", volleyError.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=UTF-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };

        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        MySingleton.getInstance(this).addToRequestQueue(request);

    }


}
