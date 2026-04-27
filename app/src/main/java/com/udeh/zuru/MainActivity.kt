package com.udeh.zuru

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.udeh.zuru.navigation.AppNavHost
import com.udeh.zuru.ui.theme.ZuruTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZuruTheme {
                AppNavHost() // ← actually call your nav host
            }
        }
    }
}