package vn.altalab.app.crmvietpack;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class CrmSplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crm_splash_screen);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();

        Realm.setDefaultConfiguration(realmConfiguration);

        Thread timeToShow = new Thread() {
            @Override
            public void run() {
                Configuration config = getBaseContext().getResources().getConfiguration();
                Locale locale = new Locale("vi");
                Locale.setDefault(locale);
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                try {
                    sleep(500);
                } catch (InterruptedException ie) {
                    Log.e("CrmSplashScreen", ie.getLocalizedMessage());
                } finally {
                    Intent intent = new Intent(CrmSplashScreenActivity.this, CrmLoginActivity.class);
                    startActivity(intent);
                }
            }
        };
        timeToShow.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
