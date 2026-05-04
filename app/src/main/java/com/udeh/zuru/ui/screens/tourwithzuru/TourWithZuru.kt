package com.udeh.zuru.ui.screens.tourwithzuru

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.udeh.zuru.navigation.*
import com.udeh.zuru.ui.theme.zurublue
import kotlinx.coroutines.launch


// ── Reason card data ──────────────────────────────────────────────────────────

data class ZuruReason(
    val icon: ImageVector,
    val title: String,
    val description: String
)

val zuruReasons = listOf(
    ZuruReason(Icons.Default.Star, "Curated Experiences", "Every Zuru tour is hand-crafted by locals who know Kenya inside out — from hidden waterfalls to secret viewpoints no guidebook mentions."),
    ZuruReason(Icons.Default.Person, "Expert Local Guides", "Our guides are passionate Kenyans with years of field experience. They speak the land, its animals, and its people fluently."),
    ZuruReason(Icons.Default.LocationOn, "All-Inclusive Packages", "Accommodation, transport, meals, and park fees — all bundled into one transparent price. No surprise costs, ever."),
    ZuruReason(Icons.Default.CheckCircle, "Small Group Sizes", "We cap every tour at 8 travellers. Fewer people means more wildlife sightings, better photos, and a more personal experience."),
    ZuruReason(Icons.Default.Favorite, "Community First", "A portion of every Zuru booking goes directly to local Maasai communities and conservation projects that protect Kenya's wildlife."),
    ZuruReason(Icons.Default.Star, "Flexible Itineraries", "Want to spend an extra day at the Mara? No problem. We adapt to you — not the other way around."),
    ZuruReason(Icons.Default.CheckCircle, "24/7 Support", "From the moment you book to the day you fly home, our team is reachable around the clock for anything you need.")
)


// ── Single reason card ────────────────────────────────────────────────────────

@Composable
fun ZuruReasonCard(reason: ZuruReason) {
    val isDark = isSystemInDarkTheme()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isDark) Color(0xFF1A1A1A) else Color(0xFFF5F7FF))
            .border(1.dp, zurublue.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Icon box
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(zurublue.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(reason.icon, contentDescription = null, tint = zurublue, modifier = Modifier.size(22.dp))
        }

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = reason.title, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(text = reason.description, fontSize = 13.sp, lineHeight = 19.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
        }
    }
}


// ── TourWithZuru Screen ───────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TourWithZuruScreen(navController: NavController) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(24.dp))
                Text("Zuru", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = zurublue, modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp))
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                NavigationDrawerItem(icon = { Icon(Icons.Default.Person, null, tint = zurublue) }, label = { Text("Profile", color = zurublue) }, selected = false, onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_PROFILE) })
                NavigationDrawerItem(icon = { Icon(Icons.Default.Home, null, tint = zurublue) }, label = { Text("Home", color = zurublue) }, selected = false, onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_HOME) })
                NavigationDrawerItem(icon = { Icon(Icons.Default.Home, null, tint = zurublue) }, label = { Text("Hotels", color = zurublue) }, selected = false, onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_HOTELS) })
                NavigationDrawerItem(icon = { Icon(Icons.Default.LocationOn, null, tint = zurublue) }, label = { Text("Maps", color = zurublue) }, selected = false, onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_MAPS) })
                NavigationDrawerItem(icon = { Icon(Icons.Default.Person, null, tint = zurublue) }, label = { Text("Tourers", color = zurublue) }, selected = false, onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_TOURERS) })
                NavigationDrawerItem(icon = { Icon(Icons.Default.Search, null, tint = zurublue) }, label = { Text("Discover", color = zurublue) }, selected = false, onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_DISCOVER) })
                NavigationDrawerItem(icon = { Icon(Icons.Default.Close, null, tint = Color.Red) }, label = { Text("Logout", color = Color.Red) }, selected = false, onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_LOGIN) })
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Tour with Zuru", color = zurublue, fontSize = 26.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 10.dp)) },
                    actions = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = zurublue)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors()
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {

                // ── Hero section ──────────────────────────────────────────────
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(zurublue)
                            .padding(horizontal = 24.dp, vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(48.dp))
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "Kenya, Your Way",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Zuru is not just a travel app — it's your personal Kenyan adventure partner. We plan, you explore.",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.85f),
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                    }
                }

                // ── Why choose Zuru header ────────────────────────────────────
                item {
                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = "Why Tour with Zuru?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Here's what makes us different from everyone else",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                }

                // ── Reason cards ──────────────────────────────────────────────
                items(zuruReasons.size) { index ->
                    ZuruReasonCard(reason = zuruReasons[index])
                }

                // ── Stats row ─────────────────────────────────────────────────
                item {
                    Spacer(Modifier.height(24.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        listOf(
                            Triple("5,000+", "Happy\nTravellers", Icons.Default.Person),
                            Triple("50+", "Destinations\nCovered", Icons.Default.LocationOn),
                            Triple("4.9★", "Average\nRating", Icons.Default.Star)
                        ).forEach { (stat, label, icon) ->
                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = zurublue)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                                    Spacer(Modifier.height(6.dp))
                                    Text(text = stat, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                    Text(text = label, fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f), textAlign = TextAlign.Center, lineHeight = 14.sp)
                                }
                            }
                        }
                    }
                }

                // ── Tour with Zuru button ─────────────────────────────────────
                item {
                    Spacer(Modifier.height(28.dp))
                    Button(
                        onClick = { navController.navigate(ROUT_DISCOVER) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = zurublue)
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(10.dp))
                        Text("Tour with Zuru", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TourWithZuruScreenPreview() {
    TourWithZuruScreen(rememberNavController())
}