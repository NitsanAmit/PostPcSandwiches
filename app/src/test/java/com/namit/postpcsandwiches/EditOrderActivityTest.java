package com.namit.postpcsandwiches;

import android.content.Intent;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.namit.postpcsandwiches.base.models.Order;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class EditOrderActivityTest {

    @Test
    public void when_orderFoundInIntent_shouldUpdateUiWithData() {
        // setup
        Order order = new Order("Nitsan", 9, true, false, "Harbe Harif");
        Intent intent = new Intent();
        intent.putExtra("order", order);
        EditOrderActivity activityUnderTest = Robolectric.buildActivity(EditOrderActivity.class, intent).create().visible().get();

        // verify
        Assert.assertEquals(String.valueOf(order.getPickles()) ,((TextView) activityUnderTest.findViewById(R.id.txt_pickles_count)).getText());
        Assert.assertEquals(order.isHummus() ,((CheckBox) activityUnderTest.findViewById(R.id.checkbox_hummus)).isChecked());
        Assert.assertEquals(order.isTahini() ,((CheckBox) activityUnderTest.findViewById(R.id.checkbox_tahini)).isChecked());
        Assert.assertEquals(order.getCustomerName() ,((EditText) activityUnderTest.findViewById(R.id.edit_text_name)).getText().toString());
        Assert.assertEquals(order.getComment() ,((EditText) activityUnderTest.findViewById(R.id.edit_text_comment)).getText().toString());
    }

}