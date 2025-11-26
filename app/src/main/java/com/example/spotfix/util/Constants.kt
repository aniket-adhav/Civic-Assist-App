package com.example.spotfix.util

object Constants {
    // IMPORTANT: Use the computer's local IP address or 10.0.2.2 for emulator
    // If running server on localhost, use 10.0.2.2 for Android Emulator
    // Or replace with your local network IP (e.g., 192.168.1.XX)
    const val BASE_URL = "http://10.0.2.2:5000/api/"

    // SharedPreferences key for saving session cookies
    const val SESSION_PREFS = "session_prefs"
    const val COOKIE_KEY = "cookie_key"
}