package com.example.spotfix.ScreensUser

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlin.random.Random
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import com.example.spotfix.ui.theme.Inter
import kotlinx.coroutines.delay
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

// ---------- COLORS ----------
//val primaryColor = Color(0xFFFF6A00)
//val onSurfaceVariantColor = Color.Gray
//val onSurfaceColor = Color.White
//val backgroundColor = Color(0xFF191919)
//val containerColor = Color(0xFF2B2B2B)

// Dummy image resources
private val DUMMY_IMAGE_RESOURCES = listOf(
    R.drawable.image_1, R.drawable.image_2, R.drawable.image_3, R.drawable.image_4,
    R.drawable.image_5, R.drawable.image_6, R.drawable.image_7, R.drawable.image_8
)

// Generates dummy complaints for feed
// ---------------- Dummy Data ----------------
@Composable
fun getDummyComplaints(): List<Complaint> {
    val names = listOf("Amit Sharma", "Priya Singh", "Rohan Gupta", "Deepa Patel")
    val descriptions = listOf(
        "Pothole in the main street causing traffic congestion.",
        "Overflowing public dustbins creating sanitation problem.",
        "Streetlight on the corner not working, dark at night.",
        "Illegal dumping of construction waste on empty plot."
    )
    val locations = listOf("New Delhi", "Pune", "Bangalore", "Mumbai")
    val dummyProfiles = DUMMY_IMAGE_RESOURCES.map { painterResource(id = it) }
    val dummyDates = listOf(
        "Sep 10, 2025", "Sep 12, 2025", "Sep 15, 2025", "Sep 18, 2025",
        "Sep 20, 2025", "Sep 22, 2025", "Sep 25, 2025", "Sep 27, 2025"
    )

    return (0..7).map { i ->
        Complaint(
            id = i,
            userName = names[i % names.size],
            profileImage = dummyProfiles[i % dummyProfiles.size],
            location = locations[i % locations.size],
            problemTitle = descriptions[i % descriptions.size].substringBefore("."),
            problemDescription = descriptions[i % descriptions.size],
            postImage = painterResource(id = DUMMY_IMAGE_RESOURCES[i % DUMMY_IMAGE_RESOURCES.size]),
            locationLatLng = com.google.android.gms.maps.model.LatLng(
                Random.nextDouble(20.0, 30.0),
                Random.nextDouble(75.0, 85.0)
            ),
            initialLikes = Random.nextInt(0, 100),
            initialDislikes = Random.nextInt(0, 50),
            date = dummyDates[i % dummyDates.size] // ✅ assign dummy date
        )
    }
}


// ---------------- Home / Feed Page ----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage() {
    var selectedScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    val systemUiController = rememberSystemUiController()
    SideEffect {
        // Top status bar
        systemUiController.setStatusBarColor(
            color = containerColor,
            darkIcons = false
        )

        // Bottom navigation bar (system bar)
        systemUiController.setNavigationBarColor(
            color = Color(0xFF1C1C1E) ,
            darkIcons = false
        )
    }

    Scaffold(
        topBar = {
            when (selectedScreen) {
                Screen.Home -> HomeTopBar()
                Screen.NewComplaint -> {
                    TopAppBar(
                        title = { Text("New Complaint", color = onSurfaceColor, fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = Inter, modifier = Modifier.padding(start = 8.dp)) },

                        colors = TopAppBarDefaults.topAppBarColors(containerColor = containerColor)
                    )
                }
                Screen.MyComplaints -> {
                    TopAppBar(
                        title = { Text("My Complaints", color = onSurfaceColor,fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = Inter, modifier = Modifier.padding(start = 8.dp)) },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = containerColor)
                    )
                }
                Screen.Profile -> {
                    TopAppBar(
                        title = { Text("Settings", color = onSurfaceColor,fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = Inter, modifier = Modifier.padding(start = 8.dp)) },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = containerColor)
                    )
                }
                is Screen.FullPost -> {
                    TopAppBar(
                        title = { Text("Details", color = onSurfaceColor, fontSize = 22.sp,fontWeight = FontWeight.Bold, fontFamily = Inter, modifier = Modifier.padding(start = 8.dp)) },
                        navigationIcon = {
                            IconButton(onClick = { selectedScreen = Screen.Home }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = onSurfaceColor)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = containerColor)
                    )
                }
                else -> {}
            }
        },
        bottomBar = {
            // ✅ Always show bottom bar on every screen
            BottomNavigationBar(selectedScreen = selectedScreen) { selectedScreen = it }
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // ✅ Prevents content overlap with top/bottom bars
        ) {
            when (val currentScreen = selectedScreen) {
                // inside your composable where `selectedScreen` is available
                Screen.Home -> {
                    val dummyPosts = getDummyComplaints() // make sure this is in scope

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(backgroundColor)
                            .padding(
                            top = 8.dp,        // 👈 padding below TopBar
                            bottom = 8.dp      // 👈 padding above BottomBar
                            )
                    ) {
                        // Trending section - no side padding
                        item {
                            TrendingComplaintsSection()
                        }

                        // Feed section with horizontal padding
                        items(dummyPosts) { post ->
                            PostCard(
                                post = post,
                                onPostClick = { complaint ->
                                    selectedScreen = Screen.FullPost(complaint)
                                },
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp) // 👈 feed margin only here
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                }


                Screen.MyComplaints -> MyComplaintsScreen()
                Screen.Profile -> SettingsScreen()
                is Screen.FullPost -> FullPostScreen(
                    complaint = currentScreen.complaint,
                    onBackClick = { selectedScreen = Screen.Home }
                )
                Screen.NewComplaint -> NewComplaintScreen(
                    onBackClick = { selectedScreen = Screen.Home }
                )
                else -> {}
            }
        }
    }
}


@Composable
fun TrendingComplaintsSection() {
    val trendingComplaints = getDummyComplaints().take(5)
    val pagerState = rememberPagerState(pageCount = { trendingComplaints.size })

    // Auto-scroll every 3 seconds
    LaunchedEffect(pagerState) {
        while (true) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % trendingComplaints.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor) // 👈 clean background
    ) {
        Text(
            text = "Trending Complaints",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Inter,
            modifier = Modifier.padding(start = 16.dp,bottom = 8.dp)
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp) // 👈 full width & taller card
        ) { page ->
            val complaint = trendingComplaints[page]

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp), // 👈 keeps margin left/right
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = containerColor),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Image(
                        complaint.postImage,
                        contentDescription = "Trending Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        complaint.problemTitle,
                        color = onSurfaceColor,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Inter,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    Text(
                        complaint.location,
                        color = onSurfaceVariantColor,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Pager indicator (dots)
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp, bottom = 6.dp) // 👈 removed big padding
        ) {
            repeat(trendingComplaints.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(3.dp)
                        .size(if (isSelected) 9.dp else 6.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) primaryColor else Color.Gray)
                )
            }
        }
    }
}





// ---------------- TopBar ----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar() {
    val topBarColor = containerColor // same as status bar

    TopAppBar(
        title = {
            Text(
                "Civic Assist",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                fontFamily = Inter,
                color = Color.White,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(painterResource(id = R.drawable.search), contentDescription = "Search", tint = primaryColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            IconButton(onClick = { }) {
                Icon(painterResource(id = R.drawable.location), contentDescription = "Location", tint = primaryColor,
                        modifier = Modifier.size(20.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = topBarColor),
        modifier = Modifier.shadow(6.dp)
    )
}


// ---------------- Feed / Posts ----------------
@Composable
fun DummyPostFeed(onPostClick: (Complaint) -> Unit) {
    val dummyPosts = getDummyComplaints()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(10.dp)
    ) {
        items(dummyPosts) { post ->
            PostCard(post, onPostClick)
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun PostCard(post: Complaint, onPostClick: (Complaint) -> Unit, modifier: Modifier = Modifier) {
    var likes by remember { mutableStateOf(post.initialLikes) }
    var dislikes by remember { mutableStateOf(post.initialDislikes) }
    var isLiked by remember { mutableStateOf(post.isLiked) }
    var isDisliked by remember { mutableStateOf(post.isDisliked) }

    val likeColor by animateColorAsState(if (isLiked) Color(0xFFFF6A00) else Color.Gray)
    val dislikeColor by animateColorAsState(if (isDisliked) Color.Gray else Color.Gray)

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // --- Header ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        post.profileImage,
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .border(2.dp, primaryColor, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(post.userName, color = onSurfaceColor, fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = Inter)
                        Text(post.location, color = onSurfaceVariantColor, fontSize = 12.sp)
                    }
                }

                OutlinedButton(
                    onClick = { onPostClick(post) },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = primaryColor
                    ),
                    border = BorderStroke(1.dp, primaryColor),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text("View", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, fontFamily = Inter)
                }
            }

            Spacer(Modifier.height(12.dp))

            // --- Post Image with double-tap detection ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                if (!isLiked) {
                                    likes++
                                    isLiked = true
                                    if (isDisliked) {
                                        dislikes--
                                        isDisliked = false
                                    }
                                }
                            },
                            onTap = { onPostClick(post) } // single tap still opens post
                        )
                    }
            ) {
                Image(
                    post.postImage,
                    contentDescription = "Post Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(Modifier.height(8.dp))

            // --- Description ---
            Text(post.problemDescription, color = onSurfaceColor, fontSize = 14.sp)

            Spacer(Modifier.height(8.dp))

            // --- Action Row ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ❤️ Like
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        if (isLiked) { likes--; isLiked = false }
                        else {
                            if (isDisliked) { dislikes--; isDisliked = false }
                            likes++; isLiked = true
                        }
                    }) {
                        Icon(
                            painter = painterResource(
                                id = if (isLiked) R.drawable.heart_filled else R.drawable.heart
                            ),
                            contentDescription = "Like",
                            tint = likeColor,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Text(likes.toString(), color = onSurfaceColor, fontSize = 14.sp)
                }

                // 👎 Dislike
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        if (isDisliked) { dislikes--; isDisliked = false }
                        else {
                            if (isLiked) { likes--; isLiked = false }
                            dislikes++; isDisliked = true
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.chatbox),
                            contentDescription = "Dislike",
                            tint = dislikeColor,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Text(dislikes.toString(), color = onSurfaceColor, fontSize = 14.sp)
                }

                // 📤 Share
                IconButton(onClick = { /* share */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.send),
                        contentDescription = "Share",
                        tint = Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // --- Date & Location at bottom-right ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("📅 ${post.date}", color = onSurfaceVariantColor, fontSize = 12.sp, fontWeight = FontWeight.Normal, fontFamily = Inter)
            }
        }
    }
}


// ---------------- Full Post ----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullPostScreen(complaint: Complaint, onBackClick: () -> Unit) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(complaint.locationLatLng, 12f)
    }
    var showImage by remember { mutableStateOf(false) }

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(containerColor)
    }

    Scaffold(
        containerColor = backgroundColor,
//        topBar = {
//            TopAppBar(
//                windowInsets = WindowInsets(0),
//                colors = TopAppBarDefaults.topAppBarColors(containerColor),
//                title = {
//                    Text(
//                        text = "Details",
//                        color = onSurfaceColor,
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold,
//                        modifier = Modifier.padding(start = 88.dp)
//                    )
//                },
//                navigationIcon = {
//                    IconButton(onClick = onBackClick) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = onSurfaceColor)
//                    }
//                },
//                actions = {
//                    IconButton(onClick = { /* TODO: share */ }) {
//                        Icon(Icons.Default.Share, contentDescription = "Share", tint = onSurfaceColor)
//                    }
//                }
//            )
//        }
    ) { paddingValues ->
        Box(Modifier.fillMaxSize()) {
            if (showImage) {
                // Full screen image
                Box(Modifier.fillMaxSize()) {
                    Image(
                        complaint.postImage,
                        contentDescription = "Full Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = { showImage = false },
                        modifier = Modifier
                            .padding(16.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = onSurfaceColor)
                    }
                }
            } else {
                // Details screen
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = paddingValues.calculateTopPadding()-25.dp,
                            bottom = paddingValues.calculateBottomPadding()-8.dp
                            )
                ) {
                    item {
                        Column(Modifier.padding(start = 16.dp, end = 16.dp)) {
                            Text(
                                complaint.problemTitle,
                                color = onSurfaceColor,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                complaint.problemDescription,
                                color = onSurfaceVariantColor,
                                fontSize = 15.sp
                            )
                            Spacer(Modifier.height(16.dp))

                            Text(
                                "This is additional dummy description text shown in every post. " +
                                        "It provides more details about the issue for testing purposes. " +
                                        "You can replace this with actual complaint data when needed. " +
                                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                                        "Curabitur in nisl sed sapien posuere tincidunt. " +
                                        "Suspendisse potenti. " +
                                        "Donec viverra justo nec orci hendrerit, eget cursus orci pretium. " +
                                        "Nulla facilisi. " +
                                        "Praesent feugiat, risus eget bibendum dictum, sapien urna pretium orci, " +
                                        "vel luctus leo purus ac nunc. " +
                                        "Sed convallis ex ac semper ullamcorper. " +
                                        "Integer id varius lorem. " +
                                        "Nam vel diam eget erat ultricies porttitor non sit amet lacus. " +
                                        "Curabitur sagittis, dui vitae mattis luctus, augue nulla porttitor felis, " +
                                        "vel dapibus eros mauris nec justo.",
                                color = onSurfaceVariantColor,
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column {
                                    Text("Category", color = onSurfaceVariantColor, fontSize = 14.sp)
                                    Text(
                                        "Public Safety",
                                        color = onSurfaceColor,
                                        fontSize = 16.sp,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("Status", color = onSurfaceVariantColor, fontSize = 14.sp)
                                    Box(
                                        modifier = Modifier
                                            .background(primaryColor, RoundedCornerShape(8.dp))
                                            .padding(horizontal = 12.dp, vertical = 4.dp)
                                    ) {
                                        Text("Open", color = onSurfaceColor, fontSize = 14.sp)
                                    }
                                }
                            }

                            // 👇 Button to view image
                            Spacer(Modifier.height(16.dp))
                            // 👇 Transparent Button to view image
                            Spacer(Modifier.height(16.dp))
                            Button(
                                onClick = { showImage = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,   // 🚀 Transparent background
                                    contentColor = primaryColor           // Text/Icon color
                                ),
                                border = BorderStroke(1.dp, primaryColor) // Optional outline
                            ) {
                                Text("View Image", fontSize = 16.sp)
                            }

                            Spacer(Modifier.height(20.dp))
                            Text("Location", color = onSurfaceColor, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(Modifier.height(8.dp))
                        }
                    }

                    item {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(horizontal = 16.dp)
                                .clip(RoundedCornerShape(16.dp))
                        ) {
                            GoogleMap(
                                modifier = Modifier.fillMaxSize(),
                                cameraPositionState = cameraPositionState
                            ) {
                                Marker(
                                    state = rememberMarkerState(position = complaint.locationLatLng),
                                    title = complaint.location
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}




// ---------------- Bottom Navigation ----------------
@Composable
fun BottomNavigationBar(selectedScreen: Screen, onScreenSelected: (Screen) -> Unit) {
    val containerColor = Color(0xFF1C1C1E)
    val inactiveColor = Color.Gray

    val items = listOf(
        Triple("Home", R.drawable.home3, Screen.Home),
        Triple("New", R.drawable.plus14, Screen.NewComplaint),
        Triple("My Posts", R.drawable.checklist1, Screen.MyComplaints),
        Triple("Settings", R.drawable.sett, Screen.Profile)
    )

    Row(
        Modifier
            .fillMaxWidth()
            .background(containerColor)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            val isSelected = selectedScreen == item.third
            val iconColor = if (isSelected) primaryColor else Color.Gray
            val labelColor = if (isSelected) primaryColor else Color.Gray

            // Get the painter for the icon
            val iconPainter = painterResource(id = item.second)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(bottom = 5.dp, top = 5.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onScreenSelected(item.third) }
            ) {
                if (isSelected) {
                    // Draw the icon and fill it with color if selected
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .padding(bottom = 2.dp)
                    ) {
                        Image(
                            painter = iconPainter,
                            contentDescription = item.first,
                            colorFilter = ColorFilter.tint(primaryColor)
                        )
                    }
                } else {
                    // Keep the original behavior for unselected icons
                    Icon(
                        painter = iconPainter,
                        contentDescription = item.first,
                        tint = iconColor,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(bottom = 2.dp)
                    )
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    text = item.first,
                    color = labelColor,
                    fontFamily = Inter,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}



// ---------------- New Complaint ----------------
