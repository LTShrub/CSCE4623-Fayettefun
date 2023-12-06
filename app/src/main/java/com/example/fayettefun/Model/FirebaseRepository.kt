package com.example.fayettefun.Model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.util.Random

class FirebaseRepository {
    private val database = FirebaseFirestore.getInstance()
    private val addEventRef = database.collection("active-events")
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userDataLiveData = MutableLiveData<User?>()


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

    fun getUserDataLiveData(): MutableLiveData<User?> {
        return userDataLiveData
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

    fun signInWithGoogle(idToken: String, onComplete: (Boolean, String?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    val userId = firebaseAuth.currentUser?.uid
                    val email = user?.email

                    if (userId != null && email != null) {
                        // Create or update the user document in the "users" collection
                        updateUserInFirestore(userId, email)
                    }


                    onComplete.invoke(true, userId)
                } else {
                    onComplete.invoke(false, null)
                }
            }
    }
    private fun updateUserInFirestore(userId: String, email: String) {
        val userDocRef = database.collection("users").document(userId)

        val userData = hashMapOf(
            "email" to email,
        )

        userDocRef.set(userData)
            .addOnSuccessListener {
                println("User document created/updated in Firestore for user: $userId")
            }
            .addOnFailureListener { e ->
                println("Error creating/updating user document in Firestore: $e")
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

    fun updateUserData(userId: String, newName: String, newBio: String) {
        val userDocRef = database.collection("users").document(userId)

        val userData = hashMapOf(
            "username" to newName,
            "description" to newBio
            // Add other fields as needed
        )
        userDocRef.update(userData as Map<String, Any>)
            .addOnSuccessListener {
                // Handle success
            }
            .addOnFailureListener { e ->
                // Handle failure
            }
    }

    fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    fun getUserName(userUid: String, onSuccess: (String) -> Unit, onFailure: () -> Unit) {
        val userDocRef = database.collection("users").document(userUid)

        userDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userName = documentSnapshot.getString("username")
                    if (userName != null) {
                        onSuccess.invoke(userName)
                    } else {
                        onFailure.invoke()
                    }
                } else {
                    onFailure.invoke()
                }
            }
            .addOnFailureListener { e ->
                onFailure.invoke()
            }
    }

}
