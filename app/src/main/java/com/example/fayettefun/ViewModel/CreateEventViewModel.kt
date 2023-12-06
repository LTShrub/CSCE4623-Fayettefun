package com.example.fayettefun.ViewModel

import androidx.lifecycle.ViewModel
import com.example.fayettefun.Model.FirebaseRepository
import com.example.fayettefun.Model.MapPoint
class CreateEventViewModel : ViewModel() {
    private val firebaseRepository = FirebaseRepository()

    // Function to add a new event to Firestore
    fun addMapPointToDatabase(mapPoint: MapPoint) {
        firebaseRepository.addMapPoint(mapPoint)
    }

    fun getCurrentUserId(): String? {
        return firebaseRepository.getCurrentUserId()
    }
    fun getCreatorName(creatorUid: String, onSuccess: (String) -> Unit, onFailure: () -> Unit) {
        firebaseRepository.getUserName(creatorUid, onSuccess, onFailure)
    }
}