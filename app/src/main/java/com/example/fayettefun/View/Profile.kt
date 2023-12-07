package com.example.fayettefun.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fayettefun.Model.FirebaseRepository
import com.example.fayettefun.R
import com.example.fayettefun.ViewModel.LoginViewModel
import com.example.fayettefun.ViewModel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.osmdroid.views.MapView
import kotlin.collections.Map

class Profile : AppCompatActivity() {

    // Instance variables
    private lateinit var picture: ImageView
    private lateinit var editTextName: EditText
    private lateinit var editTextBio: EditText
    private lateinit var buttonSave: Button
    private lateinit var userId: String

    private val profileViewModel: ProfileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            userId = currentUser.uid
        } else {
            // Error
        }


        // Initialize variables
        picture = findViewById(R.id.profile_picture)
        editTextName = findViewById(R.id.editTextName)
        editTextBio = findViewById(R.id.editTextBio)
        buttonSave = findViewById(R.id.buttonSave)

        // Load user profile data
        profileViewModel.userData.observe(this, Observer { user ->
            // Update UI elements with user data
            if (user != null) {
                editTextName.setText(user.userName)
            }
            if (user != null) {
                editTextBio.setText(user.description)
            }
            // Update other UI elements as needed
        })

        // Set up the Save button click listener
        buttonSave.setOnClickListener {
            // Update user profile data in Firestore
            profileViewModel.updateUserData(
                userId,
                editTextName.text.toString(),
                editTextBio.text.toString()
            )
            val intent = Intent(this, Map::class.java)
            startActivity(intent)
            finish()
        }
    }
}