package vn.altalab.app.crmvietpack.report.chart;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import vn.altalab.app.crmvietpack.R;

public class RevActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;
    private TabLayout tabLayout;
    ViewPager pager;
    private final static int REQUEST_CODE = 1;
    RevAdapter adapter;
    FragmentManager manager;
    LinearLayout doanhthu, kpi;
    public static Toolbar toolbar;
    Button button;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    static int monthdate, yearfrom;
    static String btMonth = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rev);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        button = (Button) findViewById(R.id.btTimeRp);
        if (btMonth.equals("")) {
            button.setText("3 tháng");
        } else {
            button.setText(btMonth);
        }


        calendar = Calendar.getInstance();


        doanhthu = (LinearLayout) findViewById(R.id.doanhthu);
        kpi = (LinearLayout) findViewById(R.id.kpi);
        doanhthu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RevActivity.this, Price_Activity.class);
                startActivity(intent);
                finish();
            }
        });
        kpi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        pager = (ViewPager) findViewById(R.id.viewPager);

        manager = getSupportFragmentManager();
        adapter = new RevAdapter(manager);
        pager.setAdapter(adapter);

        tabLayout.setupWithViewPager(pager);

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setTabsFromPagerAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RevActivity.this);
                builder.create();
                List<String> listOptions = new ArrayList<>();
                listOptions.add("3 tháng");
                listOptions.add("6 tháng");
                listOptions.add("9 tháng");
                listOptions.add("12 tháng");
                ListAdapter listAdapter = new ArrayAdapter<>(RevActivity.this, android.R.layout.simple_list_item_1, listOptions);
                builder.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            btMonth = "3 tháng";
                            simpleDateFormat = new SimpleDateFormat("M/yyyy", Locale.getDefault());
                            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 2);

                            String a = simpleDateFormat.format(calendar.getTime());
                            String b[] = a.split("/");

                            monthdate = Integer.parseInt(b[0]);
                            yearfrom = Integer.parseInt(b[1]);

                            Intent intent = new Intent(RevActivity.this, RevActivity.class);
                            startActivity(intent);
                            finish();


                        }
                        if (which == 1) {
                            btMonth = "6 tháng";
                            simpleDateFormat = new SimpleDateFormat("M/yyyy", Locale.getDefault());
                            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 5);

                            String a = simpleDateFormat.format(calendar.getTime());
                            String b[] = a.split("/");

                            monthdate = Integer.parseInt(b[0]);
                            yearfrom = Integer.parseInt(b[1]);

                            Intent intent = new Intent(RevActivity.this, RevActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        if (which == 2) {
                            btMonth = "9 tháng";

                            simpleDateFormat = new SimpleDateFormat("M/yyyy", Locale.getDefault());
                            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 8);

                            String a = simpleDateFormat.format(calendar.getTime());
                            String b[] = a.split("/");

                            monthdate = Integer.parseInt(b[0]);
                            yearfrom = Integer.parseInt(b[1]);

                            Intent intent = new Intent(RevActivity.this, RevActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        if (which == 3) {
                            btMonth = "12 tháng";

                            simpleDateFormat = new SimpleDateFormat("M/yyyy", Locale.getDefault());
                            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 11);

                            String a = simpleDateFormat.format(calendar.getTime());
                            String b[] = a.split("/");

                            monthdate = Integer.parseInt(b[0]);
                            yearfrom = Integer.parseInt(b[1]);

                            Intent intent = new Intent(RevActivity.this, RevActivity.class);
                            startActivity(intent);
                            finish();

                        }

                    }
                });
                builder.show();
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
