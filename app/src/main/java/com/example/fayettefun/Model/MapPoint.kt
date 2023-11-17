package com.example.fayettefun.Model

import com.google.firebase.database.PropertyName
data class MapPoint(
    @get:PropertyName("latitude") var latitude: Float = 0.0F,
    @get:PropertyName("longitude") var longitude: Float = 0.0F,
    @get:PropertyName("location-name") var locationName: String = "",
    @get:PropertyName("event-date") var eventDate: String = "",
    @get:PropertyName("description") var description: String = "",
    @get:PropertyName("rsvp-users") var rsvpUser: String = ""
)