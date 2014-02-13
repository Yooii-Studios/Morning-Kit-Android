package com.yooiistudios.morningkit.panel.selectpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.yooiistudios.morningkit.panel.selectpager.fragment.MNPanelSelectPagerFirstFragment;
import com.yooiistudios.morningkit.panel.selectpager.fragment.MNPanelSelectPagerSecondFragment;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 2. 4.
 *
 * MNPanelSelectPagerAdapter
 */
public class MNPanelSelectPagerAdapter extends FragmentPagerAdapter {

    FragmentManager fm;
    MNPanelSelectPagerInterface panelSelectPagerInterface;

    public MNPanelSelectPagerAdapter(FragmentManager fm, MNPanelSelectPagerInterface panelSelectPagerInterface) {
        super(fm);
        this.fm = fm;
        this.panelSelectPagerInterface = panelSelectPagerInterface;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new MNPanelSelectPagerFirstFragment(panelSelectPagerInterface);
            case 1:
                return new MNPanelSelectPagerSecondFragment(panelSelectPagerInterface);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    public Fragment getActiveFragment(ViewPager container, int position) {
        String name = makeFragmentName(container.getId(), position);
        return  fm.findFragmentByTag(name);
    }

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }
}
