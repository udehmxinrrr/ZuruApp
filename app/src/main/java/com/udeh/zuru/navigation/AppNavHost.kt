package com.udeh.zuru.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.udeh.zuru.ui.screens.auth.LoginScreen
import com.udeh.zuru.ui.screens.auth.RegisterScreen
import com.udeh.zuru.ui.screens.destinations.DestinationScreen
import com.udeh.zuru.ui.screens.destinations.HotelScreen
import com.udeh.zuru.ui.screens.destinations.MapsScreen
import com.udeh.zuru.ui.screens.destinations.TourWithZuruScreen
import com.udeh.zuru.ui.screens.discover.DiscoverScreen
import com.udeh.zuru.ui.screens.home.HomeScreen
import com.udeh.zuru.ui.screens.profile.ProfileScreen
import com.udeh.zuru.ui.screens.reservations.ReservationsScreen
import com.udeh.zuru.ui.screens.splashscreen.SplashScreen
import com.udeh.zuru.ui.screens.tourers.TourersScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUT_SPLASH,
    ) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(ROUT_HOME) {
            HomeScreen(navController)
        }
        composable(ROUT_HOTELS) {
            HotelScreen(navController)
        }
        composable(ROUT_LOGIN) {
            LoginScreen(navController)
        }
        composable(ROUT_SPLASH) {
            SplashScreen(navController)
        }
        composable(ROUT_REGISTER) {
            RegisterScreen(navController)
        }
        composable(ROUT_MAPS) {
            MapsScreen(navController)
        }
        composable(ROUT_TOURWITHZURU) {
            TourWithZuruScreen(navController)
        }
        composable(ROUT_DESTINATIONS) {
            DestinationScreen(navController)
        }
        composable(ROUT_PROFILE) {
            ProfileScreen(navController)
        }
        composable(ROUT_DISCOVER) {
            DiscoverScreen(navController)
        }
        composable(ROUT_RESERVATIONS) {
            ReservationsScreen(navController)
        }
        composable(ROUT_TOURERS) {
            TourersScreen(navController)
        }


    }
}