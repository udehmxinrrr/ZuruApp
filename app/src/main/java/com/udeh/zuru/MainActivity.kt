package com.udeh.zuru

import android.os.Bundle
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.android.libraries.places.api.Places
import com.udeh.zuru.navigation.AppNavHost
import com.udeh.zuru.ui.theme.ZuruTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Places API
        val applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        val mapsApiKey = applicationInfo.metaData.getString("com.google.android.geo.API_KEY") ?: ""
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, mapsApiKey)
        }

        setContent {
            ZuruTheme {
                AppNavHost() // ← actually call your nav host
            }
        }
    }
}