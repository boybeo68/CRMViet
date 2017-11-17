package vn.altalab.app.crmvietpack.customer.customer_detail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.Shared_Preferences;
import vn.altalab.app.crmvietpack.customer.customer_detail.adapter.CustomerDetail_MainAdapter;
import vn.altalab.app.crmvietpack.notification.Notification_Activity;

public class CustomerDetail_Activity extends AppCompatActivity {
    ViewPager viewPager = null;
    TabLayout tabLayout = null;
    Toolbar toolbar;
    CustomerDetail_MainAdapter customerDetailMainAdapter;
    public static long customer_Id;
    public static String customer_name;
    private static final String PREFS_NAME = "CRMVietPrefs";

    ProgressDialog pDialog;
    Shared_Preferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customerdetail_activity);

        Action();
    }

    public void findViewById(){
        viewPager= (ViewPager) findViewById(R.id.viewPager);
        tabLayout= (TabLayout) findViewById(R.id.tabLayout);
    }

    public void Action(){

      // Khai báo Id
      findViewById();

      // Khai bao sharedPreferences
      if (sharedPreferences == null) {
          sharedPreferences = new Shared_Preferences(this, PREFS_NAME);
      }

      getToolbar();

      Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            customer_Id = bundle.getLong("customer_id");
            customer_name=bundle.getString("customer_name");

        tabLayout.setupWithViewPager(viewPager);
        customerDetailMainAdapter = new CustomerDetail_MainAdapter(getSupportFragmentManager());
        customerDetailMainAdapter.notifyDataSetChanged();
        viewPager.setAdapter(customerDetailMainAdapter);
        viewPager.setOffscreenPageLimit(customerDetailMainAdapter.getCount());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.e("CUSTOMERDETAIL", "resultCode: " + resultCode + "-" + "requestCode: " + requestCode);
//        if (resultCode == 0 || resultCode == 1 || resultCode == 2){
//            customerDetailMainAdapter.notifyDataSetChanged();
//            viewPager.setAdapter(customerDetailMainAdapter);
//            tabLayout.getTabAt(resultCode).select();
//        }
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

    public void getToolbar(){

        if (getSupportActionBar() == null) {

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            TextView tvNotification = (TextView) findViewById(R.id.tvNotification);
            LinearLayout llBell = (LinearLayout) findViewById(R.id.llBell);
            tvNotification.setText("Chi tiết khách hàng");

            llBell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(CustomerDetail_Activity.this, Notification_Activity.class);
                    intent.putExtra("type_object", 1);
                    startActivity(intent);

                }

            });

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (pDialog != null && pDialog.isShowing())
                pDialog.dismiss();
    }
}
