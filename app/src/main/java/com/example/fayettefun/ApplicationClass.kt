package com.example.fayettefun

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        if (FirebaseApp.getApps(this).isNotEmpty()) {
            Log.d("ApplicationClass", "Firebase initialized successfully")
        } else {
            Log.e("ApplicationClass", "Firebase initialization failed")
        }
    }
}