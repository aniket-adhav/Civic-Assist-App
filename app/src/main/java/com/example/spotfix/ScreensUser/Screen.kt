package com.example.spotfix.ScreensUser

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.android.gms.maps.model.LatLng

// -------------------- MODELS --------------------

// Complaint post model
data class Complaint(
    val id: Int,
    val userName: String,
    val profileImage: Painter,
    val location: String,
    val problemTitle: String,
    val problemDescription: String,
    val postImage: Painter,
    val locationLatLng: LatLng,
    val initialLikes: Int,
    val initialDislikes: Int,
    val isLiked: Boolean = false,
    val isDisliked: Boolean = false,
    val date: String? = null,
    val status: String? = null,
    val imageRes: Int? = null
)

// Search result model
data class SearchResult(
    val title: String,
    val subtitle: String,
    val icon: ImageVector
)

// Bottom navigation item
data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

// -------------------- SCREENS --------------------
sealed class Screen(val route: String) {

    object Login : Screen("login")
    object OTPs : Screen("otp/{phone}") // phone as nav argument
    object Home : Screen("home")
    object NewComplaint : Screen("new_complaint")
    object MyComplaints : Screen("my_complaints")
    object Profile : Screen("profile")
    object Search : Screen("search")
    object AdminLogin : Screen("Admin_login")
    object Dashboard : Screen("dashboardAdmin")

    object Complaints : Screen("Complaints")
    object Analysis : Screen("Analysis")
    object Admin : Screen("Admin")

    object Details : Screen("details")

    object Settings : Screen("settings")

    // FullPost now just holds a Complaint object; no 'override'
    data class FullPost(val complaint: Complaint) : Screen("full_post")


    object OTP : Screen("otp/{phone}/{verificationId}") {
        fun createRoute(phone: String, verificationId: String): String {
            return "otp/$phone/$verificationId"
        }
    }
}
