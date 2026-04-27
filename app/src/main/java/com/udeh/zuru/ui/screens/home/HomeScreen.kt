package com.udeh.zuru.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.udeh.zuru.navigation.ROUT_HOME
import com.udeh.zuru.ui.theme.zurublue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.unit.dp
import com.udeh.zuru.navigation.ROUT_DESTINATIONS
import com.udeh.zuru.navigation.ROUT_DISCOVER
import com.udeh.zuru.navigation.ROUT_HOTELS
import com.udeh.zuru.navigation.ROUT_MAPS
import com.udeh.zuru.navigation.ROUT_PROFILE
import com.udeh.zuru.navigation.ROUT_RESERVATIONS
import com.udeh.zuru.navigation.ROUT_TOURERS
import com.udeh.zuru.navigation.ROUT_TOURWITHZURU

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {


    //Scaffold

    var selectedIndex by remember { mutableStateOf(0) }
    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(


        //TopBar
        topBar = {
            TopAppBar(
                title = { Text("Home") },


                navigationIcon = {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Profile") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Person,
                                        contentDescription = "Profile",
                                        tint = zurublue
                                    )
                                },
                                onClick = {
                                    menuExpanded = false
                                    selectedIndex = 0
                                    navController.navigate(ROUT_PROFILE)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Hotels") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Home,
                                        contentDescription = "Hotels",
                                        tint = zurublue
                                    )
                                },
                                onClick = {
                                    menuExpanded = false
                                    selectedIndex = 2
                                    navController.navigate(ROUT_HOTELS)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Destinations") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.LocationOn,
                                        contentDescription = "Destinations",
                                        tint = zurublue
                                    )
                                },
                                onClick = {
                                    menuExpanded = false
                                    selectedIndex = 3
                                    navController.navigate(ROUT_DESTINATIONS)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Tour with Zuru") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = "Tour with Zuru",
                                        tint = zurublue
                                    )
                                },
                                onClick = {
                                    menuExpanded = false
                                    selectedIndex = 4
                                    navController.navigate(ROUT_TOURWITHZURU)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Maps") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.LocationOn,
                                        contentDescription = "Maps",
                                        tint = zurublue
                                    )
                                },
                                onClick = {
                                    menuExpanded = false
                                    selectedIndex = 5
                                    navController.navigate(ROUT_MAPS)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Reservations") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = "Reservations",
                                        tint = zurublue
                                    )
                                },
                                onClick = {
                                    menuExpanded = false
                                    selectedIndex = 6
                                    navController.navigate(ROUT_RESERVATIONS)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Discover") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = "Discover",
                                        tint = zurublue
                                    )
                                },
                                onClick = {
                                    menuExpanded = false
                                    selectedIndex = 7
                                    navController.navigate(ROUT_DISCOVER)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Tourers") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = "Tourers",
                                        tint = zurublue
                                    )
                                },
                                onClick = {
                                    menuExpanded = false
                                    selectedIndex = 8
                                    navController.navigate(ROUT_TOURERS)
                                }
                            )
                        }
                    }
                },


                actions = {
                    IconButton(
                        onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications"
                        )
                    }
                    IconButton(
                        onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                    IconButton(
                        onClick = {
                            navController.navigate(ROUT_HOME)
                        }) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Share"
                        )
                    }

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = zurublue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White,

                    )
            )
        },


        //FloatingActionButton
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Add action */ },
                containerColor = zurublue,
                modifier = Modifier.padding(end = 5.dp, bottom = 5.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
            }
        },


        //Content
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                //Main Contents of the page


            }
        }
    )

    //End of scaffold

}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    HomeScreen(rememberNavController())
}