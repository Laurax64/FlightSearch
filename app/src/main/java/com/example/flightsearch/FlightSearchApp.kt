package com.example.flightsearch

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flightsearch.ui.navigation.FlightSearchNavHost

/**
 * Top level composable that represents screens for the application.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FlightSearchApp(navController: NavHostController = rememberNavController()) {
    FlightSearchNavHost(navController = navController)
}