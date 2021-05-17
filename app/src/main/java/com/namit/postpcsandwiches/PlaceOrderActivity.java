package com.namit.postpcsandwiches;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.namit.postpcsandwiches.models.Order;

public class PlaceOrderActivity extends AppCompatActivity {

    EditText customerName;
    EditText customerComment;
    CheckBox withHummus;
    CheckBox withTahini;
    ImageButton picklesUp;
    ImageButton picklesDown;
    TextView picklesCount;
    Button placeOrderButton;
    Order orderDraft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        customerName = findViewById(R.id.edit_text_name);
        customerComment = findViewById(R.id.edit_text_comment);
        withHummus = findViewById(R.id.checkbox_hummus);
        withTahini = findViewById(R.id.checkbox_tahini);
        picklesUp = findViewById(R.id.btn_pickles_up);
        picklesDown = findViewById(R.id.btn_pickles_down);
        picklesCount = findViewById(R.id.txt_pickles_count);
        placeOrderButton = findViewById(R.id.btn_place_order);

        orderDraft = new Order();
        withHummus.setChecked(orderDraft.isHummus());
        withTahini.setChecked(orderDraft.isTahini());

        orderDraft.setOnPicklesChangedListener(() -> picklesCount.setText(String.valueOf(orderDraft.getPickles())));
        picklesUp.setOnClickListener(v -> orderDraft.incrementPickles());
        picklesDown.setOnClickListener(v -> orderDraft.decrementPickles());
        withHummus.setOnCheckedChangeListener((v, isChecked) -> orderDraft.setHummus(isChecked));
        withTahini.setOnCheckedChangeListener((v, isChecked) -> orderDraft.setTahini(isChecked));
        placeOrderButton.setOnClickListener(v -> PlaceOrderActivity.this.placeOrder());
    }


    private void placeOrder() {
        if (!validateFields()) return;
        placeOrderButton.setEnabled(false);
        placeOrderButton.setText("Placing order...");

        ((SandwichesApp) getApplication()).firestoreInstance
                .collection("orders")
                .document(orderDraft.getId())
                .set(orderDraft)
                .addOnSuccessListener(documentReference -> {
                    writeOrderToSharedPrefs();
                    startEditOrderActivity();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error creating order:\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    placeOrderButton.setEnabled(true);
                    placeOrderButton.setText("Place Order");
                });

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

    private void writeOrderToSharedPrefs() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putString("running_order", orderDraft.getId()).apply();
    }


    private void startEditOrderActivity() {
        Intent intent = new Intent(this, EditOrderActivity.class);
        intent.putExtra("order", orderDraft);
        startActivity(intent);
        finish();
    }

}