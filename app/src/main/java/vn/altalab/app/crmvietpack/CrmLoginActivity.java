package vn.altalab.app.crmvietpack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import vn.altalab.app.crmvietpack.utility.MD5Utils;
import vn.altalab.app.crmvietpack.utility.MySingleton;

/**
 * A login screen that offers login via email/password.
 */
public class CrmLoginActivity extends AppCompatActivity {

    private static final int REQUEST_READ_CONTACTS = 0;

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "admin:123456", "hieudt:123456"
    };

    private EditText mPasswordView, mUsernameView, mAPIConfig;
    private View mProgressView;
    private View mLoginFormView;
    CheckBox checkBox;
    TextView domain;
    String a;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings,setting2;
    SharedPreferences.Editor editor;

    // Variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crm_login);

        // Hide action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        if (settings == null) {
            settings = getSharedPreferences(PREFS_NAME, 0);

        }
        if(setting2==null) {
            setting2 = getSharedPreferences("login", 0);
        }
        if (!"".equals(settings.getString(getResources().getString(R.string.user_name_object), ""))) {
            Intent intent = new Intent(CrmLoginActivity.this, CrmMainActivity.class);
            startActivity(intent);
            finish();
        }


        // Set up the login form.
        checkBox = (CheckBox) findViewById(R.id.checkgnh);
        a = setting2.getString("check", "");
        if (a.equals("1")) {
            checkBox.setChecked(true);
        } else if (a.equals("0")) {
            checkBox.setChecked(false);
        }
        domain = (TextView) findViewById(R.id.tvDomain);
        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);

        mUsernameView.setText(setting2.getString("username", ""));
        mPasswordView.setText(setting2.getString("pass", ""));



        mAPIConfig = (EditText) findViewById(R.id.edtAPIConfig);
        if (!"".equals(settings.getString("api_server", ""))) {
            mAPIConfig.setText(settings.getString("api_server", ""));
            mAPIConfig.setVisibility(View.INVISIBLE);
            domain.setVisibility(View.VISIBLE);
            mPasswordView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        }
        if ("".equals(settings.getString("api_server", ""))) {
            mAPIConfig.setVisibility(View.VISIBLE);
            mAPIConfig.setImeOptions(EditorInfo.IME_ACTION_DONE);
            domain.setVisibility(View.INVISIBLE);
        } else {

        }
        domain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                domain.setVisibility(View.INVISIBLE);
                mAPIConfig.setVisibility(View.VISIBLE);
                mAPIConfig.setImeOptions(EditorInfo.IME_ACTION_DONE);
            }
        });
        mAPIConfig.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                domain.setVisibility(View.VISIBLE);
                mAPIConfig.setVisibility(View.INVISIBLE);
                if ("".equals(mAPIConfig.getText().toString().trim())) {
                    Toast.makeText(CrmLoginActivity.this, view.getContext().getResources().getString(R.string.login_empty_api_server), Toast.LENGTH_SHORT).show();
                    return;
                }
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    public void showConfigAPIDialog(final View v) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edtAPIConfig = (EditText) dialogView.findViewById(R.id.edtAPIConfig);
        if (!"".equals(settings.getString("api_server", ""))) {
            edtAPIConfig.setText(settings.getString("api_server", ""));
        }

        dialogBuilder.create();
        dialogBuilder.setTitle(v.getContext().getResources().getString(R.string.login_title_dialog));
        dialogBuilder.setMessage(v.getContext().getResources().getString(R.string.login_message_dialog));
        dialogBuilder.setPositiveButton(v.getContext().getResources().getString(R.string.update), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if ("".equals(edtAPIConfig.getText().toString())) {
                    Toast.makeText(CrmLoginActivity.this, v.getContext().getResources().getString(R.string.login_toast_dialog), Toast.LENGTH_SHORT).show();
                } else {
                    if (edtAPIConfig.getText().toString().contains("http://") || edtAPIConfig.getText().toString().contains("http://")) {
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("api_server", edtAPIConfig.getText().toString().trim());
                        editor.apply();
                    } else {
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("api_server", "http://" + edtAPIConfig.getText().toString().trim());
                        editor.apply();
                    }
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            }
        }).setNegativeButton(v.getContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        dialogBuilder.show();
    }

    public void attemptLogin() {

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();
        String url = mAPIConfig.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getResources().getString(R.string.error_incorrect_password));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.

        if (checkBox.isChecked()) {
            editor = setting2.edit();
            editor.putString("username", mUsernameView.getText().toString().trim());
            editor.putString("pass", mPasswordView.getText().toString());
            editor.putString("check", String.valueOf(1));
            editor.commit();

        } else {
            editor = setting2.edit();
            editor.putString("username", "");
            editor.putString("pass", "");
            editor.putString("check", String.valueOf(0));
            editor.commit();
        }

        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(url)) {
            mAPIConfig.setError(getString(R.string.error_field_required));
            focusView = mAPIConfig;
            cancel = true;
        }

        // Save API address to SharedPreferences
        if (url.contains("http://") || url.contains("https://")) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("api_server", mAPIConfig.getText().toString().trim());
            editor.apply();
        } else {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("api_server", "http://" + mAPIConfig.getText().toString().trim());
            editor.apply();
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.

            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            password = MD5Utils.encryptMD5(password);
            showProgress(true);
            doLogin(username, password);
        }

    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void doLogin(String username, String password) {

        String url = settings.getString("api_server", "") + "/api/v1/login";


        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put(getResources().getString(R.string.username), username);
        jsonParams.put(getResources().getString(R.string.password), password);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (jsonObject != null && !"".equals(jsonObject.toString()) && getResources().getString(R.string.success).equals(jsonObject.optString(getResources().getString(R.string.messages)))) {

                            // Edit SharePreferences
                            SharedPreferences.Editor editor = settings.edit();
                            // Get JsonObject
                            JSONObject object = jsonObject.optJSONObject(getResources().getString(R.string.user));
                            editor.putString(getResources().getString(R.string.user_id_object), object.optString(getResources().getString(R.string.user_id)));
                            editor.putString(getResources().getString(R.string.user_name_object), object.optString(getResources().getString(R.string.user_name)));
                            editor.putString(getResources().getString(R.string.user_full_name_object), object.optString(getResources().getString(R.string.user_full_name)));
                            editor.putString(getResources().getString(R.string.user_email_object), object.optString(getResources().getString(R.string.user_email)));
                            editor.putString(getResources().getString(R.string.owner_object), object.optString(getResources().getString(R.string.owner)));
                            editor.putString(getResources().getString(R.string.user_subset_object), object.optString(getResources().getString(R.string.user_subset)));
                            editor.apply();

                            Intent intent = new Intent(CrmLoginActivity.this, CrmMainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(CrmLoginActivity.this, getResources().getString(R.string.sign_in_fails), Toast.LENGTH_SHORT).show();
                            showProgress(false);


                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showProgress(false);
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
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        MySingleton.getInstance(this).addToRequestQueue(request);
    }


}

