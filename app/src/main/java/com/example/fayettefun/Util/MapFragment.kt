package com.example.fayettefun.Util

import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.example.fayettefun.Model.MapPoint
import com.example.fayettefun.R
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.CopyrightOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import com.example.fayettefun.View.ViewEvent // Adjust the package path if necessary


class OpenStreetMapFragment : Fragment(), Marker.OnMarkerClickListener {

    private lateinit var mMap: MapView
    private lateinit var userMarker: Marker
    private lateinit var mapController: IMapController
    private val eventMarkers = mutableMapOf<String, MapPoint>()

    // Location variables
    private lateinit var mCurrentLocation: GeoPoint

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_map, container, false)
        mMap = root.findViewById(R.id.map)
        userMarker = Marker(mMap) // Initializes user maker
        setupMapOptions() // Sets ups map options

        mapController = mMap.controller // Sets up map controller
        mapController.setZoom(17.0) // Adjusts map zoom

        return root
    }

    override fun onResume() {
        super.onResume()
        mMap.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMap.onPause()
    }

    private fun setupMapOptions() {
        mMap.isTilesScaledToDpi = true
        mMap.setTileSource(TileSourceFactory.MAPNIK)
        addCopyrightOverlay()
        addRotationOverlay()

    }

    private fun addUserMarker() {
        mMap.overlays.remove(userMarker) // Remove existing user marker
        userMarker.position = mCurrentLocation // Updates marker position
        userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        userMarker.icon =
            ResourcesCompat.getDrawable(resources, R.drawable.user_pin, null) // Marker icon
        mMap.overlays.add(userMarker) // Add the updated user marker
        mMap.invalidate() // Force map redraw
    }

    private fun addRotationOverlay() {
        val rotationGestureOverlay = RotationGestureOverlay(mMap)
        rotationGestureOverlay.isEnabled
        mMap.setMultiTouchControls(true)
        mMap.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)  // Makes zoom controls not be visible
        mMap.overlays.add(rotationGestureOverlay)
    }

    private fun addCopyrightOverlay() {
        val copyrightNotice: String = mMap.tileProvider.tileSource.copyrightNotice
        val copyrightOverlay = CopyrightOverlay(context)
        copyrightOverlay.setCopyrightNotice(copyrightNotice)
        mMap.overlays.add(copyrightOverlay)
    }

    fun centerLocation() {
        if (::mapController.isInitialized) {
            mapController.setCenter(mCurrentLocation)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            OpenStreetMapFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onMarkerClick(marker: Marker?, mapView: MapView?): Boolean {
        marker?.let {
            val eventDescription =
                eventMarkers[marker.id]?.description ?: "No description available"
            val eventTitle = eventMarkers[marker.id]?.locationName ?: "No title available"
            val eventLocation = eventMarkers[marker.id]?.address ?: "No address available"
            val eventTime = eventMarkers[marker.id]?.eventTime ?: "No time available"
            val eventDate = eventMarkers[marker.id]?.eventDate ?: "No date available"
            val eventID = eventMarkers[marker.id]?.id ?: "No ID available"
            val eventRSVP = eventMarkers[marker.id]?.rsvpUser ?: "No RSVPs"
            val eventCreator = eventMarkers[marker.id]?.creatorName ?: "Creator Unknown"
            val intent = Intent(activity, ViewEvent::class.java).apply {
                putExtra("EVENT_ID", eventID)
                putExtra("EVENT_DESCRIPTION", eventDescription)
                putExtra("EVENT_TITLE", eventTitle)
                putExtra("EVENT_LOCATION", eventLocation)
                putExtra("EVENT_TIME", eventTime)
                putExtra("EVENT_DATE", eventDate)
                putExtra("EVENT_RSVP", eventRSVP)
                putExtra("EVENT_CREATOR", eventCreator)
            }
            startActivity(intent)
        }
        return true
    }


    fun updateCurrentLocation(location: Location) {
        mCurrentLocation = GeoPoint(
            location.latitude,
            location.longitude
        ) // Transforms current location to geolocation
        addUserMarker()
    }

    fun addActiveEventMarkers(activeEvents: List<MapPoint>) {
        for (event in activeEvents) {
            val marker = Marker(mMap)
            marker.position = GeoPoint(event.latitude, event.longitude)
            marker.icon = ResourcesCompat.getDrawable(resources, R.drawable.temp_even_icon, null)
            marker.id = event.id

            // Set this fragment as the click listener for the marker
            marker.setOnMarkerClickListener(this)

            // Add marker to the map
            mMap.overlays.add(marker)

            eventMarkers[marker.id] = event
        }

        // Force map redraw
        mMap.invalidate()
    }

    fun randomEvent(randomMapPoint: MapPoint) {
        val rLatitude = randomMapPoint.latitude // Random event latitude
        val rLongitude = randomMapPoint.longitude // Random event longitude
        val rLocation = GeoPoint(rLatitude, rLongitude) // Geolocation
        mapController.setCenter(rLocation) // Sets the camera on the random event

    }
}

