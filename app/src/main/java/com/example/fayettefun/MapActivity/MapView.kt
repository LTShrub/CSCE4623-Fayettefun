package com.example.fayettefun.MapActivity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.fayettefun.R
import com.example.fayettefun.Util.LocationUtilCallback
import com.example.fayettefun.Util.createLocationCallback
import com.example.fayettefun.Util.createLocationRequest
import com.example.fayettefun.Util.getLastLocation
import com.example.fayettefun.Util.replaceFragmentInActivity
import com.example.fayettefun.Util.stopLocationUpdates
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.osmdroid.config.Configuration
import kotlin.properties.Delegates

class MapView : AppCompatActivity() {

    private lateinit var mapsFragment: OpenStreetMapFragment // Instance variables of te map

    // Permission and location variables
    private var locationPermissionEnabled: Boolean = false
    private var locationRequestsEnabled: Boolean = false
    private lateinit var locationProviderClient: FusedLocationProviderClient
    private lateinit var mLocationCallback: LocationCallback
    private var centeredCamera = false


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        mapsFragment = supportFragmentManager.findFragmentById(R.id.map_fragment_container)
                as OpenStreetMapFragment? ?: OpenStreetMapFragment.newInstance().also {
            replaceFragmentInActivity(it, R.id.map_fragment_container)
        }
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation(this, locationProviderClient, locationUtilCallback)
    }

    override fun onStart() {
        super.onStart()
        startLocationRequests()
        //Log.d("MainActivity", "mLatitude: $mLatitude, mLongitude: $mLongitude")
    }

    override fun onRestart() {
        super.onRestart()
        // switch location flag back to off to get centered camera again
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

}


