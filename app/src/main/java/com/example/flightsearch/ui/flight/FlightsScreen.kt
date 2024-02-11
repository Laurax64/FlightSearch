/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.ui.AppViewModelProvider
import com.example.flightsearch.ui.navigation.NavigationDestination

/**
 * Represents a navigation destination for the flights screen
 */
object FlightsDestination : NavigationDestination {
    override val route = "flights"
}

/**
 * Displays the flights for a given airport
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FlightsScreen(
    flightsViewModel: FlightsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateBack: () -> Unit,
) {
    val iataCode = flightsViewModel.getIataCode()
        .collectAsState(initial = "").value
    val favorites = flightsViewModel.getFavorites()
        .collectAsState(initial = listOf()).value

    var isFavorite by remember { mutableStateOf(true) }

    if (iataCode != "") {
        val departureAirport = flightsViewModel.getAirportByIataCode(iataCode)
            .collectAsState(initial = Airport()).value
        val destinationAirports = flightsViewModel.getDestinationsAirports(iataCode)
            .collectAsState(initial = listOf()).value
        Scaffold(topBar = {
            FlightsTopBar(
                iataCode = iataCode,
                onBackClick = navigateBack
            )
        }) {
            ShowFlights(
                departure = departureAirport,
                destinations = destinationAirports,
                onStarClick = { destCode: String ->
                    favorites.forEach {
                        if (it.destinationCode == destCode
                            && it.departureCode == departureAirport.iataCode) {
                            flightsViewModel.deleteFavorite(it)
                            isFavorite = false
                        }
                        if(isFavorite) {
                            flightsViewModel.addFavorite(
                                Favorite(
                                    departureCode = departureAirport.iataCode,
                                    destinationCode = destCode
                                )
                            )
                        }
                    }
                }
            )
        }
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
            IconButton(onClick = onBackClick){
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
    onStarClick: (String) -> Unit
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
fun FlightCard(modifier: Modifier = Modifier, departure: Airport,
               arrival: Airport, onHeartClick: (String) -> Unit
) {
    var filledHeart by remember {mutableStateOf(false)}
    Card(
        modifier = modifier.padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Text(text = "Depart", style = MaterialTheme.typography.labelMedium)
                Text(text = departure.iataCode)
                Text(text = departure.name)
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Text(text = "Arrive", style = MaterialTheme.typography.labelMedium)
                Text(text = arrival.iataCode)
                Text(text = arrival.name)
            }
            IconButton(
                onClick = {
                    onHeartClick(arrival.iataCode)
                    filledHeart = !filledHeart
                          },
                modifier = modifier
                    .padding(8.dp)
                    .weight(0.5f)
            ) {
                if(filledHeart) {
                    Icon(Icons.Default.Favorite,
                        "remove from favorites")
                }
                else {
                    Icon(Icons.Default.FavoriteBorder,
                        "add to favorites")
                }
            }
        }
    }
}