package com.yooiistudios.morningkit.panel.core.selectpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.yooiistudios.morningkit.panel.core.selectpager.fragment.MNPanelSelectPagerFirstFragment;
import com.yooiistudios.morningkit.panel.core.selectpager.fragment.MNPanelSelectPagerSecondFragment;

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
                MNPanelSelectPagerFirstFragment firstFragment = new MNPanelSelectPagerFirstFragment();
                firstFragment.setPanelSelectPagerInterface(panelSelectPagerInterface);
                return firstFragment;
            case 1:
                MNPanelSelectPagerSecondFragment secondFragment = new MNPanelSelectPagerSecondFragment();
                secondFragment.setPanelSelectPagerInterface(panelSelectPagerInterface);
                return secondFragment;
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

    // 중요!! 오른쪽만 패딩을 주되 작게 다음 페이지가 보일 수 있게 하는 방법. 퍼센트로 활용하나봄.
    @Override
    public float getPageWidth(int position) {
        return 0.93f;
    }
}
