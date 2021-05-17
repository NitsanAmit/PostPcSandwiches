package com.namit.postpcsandwiches;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.ListenerRegistration;
import com.namit.postpcsandwiches.models.Order;
import java.util.HashMap;
import java.util.Map;

public class EditOrderActivity extends AppCompatActivity {

    private EditText customerName;
    private EditText customerComment;
    private CheckBox withHummus;
    private CheckBox withTahini;
    private TextView picklesCount;
    private Button updateOrderButton;
    private Button deleteOrderButton;
    private Order orderDraft;
    private Order originalOrder;
    private ListenerRegistration statusListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);

        customerName = findViewById(R.id.edit_text_name);
        customerComment = findViewById(R.id.edit_text_comment);
        withHummus = findViewById(R.id.checkbox_hummus);
        withTahini = findViewById(R.id.checkbox_tahini);
        ImageButton picklesUp = findViewById(R.id.btn_pickles_up);
        ImageButton picklesDown = findViewById(R.id.btn_pickles_down);
        picklesCount = findViewById(R.id.txt_pickles_count);
        updateOrderButton = findViewById(R.id.btn_place_order);
        deleteOrderButton = findViewById(R.id.btn_delete_order);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return; //TODO not good
        }
        originalOrder = (Order) extras.getSerializable("order");
        if (originalOrder == null) {
            return; //TODO also not good
        }
        orderDraft = new Order(originalOrder);

        setupUiWithCurrentOrder();
        orderDraft.setOnPicklesChangedListener(() -> picklesCount.setText(String.valueOf(orderDraft.getPickles())));
        picklesUp.setOnClickListener(v -> orderDraft.incrementPickles());
        picklesDown.setOnClickListener(v -> orderDraft.decrementPickles());
        withHummus.setOnCheckedChangeListener((v, isChecked) -> orderDraft.setHummus(isChecked));
        withTahini.setOnCheckedChangeListener((v, isChecked) -> orderDraft.setTahini(isChecked));
        updateOrderButton.setOnClickListener(v -> EditOrderActivity.this.updateOrder());
        deleteOrderButton.setOnClickListener(v -> EditOrderActivity.this.dialogDeleteOrder());

        statusListener = ((SandwichesApp) getApplication()).firestoreInstance
                .collection("orders")
                .document(orderDraft.getId())
                .addSnapshotListener((snapshot, error) -> {
                    if (snapshot != null && snapshot.exists()) {
                        OrderStatus status = snapshot.get("status", OrderStatus.class);
                        if (status == OrderStatus.IN_PROGRESS) {
                            startReviewOrderActivity();
                        } else if (status == OrderStatus.READY) {
                            startCollectOrderActivity();
                        } else if (status == OrderStatus.DONE) {
                            startPlaceOrderActivity();
                        }
                    }
                });
    }

    private void setupUiWithCurrentOrder() {
        withHummus.setChecked(orderDraft.isHummus());
        withTahini.setChecked(orderDraft.isTahini());
        customerName.setText(orderDraft.getCustomerName());
        customerComment.setText(orderDraft.getComment());
        picklesCount.setText(String.valueOf(orderDraft.getPickles()));
    }

    private void updateOrder() {
        if (!validateFields()) return;
        updateOrderButton.setEnabled(false);
        deleteOrderButton.setEnabled(false);
        updateOrderButton.setText("Updating...");

        Map<String, Object> updatedFields = getUpdatedFields();

        ((SandwichesApp) getApplication()).firestoreInstance
                .collection("orders")
                .document(orderDraft.getId())
                .update(updatedFields)
                .addOnSuccessListener(unused -> originalOrder = new Order(orderDraft))
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error updating order:\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                })
                .addOnCompleteListener(task -> {
                    updateOrderButton.setEnabled(true);
                    deleteOrderButton.setEnabled(true);
                    updateOrderButton.setText("Update order");
                });
    }

    private void dialogDeleteOrder() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder
                .setTitle("Delete Order")
                .setMessage("Are you sure you want to delete this order?\nOrder #: " + originalOrder.getId())
                .setNegativeButton("Delete order", (dialog, which) -> deleteOrder())
                .setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void deleteOrder() {
        updateOrderButton.setEnabled(false);
        deleteOrderButton.setEnabled(false);
        deleteOrderButton.setText("Deleting...");

        ((SandwichesApp) getApplication()).firestoreInstance
                .collection("orders")
                .document(orderDraft.getId())
                .delete()
                .addOnSuccessListener(unused -> startPlaceOrderActivity())
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error deleting order:\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    deleteOrderButton.setEnabled(true);
                    deleteOrderButton.setText("Delete order");
                });
    }

    private Map<String, Object> getUpdatedFields() {
        Map<String, Object> updatedFields = new HashMap<>();
        if (!originalOrder.getCustomerName().equals(orderDraft.getCustomerName())) {
            updatedFields.put("customerName", orderDraft.getCustomerName());
        }
        if (!originalOrder.getComment().equals(orderDraft.getComment())) {
            updatedFields.put("comment", orderDraft.getComment());
        }
        if (originalOrder.isHummus() != orderDraft.isHummus()) {
            updatedFields.put("hummus", orderDraft.isHummus());
        }
        if (originalOrder.isTahini() != orderDraft.isTahini()) {
            updatedFields.put("tahini", orderDraft.isTahini());
        }
        if (originalOrder.getPickles() != orderDraft.getPickles()) {
            updatedFields.put("pickles", orderDraft.getPickles());
        }
        return updatedFields;
    }

    private boolean validateFields() {
        if (TextUtils.isEmpty(customerName.getText())) {
            customerName.setError("Name cannot be blank!");
            return false;
        }
        orderDraft.setCustomerName(customerName.getText().toString());
        orderDraft.setComment(customerComment.getText().toString());
        return true;
    }

    private void startReviewOrderActivity() {
        Intent intent = new Intent(this, ReviewOrderActivity.class);
        intent.putExtra("order", originalOrder);
        startActivity(intent);
        finish();
    }

    private void startCollectOrderActivity() {
        Intent intent = new Intent(this, CollectOrderActivity.class);
        intent.putExtra("order", originalOrder);
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