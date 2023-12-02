package com.example.fayettefun.ViewModel

import androidx.lifecycle.ViewModel
import com.example.fayettefun.Model.FirebaseRepository
import com.example.fayettefun.Model.MapPoint

class MapViewModel : ViewModel(){
    private val firebaseRepository = FirebaseRepository()
    fun startMapPointsListener(listener: (List<MapPoint>) -> Unit) {
        firebaseRepository.addMapPointsListener(listener)
    }
}
