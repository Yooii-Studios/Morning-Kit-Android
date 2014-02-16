package com.yooiistudios.morningkit.main;

import android.content.res.Configuration;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.yooiistudios.morningkit.common.RobolectricGradleTestRunner;
import com.yooiistudios.morningkit.main.admob.AdWebViewShadow;
import com.yooiistudios.morningkit.main.layout.MNMainLayoutSetter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
//import static org.junit.matchers.JUnitMatchers.*;

/**
 * Created by StevenKim on 2013. 11. 12..
 *
 * MNMainWidgetWindowTest
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config (shadows = { AdWebViewShadow.class })
public class MNMainWidgetWindowTest {

    MNMainActivity mainActivity;

    @Before
    public void setUp() {
        ShadowLog.stream = System.out;

        // visible() 이 뷰를 띄울 수 있게 해주는 중요한 메서드
//        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().start().resume().visible().get();
//        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().postResume().get();
        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().visible().get();
    }

    /**
     * Widget Window
     */
    @Test
    @Config(qualifiers="port")
    public void checkWidgetWindowLayoutHeightOnPortrait() throws Exception {

//        mainActivity.onConfigurationChanged(mainActivity.getResources().getConfiguration());

        float expectedHeight = MNMainLayoutSetter.getPanelWindowLayoutHeight(mainActivity, Configuration.ORIENTATION_PORTRAIT);

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.MATCH_PARENT, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec((int)expectedHeight, View.MeasureSpec.EXACTLY);
        mainActivity.getPanelWindowLayout().measure(widthMeasureSpec, heightMeasureSpec);

        int widgetMatrix;
        // 2 * 2일 경우
        widgetMatrix = 2;

        Log.i("MNMainWidgetWindowTest", "port/height: " + expectedHeight);
        assertThat(mainActivity.getPanelWindowLayout().getMeasuredHeight(), is((int) expectedHeight));

        // 2 * 1일 경우는 추후 테스트
        widgetMatrix = 1;
        // (위젯 높이 * 2) + outer margin(위쪽) + (inner margin * 2(중앙) * 0) + inner margin(아래쪽)
    }

    @Test
    @Config(qualifiers="land")
    public void checkWidgetWindowLayoutHeightOnLandscape() throws Exception {
        Configuration newConfig = new Configuration();
        newConfig.orientation = Configuration.ORIENTATION_LANDSCAPE;
        mainActivity.onConfigurationChanged(newConfig);

//        float expectedHeight = MNMainLayoutSetter.adjustWidgetLayoutParamsAtOrientation(mainActivity, Configuration.ORIENTATION_LANDSCAPE);
        float expectedHeight = MNMainLayoutSetter.getPanelWindowLayoutHeight(mainActivity, Configuration.ORIENTATION_LANDSCAPE);

        // Device height - buttonLayout height - (outerPadding - innerPadding)를 확인하면 됨
        // 위젯 윈도우뷰의 아래쪽은 innerPadding 만큼만 주기 때문에 (outerPadding - innerPadding)만큼의 공간을 따로 주어야 함
        assertThat(mainActivity.getPanelWindowLayout().getLayoutParams().height, is((int)expectedHeight));
    }
}

