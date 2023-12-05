package com.example.fayettefun.Model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.PropertyName

data class MapPoint(
    @get:PropertyName("id") var id: String = "",
    @get:PropertyName("latitude") var latitude: Double = 0.0,
    @get:PropertyName("longitude") var longitude: Double = 0.0,
    @get:PropertyName("location-name") var locationName: String = "",
    @get:PropertyName("event-date") var eventDate: String = "",
    @get:PropertyName("event-time") var eventTime: String = "",
    @get:PropertyName("description") var description: String = "",
    @get:PropertyName("address") var address: String = "",
    @get:PropertyName("rsvp-users") var rsvpUser: String = "",
    @get:PropertyName("tag") var tag: String = "",
) : Parcelable { // Will make he MapPoint objects be parcelable so they can be send through intents

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(locationName)
        parcel.writeString(eventDate)
        parcel.writeString(eventTime)
        parcel.writeString(description)
        parcel.writeString(address)
        parcel.writeString(rsvpUser)
        parcel.writeString(tag)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MapPoint> {
        override fun createFromParcel(parcel: Parcel): MapPoint {
            return MapPoint(parcel)
        }

        override fun newArray(size: Int): Array<MapPoint?> {
            return arrayOfNulls(size)
        }
    }
}
