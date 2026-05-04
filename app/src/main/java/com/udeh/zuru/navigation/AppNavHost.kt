package com.udeh.zuru.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.udeh.zuru.ui.screens.ai.DestinationFactsScreen
import com.udeh.zuru.ui.screens.auth.LoginScreen
import com.udeh.zuru.ui.screens.auth.RegisterScreen
import com.udeh.zuru.ui.screens.discover.DiscoverScreen
import com.udeh.zuru.ui.screens.home.HomeScreen
import com.udeh.zuru.ui.screens.hotels.HotelsScreen
import com.udeh.zuru.ui.screens.hotels.hotelList
import com.udeh.zuru.ui.screens.maps.MapsScreen
import com.udeh.zuru.ui.screens.payment.PaymentScreen
import com.udeh.zuru.ui.screens.profile.ProfileScreen
import com.udeh.zuru.ui.screens.reservations.ReservationsScreen
import com.udeh.zuru.ui.screens.splashscreen.SplashScreen
import com.udeh.zuru.ui.screens.tourers.TourersScreen
import com.udeh.zuru.ui.screens.tourpayment.TourPaymentScreen
import com.udeh.zuru.ui.screens.tourwithzuru.TourWithZuruScreen

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
            HotelsScreen(navController)
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
        composable(ROUT_PAYMENT) {
            PaymentScreen(navController)
        }
        composable(ROUT_TOURSPAYMENT) {
            TourPaymentScreen(navController)
        }
        composable(ROUT_PROFILE) {
            ProfileScreen(navController)
        }
        composable(ROUT_DISCOVER) {
            DiscoverScreen(navController)
        }
        composable("reservations/{hotelIndex}") { backStackEntry ->
            val index = backStackEntry.arguments?.getString("hotelIndex")?.toIntOrNull() ?: 0
            ReservationsScreen(navController = navController, hotel = hotelList[index])
        }
        composable(ROUT_TOURERS) {
            TourersScreen(navController)
        }
        composable("destination_facts/{destinationName}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("destinationName") ?: ""
            DestinationFactsScreen(navController = navController, destinationName = name)
        }



    }
}