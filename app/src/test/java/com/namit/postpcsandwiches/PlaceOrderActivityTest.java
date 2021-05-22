package com.namit.postpcsandwiches;

import android.widget.TextView;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class PlaceOrderActivityTest {

    private ActivityController<PlaceOrderActivity> activityController;
    @Before
    public void setup() {
        activityController = Robolectric.buildActivity(PlaceOrderActivity.class);
    }


    @Test
    public void when_picklesButtonsClicked_shouldUpdatePicklesText() {
        // setup
        PlaceOrderActivity activityUnderTest = activityController.create().visible().get();

        // verify
        Assert.assertEquals("0", ((TextView) activityUnderTest.findViewById(R.id.txt_pickles_count)).getText());
        activityUnderTest.findViewById(R.id.btn_pickles_up).performClick();
        Assert.assertEquals("1", ((TextView) activityUnderTest.findViewById(R.id.txt_pickles_count)).getText());
        activityUnderTest.findViewById(R.id.btn_pickles_up).performClick();
        activityUnderTest.findViewById(R.id.btn_pickles_up).performClick();
        Assert.assertEquals("3", ((TextView) activityUnderTest.findViewById(R.id.txt_pickles_count)).getText());
        activityUnderTest.findViewById(R.id.btn_pickles_down).performClick();
        Assert.assertEquals("2", ((TextView) activityUnderTest.findViewById(R.id.txt_pickles_count)).getText());
        activityUnderTest.findViewById(R.id.btn_pickles_down).performClick();
        activityUnderTest.findViewById(R.id.btn_pickles_down).performClick();
        activityUnderTest.findViewById(R.id.btn_pickles_down).performClick();
        activityUnderTest.findViewById(R.id.btn_pickles_down).performClick();
        activityUnderTest.findViewById(R.id.btn_pickles_down).performClick();
        Assert.assertEquals("0", ((TextView) activityUnderTest.findViewById(R.id.txt_pickles_count)).getText());
    }

}