package vn.altalab.app.crmvietpack.orders_fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.altalab.app.crmvietpack.CrmMainActivity;
import vn.altalab.app.crmvietpack.R;

public class OrderFragment extends Fragment {
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;
    private TabLayout tabLayout;
    ViewPager pager;
    private final static int REQUEST_CODE = 1;
    OrderTabAdapter adapter;
    FragmentManager manager;
    Context mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        CrmMainActivity.toolbar.setSubtitle("Đơn hàng");
        if (settings == null) {
            settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        }


        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        pager = (ViewPager) view.findViewById(R.id.viewPager);

        manager = getActivity().getSupportFragmentManager();

        adapter = new OrderTabAdapter(manager,mContext);
        pager.setAdapter(adapter);

        tabLayout.setupWithViewPager(pager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setTabsFromPagerAdapter(adapter);
//        CrmMainActivity crmMainActivity = new CrmMainActivity();
//        tabLayout.getTabAt(0).setText("Tất cả\n"+crmMainActivity.a);
//        tabLayout.getTabAt(1).setText("Mới\n"+crmMainActivity.a1);
//        tabLayout.getTabAt(2).setText("Quan tâm\n"+crmMainActivity.a2);
//        tabLayout.getTabAt(3).setText("Đã ký\n"+crmMainActivity.a3);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.floatingButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddOrderActivity.class);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {

//            if (resultCode == Activity.RESULT_OK) {
//                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
//            }
        }
    }

}
