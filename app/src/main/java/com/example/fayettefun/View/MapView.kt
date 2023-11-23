package com.example.fayettefun.View

import android.Manifest
import android.content.Intent
import android.graphics.PorterDuff
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.fayettefun.R
import com.example.fayettefun.Util.LocationUtilCallback
import com.example.fayettefun.Util.OpenStreetMapFragment
import com.example.fayettefun.Util.createLocationCallback
import com.example.fayettefun.Util.createLocationRequest
import com.example.fayettefun.Util.getLastLocation
import com.example.fayettefun.Util.replaceFragmentInActivity
import com.example.fayettefun.Util.stopLocationUpdates
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import org.osmdroid.config.Configuration

class MapView : AppCompatActivity() {

    private lateinit var mapsFragment: OpenStreetMapFragment // Instance variables of map fragment

    // Permission and location variables
    private var locationPermissionEnabled: Boolean = false
    private var locationRequestsEnabled: Boolean = false
    private lateinit var locationProviderClient: FusedLocationProviderClient
    private lateinit var mLocationCallback: LocationCallback
    private var centeredCamera = false // If it is false, camera is focused. Once it is true camera is not focused.

    // On-Screen Buttons
    private lateinit var centerCameraButton: ImageButton
    private lateinit var randomEventButton: ImageButton
    private lateinit var userProfileButton: ImageButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Load the configuration settings for the OSMDroid library
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        // Initializes the map fragment
        mapsFragment = supportFragmentManager.findFragmentById(R.id.map_fragment_container)
                as OpenStreetMapFragment? ?: OpenStreetMapFragment.newInstance().also {
            replaceFragmentInActivity(it, R.id.map_fragment_container)
        }
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation(this, locationProviderClient, locationUtilCallback)

        // Initializes buttons
        centerCameraButton = findViewById(R.id.center_camera_button)
        randomEventButton = findViewById(R.id.random_event_button)
        userProfileButton = findViewById(R.id.user_profile_button)

        // OnClickListener for buttons
        centerCameraButton.setOnClickListener { // When pressed it will center the camera on the user
            centeredCamera = false
            Log.d("Center Camera Button", "Clicking")
        }
        randomEventButton.setOnClickListener {  // It will take the user to a random local event
            Log.d("Random Button", "Clicking")
        }
        userProfileButton.setOnClickListener {  // It will take the user to their profile
            val intent = Intent(this, ProfileView::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        startLocationRequests()
    }

    override fun onRestart() {
        super.onRestart()
        centeredCamera = false // Centered camera is centered again when this is false
    }


    override fun onStop() {
        super.onStop()
        if (locationRequestsEnabled) {
            locationRequestsEnabled = false
            stopLocationUpdates(locationProviderClient, mLocationCallback)
        }
    }

    private fun startLocationRequests() {
        if (!locationRequestsEnabled) {
            mLocationCallback = createLocationCallback(locationUtilCallback)
            locationRequestsEnabled =
                createLocationRequest(this, locationProviderClient, mLocationCallback)
        }
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                locationPermissionEnabled = true
                startLocationRequests()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                locationPermissionEnabled = true
                startLocationRequests()
            }
            else -> {
                locationPermissionEnabled = false
                Toast.makeText(this, "Location Not Enabled", Toast.LENGTH_LONG).show()
            }
        }
    }

    private val locationUtilCallback = object : LocationUtilCallback {
        override fun requestPermissionCallback() {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
        override fun locationUpdatedCallback(location: Location) {
            if(!centeredCamera){ // Changes the camera center once when the activity is launched
                mapsFragment.updateCurrentLocation(location)
                centeredCamera = true
            }
            mapsFragment.addUserMarker(location) // Updates the location of the user marker all the time

        }
    }

}


