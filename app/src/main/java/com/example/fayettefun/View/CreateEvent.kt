package com.example.fayettefun.View

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.example.fayettefun.R
import java.util.Calendar
import java.util.Locale

class CreateEvent : AppCompatActivity() {

    // Instance variables
    private lateinit var nameEvent: EditText
    private lateinit var addressEvent: EditText
    private lateinit var timeEvent: Button
    private lateinit var dateEvent: Button
    private lateinit var descriptionEvent: EditText
    private lateinit var createButton: Button
    private lateinit var geocoder: Geocoder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

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
            createEvent()
        }

    }

    // This method will get the address entered by the user and will transform it to a geolocation 
    private fun createEvent(){
        val setName = nameEvent.text.toString()
        val setTime = timeEvent.text.toString()
        val setDate = dateEvent.text.toString()
        val setDescription = descriptionEvent.text.toString()
        var setLatitude = 0.0
        var setLongitude = 0.0

        // Gets the address entered by the user and will get latitude and longitude from it
        val setAddress = geocoder.getFromLocationName(addressEvent.text.toString(), 1)
        if(!setAddress.isNullOrEmpty()){
            // Need to be transformed to string before creating MapPoint object
            setLatitude = setAddress[0].latitude
            setLongitude = setAddress[0].longitude
        }
        Log.d("Name", "$setName")
        Log.d("Time", "$setTime")
        Log.d("Date", "$setDate")
        Log.d("Description", "$setDescription")
        Log.d("Latitude", "$setLatitude")
        Log.d("Longitude", "$setLongitude")
    }

}