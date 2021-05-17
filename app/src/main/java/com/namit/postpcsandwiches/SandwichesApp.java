package com.namit.postpcsandwiches;

import android.app.Application;
import com.google.firebase.firestore.FirebaseFirestore;

public class SandwichesApp extends Application {

    public FirebaseFirestore firestoreInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        firestoreInstance = FirebaseFirestore.getInstance();
    }

}
