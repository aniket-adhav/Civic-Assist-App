package com.example.spotfix

import android.app.Application
import com.google.firebase.FirebaseApp

class SpotFixApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
