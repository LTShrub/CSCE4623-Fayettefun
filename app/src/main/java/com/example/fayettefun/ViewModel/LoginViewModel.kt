package com.example.fayettefun.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fayettefun.Model.FirebaseRepository

class LoginViewModel(private val firebaseRepository: FirebaseRepository) : ViewModel() {
    private val _userId = MutableLiveData<String?>()
    val userId: LiveData<String?> get() = _userId

    @Suppress("unused")
    constructor() : this(FirebaseRepository())

    fun signInWithGoogle(idToken: String) {
        firebaseRepository.signInWithGoogle(idToken) { success, userId ->
            if (success) {
                _userId.value = userId
                // Handle success, update UI, navigate to next screen, etc.
            } else {
                // Skill issue :v
            }
        }
    }
}