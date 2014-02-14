package com.yooiistudios.morningkit.setting;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 2. 4.
 *
 * MNSettingViewPager
 */
public class MNSettingViewPager extends ViewPager {

    public MNSettingViewPager(Context context) {
        super(context);
        if (isInEditMode()) {
            init();
        }
    }

    public MNSettingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            init();
        }
    }

    private void init() {
        setOffscreenPageLimit(1); // 4개 탭 까지는 항상 유지하고 있게 구현 -> 빠른 로딩을 위해 1개로
    }

    // 페이저 안의 페이저에서 스크롤이 가능하게 만들어주는 코드
    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if(v != this && v instanceof ViewPager) {
            return true;
        }
        return super.canScroll(v, checkV, dx, x, y);
    }
}
