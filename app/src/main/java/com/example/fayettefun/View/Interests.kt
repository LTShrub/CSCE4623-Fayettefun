package com.example.fayettefun.View

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.fayettefun.R
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth

class Interests : AppCompatActivity() {

    // Instance variables
    private lateinit var barButton: ImageButton
    private lateinit var natureButton: ImageButton
    private lateinit var foodButton: ImageButton
    private lateinit var fitnessButton: ImageButton
    private lateinit var partyButton: ImageButton
    private lateinit var greekButton: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interests)

        // Initialize variables
        barButton = findViewById(R.id.barButton)
        natureButton = findViewById(R.id.natureButton)
        foodButton = findViewById(R.id.foodButton)
        fitnessButton = findViewById(R.id.fitnessButton)
        partyButton = findViewById(R.id.partyButton)
        greekButton = findViewById(R.id.greekButton)

        barButton.setOnClickListener {
            Log.d("barButton", "Clicking")
        }

        natureButton.setOnClickListener {
            Log.d("natureButton", "Clicking")
        }

        foodButton.setOnClickListener {
            Log.d("foodButton", "Clicking")
        }

        fitnessButton.setOnClickListener {
            Log.d("fitnessButton", "Clicking")
        }

        partyButton.setOnClickListener {
            Log.d("partyButton", "Clicking")
        }

        greekButton.setOnClickListener {
            Log.d("greekButton", "Clicking")
        }
    }

}