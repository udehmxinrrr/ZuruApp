package com.udeh.zuru.ui.screens.maps

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.*
import com.udeh.zuru.navigation.*
import com.udeh.zuru.ui.theme.zurublue
import com.udeh.zuru.utils.checkAndRequestLocationSettings
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MapsScreen(navController: NavController, viewModel: MapsViewModel = viewModel()) {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    val currentLocation by viewModel.currentLocation.collectAsState()
    val touristSites by viewModel.touristSites.collectAsState()
    val hotels by viewModel.hotels.collectAsState()
    val selectedPlace by viewModel.selectedPlace.collectAsState()
    
    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val settingResultRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            viewModel.fetchCurrentLocation()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.initialize(context)
        if (!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
        } else {
            checkAndRequestLocationSettings(
                context,
                onSuccess = { viewModel.fetchCurrentLocation() },
                launcher = settingResultRequest
            )
        }
    }

    LaunchedEffect(locationPermissionState.status) {
        if (locationPermissionState.status.isGranted) {
            checkAndRequestLocationSettings(
                context,
                onSuccess = { viewModel.fetchCurrentLocation() },
                launcher = settingResultRequest
            )
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation ?: com.google.android.gms.maps.model.LatLng(0.0, 0.0), 10f)
    }

    LaunchedEffect(currentLocation) {
        currentLocation?.let {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newCameraPosition(
                    CameraPosition.fromLatLngZoom(it, 14f)
                ),
                durationMs = 1000
            )
        }
    }

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
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        if (locationPermissionState.status.isGranted) {
                            checkAndRequestLocationSettings(
                                context,
                                onSuccess = { viewModel.fetchCurrentLocation() },
                                launcher = settingResultRequest
                            )
                        } else {
                            locationPermissionState.launchPermissionRequest()
                        }
                    },
                    containerColor = zurublue,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = "My Location")
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = locationPermissionState.status.isGranted)
                ) {
                    touristSites.forEach { place ->
                        place.latLng?.let { latLng ->
                            Marker(
                                state = MarkerState(position = latLng),
                                title = place.name,
                                snippet = "Tourist Attraction",
                                onClick = {
                                    viewModel.selectPlace(place)
                                    false
                                }
                            )
                        }
                    }
                    hotels.forEach { place ->
                        place.latLng?.let { latLng ->
                            Marker(
                                state = MarkerState(position = latLng),
                                title = place.name,
                                snippet = "Hotel",
                                onClick = {
                                    viewModel.selectPlace(place)
                                    false
                                }
                            )
                        }
                    }
                }

                if (selectedPlace != null) {
                    PlaceDetailsBottomSheet(
                        place = selectedPlace!!,
                        onDismiss = { viewModel.selectPlace(null) },
                        onGetDirections = {
                            val uri = Uri.parse("google.navigation:q=${selectedPlace!!.latLng?.latitude},${selectedPlace!!.latLng?.longitude}")
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            intent.setPackage("com.google.android.apps.maps")
                            context.startActivity(intent)
                        },
                        onFindHotels = {
                            selectedPlace!!.latLng?.let { viewModel.findHotelsNear(it) }
                        },
                        onBookHotel = {
                            // Assuming passing index 0 for now or pass hotel ID
                            navController.navigate("reservations/0")
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetailsBottomSheet(
    place: com.google.android.libraries.places.api.model.Place,
    onDismiss: () -> Unit,
    onGetDirections: () -> Unit,
    onFindHotels: () -> Unit,
    onBookHotel: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = place.name ?: "Unknown Place",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFC107), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "${place.rating ?: "N/A"}", fontSize = 16.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = place.address ?: "No address available",                fontSize = 14.sp,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(16.dp))

            val isHotel = place.types?.contains(com.google.android.libraries.places.api.model.Place.Type.LODGING) == true

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onGetDirections,
                    colors = ButtonDefaults.buttonColors(containerColor = zurublue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Directions")
                }
                
                if (isHotel) {
                    Button(
                        onClick = onBookHotel,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Book Now")
                    }
                } else {
                    OutlinedButton(
                        onClick = onFindHotels,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Find Hotels")
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}