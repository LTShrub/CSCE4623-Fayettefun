package com.example.fayettefun.View

import android.Manifest
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.fayettefun.R
import com.example.fayettefun.Util.LocationUtilCallback
import com.example.fayettefun.Util.OpenStreetMapFragment
import com.example.fayettefun.Util.createLocationCallback
import com.example.fayettefun.Util.createLocationRequest
import com.example.fayettefun.Util.getLastLocation
import com.example.fayettefun.Util.replaceFragmentInActivity
import com.example.fayettefun.Util.stopLocationUpdates
import com.example.fayettefun.ViewModel.MapViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import org.osmdroid.config.Configuration

class Map : AppCompatActivity() {

    private lateinit var mapsFragment: OpenStreetMapFragment // Instance variables of map fragment
    private lateinit var mapViewModel: MapViewModel //create map viewmodel

    // Permission and location variables
    private var locationPermissionEnabled: Boolean = false
    private var locationRequestsEnabled: Boolean = false
    private var centerCamera = true // Used to center the camera on the user marker initially and when returning to activity
    private lateinit var locationProviderClient: FusedLocationProviderClient
    private lateinit var mLocationCallback: LocationCallback

    // On-Screen Buttons
    private lateinit var centerCameraButton: ImageButton
    private lateinit var randomEventButton: ImageButton
    private lateinit var userProfileButton: ImageButton
    private lateinit var newEventButton: ImageButton
    private lateinit var helpButton: ImageButton



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

        // Centers the camera
        mapsFragment.centerLocation()

        // Initializes buttons
        centerCameraButton = findViewById(R.id.center_camera_button)
        randomEventButton = findViewById(R.id.random_event_button)
        userProfileButton = findViewById(R.id.user_profile_button)
        newEventButton = findViewById(R.id.new_event_button)
        helpButton = findViewById(R.id.help_button)

        // OnClickListener for buttons
        centerCameraButton.setOnClickListener { // When pressed it will center the camera on the user
            mapsFragment.centerLocation()
        }
        randomEventButton.setOnClickListener {  // It will take the user to a random local event
            mapViewModel.getRandomEvent { randomMapPoint ->
                if (randomMapPoint != null) {
                    mapsFragment.randomEvent(randomMapPoint) // Passes the random event to the map fragment to be used
                } else {
                    Log.d("Map", "Error with random map point")
                }
                // Opens the random event
                val intent = Intent(this, ViewEvent::class.java)
                intent.putExtra("RANDOM_EVENT_KEY", randomMapPoint)
                startActivity(intent)
            }
        }
        userProfileButton.setOnClickListener {  // It will take the user to their profile
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }
        newEventButton.setOnClickListener {
            val intent = Intent(this, CreateEvent::class.java) // It will take user to event creation activity
            startActivity(intent)
        }
        helpButton.setOnClickListener {
            val intent = Intent(this, Help::class.java)
            startActivity(intent)
        }

        mapViewModel = ViewModelProvider(this)[MapViewModel::class.java]

        mapViewModel.startMapPointsListener { mapPoints ->
            // Update the map with the new map points
            mapsFragment.addActiveEventMarkers(mapPoints)
        }
    }

    override fun onStart() {
        super.onStart()
        startLocationRequests()
    }

    override fun onRestart() {
        super.onRestart()
        centerCamera = true // Makes the camera get focused over the user after coming back to the activity
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
            mapsFragment.updateCurrentLocation(location)
            if(centerCamera){ // If true it centers the camera on the user's position
                mapsFragment.centerLocation()
                centerCamera = false
            }
        }
    }

}


