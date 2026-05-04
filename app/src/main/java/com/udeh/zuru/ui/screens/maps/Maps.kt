package com.udeh.zuru.ui.screens.maps

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.udeh.zuru.navigation.*
import com.udeh.zuru.ui.theme.zurublue
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapsScreen(navController: NavController) {

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
                NavigationDrawerItem(icon = { Icon(Icons.Default.Star, null, tint = zurublue) }, label = { Text("Tour with Zuru", color = zurublue) }, selected = false, onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_TOURWITHZURU) })
                NavigationDrawerItem(icon = { Icon(Icons.Default.Person, null, tint = zurublue) }, label = { Text("Tourers", color = zurublue) }, selected = false, onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_TOURERS) })
                NavigationDrawerItem(icon = { Icon(Icons.Default.Search, null, tint = zurublue) }, label = { Text("Discover", color = zurublue) }, selected = false, onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_DISCOVER) })
                NavigationDrawerItem(icon = { Icon(Icons.Default.Close, null, tint = Color.Red) }, label = { Text("Logout", color = Color.Red) }, selected = false, onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_LOGIN) })
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Maps", color = zurublue, fontSize = 30.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 10.dp)) },
                    actions = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = zurublue)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors()
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues).fillMaxSize()
            ) {
                // Add your Maps content here
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MapsScreenPreview() {
    MapsScreen(rememberNavController())
}