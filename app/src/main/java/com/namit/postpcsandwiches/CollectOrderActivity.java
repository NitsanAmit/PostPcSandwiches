package com.namit.postpcsandwiches;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import com.namit.postpcsandwiches.base.components.SandwichesActivity;
import com.namit.postpcsandwiches.base.components.SandwichesApp;
import com.namit.postpcsandwiches.base.models.Order;
import com.namit.postpcsandwiches.base.models.OrderStatus;

public class CollectOrderActivity extends SandwichesActivity {

    private Order order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_order);


        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            toastError("Error finding order");
            return;
        }
        order = (Order) extras.getSerializable("order");
        if (order == null) {
            toastError("Error finding order");
            return;
        }

        ((TextView) findViewById(R.id.txt_order_id)).setText(String.format("Order #: %s", order.getId()));
        findViewById(R.id.btn_got_it).setOnClickListener(v -> finishOrder());
    }



    private void finishOrder() {
        findViewById(R.id.btn_got_it).setEnabled(false);
        ((SandwichesApp) getApplication()).firestoreInstance
                .collection("orders")
                .document(order.getId())
                .update("status", OrderStatus.DONE)
                .addOnSuccessListener(unused -> {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    preferences.edit().putString("running_order", null).apply();
                    startActivity(new Intent(CollectOrderActivity.this, PlaceOrderActivity.class));
                })
                .addOnFailureListener(e -> {
                    toastError("Error completing order:\n" + e.getMessage());
                })
                .addOnCompleteListener(task -> {
                    findViewById(R.id.btn_got_it).setEnabled(true);
                });
    }

}