package com.yooii.morningkit;

// necessary import
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by StevenKim on 2013. 10. 25..
 */
// Class name must be ended with xxxTest in test class
public class mainTest {
    @Test
    public void testRobolTest1() {
        assertThat(1, is(1));
    }
}
