package vn.altalab.app.crmvietpack.orders_fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import vn.altalab.app.crmvietpack.R;

/**
 * Created by Tung on 3/5/2017.
 */

public class OrderTabAdapter extends FragmentStatePagerAdapter {
    Context mContext;
    String tab_title[];

    public OrderTabAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        String temp[] = {mContext.getResources().getString(R.string.chuaxacnhan),
                mContext.getResources().getString(R.string.daxacnhan), mContext.getResources().getString(R.string.dagiao)
                , mContext.getResources().getString(R.string.dahuy), mContext.getResources().getString(R.string.trave)};
        tab_title = temp;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                return new UnconfimredFragment();

            case 1:
                return new ConfirmedFragment();

            case 2:
                return new DeliveredFragment();

            case 3:
                return new CancelledFragment();
            case 4:
                return new BackFragment();

            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = " ";
        switch (position) {
            case 0:
                title = tab_title[0];
                break;
            case 1:
                title = tab_title[1];
                break;
            case 2:
                title = tab_title[2];
                break;
            case 3:
                title = tab_title[3];
                break;
            case 4:
                title = tab_title[4];
                break;
        }

        return title;
    }

    @Override
    public int getCount() {
        return 5;
    }
}
