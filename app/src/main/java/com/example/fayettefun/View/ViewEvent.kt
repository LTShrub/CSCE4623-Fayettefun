package com.example.fayettefun.View

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fayettefun.Model.MapPoint
import com.example.fayettefun.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction

private val databaseReference = FirebaseDatabase.getInstance().getReference("events")


class ViewEvent : AppCompatActivity() {

    // Instance variables
    private lateinit var imageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var addressEditText: TextView
    private lateinit var dateEditText: TextView
    private lateinit var timeEditText: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var rsvpFab: FloatingActionButton
    private lateinit var profilePicture: ImageView
    private lateinit var usernameTextView: TextView
    private lateinit var postedByTextView: TextView
    private lateinit var numberEditText: TextView
    private lateinit var tagsTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        // Initialize variables
        imageView = findViewById(R.id.imageView3)
        titleTextView = findViewById(R.id.textTile)
        addressEditText = findViewById(R.id.editTextAddress)
        dateEditText = findViewById(R.id.editTextDate)
        timeEditText = findViewById(R.id.editTextTime)
        rsvpFab = findViewById(R.id.rsvpFab)
        profilePicture = findViewById(R.id.profilePicture)
        usernameTextView = findViewById(R.id.textUsername)
        postedByTextView = findViewById(R.id.textView12)
        numberEditText = findViewById(R.id.editTextNumber)
        descriptionTextView = findViewById(R.id.editTextDescription)
        tagsTextView = findViewById(R.id.textView9)


        // Handles the intent objects
        val intent = intent
        val data = intent.extras

        if (data != null) { // Handles intent from random button in map activity
            if (data.containsKey("RANDOM_EVENT_KEY")) {
                val event = data.getParcelable<MapPoint>("RANDOM_EVENT_KEY")
                Log.d("Intent", "Received Event: $event")
                val eventDescription = event?.description
                descriptionTextView.text = eventDescription

                val eventTitle = event?.locationName
                titleTextView.text = eventTitle

                val eventLocation = event?.locationName
                addressEditText.text = eventLocation

                val eventTime = event?.eventTime
                timeEditText.text = eventTime

                val eventDate = event?.eventDate
                dateEditText.text = eventDate

                val rsvpNum = event?.rsvpUser
                numberEditText.text = rsvpNum
            }
            else{ // Handles intent from clicking an event in map fragment
                val eventDescription = intent.getStringExtra("EVENT_DESCRIPTION")
                descriptionTextView.text = eventDescription

                // Get the event title
                val eventTitle = intent.getStringExtra("EVENT_TITLE")
                titleTextView.text = eventTitle

                //Get the event location
                val eventLocation = intent.getStringExtra("EVENT_LOCATION")
                addressEditText.text = eventLocation

                //get the event time
                val eventTime = intent.getStringExtra("EVENT_TIME")
                timeEditText.text = eventTime

                //get the event date
                val eventDate = intent.getStringExtra("EVENT_DATE")
                dateEditText.text = eventDate

                //get total rsvp
                val rsvpNum = intent.getStringExtra("EVENT_RSVP")
                numberEditText.text = rsvpNum

                val tags = intent.getStringArrayExtra("EVENT_TAGS")?.toList() ?: listOf()
                displayTags(tags)
            }
        }

        // Button
        rsvpFab.setOnClickListener {
            val eventId = "EVENT_ID"
            incrementRsvpCount(eventId) //Call increment function
        }
    }

    // Display tags in the TextView
    private fun displayTags(tags: List<String>) {
        tagsTextView.text = tags.joinToString(", ")
    }

    //Increment number of RSVPs
    private fun incrementRsvpCount(eventId: String) {
        val eventRef = databaseReference.child(eventId).child("rsvpCount")
        eventRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                var count = mutableData.getValue(Int::class.java) ?: 0
                mutableData.value = count + 1
                return Transaction.success(mutableData)
            }

            override fun onComplete(databaseError: DatabaseError?, committed: Boolean, dataSnapshot: DataSnapshot?) {
                // Transaction completed
                Log.d("ViewEvent", "incrementRsvpCount:onComplete: $databaseError")
            }
        })
    }
}
