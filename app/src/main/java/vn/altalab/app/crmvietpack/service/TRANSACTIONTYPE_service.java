package vn.altalab.app.crmvietpack.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import vn.altalab.app.crmvietpack.Link;
import vn.altalab.app.crmvietpack.Shared_Preferences;
import vn.altalab.app.crmvietpack.transaction.json.TransactionType_Json;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class TRANSACTIONTYPE_service extends Service {

    Shared_Preferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = new Shared_Preferences(this, "TRANSACTION_TYPE");
        load_TYPE();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void load_TYPE(){

        Link link = new Link(this);

        String url = link.get_TYPE();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("TRANSACTIONTYPE_ser", "JSONObject: " + jsonObject);

                if (jsonObject != null){

                    final TransactionType_Json transactionTypeJson = new TransactionType_Json(String.valueOf(jsonObject));

                    if (transactionTypeJson.getIsStatus() == true) {
                        Log.e("TRANSACTIONTYPE_ser", "data: " + jsonObject);
                        sharedPreferences.putString("type", String.valueOf(jsonObject));
                    }

                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TRANSACTIONTYPE", "Error: " + volleyError);
                }
            })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=UTF-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };

        jsonObjectRequest.setShouldCache(false);
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }

}
