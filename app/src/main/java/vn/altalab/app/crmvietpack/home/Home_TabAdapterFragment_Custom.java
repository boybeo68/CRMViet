package vn.altalab.app.crmvietpack.home;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.home.Consigned.Consigned_Fragment;
import vn.altalab.app.crmvietpack.home.Deathline.Deathline_Fragment;
import vn.altalab.app.crmvietpack.home.NeededDid.NeededDo_Fragment;
import vn.altalab.app.crmvietpack.home.Overview.Overview_Fragment;

public class Home_TabAdapterFragment_Custom extends FragmentStatePagerAdapter {
Context mContext;
    private String[]  tab_title;

    private int count  = 4;

    public Home_TabAdapterFragment_Custom(FragmentManager fm, Context context) {
        super(fm);

        mContext = context;
        String[] temp = {mContext.getString(R.string.Overview), mContext.getString(R.string.Need_to_do), mContext.getString(R.string.Overdue)
                , mContext.getString(R.string.Assigned)};
        tab_title = temp;
    }



    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                return new Overview_Fragment();

            case 1:
                return new NeededDo_Fragment();

            case 2:
                return new Deathline_Fragment();

            case 3:
                return new Consigned_Fragment();

            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tab_title[position];
    }
}
