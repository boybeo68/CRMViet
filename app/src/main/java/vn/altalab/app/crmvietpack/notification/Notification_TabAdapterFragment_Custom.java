package vn.altalab.app.crmvietpack.notification;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class Notification_TabAdapterFragment_Custom extends FragmentStatePagerAdapter {

    private String[] tab_title = {"Giao dịch", "Khách hàng", "Duyệt"};

    private int count  = tab_title.length;

    public Notification_TabAdapterFragment_Custom(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                return new Notification_Transaction_Fragment();

            case 1:
                return new Notification_Customer_Fragment();

            case 2:
                return new Notification_Approval_Fragment();

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
