package com.example.spotfix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.example.spotfix.ScreensUser.HomePage
import com.example.spotfix.navigation.NavGraph
import com.example.spotfix.screens.Dashboard
import com.example.spotfix.ui.theme.SpotFixTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpotFixTheme{ // Disable dynamic colors
                Surface(
                    modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()
                    NavGraph(navController)
                   // HomePage()
                }
            }
        }
    }
}

//@Composable
//fun FullScreenMap() {
//    val myLocation = LatLng(37.7749, -122.4194) // San Francisco
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(myLocation, 12f)
//    }
//
//    GoogleMap(
//        modifier = Modifier.fillMaxSize(),
//        cameraPositionState = cameraPositionState
//    ) {
//        Marker(
//            state = rememberMarkerState(position = myLocation),
//            title = "My Location"
//        )
//    }
//}