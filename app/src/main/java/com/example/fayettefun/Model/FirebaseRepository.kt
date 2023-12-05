package com.example.fayettefun.Model

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.util.Random

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
                    try {
                        val mapPoint = document.toObject(MapPoint::class.java)
                        mapPoint?.let {
                            mapPoints.add(it)
                        }
                    } catch (e: Exception) {
                        println("Error converting document to MapPoint: $e")
                        println("Document data: ${document.data}")
                    }
                }

                listener(mapPoints)
            }
        }
    }

    fun getRandomEvent(onSuccess: (MapPoint?) -> Unit) {
        addEventRef.get() // Query that gets the collection of events
            .addOnSuccessListener { documentReference -> // Handles the result of the query, documentReference represent the result of the query
                if (!documentReference.isEmpty) {
                    val documents = documentReference.documents // list in order of the query
                    val randomIndex = Random().nextInt(documents.size) // Selects a random number with the size of the collection
                    val randomDocument = documents[randomIndex] // Gets the random event using the index created

                    try {
                        val mapPoint = randomDocument.toObject(MapPoint::class.java)
                        onSuccess(mapPoint)
                        println("Success getting the random event")
                    } catch (e: Exception) {
                        println("Error getting the random event")
                    }
                } else {
                    println("Error: Database is empty. Cannot get a random event")
                }
            }
            .addOnFailureListener {
                println("Error getting random event Database is empty")
            }
    }

}
