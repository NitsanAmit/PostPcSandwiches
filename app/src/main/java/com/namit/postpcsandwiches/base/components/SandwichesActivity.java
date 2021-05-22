package com.namit.postpcsandwiches.base.components;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SandwichesActivity extends AppCompatActivity {


    protected void toastError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }
}
