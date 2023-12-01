package com.example.fayettefun.View

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.ImageButton
import com.example.fayettefun.R

class ProfileView : AppCompatActivity() {

    // On-Screen Buttons
    private lateinit var returnButton: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initializes buttons
        returnButton = findViewById(R.id.return_back_button)

        // Allow for edge-to-edge display
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }

        // OnClickListener for buttons
        returnButton.setOnClickListener {  // Take user back to Map
            val intent = Intent(this, MapView::class.java)
            startActivity(intent)
        }
    }

}