package com.udeh.zuru.ui.screens.splashscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.udeh.zuru.R
import com.udeh.zuru.navigation.ROUT_REGISTER
import com.udeh.zuru.ui.theme.zurublue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SplashScreen(navController: NavController){

    LaunchedEffect(Unit) { // ✅ safe to launch coroutines here
        delay(2000)
        navController.navigate(ROUT_REGISTER)
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(zurublue),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.zlogo),
            contentDescription = "product",
            modifier = Modifier.size(100.dp),
        )
    }

}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(rememberNavController())
}

