package com.yooii.morningkit.main;

import android.content.res.Resources;
import android.graphics.Point;

import com.yooii.morningkit.R;
import com.yooii.morningkit.RobolectricGradleTestRunner;
import com.yooii.morningkit.common.MNViewSizeMeasure;
import com.yooii.morningkit.main.admob.AdWebViewShadow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by StevenKim on 2013. 11. 4..
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config (shadows = { AdWebViewShadow.class })
public class MNMainAdmobLayoutTest {

    MNMainActivity mainActivity;

    @Before
    public void setUp() {
        // visible() 이 뷰를 띄울 수 있게 해주는 중요한 메서드
        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().get();
    }

    /**
     * ETC
     */
    @Test
    @Config(qualifiers="port")
    public void checkAdmobLayoutHeightOnPortrait() throws Exception {


        MNViewSizeMeasure.setViewSizeObserver(mainActivity.getButtonLayout(), new MNViewSizeMeasure.OnGlobalLayoutObserver() {
            @Override
            public void onLayoutLoad(Point size) {
                // 1. 광고가 있을 때
                Resources resources = mainActivity.getResources();
                float expectedHeight = resources.getDimension(R.dimen.main_admob_layout_height);
                assertThat(mainActivity.getButtonLayout().getHeight(), is((int) expectedHeight));

                // 2. 광고가 없을 때는 높이가 0이어야 함
            }
        });
    }

    @Test
    @Config(qualifiers="land")
    public void checkAdmobLayoutOnLandscape() throws Exception {
        // 가로는 무조건 이 레이아웃이 0이어야만 한다.
        MNViewSizeMeasure.setViewSizeObserver(mainActivity.getButtonLayout(), new MNViewSizeMeasure.OnGlobalLayoutObserver() {
            @Override
            public void onLayoutLoad(Point size) {
                Resources resources = mainActivity.getResources();
                float expectedHeight = 0;
                assertThat(mainActivity.getButtonLayout().getHeight(), is((int) expectedHeight));
            }
        });
    }
}
