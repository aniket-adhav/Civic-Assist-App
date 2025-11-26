package com.example.spotfix.ScreensAdmin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spotfix.ScreensUser.Screen
import com.example.spotfix.ScreensUser.backgroundColor
import com.example.spotfix.ScreensUser.containerColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController

// Define the data model for a single complaint
data class Complaint(
    val id: String,
    val location: String,
    val user: String,
    val status: ComplaintStatus,
    val category: ComplaintCategory
)

// Enum for complaint status and their corresponding colors
enum class ComplaintStatus(val color: Color) {
    Pending(Color(0xFFE0C40F)), // Yellowish color for Pending
    InProgress(Color(0xFF00A3FF)), // Blueish color for In Progress
    Resolved(Color(0xFF34C759)) // Green color for Resolved
}

// Enum for complaint categories
enum class ComplaintCategory {
    All,
    Potholes,
    Streetlight,
    WaterSupply,
    GarbageCollection,
    IllegalConstruction,
    NoisePollution
}

// Dummy data for 10 complaints with Indian locations
val dummyComplaints = listOf(
    Complaint("#12345", "123, MG Road, Bangalore", "@Priya_Sharma", ComplaintStatus.Pending, ComplaintCategory.Potholes),
    Complaint("#67890", "456, Gachibowli, Hyderabad", "@Rohan_Singh", ComplaintStatus.InProgress, ComplaintCategory.Streetlight),
    Complaint("#24680", "789, Connaught Place, New Delhi", "@Neha_Verma", ComplaintStatus.Resolved, ComplaintCategory.WaterSupply),
    Complaint("#13579", "101, Marine Drive, Mumbai", "@Amit_Patel", ComplaintStatus.Pending, ComplaintCategory.GarbageCollection),
    Complaint("#97531", "222, Park Street, Kolkata", "@Ananya_Das", ComplaintStatus.InProgress, ComplaintCategory.IllegalConstruction),
    Complaint("#86420", "333, Jubilee Hills, Hyderabad", "@Vikram_Reddy", ComplaintStatus.Resolved, ComplaintCategory.Streetlight),
    Complaint("#55555", "444, Sector 17, Chandigarh", "@Deepak_Kumar", ComplaintStatus.Pending, ComplaintCategory.NoisePollution),
    Complaint("#66666", "555, Koramangala, Bangalore", "@Shweta_Goel", ComplaintStatus.InProgress, ComplaintCategory.Potholes),
    Complaint("#77777", "666, Indiranagar, Bangalore", "@Kunal_Jain", ComplaintStatus.Resolved, ComplaintCategory.WaterSupply),
    Complaint("#88888", "777, Adarsh Nagar, New Delhi", "@Divya_Rao", ComplaintStatus.InProgress, ComplaintCategory.GarbageCollection)
)

val fieldBg = Color(0xFF2B2B2B)
val placeholder = Color(0xFF9AA39B)
val accentGreen = Color(0xFF00E676)
val AccentGreenLight = Color(0xFF81C784)
val GreenTint3 = Color(0xFF99E0B2)
val DarkBackground = Color(0xFF121212)

@Composable
fun Complaints(navController: NavController) {

    var selectedCategory by remember { mutableStateOf(ComplaintCategory.All) }
    var selectedStatus by remember { mutableStateOf<ComplaintStatus?>(null) }

    // Filter complaints based on selected category and status
    val filteredComplaints = remember(selectedCategory, selectedStatus) {
        dummyComplaints.filter { complaint ->
            (selectedCategory == ComplaintCategory.All || complaint.category == selectedCategory) &&
                    (selectedStatus == null || complaint.status == selectedStatus)
        }
    }

    Scaffold(
        containerColor = fieldBg
    ) { paddingValues ->

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(top = paddingValues.calculateTopPadding() - 27.dp, bottom = paddingValues.calculateBottomPadding())

        ) {
            // LazyRow with sequence: All, Pending, InProgress, Resolved, other categories
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // 1️⃣ "All" category
                item {
                    CategoryChip(
                        category = ComplaintCategory.All,
                        isSelected = ComplaintCategory.All == selectedCategory,
                        onClick = { selectedCategory = ComplaintCategory.All; selectedStatus = null }
                    )
                }

                // 2️⃣ Status chips in sequence
                items(ComplaintStatus.values()) { status ->
                    StatusChip(
                        status = status,
                        isSelected = status == selectedStatus,
                        onClick = { selectedStatus = status; selectedCategory = ComplaintCategory.All }
                    )
                }

                // 3️⃣ Remaining categories (excluding "All")
                items(ComplaintCategory.values().filter { it != ComplaintCategory.All }) { category ->
                    CategoryChip(
                        category = category,
                        isSelected = category == selectedCategory,
                        onClick = { selectedCategory = category; selectedStatus = null }
                    )
                }
            }

            // Complaint cards
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(filteredComplaints) { complaint ->
                    ComplaintCard(complaint, navController = navController)
                }
            }
        }
    }
}

@Composable
fun CategoryChip(category: ComplaintCategory, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(if (isSelected) accentGreen else fieldBg)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = category.name,
            color = if (isSelected) Color.White else Color.Gray,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}

@Composable
fun StatusChip(status: ComplaintStatus, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(if (isSelected) accentGreen else fieldBg)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = status.name,
            color = if (isSelected) Color.White else Color.Gray,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}



@Composable
fun ComplaintCard(complaint: Complaint, onClick: () -> Unit = {},navController: NavController) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2E)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("details") } // Make the card clickable
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Complaint ID: ${complaint.id}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Location: ${complaint.location}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "User: ${complaint.user}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            // Status button
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .background(
                        color = complaint.status.color,
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Text(
                    text = complaint.status.name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}
