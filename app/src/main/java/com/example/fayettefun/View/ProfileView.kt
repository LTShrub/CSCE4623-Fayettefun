package com.example.fayettefun.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.fayettefun.R

class ProfileView : AppCompatActivity() {

    // Instance variables
    private lateinit var picture: ImageView
    private lateinit var name: TextView
    private lateinit var bio: TextView
    private lateinit var mRegistrations: Button
    private lateinit var createdEvents: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize variables
        picture = findViewById(R.id.profile_picture)
        name = findViewById(R.id.profile_name)
        bio = findViewById(R.id.profile_bio)
        mRegistrations = findViewById(R.id.button1)
        createdEvents = findViewById(R.id.button2)


        mRegistrations.setOnClickListener {
            Log.d("REG BUT", "Clicking")
        }
        createdEvents.setOnClickListener {
            Log.d("CREA BUT", "Clicking")
        }
    }
}