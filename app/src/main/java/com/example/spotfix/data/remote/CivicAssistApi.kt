package com.example.spotfix.data.remote


data class Report(
    val _id: String,
    val imageUrl: String,
    val description: String,
    val category: String,
    val location: Location,
    val username: String,
    val status: String,
    val createdAt: String
    // Note: Use @SerializedName if JSON key names differ from Kotlin property names
)

data class Location(
    val latitude: Double,
    val longitude: Double
)