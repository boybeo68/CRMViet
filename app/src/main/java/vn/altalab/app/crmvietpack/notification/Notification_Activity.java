package vn.altalab.app.crmvietpack.notification;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import vn.altalab.app.crmvietpack.R;

public class Notification_Activity extends AppCompatActivity {

    TabLayout tabLayout;

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);

        // support Toolbar
        findViewId();

        getToolbar();

        Notification_TabAdapterFragment_Custom CUSTOM = new Notification_TabAdapterFragment_Custom(getSupportFragmentManager());
        CUSTOM.notifyDataSetChanged();

        tabLayout.setupWithViewPager(viewPager);

        viewPager.setAdapter(CUSTOM);
        viewPager.setOffscreenPageLimit(CUSTOM.getCount());

    }

    public void findViewId(){

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_Pager);

    }

    public void getToolbar(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvNotification = (TextView) findViewById(R.id.tvNotification);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvNotification.setText("Thông báo");

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
