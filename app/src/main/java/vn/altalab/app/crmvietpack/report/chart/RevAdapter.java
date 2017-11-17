package vn.altalab.app.crmvietpack.report.chart;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Tung on 4/18/2017.
 */

public class RevAdapter extends FragmentStatePagerAdapter {
    public RevAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                return new NewFragment();

            case 1:
                return new SignedFragment();




            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = " ";
        switch (position) {
            case 0:
                title = "Tất cả";
                break;
            case 1:
                title = "Đã ký";
                break;


        }

        return title;
    }


    @Override
    public int getCount() {
        return 2;
    }
}
