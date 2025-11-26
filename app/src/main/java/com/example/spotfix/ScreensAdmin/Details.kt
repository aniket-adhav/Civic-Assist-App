package com.example.spotfix.ScreensAdmin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.WhitePoint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.spotfix.R
import com.example.spotfix.ScreensUser.containerColor
import com.example.spotfix.ui.theme.GreenTint1
import com.example.spotfix.ui.theme.Inter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

data class DummyComplaint(
    val id: String,
    val sender: String,
    val description: String,
    val location: String,
    val status: String,
    val imageRes: Int,
    val latLng: LatLng
)

val dummyComplaint = DummyComplaint(
    id = "#12345",
    sender = "@Priya_Sharma",
    description = "This is a dummy complaint description. Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
    location = "123 MG Road, Bangalore",
    status = "Pending",
    imageRes = R.drawable.image_3, // Add a dummy image in res/drawable
    latLng = LatLng(12.9716, 77.5946)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Details(navController: NavController) {

    var showImage by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedStatus by remember { mutableStateOf<String?>(null) }

    val fieldBg = Color(0xFF2B2B2B)
    val placeholder = Color(0xFF9AA39B)
    val accentGreen = Color(0xFF00E676)
    val GreenTint3 = Color(0xFF99E0B2)
    val DarkBackground = Color(0xFF121212)
    val buttonRed = Color(0xFFF44336) // Red color for "Other Action" button

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Complaint Details", color = Color.White, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = fieldBg)
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->

        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setStatusBarColor(
                color = containerColor,
                darkIcons = false // assuming your container color is dark
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 20.dp)
                .padding(paddingValues), // Apply padding to the main content LazyColumn
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Complaint Info Section
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Complaint ID: ${dummyComplaint.id}",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Sender: ${dummyComplaint.sender}", color = placeholder, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(dummyComplaint.description, color = Color.White, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showImage = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        border = BorderStroke(1.dp, accentGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("View Image", color = accentGreen)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Location: ${dummyComplaint.location}",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Current Status: ${dummyComplaint.status}",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }

            // Map Section
            item {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(dummyComplaint.latLng, 12f)
                    }
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState
                    ) {
                        Marker(
                            state = rememberMarkerState(position = dummyComplaint.latLng),
                            title = dummyComplaint.location
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Change Status",
                    color = Color.White,
                    fontFamily = Inter,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
            }
            // Change Status Buttons Section (now a simple Column)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // In Progress - Outlined with Green
                    OutlinedButton(
                        onClick = {
                            selectedStatus = "In Progress"
                            showDialog = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = accentGreen
                        ),
                        border = BorderStroke(1.dp, accentGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("In Progress") }

                    // Resolved - Solid Button
                    Button(
                        onClick = {
                            selectedStatus = "Resolved"
                            showDialog = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenTint1),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Resolved", color = Color.White) }

                    // Other Action - Outlined with Red
                    OutlinedButton(
                        onClick = {
                            selectedStatus = "Other Action"
                            showDialog = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = buttonRed
                        ),
                        border = BorderStroke(1.dp, buttonRed),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Other Action") }
                }
            }
        }
    }

    if (showDialog && selectedStatus != null) {
        StatusConfirmationDialog(
            status = selectedStatus!!,
            onConfirm = {
                // Here you would add the logic to actually change the status
                // For example: update a database, call an API, etc.
                // For now, we'll just print a message.
                println("Status confirmed for: ${selectedStatus}")
                showDialog = false
                selectedStatus = null
            },
            onDismiss = {
                showDialog = false
                selectedStatus = null
            }
        )
    }

    // Fullscreen Image Overlay
    if (showImage) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .zIndex(2f) // Ensure it's on top of everything
        ) {
            // Image scaled to fit screen
            Image(
                painter = painterResource(dummyComplaint.imageRes),
                contentDescription = "Full Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .align(Alignment.Center),
                contentScale = ContentScale.Inside
            )

            // Close button "on top" of the image
            IconButton(
                onClick = { showImage = false },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(48.dp)
                    .background(Color.White.copy(alpha = 0.8f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
fun StatusConfirmationDialog(
    status: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF2B2B2B), // Dialog background color
        title = {
            Text(
                text = "Confirm Status Change",
                fontSize = 20.sp,
                fontFamily = Inter,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        },
        text = {
            Text(
                text = "Are you sure you want to change the status to '$status'?",
                fontSize = 14.sp,
                fontFamily = Inter,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("OK", color = Color(0xFF00E676), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = Color.Red, fontFamily = Inter,fontWeight = FontWeight.Bold)
            }
        }
    )
}