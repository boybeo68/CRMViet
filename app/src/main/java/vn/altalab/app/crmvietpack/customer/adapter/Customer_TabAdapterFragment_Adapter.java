package vn.altalab.app.crmvietpack.customer.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import vn.altalab.app.crmvietpack.CrmMainActivity;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.customer.Customer_All_Fragment;
import vn.altalab.app.crmvietpack.customer.Customer_Care_Fragment;
import vn.altalab.app.crmvietpack.customer.Customer_New_Fragment;
import vn.altalab.app.crmvietpack.customer.Customer_Signed_Fragment;
import vn.altalab.app.crmvietpack.customer.OrtherFragment;
import vn.altalab.app.crmvietpack.customer.PauseFragment;

public class Customer_TabAdapterFragment_Adapter extends FragmentStatePagerAdapter {
    Context mContext;
    CrmMainActivity crmMainActivity = new CrmMainActivity();

    private String[] tab_title ;


    public Customer_TabAdapterFragment_Adapter(FragmentManager fm,Context context) {
        super(fm);
        mContext = context;
        String[] temp = {mContext.getResources().getString(R.string.tatca)+"\n" + crmMainActivity.a,
                mContext.getResources().getString(R.string.moi)+"\n" + crmMainActivity.a1, mContext.getResources().getString(R.string.quan_tam)+"\n" + crmMainActivity.a2,
                mContext.getResources().getString(R.string.daky)+"\n" + crmMainActivity.a3, mContext.getResources().getString(R.string.tamdung)+"\n" + crmMainActivity.a4, mContext.getResources().getString(R.string.khac)+"\n" + crmMainActivity.a5};
        tab_title = temp;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Customer_All_Fragment();

            case 1:
                return new Customer_New_Fragment();

            case 2:
                return new Customer_Care_Fragment();

            case 3:
                return new Customer_Signed_Fragment();
            case 4:

                return new PauseFragment();
            case 5:
                return new OrtherFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tab_title[position];
    }

    @Override
    public int getCount() {
        return 6;
    }

}
