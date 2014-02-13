package com.yooiistudios.morningkit.setting.info.moreinfo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.sound.MNSoundEffectsPlayer;
import com.yooiistudios.morningkit.setting.info.moreinfo.license.MNLicenseFragment;
import com.yooiistudios.morningkit.setting.theme.soundeffect.MNSound;


/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 7.
 *
 * MNMoreInfoFragment
 *  세팅 - 인포 - 모닝키트 인포
 */
public class MNMoreInfoFragment extends Fragment implements MNMoreInfoItemClickListener {
    private static final String TAG = "MNMoreInfoFragment";
//    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.setting_info_fragment, container, false);
        if (rootView != null) {
            ListView listView = (ListView) rootView.findViewById(R.id.setting_info_listview);
            listView.setAdapter(new MNMoreInfoListAdapter(getActivity(), this));

            // KeyListener
//            rootView.setFocusableInTouchMode(true);
//            rootView.requestFocus();
//            rootView.setOnKeyListener(this);

//            this.rootView = rootView;
        }
        return rootView;
    }

    @Override
    public void onItemClick(int position) {
        if (MNSound.isSoundOn(getActivity())) {
            MNSoundEffectsPlayer.play(R.raw.effect_view_open, getActivity());
        }
        MNMoreInfoItemType type = MNMoreInfoItemType.valueOf(position);
        switch (type) {
            case YOOII_STUDIOS: {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://yooiistudios.com"));
                startActivity(intent);
                break;
            }
            case MORNING_KIT_HELP: {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.yooiistudios.com/morning/help.php"));
                startActivity(intent);
                break;
            }
            case LICENSE: {
//                this.rootView.setFocusableInTouchMode(false);
//                this.rootView.clearFocus();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // animate - (upstack)incoming enterAnim, (backstack)outgoing exitAnim /
                // in reverse - (backstack)incoming enterAnim, (upstack)outgoing exitAnim
                transaction.setCustomAnimations(R.anim.fragment_enter, 0);

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.add(R.id.setting_info_moreinfo_container, new MNLicenseFragment());
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();

//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.yooiistudios.com/termservice.html"));
//                startActivity(intent);
                break;
            }
            case VERSION:
                break;
        }
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
        Log.i(TAG, "onKey");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.i(TAG, "KEYCODE_BACK");
            removeFragment();
            return true;
        } else {
            return false;
        }
    }
    */
}
