package vn.altalab.app.crmvietpack.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.Shared_Preferences;
import vn.altalab.app.crmvietpack.customer.customer_detail.CustomerDetail_Activity;
import vn.altalab.app.crmvietpack.notification.Notification_Activity;
import vn.altalab.app.crmvietpack.transaction.TransactionDetail_Editation_Activity;

/**
 * Created by mac2 on 2/20/17.
 */

public class Home_NeededDo_Detail extends AppCompatActivity {

    TextView tvTitle, tvKhachhang, tvSodienthoai, tvOffice, tvNguoigiaoviec, tvDate, tvDes;
    private static final String PREFS_NAME = "CRMVietPrefs";
    Toolbar toolbar;

    Shared_Preferences sharedPreferences;
    FloatingActionButton FButton;

    String TRANSACTION_NAME_TEXT = "";
    String CUSTOMER_NAME = "";
    String TELEPHONE = "";
    String ASSIGNED_USER_NAME = "";
    String START_DATE = "";
    String END_DATE = "";
    String TRANSACTION_DESCRIPTION = "";
    String TRANSACTION_USER_NAME = "";
    String OFFICE_ADDRESS = "";
    String DATE = "";
    String TRANSACTION_ID = "";
    String CUSTOMER_ID = "";
    static int checkPhone = 1;
    static int checkEmail = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home__needed_do__detail);

        try {
            Action();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void Action() {

        getToolbar();

        sharedPreferences = new Shared_Preferences(this, PREFS_NAME);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvKhachhang = (TextView) findViewById(R.id.tvKhachhang);
        tvSodienthoai = (TextView) findViewById(R.id.tvSodienthoai);
        tvOffice = (TextView) findViewById(R.id.tvEmail);
        tvNguoigiaoviec = (TextView) findViewById(R.id.tvNguoigiaoviec);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvDes = (TextView) findViewById(R.id.tvDes);
        FButton = (FloatingActionButton) findViewById(R.id.FButton);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            checkPhone = bundle.getInt("checkPhone");
            checkEmail = bundle.getInt("checkEmail");
            try {
                CUSTOMER_ID = bundle.getString("CUSTOMER_ID");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                TRANSACTION_ID = bundle.getString("TRANSACTION_ID");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                TRANSACTION_NAME_TEXT = bundle.getString("TRANSACTION_NAME_TEXT");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                CUSTOMER_NAME = bundle.getString("CUSTOMER_NAME");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                TELEPHONE = bundle.getString("TELEPHONE");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                OFFICE_ADDRESS = bundle.getString("OFFICE_ADDRESS");
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                ASSIGNED_USER_NAME = bundle.getString("ASSIGNED_USER_NAME");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                TRANSACTION_USER_NAME = bundle.getString("TRANSACTION_USER_NAME");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                START_DATE = bundle.getString("START_DATE");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                END_DATE = bundle.getString("END_DATE");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                TRANSACTION_DESCRIPTION = bundle.getString("TRANSACTION_DESCRIPTION");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (TRANSACTION_NAME_TEXT != null && !TRANSACTION_NAME_TEXT.equals("null")) {
                tvTitle.setText(TRANSACTION_NAME_TEXT);
            }

            if (CUSTOMER_NAME != null && !CUSTOMER_NAME.equals("null")) {
                tvKhachhang.setText(CUSTOMER_NAME);
            }

            if (TELEPHONE != null && !TELEPHONE.equals("null")) {
                tvSodienthoai.setText(TELEPHONE);
            }

            if (OFFICE_ADDRESS != null && !OFFICE_ADDRESS.equals("null")) {
                tvOffice.setText(OFFICE_ADDRESS);
            }

            if (ASSIGNED_USER_NAME != null && !ASSIGNED_USER_NAME.equals("null")) {
                tvNguoigiaoviec.setText(ASSIGNED_USER_NAME);
            }

            if (TRANSACTION_USER_NAME != null && !TRANSACTION_USER_NAME.equals("null") && !TRANSACTION_USER_NAME.equals("")) {
                tvNguoigiaoviec.setText(TRANSACTION_USER_NAME);
            }

            if (START_DATE != null && !START_DATE.equals("null")) {
                DATE = START_DATE;
            }

            if (END_DATE != null && !END_DATE.equals("null")) {
                if (START_DATE != null && !START_DATE.equals("null"))
                    DATE = DATE + " -> " + END_DATE;
                else DATE = END_DATE;
            }

            tvDate.setText(DATE);

            if (TRANSACTION_DESCRIPTION != null && !TRANSACTION_DESCRIPTION.equals("null")) {
                Spanned spanned = Html.fromHtml(TRANSACTION_DESCRIPTION);
                tvDes.setText(spanned);
            }

            tvKhachhang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!CUSTOMER_ID.equals("")) {

                        Intent intent = new Intent(Home_NeededDo_Detail.this, CustomerDetail_Activity.class);

                        intent.putExtra("customer_id", Long.valueOf(CUSTOMER_ID));
                        intent.putExtra("checkPhone", checkPhone);
                        intent.putExtra("checkEmail", checkEmail);
                        startActivityForResult(intent, 0);

                    }

                }

            });

            FButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Home_NeededDo_Detail.this, TransactionDetail_Editation_Activity.class);

                    intent.putExtra("transaction_id", TRANSACTION_ID);

                    intent.putExtra("json", "");

                    startActivityForResult(intent, 0);

                }

            });

        }

    }

    public void getToolbar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvNotification = (TextView) findViewById(R.id.tvNotification);
        LinearLayout llBell = (LinearLayout) findViewById(R.id.llBell);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvNotification.setText("Chi tiết công việc");

        llBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_NeededDo_Detail.this, Notification_Activity.class);
                intent.putExtra("type_object", 1);
                startActivity(intent);
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {

        }
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
