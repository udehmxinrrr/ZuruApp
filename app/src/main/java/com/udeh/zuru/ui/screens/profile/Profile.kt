package com.udeh.zuru.ui.screens.profile

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.udeh.zuru.navigation.*
import com.udeh.zuru.ui.screens.home.featuredImages
import com.udeh.zuru.ui.theme.zurublue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ── Reusable profile menu row ──────────────────────────────────────────────────

@Composable
fun ProfileMenuRow(
    icon: ImageVector,
    label: String,
    subtitle: String? = null,
    tint: Color = zurublue,
    trailingContent: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(tint.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = label, tint = tint, modifier = Modifier.size(22.dp))
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                if (subtitle != null) {
                    Text(text = subtitle, fontSize = 12.sp, color = zurublue)
                }
            }

            if (trailingContent != null) {
                trailingContent()
            } else {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.LightGray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ── Stat chip ──────────────────────────────────────────────────────────────────

@Composable
fun StatChip(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text(text = label, fontSize = 11.sp, color = Color.White.copy(alpha = 0.75f))
    }
}

// ── Profile Screen ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {

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
                    label = { Text("Home", color = zurublue) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }; navController.navigate(
                        ROUT_HOME
                    )
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null, tint = zurublue) },
                    label = { Text("Hotels", color = zurublue) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }; navController.navigate(
                        ROUT_HOTELS
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
                            "My Profile",
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
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {

                    // ── Hero banner with avatar ────────────────────────────────────
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {

                            // Avatar circle — overlaps the banner bottom edge
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .border(3.dp, zurublue, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Avatar",
                                    tint = zurublue,
                                    modifier = Modifier.size(80.dp)
                                )
                            }
                        }
                    }

                    // ── Name & location ───────────────────────────────────────────
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Sightseer",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(text = "Nairobi, Kenya", fontSize = 13.sp, color = zurublue)
                            }
                        }
                    }

                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(rememberNavController())
}