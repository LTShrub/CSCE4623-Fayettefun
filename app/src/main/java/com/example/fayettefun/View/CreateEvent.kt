package com.example.fayettefun.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.fayettefun.R

class CreateEvent : AppCompatActivity() {

    // Instance variables
    private lateinit var nameEvent: EditText
    private lateinit var descriptionEvent: EditText
    private lateinit var dateEvent: EditText
    private lateinit var timeEvent: EditText
    private lateinit var address: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)
    }
}