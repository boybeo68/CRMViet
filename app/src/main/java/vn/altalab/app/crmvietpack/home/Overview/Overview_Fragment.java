package vn.altalab.app.crmvietpack.home.Overview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.altalab.app.crmvietpack.R;

public class Overview_Fragment extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    if (view == null)
        view = inflater.inflate(R.layout.home_overview_fragment, container, false);

        return view;
    }

}
