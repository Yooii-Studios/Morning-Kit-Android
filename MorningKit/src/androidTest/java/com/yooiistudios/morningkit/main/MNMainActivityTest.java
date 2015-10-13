package com.yooiistudios.morningkit.main;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.RelativeLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by StevenKim on 2013. 10. 31..
 *
 * MNMainActivityTest
 */
@RunWith(AndroidJUnit4.class)
public class MNMainActivityTest extends ActivityInstrumentationTestCase2<MNMainActivity> {
    MNMainActivity mainActivity;

    public MNMainActivityTest() {
        super(MNMainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mainActivity = getActivity();
    }

    @Test
    public void shouldViewsAndLayoutsBeNotNull() throws Exception {
        assertThat(mainActivity, notNullValue());
        assertThat(mainActivity, instanceOf(MNMainActivity.class));

        assertThat(mainActivity.getAlarmListView(), notNullValue());
        assertThat(mainActivity.getAlarmListView(), instanceOf(MNMainAlarmListView.class));

        assertThat(mainActivity.getPanelWindowLayout(), notNullValue());
        assertThat(mainActivity.getPanelWindowLayout(), instanceOf(MNPanelWindowLayout.class));

        assertThat(mainActivity.getButtonLayout(), notNullValue());
        assertThat(mainActivity.getButtonLayout(), instanceOf(RelativeLayout.class));

        assertThat(mainActivity.getAdmobLayout(), notNullValue());
        assertThat(mainActivity.getAdmobLayout(), instanceOf(RelativeLayout.class));
    }
}
