package com.udeh.zuru.ui.screens.discover

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.udeh.zuru.navigation.ROUT_DESTINATIONS
import com.udeh.zuru.navigation.ROUT_DISCOVER
import com.udeh.zuru.navigation.ROUT_HOME
import com.udeh.zuru.navigation.ROUT_HOTELS
import com.udeh.zuru.navigation.ROUT_LOGIN
import com.udeh.zuru.navigation.ROUT_MAPS
import com.udeh.zuru.navigation.ROUT_PROFILE
import com.udeh.zuru.navigation.ROUT_TOURERS
import com.udeh.zuru.navigation.ROUT_TOURWITHZURU
import com.udeh.zuru.ui.theme.zurublue
import kotlinx.coroutines.launch


// ── 1. Data model ─────────────────────────────────────────────────────────────

data class Destination(
    val imageRes: Int,
    val name: String,
    val description: String,
    val location: String = "Kenya",
    val tags: List<String> = emptyList()
)

val sampleDestinations = listOf(
    Destination(
        imageRes = R.drawable.maasaimara,
        name = "Maasai Mara",
        description = "Kenya's most iconic wildlife reserve, home to the Great Migration and the Big Five.",
        location = "Narok County",
        tags = listOf("Wildlife", "Safari", "Big Five")
    ),
    Destination(
        imageRes = R.drawable.diani,
        name = "Diani Beach",
        description = "A pristine white-sand beach on the Kenyan coast, perfect for snorkeling and relaxing.",
        location = "Kwale County",
        tags = listOf("Beach", "Snorkeling", "Relaxation")
    ),
    Destination(
        imageRes = R.drawable.mountkenya,
        name = "Mount Kenya",
        description = "Africa's second-highest peak, offering trekking routes through stunning alpine landscapes.",
        location = "Central Kenya",
        tags = listOf("Trekking", "Alpine", "Adventure")
    )
)


// ── 2. DiscoverCard ────────────────────────────────────────────────────────────

@Composable
fun DiscoverCard(
    destination: Destination,
    onDiscoverClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

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
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color.Black else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // ── Name header ───────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = destination.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

            }

            // ── Image ─────────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
            ) {
                Image(
                    painter = painterResource(id = destination.imageRes),
                    contentDescription = destination.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Gradient at bottom of image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.25f)
                                )
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
                    destination.tags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(zurublue.copy(alpha = 0.85f))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = tag,
                                fontSize = 10.sp,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // ── Description + location callout ────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = destination.description,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Location callout box
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(zurublue.copy(alpha = 0.07f))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = zurublue,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = destination.location,
                        fontSize = 12.sp,
                        color = zurublue,
                        lineHeight = 17.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ── Discover button ───────────────────────────────────────────
                Button(
                    onClick = onDiscoverClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = zurublue),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Text(
                        text = "Discover",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}


// ── 3. DiscoverScreen ─────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DiscoverScreen(navController: NavController) {

    var searchQuery by remember { mutableStateOf("") }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val filteredDestinations = remember(searchQuery) {
        if (searchQuery.isBlank()) sampleDestinations
        else sampleDestinations.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.description.contains(searchQuery, ignoreCase = true)
        }
    }

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
                    label = { Text("Profile", color = zurublue) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_PROFILE) }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Search, contentDescription = null, tint = zurublue) },
                    label = { Text("Home", color = zurublue) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_HOME) }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null, tint = zurublue) },
                    label = { Text("Hotels", color = zurublue) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_HOTELS) }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Star, contentDescription = null, tint = zurublue) },
                    label = { Text("Tour with Zuru", color = zurublue) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_TOURWITHZURU) }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = zurublue) },
                    label = { Text("Maps", color = zurublue) },
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
                    icon = { Icon(Icons.Default.Close, contentDescription = null, tint = Color.Red) },
                    label = { Text("Logout", color = Color.Red) },
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
                            "Discover",
                            color = zurublue,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    },
                    actions = {
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
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {

                // ── Search bar ────────────────────────────────────────────────
                stickyHeader {
                    Surface(shadowElevation = 5.dp) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search Destinations…", color = zurublue) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = zurublue
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(30.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = zurublue,
                                unfocusedBorderColor = Color.LightGray,
                                cursorColor = zurublue
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        )
                    }
                }

                // ── Destination cards ─────────────────────────────────────────
                if (filteredDestinations.isEmpty()) {
                    item {
                        Text(
                            text = "No destinations found for \"$searchQuery\"",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(24.dp)
                        )
                    }
                } else {
                    items(filteredDestinations) { destination ->
                        DiscoverCard(
                            destination = destination,
                            onDiscoverClick = {
                                 navController.navigate("destination_facts/${destination.name}")
                            }
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}


// ── 4. Preview ────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
fun DiscoverScreenPreview() {
    DiscoverScreen(rememberNavController())
}