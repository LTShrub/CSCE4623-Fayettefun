package com.example.fayettefun.Model

import com.google.firebase.database.PropertyName
data class User(
    @get:PropertyName("username") var userName: String = "",
    @get:PropertyName("description") var description: String = "",
)