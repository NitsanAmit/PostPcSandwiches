package com.namit.postpcsandwiches;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.namit.postpcsandwiches.models.Order;

public class CollectOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_order);


        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return; //TODO not good
        }
        Order order = (Order) extras.getSerializable("order");
        if (order == null) {
            return; //TODO also not good
        }

        ((TextView) findViewById(R.id.txt_order_id)).setText(String.format("Order #: %s", order.getId()));
        findViewById(R.id.btn_got_it).setOnClickListener(v -> finishOrder());
    }

    private void finishOrder() {
        //TODO delete preferences
        // TODO update status to done
        //TODO move to place order
    }
}