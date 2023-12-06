package com.example.fayettefun.View

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.fayettefun.Model.MapPoint
import com.example.fayettefun.R
import com.example.fayettefun.ViewModel.CreateEventViewModel
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class CreateEvent : AppCompatActivity() {

    private lateinit var createEventViewModel: CreateEventViewModel

    // Instance variables
    private lateinit var nameEvent: EditText
    private lateinit var addressEvent: EditText
    private lateinit var timeEvent: Button
    private lateinit var dateEvent: Button
    private lateinit var descriptionEvent: EditText
    private lateinit var createButton: Button
    private lateinit var geocoder: Geocoder
    private lateinit var checkBar: CheckBox
    private lateinit var checkNature: CheckBox
    private lateinit var checkFood: CheckBox
    private lateinit var checkHealth: CheckBox
    private lateinit var checkParty: CheckBox
    private lateinit var checkGreek: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        // Initialize ViewModel
        createEventViewModel = ViewModelProvider(this).get(CreateEventViewModel::class.java)

        // Initialize components
        nameEvent = findViewById(R.id.event_name)
        addressEvent = findViewById(R.id.event_address)
        timeEvent = findViewById(R.id.event_time)
        dateEvent = findViewById(R.id.event_date)
        descriptionEvent = findViewById(R.id.event_description)
        createButton = findViewById(R.id.create_button)
        geocoder = Geocoder(this, Locale.getDefault())

        // Button methods
        timeEvent.setOnClickListener { // Used to get the current time(hour and minute) calling the Calendar class
            val currentTime = Calendar.getInstance()
            val currentHour = currentTime.get(Calendar.HOUR) // Current hour
            val currentMinute = currentTime.get(Calendar.MINUTE) // Current min

            // Set the data in the format (HH:MM)
            TimePickerDialog(this, { _, hour, minute ->
                timeEvent.text = "$hour:$minute"
            }, currentHour, currentMinute, false).show()
        }

        dateEvent.setOnClickListener {  // Used to get the current data(month, day, year) calling the Calendar class
            val currentDate = Calendar.getInstance()
            val currentMonth = currentDate.get(Calendar.MONTH) // Current month
            val currentDay = currentDate.get(Calendar.DAY_OF_MONTH) // Current day
            val currentYear = currentDate.get(Calendar.YEAR) // Current year

            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, day ->
                val formattedDate = String.format("%02d-%02d-%d", month + 1, day, year) // Sets the data in the format (MM:DD:YYYY)
                dateEvent.text = formattedDate
            }, currentYear, currentMonth, currentDay).show()
        }

        createButton.setOnClickListener { // When pressed an event will be create and added to the database and map
            val event = createEvent()
            if(event){ // If the event return true(all the fields were entered and it was created..
                finish() // Finish the activity
            }
        }

    }

    // This method will get the address entered by the user and will transform it to a geolocation 
    private fun createEvent(): Boolean {
        val setName = nameEvent.text.toString()
        val setCreatorId = createEventViewModel.getCurrentUserId() ?: ""
        val setCreatorName = getCreatorName() ?: ""
        val setAddress = addressEvent.text.toString()
        val setTime = timeEvent.text.toString()
        val setDate = dateEvent.text.toString()
        val setDescription = descriptionEvent.text.toString()
        var setLatitude = 0.0
        var setLongitude = 0.0

        // Gets the address entered by the user and will get latitude and longitude from it
        if (setAddress.isNotEmpty()) {
            val coordinates = geocoder.getFromLocationName(setAddress, 1)
            if (coordinates != null && coordinates.isNotEmpty()) {
                setLatitude = coordinates[0].latitude
                setLongitude = coordinates[0].longitude
            }
        }

        return if (setName.isNotEmpty() && setAddress.isNotEmpty() && setTime.isNotEmpty() && setDate.isNotEmpty() && setDescription.isNotEmpty()) {
            // Create MapPoint object to add it as a record to Firebase
            val eventId = generateEventId()  // Generate a unique event ID
            val newEvent = MapPoint(eventId, setCreatorId, setCreatorName, setLatitude, setLongitude, setName, setDate, setTime, setDescription,setAddress, "")

            if (newEvent != null) {
                createEventViewModel.addMapPointToDatabase(newEvent)
            }
            toastMessage("Event Created!")
            true
        } else {
            toastMessage("Enter all fields to create event")
            false
        }
    }

    // Function to generate a unique UUID
    private fun generateEventId(): String {
        return UUID.randomUUID().toString()
    }


    private fun toastMessage(message: String){ // Used to display messages to the user
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(this, message, duration)
        toast.show()
    }

    private fun getCreatorName(): String? {
        val currentUserId = createEventViewModel.getCurrentUserId()
        return if (currentUserId != null) {
            var creatorName: String? = null
            createEventViewModel.getCreatorName(
                currentUserId,
                onSuccess = { name ->
                    creatorName = name
                },
                onFailure = {
                    Log.w("NewEventActivity", "Failed to fetch creator's name.")
                }
            )
            creatorName
        } else {
            Log.w("NewEventActivity", "No user signed in.")
            null
        }
    }

}