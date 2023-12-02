package com.example.fayettefun.Model

//
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class FirebaseRepository {
    private val database = FirebaseFirestore.getInstance()
    private val addEventRef = database.collection("active-events")


    // Call to add a new event to the Firestore database
    fun addMapPoint(mapPoint: MapPoint) {
        addEventRef.add(mapPoint)
            .addOnSuccessListener { documentReference ->
                // Handle success if needed
                println("DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                // Handle errors if needed
                println("Error adding document: $e")
            }
    }

    fun addMapPointsListener(listener: (List<MapPoint>) -> Unit): ListenerRegistration {
        return addEventRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                // Handle the exception
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                val mapPoints = mutableListOf<MapPoint>()
                for (document in snapshot.documents) {
                    val mapPoint = document.toObject(MapPoint::class.java)
                    mapPoint?.let {
                        mapPoints.add(it)
                    }
                }

                listener(mapPoints)
            }
        }
    }
}