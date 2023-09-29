package com.example.fayettefun

import android.app.Application
import com.example.fayettefun.Model.WordRepository
import com.example.fayettefun.Model.WordRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class FayettefunApplication : Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { WordRoomDatabase.getDatabase(this,applicationScope) }
    val repository by lazy { WordRepository(database.wordDao()) }
}
