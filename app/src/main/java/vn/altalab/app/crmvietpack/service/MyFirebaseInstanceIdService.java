package vn.altalab.app.crmvietpack.service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import vn.altalab.app.crmvietpack.utility.Config;
import vn.altalab.app.crmvietpack.utility.MySingleton;

/**
 * Created by Simple on 12/5/2016.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;

    private static final String TAG = "MyFirebaseIDService";
    private static final String CRMVIET_TOPIC = "crmviet";

    @Override
    public void onTokenRefresh() {
        // If you need to handle the generation of a token, initially or
        // after a refresh this is where you should do that.

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "FCM Token: " + token);
        if (settings == null) {
            settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        }
        storeRegIdInPref(token);

        sendRegistrationToServer(token);

        Intent regisComplete = new Intent(Config.REGISTRATION_COMPLETE);
        Bundle bundle = new Bundle();
        bundle.putString("token", token);
        regisComplete.putExtras(bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(regisComplete);
// Once a token is generated, we subscribe to topic.
//        FirebaseMessaging.getInstance()
//                .subscribeToTopic(CRMVIET_TOPIC);
    }

    private void sendRegistrationToServer(String token) {
        String url = settings.getString("api_server", "") + "/api/v1/users/token";

        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("USER_ID", settings.getString("USER_ID", ""));
        jsonParams.put("device_token", token);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && "success".equals(jsonObject.optString("messages"))) {
                    Log.d("registerdevice", "======= Gửi device token thành công =======");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("serviceerror", volleyError.toString());
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

    private void storeRegIdInPref(String token) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("regId", token);
        editor.apply();
    }
}
