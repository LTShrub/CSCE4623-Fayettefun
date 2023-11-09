package com.example.fayettefun.MapActivity

import android.Manifest
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
import com.example.fayettefun.Util.replaceFragmentInActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.osmdroid.config.Configuration

class MapView : AppCompatActivity() {

    private lateinit var mapsFragment: OpenStreetMapFragment // Instance of map fragment

    // Permission flag variables
    private var locationPermissionEnabled: Boolean = false
    private var locationRequestsEnabled: Boolean = false
    private var cameraPermissionEnabled: Boolean = false
    private var centeredCamera = false // Used to centered


    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("MapsActivity","Permission Granted")
            } else {
                Toast.makeText(this,"Location Permissions not granted. Location disabled on map",
                    Toast.LENGTH_LONG).show()
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
            if(!centeredCamera){
                mapsFragment.updateCurrentLocation(location)
                centeredCamera = true
            }
            mLatitude = location.latitude
            mLongitude = location.longitude
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)


        //Get preferences for tile cache
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        //Check for location permissions
        checkForLocationPermission()

        //Get access to mapsFragment object
        mapsFragment = supportFragmentManager.findFragmentById(R.id.map_fragment_container)
                as OpenStreetMapFragment? ?: OpenStreetMapFragment.newInstance().also {
            replaceFragmentInActivity(it, R.id.map_fragment_container)
        }


    }

    private fun checkForLocationPermission(){
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                //getLastKnownLocation()
                //registerLocationUpdateCallbacks()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
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


}


