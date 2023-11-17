package com.example.fayettefun.Model

//
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
class FirebaseRepository {
    private val database = FirebaseDatabase.getInstance()
    private val addEventRef = database.getReference("active-events")

    //call to add a new event to the database
    fun addMapPoint(mapPoint: MapPoint) {
        val key = addEventRef.push().key
        key?.let {
            addEventRef.child(it).setValue(mapPoint)
        }
    }

}