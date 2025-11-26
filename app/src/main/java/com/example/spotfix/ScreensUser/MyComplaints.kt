package com.example.spotfix.ScreensUser

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spotfix.R
import com.example.spotfix.ui.theme.Inter

// ✅ Theme Colors
//val primaryColor = Color(0xFFFF6A00)
//val onSurfaceVariantColor = Color.Gray
//val onSurfaceColor = Color.White
//val backgroundColor = Color(0xFF191919)
//val containerColor = Color(0xFF2B2B2B)

// Complaint Model
data class MyComplaint(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val status: String,
    val imageRes: Int
)

// Dummy Data
val complaints = listOf(
    MyComplaint(
        1, "Noise Complaint", "Loud music from the apartment above is disturbing my sleep.",
        "Oct 26, 2023", "Apt 3B, 123 Main St", "Pending", R.drawable.image_1
    ),
    MyComplaint(
        2, "Parking Violation", "A car is blocking the driveway, preventing access to my property.",
        "Oct 24, 2023", "My Driveway", "Resolved", R.drawable.image_2
    ),
    MyComplaint(
        3, "Streetlight Outage", "The streetlight on the corner is not working, creating a safety hazard.",
        "Oct 22, 2023", "Oak & Pine Corner", "In Progress", R.drawable.image_3
    ),
    MyComplaint(
        4, "Garbage Overflow", "Trash bins are overflowing and attracting stray animals.",
        "Oct 20, 2023", "Block C, Lane 5", "Pending", R.drawable.image_5
    ),
    MyComplaint(
        5, "Water Leakage", "A leaking pipeline is wasting water near my building.",
        "Oct 18, 2023", "Sector 9 Market", "In Progress", R.drawable.image_4
    ),
    MyComplaint(
        6, "Road Potholes", "Large potholes making driving unsafe in my street.",
        "Oct 15, 2023", "Main Square Road", "Resolved", R.drawable.image_6
    ),
    MyComplaint(
        7, "Broken Bench", "A bench in the park is broken and unsafe to sit on.",
        "Oct 12, 2023", "Community Park", "Pending", R.drawable.image_7
    ),
)

@Composable
fun MyComplaintsScreen() {
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Pending", "In Progress", "Resolved")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 12.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // 🔹 Filter Categories Row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { category ->
                FilterChip(
                    text = category,
                    isSelected = selectedCategory == category,
                    onClick = { selectedCategory = category }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Apply filtering
        val filteredComplaints = when (selectedCategory) {
            "All" -> complaints
            else -> complaints.filter { it.status == selectedCategory }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredComplaints) { complaint ->
                ComplaintCard(complaint)
            }
            item { Spacer(modifier = Modifier.height(70.dp)) } // bottom nav space
        }
    }
}

@Composable
fun FilterChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(if (isSelected) primaryColor else containerColor)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else onSurfaceVariantColor,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ComplaintCard(complaint: MyComplaint) {
    Card(
        colors = CardDefaults.cardColors(containerColor),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Image
            Image(
                painter = painterResource(id = complaint.imageRes),
                contentDescription = complaint.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )

            // Status Badge
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        when (complaint.status) {
                            "Pending" -> Color(0xFFFF6A00).copy(alpha = 0.9f)
                            "In Progress" -> Color(0xFFFFB300)
                            "Resolved" -> Color(0xFF00C853)
                            else -> containerColor
                        }
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = complaint.status,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Details
            Column(modifier = Modifier.padding(12.dp)) {
                Text(complaint.title, color = onSurfaceColor, fontWeight = FontWeight.Bold,fontSize = 18.sp, fontFamily = Inter)
                Spacer(Modifier.height(4.dp))
                Text(complaint.description, color = onSurfaceVariantColor, fontSize = 13.sp)
                Spacer(Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("📅 ${complaint.date}", color = onSurfaceVariantColor, fontSize = 12.sp, fontFamily = Inter, fontWeight = FontWeight.Normal)
                    Text("📍 ${complaint.location}", color = onSurfaceVariantColor, fontSize = 12.sp,fontFamily = Inter, fontWeight = FontWeight.Normal)
                }
            }
        }
    }
}
