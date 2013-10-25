package com.yooii.morningkit;

import android.widget.TextView;
import android.app.Activity;
import com.yooii.morningkit.main.MNMainActivity;

// necessary import
import org.robolectric.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by StevenKim on 2013. 10. 25..
 */
// guide for using Robolectric and JUnit
// Class name must be ended with xxxTest in test class
@RunWith(RobolectricTestRunner.class)
public class mainRobolTest {
    @Test
    public void shouldUpdateResultsWhenButtonIsClicked() throws Exception {
        String testText = "Hello World!";
        TextView view = new TextView(Robolectric.application);

        view.setText(testText);
        String viewText = view.getText().toString();

        assertThat(viewText, equalTo(testText));
    }
}
