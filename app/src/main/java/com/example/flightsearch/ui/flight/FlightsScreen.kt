package com.example.flightsearch.ui.flight

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch.data.Airport
import com.example.flightsearch.ui.AppViewModelProvider
import com.example.flightsearch.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object FlightsDestination : NavigationDestination {
    override val route = "flights"
    const val departureAirport = "departure"
    val routeWithArgs = "$route/{$departureAirport}"
}

/**
 * Displays the flights for a given airport.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FlightsScreen(
    flightsViewModel: FlightsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateBack: () -> Unit,
){
    val flightsUiState by flightsViewModel.flightsUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(topBar = {
        FlightsTopBar(iataCode = flightsUiState.departureIataCode,
            onBackClick = navigateBack) }) {
        ShowFlights(
            departure = Airport(-1, flightsUiState.departureIataCode, "name",-1),
            destinations = flightsUiState.destinationAirports,
             onStarClick = {
                 coroutineScope.launch {

                 }
             }
        )
    }
}

/**
 * Displays the flight screen's top bar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightsTopBar(modifier: Modifier = Modifier, iataCode: String, onBackClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text(
            text = "Flights from $iataCode",
            style = MaterialTheme.typography.titleLarge) },
        navigationIcon = {
            IconButton(onClick = onBackClick ){
                Icon(Icons.Default.ArrowBack, "Arrow back")
            }
        }
    )
}

/**
 * Display's the flight's for the give departure airport
 */
@Composable
fun ShowFlights(
    modifier: Modifier = Modifier,
    departure: Airport,
    destinations: List<Airport>,
    onStarClick: () -> Unit
) {
    LazyColumn(modifier.fillMaxWidth()) {
        items(destinations) {
            FlightCard(modifier, departure, it, onStarClick)
        }
    }
}

/**
 * Displays a card containing a route between the given [Airport]s and a radio button
 * that lets the user add or remove the route from the favorite routes
 */
@Composable
fun FlightCard( modifier: Modifier = Modifier, departure: Airport,
                arrival: Airport, onStarClick: () -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(contentColor = Color(0xff1d1b20))
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 24.dp, top = 12.dp, bottom = 12.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(weight = 1f)
            ) {
                Text(text = "Depart", style = MaterialTheme.typography.labelMedium)
                Text(text = departure.iataCode)
                Text(text = departure.name)
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(weight = 1f)
            ) {
                Text(text = "Arrive", style = MaterialTheme.typography.labelMedium)
                Text(text = arrival.iataCode)
                Text(text = arrival.name)
            }
            IconButton(onClick = onStarClick) {
                Icons.Outlined.Star
            }
        }
    }
}