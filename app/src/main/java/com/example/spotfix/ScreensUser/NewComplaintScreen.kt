package com.example.spotfix.ScreensUser

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.location.Geocoder
import android.content.Context
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.zIndex
import com.example.spotfix.R
import com.example.spotfix.ui.theme.Inter
import java.util.Locale
import com.google.accompanist.systemuicontroller.rememberSystemUiController

// Colors
val primaryColor = Color(0xFFFF6A00)
val onSurfaceVariantColor = Color.Gray
val onSurfaceColor = Color.White
val backgroundColor = Color(0xFF121212)
val containerColor = Color(0xFF2B2B2B)

data class SelectedImage(
    val id: Long,
    val bitmap: Bitmap,
    var locationName: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewComplaintScreen(onBackClick: () -> Unit = {}) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val coroutineScope = rememberCoroutineScope()

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(containerColor)
    }

    val categories = listOf(
        "Road Damage", "Garbage", "Streetlight", "Water Logging", "Illegal Dumping",
        "Noise Pollution", "Public Safety", "Sewage Issue", "Encroachment", "Others"
    )

    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var description by remember { mutableStateOf("") }

    var location by remember { mutableStateOf<String?>(null) }
    var showLocationDialog by remember { mutableStateOf(false) }
    var showMapPicker by remember { mutableStateOf(false) }
    var manualLocation by remember { mutableStateOf("") }
    var selectedLatLng by remember { mutableStateOf<LatLng?>(null) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(20.5937, 78.9629), 4f)
    }

    val selectedImages = remember { mutableStateListOf<SelectedImage>() }
    var imageIdCounter by remember { mutableStateOf(0L) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        val bmp = data?.extras?.get("data") as? Bitmap
        bmp?.let {
            imageIdCounter += 1
            selectedImages.add(0, SelectedImage(imageIdCounter, it))
            Toast.makeText(context, "Image added", Toast.LENGTH_SHORT).show()
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        if (data != null) {
            val clip = data.clipData
            if (clip != null) {
                for (i in 0 until clip.itemCount) {
                    val uri = clip.getItemAt(i).uri
                    coroutineScope.launch {
                        val bmp = loadBitmapFromUri(context, uri)
                        bmp?.let {
                            imageIdCounter += 1
                            selectedImages.add(0, SelectedImage(imageIdCounter, it))
                        }
                    }
                }
            } else {
                val uri = data.data
                uri?.let {
                    coroutineScope.launch {
                        val bmp = loadBitmapFromUri(context, it)
                        bmp?.let { bm ->
                            imageIdCounter += 1
                            selectedImages.add(0, SelectedImage(imageIdCounter, bm))
                        }
                    }
                }
            }
            Toast.makeText(context, "Image(s) added", Toast.LENGTH_SHORT).show()
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(intent)
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor) // No statusBarsPadding here
    ) {
        // Top Bar stays flush at the very top
//        TopAppBar(
//            title = {
//                Text("New Complaint", color = onSurfaceColor, fontSize = 20.sp)
//            },
//            navigationIcon = {
//                IconButton(onClick = onBackClick) {
//                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = onSurfaceColor)
//                }
//            },
//            colors = TopAppBarDefaults.topAppBarColors(containerColor = containerColor)
//        )

        // ✅ Only apply statusBarsPadding to the content area, not the top bar
        Spacer(Modifier.height(10.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .clipToBounds(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 80.dp) // Optional for better spacing
        ) {
            item { Text("Add a photo", color = Color.White, fontFamily = Inter, fontWeight = FontWeight.Bold, ) }

            item {
                Button(
                    onClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = containerColor),
                    shape = RoundedCornerShape(8.dp),
                ) {

                    IconButton(onClick = { }) {
                        Icon(painterResource(id = R.drawable.camera), contentDescription = "Search", tint = primaryColor,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Spacer(Modifier.width(4.dp))
                    Text("Take a photo", color = onSurfaceColor,fontWeight = FontWeight.SemiBold, fontSize = 14.sp
                        ,fontFamily = Inter)
                }
            }

            item {
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                            type = "image/*"
                            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                        }
                        galleryLauncher.launch(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = containerColor),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    IconButton(onClick = { }) {
                        Icon(painterResource(id = R.drawable.gallery), contentDescription = "Search", tint = primaryColor,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Spacer(Modifier.width(4.dp))
                    Text("Choose from gallery", color = onSurfaceColor, fontWeight = FontWeight.SemiBold, fontSize = 14.sp
                        ,fontFamily = Inter)
                }
            }

            if (selectedImages.isNotEmpty()) {
                item {
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(selectedImages, key = { it.id }) { img ->
                            Box(
                                modifier = Modifier
                                    .padding(end = 12.dp)
                                    .size(120.dp)
                            ) {
                                Image(
                                    bitmap = img.bitmap.asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(8.dp))
                                )

                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(6.dp)
                                        .size(30.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color.Black.copy(alpha = 0.5f))
                                        .clickable {
                                            selectedImages.remove(img)
                                            Toast.makeText(
                                                context,
                                                "Image deleted",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                                }
                            }
                        }
                    }
                }
            }

            item { Text("Location", color = Color.White,fontWeight = FontWeight.Bold
                ,fontFamily = Inter) }

            item {
                Button(
                    onClick = { showLocationDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = containerColor),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    IconButton(onClick = { }) {
                        Icon(painterResource(id = R.drawable.location), contentDescription = "Search", tint = primaryColor,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Text("Select location", color = onSurfaceColor,fontWeight = FontWeight.SemiBold, fontSize = 15.sp
                        ,fontFamily = Inter)
                }
            }

            if (location != null) {
                item {
                    Text(
                        text = "Selected Location: $location",
                        color = primaryColor,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            item { Text("Description", color = Color.White,fontWeight = FontWeight.Bold
                ,fontFamily = Inter) }

            item {
                var isFocused by remember { mutableStateOf(false) } // Track focus state

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = {
                        Text(
                            "Describe the issue in detail...",
                            color = onSurfaceVariantColor
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused // Update focus state
                        },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryColor,            // Outline when focused
                        unfocusedBorderColor = Color.Transparent,     // No outline when unfocused
                        unfocusedContainerColor = containerColor,
                        focusedContainerColor = containerColor,
                        unfocusedTextColor = onSurfaceColor,
                        focusedTextColor = onSurfaceColor
                    )
                )
            }

            item {
                Text(
                    "Category",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Inter
                )
            }

            item {
                CategoryDropdown(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )
            }



            item {
                Spacer(Modifier.height(22.dp))
                Button(
                    onClick = {
                        Toast.makeText(context, "Complaint Submitted Successfully.", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryColor,
                        contentColor = onSurfaceColor
                    )
                ) {
                    Text(
                        text = "Submit Complaint",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                }
            }
        }
    }

    if (showLocationDialog) {
        AlertDialog(
            onDismissRequest = { showLocationDialog = false },
            containerColor = containerColor,
            title = { Text("Choose Location", color = Color.White, fontFamily = Inter, fontSize = 24.sp, fontWeight = FontWeight.SemiBold) },
            text = {
                Column {
                    OutlinedButton(
                        onClick = {
                            showLocationDialog = false
                            if (ActivityCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            } else {
                                fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                                    loc?.let {
                                        coroutineScope.launch {
                                            val name = reverseGeocode(context, it.latitude, it.longitude)
                                            location = name ?: "Lat: ${it.latitude}, Lng: ${it.longitude}"
                                        }
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Use current location",fontWeight = FontWeight.SemiBold, fontFamily = Inter)
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(
                        onClick = {
                            showLocationDialog = false
                            showMapPicker = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Select on map", fontWeight = FontWeight.SemiBold, fontFamily = Inter)
                    }

                }
            },
            confirmButton = {}
        )
    }

    if (showMapPicker) {
        Dialog(onDismissRequest = { showMapPicker = false }) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    onMapClick = { latLng -> selectedLatLng = latLng }
                ) {
                    selectedLatLng?.let {
                        Marker(state = MarkerState(it), title = "Selected Location")
                    }
                }
                Button(
                    onClick = {
                        selectedLatLng?.let { latLng ->
                            coroutineScope.launch {
                                val name = reverseGeocode(context, latLng.latitude, latLng.longitude)
                                location = name ?: "Lat: ${latLng.latitude}, Lng: ${latLng.longitude}"
                            }
                        }
                        showMapPicker = false
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                ) {
                    Text("Confirm Location", color = Color.White, fontWeight = FontWeight.SemiBold, fontFamily = Inter)
                }
            }
        }
    }
}

suspend fun reverseGeocode(context: Context, latitude: Double, longitude: Double): String? {
    return withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)

            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                listOfNotNull(
                    address.thoroughfare,
                    address.subLocality,
                    address.locality,
                    address.adminArea,
                    address.countryName
                ).joinToString(", ")
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    categories: List<String>,
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(10f)
            .clip(RoundedCornerShape(8.dp))// Keep dropdown above other UI
    ) {
        // TextField that acts as the dropdown anchor
        OutlinedTextField(
            value = selectedCategory ?: "Select category",
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = primaryColor,
                unfocusedBorderColor = Color.Transparent,
                unfocusedContainerColor = containerColor,
                focusedContainerColor = containerColor,
                unfocusedTextColor = onSurfaceColor,
                focusedTextColor = onSurfaceColor
            ),
            modifier = Modifier
                .menuAnchor() // Anchors the dropdown to this field
                .fillMaxWidth()
        )

        // Dropdown that appears BELOW the text field
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(containerColor)
                .offset(y = 4.dp) // Pushes dropdown slightly below text field
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category, color = onSurfaceColor) },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }
        }
    }
}
