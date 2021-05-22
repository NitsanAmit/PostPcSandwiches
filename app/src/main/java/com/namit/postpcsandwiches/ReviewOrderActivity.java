package com.namit.postpcsandwiches;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.google.firebase.firestore.ListenerRegistration;
import com.namit.postpcsandwiches.base.components.SandwichesActivity;
import com.namit.postpcsandwiches.base.components.SandwichesApp;
import com.namit.postpcsandwiches.base.models.Order;
import com.namit.postpcsandwiches.base.models.OrderStatus;

public class ReviewOrderActivity extends SandwichesActivity {

    private ListenerRegistration statusListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_order);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            toastError("Error finding order");
            return;
        }
        Order order = (Order) extras.getSerializable("order");
        if (order == null) {
            toastError("Error finding order");
            return;
        }

        ((TextView) findViewById(R.id.txt_order_id)).setText(String.format("Order #: %s", order.getId()));

        statusListener = ((SandwichesApp) getApplication()).firestoreInstance
                .collection("orders")
                .document(order.getId())
                .addSnapshotListener((snapshot, error) -> {
                    if (snapshot != null && snapshot.exists()) {
                        OrderStatus status = snapshot.get("status", OrderStatus.class);
                        if (status == OrderStatus.READY) {
                            startCollectOrderActivity(order);
                        } else if (status == OrderStatus.DONE) {
                            startPlaceOrderActivity();
                        }
                    } else {
                        startPlaceOrderActivity();
                    }
                });
    }

    private void startCollectOrderActivity(Order order) {
        Intent intent = new Intent(this, CollectOrderActivity.class);
        intent.putExtra("order", order);
        startActivity(intent);
        finish();
    }

    private void startPlaceOrderActivity() {
        Intent intent = new Intent(this, PlaceOrderActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        statusListener.remove();
        super.onDestroy();
    }

}