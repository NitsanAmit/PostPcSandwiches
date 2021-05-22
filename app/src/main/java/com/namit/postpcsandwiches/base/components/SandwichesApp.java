package com.namit.postpcsandwiches.base.components;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class SandwichesApp extends Application {

    public FirebaseFirestore firestoreInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        firestoreInstance = FirebaseFirestore.getInstance();
    }

}
