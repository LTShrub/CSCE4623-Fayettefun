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

        editTextName.setText(getUserName())
        editTextBio.setText(getUserBio())

        // Set up the Save button click listener
        buttonSave.setOnClickListener {
            // Update user profile data in Firestore
            profileViewModel.updateUserData(
                userId,
                editTextName.text.toString(),
                editTextBio.text.toString()
            )
            val intent = Intent(this, com.example.fayettefun.View.Map::class.java)
            startActivity(intent)
        }
    }

    private fun getUserName(): String? {
        val currentUserId = userId
        return if (currentUserId != null) {
            var creatorName: String? = null
            profileViewModel.getUserName(
                currentUserId,
                onSuccess = { name ->
                    creatorName = name
                    // Update UI with user name when it's available
                    editTextName.setText(name)
                },
                onFailure = {
                    Log.w("ProfileActivity", "Failed to fetch user's name.")
                }
            )
            // Return null or a default value if the name is not available immediately
            creatorName ?: ""
        } else {
            Log.w("ProfileActivity", "No user signed in.")
            null
        }
    }

    private fun getUserBio(): String? {
        val currentUserId = userId
        return if (currentUserId != null) {
            var creatorBio: String? = null
            profileViewModel.getUserBio(
                currentUserId,
                onSuccess = { description ->
                    creatorBio =  description
                    // Update UI with user name when it's available
                    editTextBio.setText(description)
                },
                onFailure = {
                    Log.w("ProfileActivity", "Failed to fetch user's name.")
                }
            )
            // Return null or a default value if the name is not available immediately
            creatorBio ?: ""
        } else {
            Log.w("ProfileActivity", "No user signed in.")
            null
        }
    }

}