package com.yooii.morningkit.main;

// necessary import
import android.widget.TextView;

import org.robolectric.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by StevenKim on 2013. 10. 31..
 */
// guide for using Robolectric and JUnit
// Class name must be ended with xxxTest in test class
@RunWith(RobolectricTestRunner.class)
public class MNMainActivityTest {
    @Test
    public void shouldUpdateResultsWhenButtonIsClicked() throws Exception {
        String testText = "Hello World!";
        TextView view = new TextView(Robolectric.application);

        view.setText(testText);
        String viewText = view.getText().toString();

        assertThat(viewText, equalTo(testText));
    }
}
