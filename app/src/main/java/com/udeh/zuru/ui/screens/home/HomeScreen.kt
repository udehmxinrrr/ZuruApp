package com.udeh.zuru.ui.screens.home

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.rememberDrawerState
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.udeh.zuru.navigation.ROUT_DISCOVER
import com.udeh.zuru.navigation.ROUT_HOTELS
import com.udeh.zuru.navigation.ROUT_LOGIN
import com.udeh.zuru.navigation.ROUT_MAPS
import com.udeh.zuru.navigation.ROUT_PROFILE
import com.udeh.zuru.navigation.ROUT_TOURERS
import com.udeh.zuru.navigation.ROUT_TOURWITHZURU
import com.udeh.zuru.ui.theme.zurublue
import kotlin.math.absoluteValue
import kotlinx.coroutines.delay


// ── Data ──────────────────────────────────────────────────────────────────────

data class QuickAction(val label: String, val icon: ImageVector, val route: String)
data class PopularDestination(val imageRes: Int, val name: String, val location: String)
data class NewsItem(
    val tag: String,
    val tagColor: Color,
    val headline: String,
    val summary: String,
    val timeAgo: String
)

val featuredImages = listOf(
    R.drawable.waterfall,
    R.drawable.mountkenya,
    R.drawable.kenyanbeach,
    R.drawable.maasaimara,
)

val popularDestinations = listOf(
    PopularDestination(R.drawable.lakevasha, "Lake Naivasha", "Nairobi, Kenya"),
    PopularDestination(R.drawable.hab, "Habari Lodge", "Mombasa, Kenya"),
    PopularDestination(R.drawable.lakenaivasha, "Lake Naivasha", "Naivasha, Kenya"),
    PopularDestination(R.drawable.savannah, "The Savannah", "Maasai Mara, Kenya"),
)

val newsItems = listOf(
    NewsItem(
        tag = "New Attraction",
        tagColor = Color(0xFF4CAF50),
        headline = "Ol Pejeta Opens New Night Safari Experience",
        summary = "Kenya's largest black rhino sanctuary now offers guided night game drives with thermal imaging.",
        timeAgo = "2h ago"
    ),
    NewsItem(
        tag = "New Hotel",
        tagColor = Color(0xFF2196F3),
        headline = "Radisson Blu Opens Doors in Nairobi CBD",
        summary = "The 5-star property features 300 rooms, a rooftop pool and panoramic views of the Nairobi skyline.",
        timeAgo = "5h ago"
    ),
    NewsItem(
        tag = "Offer",
        tagColor = Color(0xFFFF9800),
        headline = "Sarova Hotels: 30% Off Beach Packages This December",
        summary = "Book any Sarova Whitesands or Salt Lick Safari package before November 30th and save big.",
        timeAgo = "1d ago"
    ),
    NewsItem(
        tag = "Upcoming",
        tagColor = Color(0xFF9C27B0),
        headline = "Lamu Cultural Festival Returns February 2026",
        summary = "The iconic dhow races, Swahili cuisine and live taarab music are back for a week-long celebration.",
        timeAgo = "2d ago"
    ),
    NewsItem(
        tag = "Offer",
        tagColor = Color(0xFFFF9800),
        headline = "Maasai Mara Lodges Slash Prices for Green Season",
        summary = "Several top lodges including Angama and &Beyond are offering up to 40% discounts during low season.",
        timeAgo = "3d ago"
    ),
    NewsItem(
        tag = "New Attraction",
        tagColor = Color(0xFF4CAF50),
        headline = "Karura Forest Adds New Cycling Trails",
        summary = "Nairobi's urban forest has expanded its network with 8km of new dedicated mountain biking paths.",
        timeAgo = "4d ago"
    ),
)





// ── Quick Action Button ────────────────────────────────────────────────────────

@Composable
fun QuickActionButton(action: QuickAction, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(zurublue.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = action.icon,
                contentDescription = action.label,
                tint = zurublue,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = action.label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.DarkGray
        )
    }
}


// ── Popular Destination Card ───────────────────────────────────────────────────

@Composable
fun PopularCard(destination: PopularDestination) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = destination.imageRes),
                contentDescription = destination.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f))
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
            ) {
                Text(text = destination.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = destination.location, color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}


// ── News Card ─────────────────────────────────────────────────────────────────

@Composable
fun NewsCard(item: NewsItem) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(150.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(item.tagColor.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(text = item.tag, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = item.tagColor)
                }
                Text(text = item.timeAgo, fontSize = 10.sp, color = Color.Gray)
            }
            Text(text = item.headline, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Text(text = item.summary, fontSize = 11.sp, color = Color.Gray, maxLines = 2, overflow = TextOverflow.Ellipsis, lineHeight = 15.sp)
        }
    }
}

@Composable
fun SmartRecommendationCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.15f)
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(zurublue.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = zurublue
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Trending Now 🔥",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = "Naivasha trips are popular this weekend. Don't miss out!",
                    fontSize = 12.sp,
                    color = zurublue
                )
            }
        }
    }
}


// ── Home Screen ───────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

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
                    icon = { Icon(Icons.Default.Person, contentDescription = null, tint = zurublue) },
                    label = { Text("Profile",color = zurublue) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_PROFILE) }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null, tint = zurublue) },
                    label = { Text("Hotels",color = zurublue) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_HOTELS) }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Star, contentDescription = null, tint = zurublue) },
                    label = { Text("Tour with Zuru",color = zurublue) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_TOURWITHZURU) }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = zurublue) },
                    label = { Text("Maps",color = zurublue) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_MAPS) }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = null, tint = zurublue) },
                    label = { Text("Tourers", color = zurublue) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_TOURERS) }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Search, contentDescription = null, tint = zurublue) },
                    label = { Text("Discover",color = zurublue) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_DISCOVER) }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Close, contentDescription = null, tint = Color.Red) },
                    label = { Text("Logout",color = Color.Red) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_LOGIN) }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Zuru",
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
                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    // ── Welcome greeting ──────────────────────────────────────
                    item {
                        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                            Text(
                                text = "Hello, Traveller 👋",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Where would you like to go today?",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    item {
                        SmartRecommendationCard()
                    }

                    // ── Search bar ────────────────────────────────────────────
                    item {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search destinations...") },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null, tint = zurublue)
                            },
                            shape = RoundedCornerShape(30.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = zurublue,
                            ),
                            singleLine = true
                        )
                    }

                    // ── Featured carousel ─────────────────────────────────────
                    item {
                        Text(
                            text = "Featured",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .padding(horizontal = 16.dp)
                                .clip(RoundedCornerShape(20.dp))
                        ) { page ->
                            val pageOffset =
                                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                            val alpha = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
                            Box(modifier = Modifier.fillMaxSize()) {
                                Image(
                                    painter = painterResource(id = featuredImages[page]),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .graphicsLayer { this.alpha = alpha }
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.Transparent,
                                                    Color.Black.copy(alpha = 0.5f)
                                                )
                                            )
                                        )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            repeat(featuredImages.size) { index ->
                                val isSelected = pagerState.currentPage == index
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 4.dp)
                                        .size(if (isSelected) 10.dp else 7.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) zurublue else Color.LightGray)
                                )
                            }
                        }
                    }

                    // ── Quick actions ─────────────────────────────────────────
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {}
                    }

                    // ── Popular destinations ──────────────────────────────────
                    item {
                        Text(
                            text = "Popular Destinations",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(popularDestinations) { destination ->
                                PopularCard(destination = destination)
                            }
                        }
                    }

                    // ── News bar ──────────────────────────────────────────────
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Travel News", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = null,
                                    tint = zurublue,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Live",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = zurublue
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(newsItems) { item -> NewsCard(item = item) }
                        }
                    }
                }
            }
        )
    }
}




@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(rememberNavController())
}