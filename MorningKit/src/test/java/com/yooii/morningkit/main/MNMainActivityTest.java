package com.yooii.morningkit.main;

// necessary import
import com.yooii.morningkit.RobolectricGradleTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

import java.lang.Exception;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
//import static org.junit.matchers.JUnitMatchers.*;

/**
 * Created by StevenKim on 2013. 10. 31..
 */
// guide for using Robolectric and JUnit
// Class name must be ended with xxxTest in test class
@RunWith(RobolectricGradleTestRunner.class)
public class MNMainActivityTest {

    MNMainActivity mainActivity;

    @Before
    public void setUp() {
        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().get();
    }

    @Test
    public void shouldAlarmListViewAndWidgetWindowViewNotNull() throws Exception {
        assertThat(mainActivity, instanceOf(MNMainActivity.class));
        assertNotNull(mainActivity);
    }
}
