package vn.altalab.app.crmvietpack.home.Overview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import vn.altalab.app.crmvietpack.R;

public class Overview_Paid_Fragment extends Fragment {

    private View view;

    ArrayList<String> listNgay;
    Spinner spinner;

    View fragment7day, fragment30day, fragment90day, fragment1column, fragment2columns, fragment3columns, fragment4columns;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.home_overview_paid_fragment, container, false);

            findViewID();

        if (getActivity() != null)
            try {
                Action();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        return view;
    }

    public void findViewID(){

        spinner = (Spinner) view.findViewById(R.id.spinner_ngay);
        fragment7day = view.findViewById(R.id.fm_7columns_7day);
        fragment30day = view.findViewById(R.id.fm_4colums_30day);
        fragment90day = view.findViewById(R.id.fm_3columns_90day);

        fragment1column = view.findViewById(R.id.fm_1column_thismonth);
        fragment2columns = view.findViewById(R.id.fm_2columns_thismonth);
        fragment3columns = view.findViewById(R.id.fm_3columns_thismonth);
        fragment4columns = view.findViewById(R.id.fm_4columns_thismonth);

    }

    public void Action(){

        listNgay = new ArrayList<>();
        listNgay.add(getResources().getString(R.string.ngay7));
        listNgay.add(getResources().getString(R.string.ngay30));
        listNgay.add(getResources().getString(R.string.ngay90));
        listNgay.add(getResources().getString(R.string.Thangnay));

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getActivity(), R.layout.home_overview_spinner_custom, listNgay);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0){
                    fragment7day.setVisibility(View.VISIBLE);
                    fragment30day.setVisibility(View.INVISIBLE);
                    fragment90day.setVisibility(View.INVISIBLE);
                    fragment1column.setVisibility(View.INVISIBLE);
                    fragment2columns.setVisibility(View.INVISIBLE);
                    fragment3columns.setVisibility(View.INVISIBLE);
                    fragment4columns.setVisibility(View.INVISIBLE);
                } else if (position == 1) {
                    fragment7day.setVisibility(View.INVISIBLE);
                    fragment30day.setVisibility(View.VISIBLE);
                    fragment90day.setVisibility(View.INVISIBLE);
                    fragment1column.setVisibility(View.INVISIBLE);
                    fragment2columns.setVisibility(View.INVISIBLE);
                    fragment3columns.setVisibility(View.INVISIBLE);
                    fragment4columns.setVisibility(View.INVISIBLE);
                } else if (position == 2) {
                    fragment7day.setVisibility(View.INVISIBLE);
                    fragment30day.setVisibility(View.INVISIBLE);
                    fragment90day.setVisibility(View.VISIBLE);
                    fragment1column.setVisibility(View.INVISIBLE);
                    fragment2columns.setVisibility(View.INVISIBLE);
                    fragment3columns.setVisibility(View.INVISIBLE);
                    fragment4columns.setVisibility(View.INVISIBLE);
                } else if (position == 3){
                    fragment7day.setVisibility(View.INVISIBLE);
                    fragment30day.setVisibility(View.INVISIBLE);
                    fragment90day.setVisibility(View.INVISIBLE);
                    fragment1column.setVisibility(View.VISIBLE);
                    fragment2columns.setVisibility(View.VISIBLE);
                    fragment3columns.setVisibility(View.VISIBLE);
                    fragment4columns.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                fragment7day.setVisibility(View.VISIBLE);
                fragment30day.setVisibility(View.INVISIBLE);
                fragment90day.setVisibility(View.INVISIBLE);
                fragment1column.setVisibility(View.INVISIBLE);
                fragment2columns.setVisibility(View.INVISIBLE);
                fragment3columns.setVisibility(View.INVISIBLE);
                fragment4columns.setVisibility(View.INVISIBLE);
            }
        });
    }

}
