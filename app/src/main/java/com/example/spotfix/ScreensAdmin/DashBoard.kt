package com.example.spotfix.screens

import android.R
import android.R.attr.padding
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spotfix.ScreensAdmin.Complaints
import com.example.spotfix.ScreensAdmin.Details
import com.example.spotfix.ScreensAdmin.fieldBg
import com.example.spotfix.ScreensUser.HomeTopBar
import com.example.spotfix.ScreensUser.Screen
import com.example.spotfix.ScreensUser.containerColor
import com.example.spotfix.ScreensUser.onSurfaceColor
import com.example.spotfix.ScreensUser.primaryColor
import com.example.spotfix.ui.theme.GreenTint1
import com.example.spotfix.ui.theme.Inter
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard(navController: NavController) {
    val fieldBg = Color(0xFF2B2B2B)
    val placeholder = Color(0xFF9AA39B)
    val accentGreen = Color(0xFF00E676)
    val AccentGreenLight = Color(0xFF81C784)
    val GreenTint3 = Color(0xFF99E0B2)
    val DarkBackground = Color(0xFF121212)

    var selectedTab by remember { mutableStateOf("Dashboard") }
    var selectedScreen by remember { mutableStateOf<Screen>(Screen.Dashboard) }

    Scaffold(
        topBar = {
            when (selectedScreen) {
                Screen.Dashboard -> AppTopBarDash()
                Screen.Complaints -> {
                    TopAppBar(
                        title = { Text("Complaints", color = onSurfaceColor, fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = Inter, modifier = Modifier.padding(start = 8.dp)) },
                        actions = {
                            IconButton(onClick = { }) {
                                Icon(painterResource(id = com.example.spotfix.R.drawable.search), contentDescription = "Search", tint = GreenTint1,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = containerColor)
                    )
                }
                Screen.Analysis -> {
                    TopAppBar(
                        title = { Text("Analysis", color = onSurfaceColor,fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = Inter, modifier = Modifier.padding(start = 8.dp)) },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = containerColor)
                    )
                }
                Screen.Admin -> {
                    TopAppBar(
                        title = { Text("Admin", color = onSurfaceColor,fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = Inter, modifier = Modifier.padding(start = 8.dp)) },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = containerColor)
                    )
                }
                else -> {}
            }
        },

        bottomBar = {
            // ✅ Always show bottom bar on every screen
                BottomNavigationBar2(selectedScreen = selectedScreen,) { selectedScreen = it }
        },
                containerColor = fieldBg
    ) { padding ->

        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setStatusBarColor(
                color = containerColor,
                darkIcons = false // assuming your container color is dark
            )
            systemUiController.setNavigationBarColor(
                color = containerColor,
                darkIcons = false // assuming your container color is dark
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .padding(top = padding.calculateTopPadding()-7.dp,
                    bottom = padding.calculateBottomPadding()-7.dp,
                    start = padding.calculateStartPadding(LayoutDirection.Ltr),
                    end = padding.calculateEndPadding(LayoutDirection.Ltr)
                    )
        ) {
            when (selectedScreen) {
                Screen.Dashboard -> DashboardContent()
                Screen.Complaints -> Complaints(navController)
                Screen.Analysis -> { /* TODO */ }
                Screen.Admin -> { /* TODO */ }
                Screen.AdminLogin -> { /* TODO */ }
                is Screen.FullPost -> { /* TODO */ }
                Screen.Home -> { /* TODO */ }
                Screen.Login -> { /* TODO */ }
                Screen.MyComplaints -> { /* TODO */ }
                Screen.NewComplaint -> { /* TODO */ }
                Screen.OTPs -> { /* probably you don't even need OTP here */ }
                Screen.Profile -> { /* TODO */ }
                Screen.Search -> { /* TODO */ }
                Screen.Details -> Details(navController)
                Screen.Settings -> { /* TODO */ }

                // 👇 This covers any remaining Screen types
                else -> { /* no-op or fallback screen */ }
            }


        }
    }
}


@Composable
fun DashboardContent(){

    val fieldBg = Color(0xFF2B2B2B)
    val placeholder = Color(0xFF9AA39B)
    val accentGreen = Color(0xFF00E676)
    val AccentGreenLight = Color(0xFF81C784)
    val GreenTint3 = Color(0xFF99E0B2)
    val DarkBackground = Color(0xFF121212)


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = DarkBackground
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Complaint summary row
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatCard(
                    title = "Total Complaints",
                    value = "120",
                    tint = accentGreen,
                    bg = fieldBg,
                    modifier = Modifier.weight(1f),
                    change = "+5% from last month"
                )
                StatCard(
                    title = "Pending Complaints",
                    value = "30",
                    tint = placeholder,
                    bg = fieldBg,
                    modifier = Modifier.weight(1f),
                    change = "-2% from last month"
                )
            }
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatCard(
                    title = "In-Progress",
                    value = "45",
                    tint = AccentGreenLight,
                    bg = fieldBg,
                    modifier = Modifier.weight(1f),
                    change = "+3% from last month"
                )
                StatCard(
                    title = "Resolved",
                    value = "27",
                    tint = GreenTint3,
                    bg = fieldBg,
                    modifier = Modifier.weight(1f),
                    change = "+8% from last month"
                )
            }
        }

        // Complaint trends (placeholder chart)
        item {
//                Spacer(
//                    modifier = Modifier.height(12.dp)
//                )

            Card(
                colors = CardDefaults.cardColors(containerColor = fieldBg),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(255.dp) // card height
            ) {
                Column(Modifier.padding(16.dp)) {
                    Spacer(Modifier.height(8.dp))

                    Text(

                        "Complaint Trends of Last 30 Days",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Spacer(Modifier.height(20.dp))

                    val data = listOf(
                        "Streetlight" to 50,
                        "Potholes" to 80,
                        "Garbage" to 40,
                        "Water" to 70,
                        "Noise" to 30,
                        "Road" to 60,
                        "Electricity" to 40
                    )
                    val maxValue = data.maxOf { it.second }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(data.size) { index ->
                            val (label, value) = data[index]

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.width(50.dp)
                            ) {
                                // Bar container with value inside
                                Box(
                                    modifier = Modifier
                                        .height(150.dp)
                                        .fillMaxWidth(),
                                    contentAlignment = Alignment.BottomCenter
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Bottom,
                                        modifier = Modifier.fillMaxHeight()
                                    ) {
                                        // Value just above the bar
                                        Text(
                                            "$value",
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            fontFamily = Inter
                                        )
                                        Spacer(Modifier.height(4.dp))
                                        // Bar itself
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight(value.toFloat() / maxValue.toFloat())
                                                .background(
                                                    accentGreen,
                                                    RoundedCornerShape(6.dp)
                                                )
                                                .clickable {
                                                    // Navigate to complaint list for this type
                                                }
                                        )
                                    }
                                }

                                Spacer(Modifier.height(4.dp))
                                // Label at the bottom
                                Text(
                                    label,
                                    color = Color.Gray,
                                    fontSize = 10.sp,
                                    maxLines = 1
                                )

                            }
                        }
                    }
                }
            }
        }


        // Complaint Status (enhanced circular progress)
        // Complaint Status – Horizontal scrollable circular indicators
        // Complaint Status – Horizontal scrollable circular indicators
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = fieldBg),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                // .padding(vertical = 8.dp)
            ) {
                Column(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        "Complaint Resolution Overview",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Quick insights for admin",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                    Spacer(Modifier.height(16.dp))

                    val data = listOf(
                        "Pending" to 30f / 75f,     // 30 out of 75
                        "In Progress" to 45f / 75f, // 45 out of 75
                        "Resolved" to 25f / 75f     // 25 out of 75
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(data.size) { index ->
                            val (label, progress) = data[index]
                            val color = when(label) {
                                "Pending" -> Color.Red
                                "In Progress" -> Color.Yellow
                                "Resolved" -> accentGreen
                                else -> accentGreen
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.width(100.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(
                                        progress = 1f,
                                        strokeWidth = 10.dp,
                                        color = Color.Gray.copy(alpha = 0.3f),
                                        modifier = Modifier.size(80.dp)
                                    )

                                    CircularProgressIndicator(
                                        progress = progress,
                                        strokeWidth = 10.dp,
                                        color = color,
                                        modifier = Modifier.size(80.dp)
                                    )

                                    // Center percentage only
                                    Text(
                                        "${(progress * 100).toInt()}%",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }

                                Spacer(Modifier.height(8.dp))
                                Text(label, color = Color.Gray, fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Total Complaints at the bottom of the card
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Total Complaints: 100",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }

                }
            }
        }

// Inside your LazyColumn for dashboard items
        item {
            Column(modifier = Modifier
                .fillMaxWidth()
            ) {
                Text(
                    "Top Complaint Categories",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }

// Cards for each category
        item {
            HighestVolumeCategoryCard(
                category = "Garbage",
                count = 120,
                percentage = 35,
                trendUp = true
            )
        }

        item {
            HighestVolumeCategoryCard(
                category = "Streetlight",
                count = 80,
                percentage = 23,
                trendUp = false
            )
        }

        item {
            HighestVolumeCategoryCard(
                category = "Potholes",
                count = 60,
                percentage = 17,
                trendUp = true
            )
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBarDash(){

            TopAppBar(
                title = {
                    Text(
                        text = "Admin Dashboard",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                actions = {
                    IconButton(onClick = { /* Profile */ }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = fieldBg)
            )
        }

@Composable
fun StatCard(
    title: String,
    value: String,
    tint: Color,
    bg: Color,
    modifier: Modifier,
    change: String? = null
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = bg),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                title,
                color = onSurfaceColor,
                fontSize = 14.sp,
                fontFamily = Inter,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(8.dp))

            Text(
                value,
                color = Color.White,
                fontSize = 24.sp,   // ⬅️ Only this changed
                fontWeight = FontWeight.ExtraBold
            )

            change?.let {
                Spacer(Modifier.height(4.dp))
                Text(
                    it,
                    color = if (it.startsWith("-")) Color.Red else Color.Green,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}


@Composable
fun ComplaintItem(name: String, description: String, status: String, statusColor: Color) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(vertical = 8.dp)
    ) {
        Text(name, color = Color.White, fontWeight = FontWeight.Bold)
        Text(description, color = Color.Gray, fontSize = 12.sp)
        Text(status, color = statusColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun HighestVolumeCategoryCard(
    category: String,
    count: Int,
    percentage: Int,
    trendUp: Boolean
) {

    val fieldBg = Color(0xFF2B2B2B)
    val placeholder = Color(0xFF9AA39B)
    val accentGreen = Color(0xFF00E676)
    val AccentGreenLight = Color(0xFF81C784)
    val GreenTint3 = Color(0xFF99E0B2)
    val DarkBackground = Color(0xFF121212)


    Card(
        colors = CardDefaults.cardColors(containerColor = fieldBg),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Top info: category and count
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        category,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "$count complaints",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (trendUp) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                            contentDescription = null,
                            tint = if (trendUp) Color.Green else Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "$percentage% vs last week",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Horizontal progress bar for proportion
            // Horizontal progress bar with percentage above
            Column(modifier = Modifier.fillMaxWidth()) {
                // Percentage text above
                Text(
                    "$percentage%",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(4.dp))

                // Progress bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .background(Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(percentage / 100f)
                            .background(accentGreen, RoundedCornerShape(12.dp))
                    )
                }
            }

        }
    }
}

@Composable
fun BottomNavigationBar2(
    selectedScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    val accentGreen = Color(0xFF00E676)
    val inactiveColor = Color.Gray

    val items = listOf(
        Triple("Dashboard", com.example.spotfix.R.drawable.dashboard, Screen.Dashboard),
        Triple("Complaints", com.example.spotfix.R.drawable.listtext, Screen.Complaints),
        Triple("Analysis", com.example.spotfix.R.drawable.analytics, Screen.Analysis),
        Triple("Admin", com.example.spotfix.R.drawable.user1, Screen.Admin)
    )

    Row(
        Modifier
            .fillMaxWidth()
            .background(containerColor)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            val color = if (selectedScreen == item.third) accentGreen else inactiveColor
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onScreenSelected(item.third) // ✅ just update state
                }
            ) {
                Icon(
                    painter = painterResource(id = item.second),
                    contentDescription = item.first,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = item.first,
                    color = color,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}


