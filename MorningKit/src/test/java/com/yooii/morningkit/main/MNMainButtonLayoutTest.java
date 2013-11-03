package com.yooii.morningkit.main;

import android.content.res.Resources;

import com.yooii.morningkit.R;
import com.yooii.morningkit.RobolectricGradleTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import java.lang.Exception;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by StevenKim on 2013. 11. 4..
 */
@RunWith(RobolectricGradleTestRunner.class)
public class MNMainButtonLayoutTest {

    MNMainActivity mainActivity;

    @Before
    public void setUp() {
        // visible() 이 뷰를 띄울 수 있게 해주는 중요한 메서드
        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().visible().get();
    }

    /**
     * ETC
     */
    @Test
    @Config(qualifiers="port")
    public void checkButtonLayoutOnPortrait() throws Exception {
        Resources resources = mainActivity.getResources();
        float expectedHeight = resources.getDimension(R.dimen.main_button_layout_height);
        assertThat(mainActivity.getButtonLayout().getHeight(), is(not(0)));
        assertThat(mainActivity.getButtonLayout().getHeight(), is((int)expectedHeight));
    }
}
