package vn.altalab.app.crmvietpack.customer.customer_detail.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import vn.altalab.app.crmvietpack.customer.customer_detail.CustomerDetail_Contact_Fragment;
import vn.altalab.app.crmvietpack.customer.customer_detail.CustomerDetail_HopDong_Fragment;
import vn.altalab.app.crmvietpack.customer.customer_detail.CustomerDetail_Info_Fragment;
import vn.altalab.app.crmvietpack.customer.customer_detail.CustomerDetail_Transaction_Fragment;

public class CustomerDetail_MainAdapter extends FragmentStatePagerAdapter {

    private String[] tab_title = {"Thông tin", "Giao dịch", "Liên hệ","Hợp đồng"};

    private int count  = tab_title.length;

    public CustomerDetail_MainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new CustomerDetail_Info_Fragment();

            case 1:
                return new CustomerDetail_Transaction_Fragment();

            case 2:
                return new CustomerDetail_Contact_Fragment();
            case 3:
                return new CustomerDetail_HopDong_Fragment();

            default: return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tab_title[position];
    }

    @Override
    public int getCount() {
        return count;
    }

}
