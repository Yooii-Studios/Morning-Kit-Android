package com.yooiistudios.morningkit.main;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.RobolectricGradleTestRunner;
import com.yooiistudios.morningkit.main.admob.AdWebViewShadow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by StevenKim on 2013. 11. 4..
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config (shadows = { AdWebViewShadow.class })
public class MNMainButtonLayoutTest {

    MNMainActivity mainActivity;

    @Before
    public void setUp() {
        // visible() 이 뷰를 띄울 수 있게 해주는 중요한 메서드
        System.setProperty("robolectric.logging", "stdout");
        ShadowLog.stream = System.out;

//        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().start().resume().visible().get();
//        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().postResume().visible().get();
        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().visible().get();
    }


    /**
     * Button
     * @throws Exception
     */
    /*
    @Test
    @Config(qualifiers="port")
    public void checkButtonLayoutHeightOnPortrait() throws Exception {

        MNViewSizeMeasure.setViewSizeObserver(mainActivity.getButtonLayout(), new MNViewSizeMeasure.OnGlobalLayoutObserver() {
            @Override
            public void onLayoutLoad(Point size) {
                Resources resources = mainActivity.getResources();
                float expectedHeight = resources.getDimension(R.dimen.main_button_layout_height);
                assertThat(mainActivity.getButtonLayout().getHeight(), is((int) expectedHeight));
            }
        });
    }

    @Test
    @Config(qualifiers="land")
    public void checkButtonLayoutHeightOnLandscape() throws Exception {
        MNViewSizeMeasure.setViewSizeObserver(mainActivity.getButtonLayout(), new MNViewSizeMeasure.OnGlobalLayoutObserver() {
            @Override
            public void onLayoutLoad(Point size) {
                // 1. 광고가 있는 경우: SMART_BANNER의 높이 + inner margin * 2가 되어야 한다.
                Resources resources = mainActivity.getResources();
                AdSize adSize = AdSize.createAdSize(AdSize.SMART_BANNER, mainActivity);
                float expectedHeight = adSize.getHeight() + resources.getDimension(R.dimen.margin_outer) * 2;
                assertThat(mainActivity.getButtonLayout().getHeight(), is((int) expectedHeight));

                // 2. 광고가 없는 경우: main_button_layout_height
            }
        });
    }
    */

    /**
     * Adview
     */

    @Test
    @Config(qualifiers="port")
    // Portrait 모드에서는 ButtonLayout 안에 속해 있어야함
    // 테스트 불가. onConfigurationChanged가 제대로 실행이 되지 않나 봄
    public void checkAdmobViewNotInButtonLayoutOnPortrait() throws Exception {
        assertThat(mainActivity.getButtonLayout().findViewById(R.id.adView), nullValue());
    }
}
