package vn.altalab.app.crmvietpack.report;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Tung on 4/13/2017.
 */

public class ReportTabAdapter extends FragmentStatePagerAdapter {
    public ReportTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                return new RevenueFragment();

            case 1:
                return new KpiFragment();

            case 2:
                return new JobFragment();



            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = " ";
        switch (position) {
            case 0:
                title = "Doanh thu NV";
                break;
            case 1:
                title = "KPI Tổng hợp";
                break;
            case 2:
                title = "Tổng hợp CV";
                break;

        }

        return title;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
