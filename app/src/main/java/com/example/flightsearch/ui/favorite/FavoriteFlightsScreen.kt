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
package com.example.flightsearch.ui.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.ui.AppViewModelProvider
import com.example.flightsearch.ui.navigation.NavigationDestination

object FavoriteFlightsDestination : NavigationDestination {
    override val route = "favorite_flights"
}

/**
 * Displays an app top bar and the user's favorite flights
 */
@Composable
fun FavoriteFlightsScreen(
    favoriteFlightsViewModel: FavoriteFlightsViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    ),
    navigateToAirportSearch: () -> Unit
) {
    val favoriteFlightUiState by favoriteFlightsViewModel
        .favoriteFlightsUiState.collectAsState()

    Scaffold(topBar = { FlightSearchTopBar(onSearchClick = navigateToAirportSearch) }) {
        FavoriteFlights(Modifier.padding(it), favoriteFlightUiState.favoriteList,
            favoriteFlightsViewModel)
    }
}

/**
 * Displays the favorite screen's top bar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchTopBar(
    modifier: Modifier = Modifier, onSearchClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text(text = "Flight search", style = MaterialTheme.typography.titleLarge) },
        navigationIcon = {
            IconButton(onClick = onSearchClick){
                Icon(Icons.Default.Search, "Search Icon")
            }
        }
    )
}

/**
 * Displays a [LazyColumn] of [Favorite] flights
 */
@Composable
fun FavoriteFlights(
    modifier: Modifier = Modifier, favorites: List<Favorite>,
    favoriteFlightsViewModel: FavoriteFlightsViewModel
) {
    LazyColumn(modifier.fillMaxWidth()) {
        items(items = favorites, key = { favorite -> favorite.id }) {
            FavoriteCard(modifier, it.departureCode, it.destinationCode) {
                favoriteFlightsViewModel.delete(it)
            }
        }
    }
}

/**
 * Displays a [Favorite] flight card
 */
@Composable
fun FavoriteCard(modifier: Modifier = Modifier, departure: String, arrival: String,
                 onClick: () -> Unit
) {
    Card {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
            modifier = modifier.fillMaxWidth()
                .padding(start = 16.dp, end = 24.dp, top = 12.dp, bottom = 12.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(weight = 1f)
            ) {
                Text(text = "Depart", style = MaterialTheme.typography.labelMedium)
                Text(text = departure)
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(weight = 1f)
            ) {
                Text(text = "Arrive", style = MaterialTheme.typography.labelMedium)
                Text(text = arrival)
            }
            IconButton(onClick = onClick) { Icons.Filled.Star }
        }
    }
}

