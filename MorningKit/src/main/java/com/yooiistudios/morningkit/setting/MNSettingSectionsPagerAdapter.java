package com.yooiistudios.morningkit.setting;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.setting.info.MNInfoFragment;
import com.yooiistudios.morningkit.setting.panel.MNPanelSettingFragment;
import com.yooiistudios.morningkit.setting.store.MNStoreFragment;
import com.yooiistudios.morningkit.setting.theme.MNThemeFragment;

import java.util.Locale;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 10.
 *
 * MNSettingSectionsPagerAdapter
 *  설정화면에 들어갈 페이저 어댑터
 */
public class MNSettingSectionsPagerAdapter extends FragmentPagerAdapter{
    /**
     * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    MNSettingActivity settingActivity;

    public MNSettingSectionsPagerAdapter(FragmentManager fm, MNSettingActivity settingActivity) {
        super(fm);
        this.settingActivity = settingActivity;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        switch (position) {
            case 0:
                return new MNPanelSettingFragment();
            case 1:
                return new MNStoreFragment(settingActivity);
            case 2:
                return new MNThemeFragment();
            case 3:
                return new MNInfoFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 4 total pages.
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return settingActivity.getString(R.string.tab_widget).toUpperCase(l);
            case 1:
                return settingActivity.getString(R.string.info_store).toUpperCase(l);
            case 2:
                return settingActivity.getString(R.string.tab_theme).toUpperCase(l);
//            case 3:
//                return settingActivity.getString(R.string.info_store).toUpperCase(l) + "_T";
            case 3:
                return settingActivity.getString(R.string.tab_info).toUpperCase(l);
        }
        return null;
    }
}
