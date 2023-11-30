package com.example.fayettefun.Model

import com.google.firebase.database.PropertyName
data class MapPoint(
    @get:PropertyName("latitude") var latitude: Double = 0.0,
    @get:PropertyName("longitude") var longitude: Double = 0.0,
    @get:PropertyName("location-name") var locationName: String = "",
    @get:PropertyName("event-date") var eventDate: String = "",
    @get:PropertyName("event-time") var eventTime: String = "",
    @get:PropertyName("description") var description: String = "",
    @get:PropertyName("rsvp-users") var rsvpUser: String = ""
)