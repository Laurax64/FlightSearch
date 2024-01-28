package com.example.flightsearch.ui.flight

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch.data.Airport
import com.example.flightsearch.ui.AppViewModelProvider

/**
 * Displays the flights for a given airport.
 */
@Composable
fun FlightScreen(flightsViewModel: FlightsViewModel = viewModel(
    factory = AppViewModelProvider.Factory)){
    val flightsUiState by flightsViewModel.flightsUiState.collectAsState()

    Scaffold(topBar = { FlightsTopBar(iataCode = flightsUiState.airport.iataCode) }) {
        ShowFlights(
            Modifier.padding(it), flightsUiState.airport, flightsUiState.destinationAirports )
    }
}

/**
 * Displays the flight screen's top bar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightsTopBar(modifier: Modifier = Modifier, iataCode: String) {
    CenterAlignedTopAppBar(
        title = { Text(
            text = "Flights from $iataCode",
            style = MaterialTheme.typography.titleLarge) },
        navigationIcon = {
            IconButton(onClick = { /* TODO */ }){
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
    destinations: List<Airport>
) {
    LazyColumn(modifier.fillMaxWidth()) {
        items(destinations) {
            FlightCard(departure, it, favorite = true)
        }
    }
}

/**
 * Displays a card containing a route between the given [Airport]s and a radio button
 * that lets the user add or remove the route from the favorite routes
 */
@Composable
fun FlightCard(
    departure: Airport, arrival: Airport, favorite: Boolean
) {
    Card(colors = CardDefaults.cardColors(contentColor = Color(0xff1d1b20))) {
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
            IconButton(onClick = {/* TODO */}) {
                Icons.Outlined.Star
            }
        }
    }
}