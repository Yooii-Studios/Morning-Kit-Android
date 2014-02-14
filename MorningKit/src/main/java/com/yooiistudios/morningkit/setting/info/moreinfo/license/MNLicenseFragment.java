package com.yooiistudios.morningkit.setting.info.moreinfo.license;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yooiistudios.morningkit.R;

/**
 * Created by StevenKim in MNSettingActivityProject from Yooii Studios Co., LTD. on 2014. 1. 8.
 *
 * MNLicenseFragment
 *  TextView에 라이센스 관련 정보를 담아 프래그먼트로 보여준다
 */
public class MNLicenseFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.setting_more_info_license_fragment, container, false);
        if (rootView != null) {
            ListView listView = (ListView) rootView.findViewById(R.id.setting_more_info_license_listview);
            listView.setAdapter(new MNLicenseListAdapter(getActivity()));

//            rootView.setFocusableInTouchMode(true);
//            rootView.requestFocus();
//            rootView.setOnKeyListener(this);
        }
        return rootView;
    }

    /*
    private void removeFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // animate
        transaction.setCustomAnimations(0, R.anim.fragment_exit);
        // remove
        transaction.remove(this);
        // Commit the transaction
        transaction.commit();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if( keyCode == KeyEvent.KEYCODE_BACK ) {
            removeFragment();
            return true;
        } else {
            return false;
        }
    }
    */
}
