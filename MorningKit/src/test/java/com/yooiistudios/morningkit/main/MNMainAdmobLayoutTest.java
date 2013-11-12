package com.yooiistudios.morningkit.main;

import com.yooiistudios.morningkit.common.RobolectricGradleTestRunner;
import com.yooiistudios.morningkit.main.admob.AdWebViewShadow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

/**
 * Created by StevenKim on 2013. 11. 4..
 */
@RunWith(RobolectricGradleTestRunner.class)
//@RunWith(RobolectricTestRunner.class)
@Config (shadows = { AdWebViewShadow.class })
public class MNMainAdmobLayoutTest {

    MNMainActivity mainActivity;

    @Before
    public void setUp() {
        // visible() 이 뷰를 띄울 수 있게 해주는 중요한 메서드
//        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().postResume().visible().get();
        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().visible().get();
         
    }

    /**
     * ETC
     */
    @Test
    public void calculateAdmobLayoutWidth() {
        // AdView의 width가 
    }
//    @Test
//    @Config(qualifiers="port")
//    public void checkAdmobLayoutWidthOnPortrait() throws Exception {
//        MNViewSizeMeasure.setViewSizeObserver(mainActivity.getAdmobLayout(), new MNViewSizeMeasure.OnGlobalLayoutObserver() {
//            @Override
//            public void onLayoutLoad(Point size) {
//                // 1. 광고가 있을 때 기기의 너비가 광고 너비보다 넓다면, layout의 width를 버튼 뷰와 같이 맞추어 주어야 함
//                // -> SMART_BANNER를 사용해서 전체를 광고 레이아웃으로 만드는 것이 좋을 듯
//                AdSize adViewSize = AdSize.createAdSize(AdSize.SMART_BANNER, mainActivity);
//                int expectedWidth = adViewSize.getWidth();
//                assertThat(mainActivity.getAdmobLayout().getWidth(), is(expectedWidth));
//            }
//        });
//    }

    /*
    @Test
    @Config(qualifiers="port")
    public void checkAdmobLayoutHeightOnPortrait() throws Exception {

        Resources resources = mainActivity.getResources();
        float expectedHeight = resources.getDimension(R.dimen.main_admob_layout_height);
        assertThat(mainActivity.getAdmobLayout().getHeight(), is((int) expectedHeight));

//        MNViewSizeMeasure.setViewSizeObserver(mainActivity.getAdmobLayout(), new MNViewSizeMeasure.OnGlobalLayoutObserver() {
//            @Override
//            public void onLayoutLoad(Point size) {
//                // 1. 광고가 있을 때
//                Resources resources = mainActivity.getResources();
//                float expectedHeight = resources.getDimension(R.dimen.main_admob_layout_height);
//                assertThat(mainActivity.getAdmobLayout().getHeight(), is((int) expectedHeight));
//
//                // 2. 광고가 없을 때는 높이가 0이어야 함
//            }
//        });
    }

    @Test
    @Config(qualifiers="land")
    public void checkAdmobLayoutHeightOnLandscape() throws Exception {
        // 가로는 무조건 이 레이아웃이 0이어야만 한다.
        MNViewSizeMeasure.setViewSizeObserver(mainActivity.getAdmobLayout(), new MNViewSizeMeasure.OnGlobalLayoutObserver() {
            @Override
            public void onLayoutLoad() {
                assertThat(mainActivity.getAdmobLayout().getHeight(), is((int) 100));
                assertTrue(false);
                float expectedHeight = 0;
                assertThat(mainActivity.getAdmobLayout().getHeight(), is((int) expectedHeight));
            }
        });
    }

    */

    /*
    @Test
    @Config(qualifiers="port")
    // Portrait 모드에서는 AdmobLayout 안에 속해 있어야함
    public void checkAdmobViewInAdmobLayoutOnPortrait() throws Exception {
        assertThat(mainActivity.getAdmobLayout().findViewById(R.id.adView), notNullValue());
    }
    */

    /*
    @Test
    @Config(qualifiers="land")
    // Landscape 모드에서는 AdmobLayout 안에 없어야함
    // 테스트 불가
    public void checkAdmobViewInAdmobLayoutOnLandscape() throws Exception {
//        assertThat(mainActivity.getAdmobLayout().findViewById(R.id.adView), nullValue());
    }
    */
}
