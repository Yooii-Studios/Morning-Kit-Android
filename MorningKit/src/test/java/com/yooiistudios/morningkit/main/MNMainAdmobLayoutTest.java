package com.yooiistudios.morningkit.main;

import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.RobolectricGradleTestRunner;
import com.yooiistudios.morningkit.common.size.MNViewSizeMeasure;
import com.yooiistudios.morningkit.main.admob.AdWebViewShadow;
import com.yooiistudios.morningkit.main.layout.MNMainLayoutSetter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

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

    @Test
    @Config(qualifiers="port")
    public void checkAdmobLayoutHeightOnPortrait() throws Exception {

        // 1. 광고가 있을 때
        float expectedHeight = MNMainLayoutSetter.adjustAdmobLayoutParamsAtOrientation(mainActivity.getAdmobLayout(), mainActivity.getResources().getConfiguration().orientation);

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.MATCH_PARENT, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec((int)expectedHeight, View.MeasureSpec.EXACTLY);
        mainActivity.getAdmobLayout().measure(widthMeasureSpec, heightMeasureSpec);

        assertThat(mainActivity.getAdmobLayout().getMeasuredHeight(), is((int) expectedHeight));
//                Resources resources = mainActivity.getResources();
//                float expectedHeight = resources.getDimension(R.dimen.main_admob_layout_height);
//                assertThat(mainActivity.getAdmobLayout().getHeight(), is((int) expectedHeight));
//
        // 2. 광고가 없을 때는 높이가 0이어야 함

    }

    @Test
    @Config(qualifiers="land")
    public void checkAdmobLayoutHeightOnLandscape() throws Exception {
        // 가로는 무조건 이 레이아웃이 0이어야만 한다.

        float expectedHeight = MNMainLayoutSetter.adjustAdmobLayoutParamsAtOrientation(mainActivity.getAdmobLayout(), mainActivity.getResources().getConfiguration().orientation);

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.MATCH_PARENT, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec((int)expectedHeight, View.MeasureSpec.EXACTLY);
        mainActivity.getAdmobLayout().measure(widthMeasureSpec, heightMeasureSpec);

        assertThat(mainActivity.getAdmobLayout().getMeasuredHeight(), is((int) expectedHeight));
    }
}
