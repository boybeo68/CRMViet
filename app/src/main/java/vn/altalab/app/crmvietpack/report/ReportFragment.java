package vn.altalab.app.crmvietpack.report;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.report.chart.Price_Activity;
import vn.altalab.app.crmvietpack.report.chart.RevActivity;


public class ReportFragment extends Fragment {
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;
    private TabLayout tabLayout;
    ViewPager pager;
    private final static int REQUEST_CODE = 1;
    ReportTabAdapter adapter;
    FragmentManager manager;
    LinearLayout tangtruong,doanhthu;
    ImageView kpi;
    TextView kpiTv;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_report, container, false);


        kpiTv = (TextView) view.findViewById(R.id.kpiTv);
        kpiTv.setTextColor(Color.parseColor("#EE0000"));

        tangtruong = (LinearLayout) view.findViewById(R.id.tangtruong);
        tangtruong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RevActivity.class);
                startActivity(intent);
            }
        });

        doanhthu = (LinearLayout) view.findViewById(R.id.doanhthu);
        doanhthu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Price_Activity.class);
                startActivity(intent);
            }
        });
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        pager = (ViewPager) view.findViewById(R.id.viewPager);

        manager = getActivity().getSupportFragmentManager();

        adapter = new ReportTabAdapter(manager);
        pager.setAdapter(adapter);

        tabLayout.setupWithViewPager(pager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setTabsFromPagerAdapter(adapter);

        return view;
    }


}
