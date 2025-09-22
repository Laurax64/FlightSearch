package com.example.flightsearch

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flightsearch.ui.navigation.FlightSearchNavHost

/**
 * Top level composable that represents screens for the application.
 *
 * @param navController The app's navigation controller
 * @param modifier Modifier to apply to this composable
 */
@Composable
fun FlightSearchApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    FlightSearchNavHost(navController = navController)
}
