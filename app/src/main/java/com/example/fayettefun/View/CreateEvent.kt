package com.example.fayettefun.View

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.example.fayettefun.R
import java.util.Calendar

class CreateEvent : AppCompatActivity() {

    // Instance variables
    private lateinit var nameEvent: EditText
    private lateinit var address: EditText
    private lateinit var timeEvent: Button
    private lateinit var dateEvent: Button
    private lateinit var descriptionEvent: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        // Initialize components
        nameEvent = findViewById(R.id.event_name)
        address = findViewById(R.id.event_address)
        timeEvent = findViewById(R.id.event_time)
        dateEvent = findViewById(R.id.event_date)
        descriptionEvent = findViewById(R.id.event_description)

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

    }

    // This method will get the address entered by the user and will transform it to a geolocation 
    private fun setAddress(){

    }

}