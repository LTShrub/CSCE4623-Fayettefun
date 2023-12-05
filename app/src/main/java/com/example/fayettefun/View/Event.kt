package com.example.fayettefun.View

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fayettefun.R
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
class Event : AppCompatActivity() {

    // Instance variables
    private lateinit var imageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var addressEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var timeEditText: EditText
    private lateinit var descriptionTextView: TextView
    private lateinit var rsvpFab: FloatingActionButton
    private lateinit var profilePicture: ImageView
    private lateinit var usernameTextView: TextView
    private lateinit var postedByTextView: TextView
    private lateinit var numberEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        val eventId = intent.getStringExtra("EVENT_ID") ?: return

        // Initialize variables
        imageView = findViewById(R.id.imageView3)
        titleTextView = findViewById(R.id.textTile)
        addressEditText = findViewById(R.id.editTextAddress)
        dateEditText = findViewById(R.id.editTextDate)
        timeEditText = findViewById(R.id.editTextTime)
        descriptionTextView = findViewById(R.id.editTextDescription)
        rsvpFab = findViewById(R.id.rsvpFab)
        profilePicture = findViewById(R.id.profilePicture)
        usernameTextView = findViewById(R.id.textUsername)
        postedByTextView = findViewById(R.id.textView12)
        numberEditText = findViewById(R.id.editTextNumber)

    }
}
