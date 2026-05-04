package com.udeh.zuru.ui.screens.hotels

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.udeh.zuru.R
import com.udeh.zuru.navigation.*
import com.udeh.zuru.ui.screens.home.featuredImages
import com.udeh.zuru.ui.theme.zurublue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


// ── Data model ────────────────────────────────────────────────────────────────

data class Hotel(
    val name: String,
    val imageRes: Int,
    val description: String,
    val standout: String,
    val rating: Float,
    val reviewCount: Int,
    val pricePerNight: String,
    val location: String,
    val tags: List<String>
)

val hotelList = listOf(
    Hotel(
        name = "The Fairmont Hotel",
        imageRes = R.drawable.fairmont,          // ← swap to your Fairmont drawable
        description = "An iconic landmark of luxury nestled in the heart of Nairobi, The Fairmont Norfolk has been welcoming guests since 1904. It blends colonial heritage with contemporary elegance.",
        standout = "Historic 5-star grandeur with lush tropical gardens and a legendary Lord Delamere Terrace.",
        rating = 4.8f,
        reviewCount = 1240,
        pricePerNight = "KES 32,000",
        location = "Nairobi CBD",
        tags = listOf("5-Star", "Heritage", "Pool")
    ),
    Hotel(
        name = "The Serena Hotel",
        imageRes = R.drawable.serenahotel,    // ← swap to your Serena drawable
        description = "Set against manicured gardens near Nairobi National Park, The Serena Hotel offers world-class hospitality infused with authentic African art and decor throughout.",
        standout = "Stunning African-inspired interiors with proximity to the National Park for early morning game drives.",
        rating = 4.7f,
        reviewCount = 987,
        pricePerNight = "KES 28,500",
        location = "Nairobi, Upper Hill",
        tags = listOf("5-Star", "Art Deco", "Spa")
    ),
    Hotel(
        name = "The Ishara Lodge",
        imageRes = R.drawable.theisharamara,     // ← swap to your Ishara drawable
        description = "A boutique eco-lodge perched on the edge of the Maasai Mara conservancy, Ishara offers an intimate wildlife experience with just 12 private tented suites.",
        standout = "Exclusive conservancy access means fewer vehicles and unrivalled big-cat sightings at dawn.",
        rating = 4.9f,
        reviewCount = 512,
        pricePerNight = "KES 95,000",
        location = "Maasai Mara Conservancy",
        tags = listOf("Eco-Lodge", "All-Inclusive", "Game Drives")
    ),
    Hotel(
        name = "The Argyle Grande Hotel",
        imageRes = R.drawable.argylegrand,          // ← swap to your Argyle drawable
        description = "Nairobi's sleekest urban retreat, The Argyle Grande sits atop Westlands and offers contemporary rooms with panoramic city skyline views and a rooftop infinity pool.",
        standout = "Rooftop infinity pool with 360° Nairobi views — the best sundowner spot in the city.",
        rating = 4.6f,
        reviewCount = 743,
        pricePerNight = "KES 22,000",
        location = "Westlands, Nairobi",
        tags = listOf("4-Star", "City View", "Rooftop Pool")
    ),
    Hotel(
        name = "The Boma Hotel",
        imageRes = R.drawable.thebomahotel, // ← swap to your Boma drawable
        description = "A modern conference and leisure hotel minutes from Jomo Kenyatta International Airport. The Boma is loved for its friendly service, lush grounds and excellent restaurant.",
        standout = "Award-winning restaurant, large outdoor pool and the most convenient location for transit travellers.",
        rating = 4.5f,
        reviewCount = 1105,
        pricePerNight = "KES 18,000",
        location = "Airport North Road, Nairobi",
        tags = listOf("4-Star", "Conference", "Airport")
    )
)


// ── Star rating row ───────────────────────────────────────────────────────────

@Composable
fun StarRatingRow(rating: Float, reviewCount: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val fullStars = rating.toInt()
        val hasHalf = (rating - fullStars) >= 0.5f
        repeat(5) { index ->
            Icon(
                imageVector = when {
                    index < fullStars -> Icons.Default.Star
                    index == fullStars && hasHalf -> Icons.Default.Star
                    else -> Icons.Default.Star
                },
                contentDescription = null,
                tint = when {
                    index < fullStars -> Color(0xFFFFC107)
                    index == fullStars && hasHalf -> Color(0xFFFFE082)
                    else -> Color(0xFFE0E0E0)
                },
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "$rating",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "  ($reviewCount reviews)",
            fontSize = 12.sp,
            color = zurublue
        )
    }
}


// ── Hotel card ────────────────────────────────────────────────────────────────

@Composable
fun HotelCard(hotel: Hotel, navController: NavController) {

    val isDarkTheme = isSystemInDarkTheme()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(
                width = 2.dp,
                color = zurublue,
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(containerColor = if (isDarkTheme) Color.Black else MaterialTheme.colorScheme.surface
        ),


    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // ── Hotel name header ─────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = hotel.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                    )
                    Spacer(modifier = Modifier.height(2.dp))

                }
                // Availability badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFE8F5E9))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Available",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF388E3C)
                    )
                }
            }

            // ── Hotel image ───────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
            ) {
                Image(
                    painter = painterResource(id = hotel.imageRes),
                    contentDescription = hotel.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Subtle gradient fade at bottom of image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.25f))
                            )
                        )
                )
                // Tags overlaid on bottom-left of image
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    hotel.tags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(zurublue.copy(alpha = 0.85f))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(text = tag, fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // ── Description + standout ────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = hotel.description,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Standout callout box
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(zurublue.copy(alpha = 0.07f))
                        .padding(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = zurublue,
                        modifier = Modifier.size(15.dp).padding(top = 1.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = hotel.standout,
                        fontSize = 12.sp,
                        color = zurublue,
                        lineHeight = 17.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // ── Rating ────────────────────────────────────────────────────
                StarRatingRow(rating = hotel.rating, reviewCount = hotel.reviewCount)

                Spacer(modifier = Modifier.height(14.dp))

                // ── Divider ───────────────────────────────────────────────────
                HorizontalDivider(color = Color.White, thickness = 1.dp)

                Spacer(modifier = Modifier.height(12.dp))

                // ── Price + Book button ───────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Per night from", fontSize = 12.sp,)
                        Text(
                            text = hotel.pricePerNight,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = zurublue
                        )
                    }
                    Button(
                        onClick = {
                            val index = hotelList.indexOf(hotel)
                            navController.navigate("reservations/$index")
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = zurublue),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "Book Now",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                }
            }
        }
    }
}


// ── Hotels Screen ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelsScreen(navController: NavController) {

    var selectedIndex by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }

    // ── Drawer state ──────────────────────────────────────────────────────────
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(pageCount = { featuredImages.size })

    LaunchedEffect(Unit) {
        while (true) {
            delay(4000)
            val nextPage = (pagerState.currentPage + 1) % featuredImages.size
            pagerState.animateScrollToPage(
                page = nextPage,
                animationSpec = tween(durationMillis = 1200, easing = EaseInOutCubic)
            )
        }
    }

    // ── Drawer wraps everything ───────────────────────────────────────────────
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(24.dp))
                Text(
                    text = "Zuru",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = zurublue,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                NavigationDrawerItem(
                    icon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = zurublue
                        )
                    },
                    label = { Text("Profile", color = zurublue) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }; navController.navigate(
                        ROUT_PROFILE
                    )
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null, tint = zurublue) },
                    label = { Text("Home", color = zurublue) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }; navController.navigate(
                        ROUT_HOME
                    )
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Star, contentDescription = null, tint = zurublue) },
                    label = { Text("Tour with Zuru", color = zurublue) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }; navController.navigate(
                        ROUT_TOURWITHZURU
                    )
                    }
                )
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = zurublue
                        )
                    },
                    label = { Text("Maps", color = zurublue) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }; navController.navigate(
                        ROUT_MAPS
                    )
                    }
                )
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = zurublue
                        )
                    },
                    label = { Text("Tourers", color = zurublue) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }; navController.navigate(
                        ROUT_TOURERS
                    )
                    }
                )
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            tint = zurublue
                        )
                    },
                    label = { Text("Discover", color = zurublue) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }; navController.navigate(
                        ROUT_DISCOVER
                    )
                    }
                )
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    },
                    label = { Text("Logout", color = Color.Red) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }; navController.navigate(
                        ROUT_LOGIN
                    )
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Hotels",
                            color = zurublue,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    },
                    actions = {
                        // ── MoreVert now opens the drawer ─────────────────
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "Menu",
                                tint = zurublue,
                                modifier = Modifier.padding(end = 10.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors()
                )
            },
            content = { paddingValues ->

                val isDarkTheme = isSystemInDarkTheme()

                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 12.dp, horizontal = 0.dp)
                ) {
                    // Subtitle
                    item {
                        Text(
                            text = "Kenya's finest stays, curated for you",
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }

                    items(hotelList) { hotel ->
                        HotelCard(hotel = hotel, navController = navController)
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun HotelsScreenPreview() {
    HotelsScreen(rememberNavController())
}
