package com.example.fayettefun.ViewModel

import androidx.lifecycle.ViewModel
import com.example.fayettefun.Model.FirebaseRepository
import com.example.fayettefun.Model.User

class ProfileViewModel(private val firebaseRepository: FirebaseRepository) : ViewModel() {

    // LiveData to observe user data changes
    val userData = firebaseRepository.getUserDataLiveData()

    fun getUserName(userId: String, onSuccess: (String) -> Unit, onFailure: () -> Unit) {
        firebaseRepository.getUserName(userId, onSuccess, onFailure)
    }
    // Function to update user data
    fun updateUserData(userId: String, newName: String, newBio: String) {
        firebaseRepository.updateUserData(userId, newName, newBio)
    }

    @Suppress("unused")
    constructor() : this(FirebaseRepository())
}