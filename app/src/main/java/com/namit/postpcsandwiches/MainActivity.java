package com.namit.postpcsandwiches;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.namit.postpcsandwiches.models.Order;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String runningOrderId = getRunningOrder();
        if (runningOrderId != null) {
            fetchOrderStatus(runningOrderId);
        } else {
            startPlaceOrderActivity();
        }
    }

    private void fetchOrderStatus(String runningOrderId) {
        ((SandwichesApp) getApplication()).firestoreInstance
                .collection("orders")
                .document(runningOrderId)
                .get()
                .addOnSuccessListener(result -> {
                    if (result == null) {
                        startPlaceOrderActivity();
                        return;
                    }
                    Order order = result.toObject(Order.class);
                    if (order == null || order.getStatus() == OrderStatus.DONE) {
                        startPlaceOrderActivity();
                        return;
                    }
                    if (order.getStatus() == OrderStatus.WAITING){
                        startEditOrderActivity(order);
                    } else if (order.getStatus() == OrderStatus.IN_PROGRESS || order.getStatus() == OrderStatus.READY) {
                        startReviewOrderActivity(order);
                    }
                })
                .addOnFailureListener(err -> {
                    Log.e(getClass().getSimpleName(), "Error in fetching data", err); //TODO remove
                    startPlaceOrderActivity();
                });
    }

    private String getRunningOrder() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString("running_order", null);
    }

    private void startPlaceOrderActivity(){
        startActivityWithOptionalOrder(PlaceOrderActivity.class, null);
    }

    private void startEditOrderActivity(Order order){
        startActivityWithOptionalOrder(EditOrderActivity.class, order);
    }

    private void startReviewOrderActivity(Order order){
        startActivityWithOptionalOrder(ReviewOrderActivity.class, order);
    }

    private void startActivityWithOptionalOrder(Class<?> activity, @Nullable Order order) {
        Intent intent = new Intent(this, activity);
        if (order != null) {
            intent.putExtra("order", order);
        }
        startActivity(intent);
        finish();
    }

}