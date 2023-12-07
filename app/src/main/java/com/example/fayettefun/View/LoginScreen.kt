package com.example.fayettefun.View

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.fayettefun.Model.FirebaseRepository
import com.example.fayettefun.R
import com.example.fayettefun.ViewModel.LoginViewModel
import com.example.fayettefun.ViewModel.LoginViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginScreen : AppCompatActivity() {

    private lateinit var buttonGoogle: Button
    private lateinit var buttonEmail: Button
    private lateinit var buttonSignUP: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var loginViewModel: LoginViewModel

    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            handleSignInResult(data)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val firebaseRepository = FirebaseRepository()
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(firebaseRepository))[LoginViewModel::class.java]

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        buttonEmail = findViewById(R.id.buttonEmail)
        buttonEmail.setOnClickListener{
            tempOverride()
        }

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        buttonGoogle = findViewById(R.id.buttonGoogle)
        buttonGoogle.setOnClickListener {
            signInWithGoogle()
        }

    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }

    private fun handleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            // Google Sign-In was successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)!!
            firebaseAuthWithGoogle(account.idToken!!)
            val intent = Intent(this, Map::class.java)
            startActivity(intent)
            finish()
        } catch (e: ApiException) {
            // Google Sign-In failed, update UI accordingly
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        loginViewModel.signInWithGoogle(idToken)
    }

    private fun tempOverride(){
        //TODO REMOVE ME ASAP
        val intent = Intent(this, Map::class.java)
        startActivity(intent)
        finish()
    }
}