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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.ui.AppViewModelProvider
import com.example.flightsearch.ui.navigation.NavigationDestination

/**
 * Represents a navigation destination for the favorite flights screen
 */
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
    val favorites = favoriteFlightsViewModel.getFavorites()
        .collectAsState(initial = listOf()).value

    Scaffold(topBar = { FlightSearchTopBar(onSearchClick = navigateToAirportSearch) }) {
        ShowFavorites(Modifier.padding(it), favorites) { depCode: String, desCode: String ->
                favorites.forEach {
                    if (it.departureCode == depCode && it.destinationCode == desCode) {
                        favoriteFlightsViewModel.delete(it)
                    }
                }
            }
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
fun ShowFavorites(
    modifier: Modifier = Modifier, favorites: List<Favorite>,
    onHeartClick: (String, String) -> Unit
) {
    LazyColumn(modifier.fillMaxWidth()) {
        items(favorites) {
            FavoriteCard(Modifier, it.departureCode, it.destinationCode
            ) { onHeartClick(it.departureCode, it.destinationCode) }
        }
    }
}

/**
 * Displays a [Favorite] flight card
 */
@Composable
fun FavoriteCard(
    modifier: Modifier = Modifier,
    departure: String, destination: String,
    onHeartClick: () -> Unit
) {
    Card(modifier.padding(8.dp)) {
        Row(Modifier.fillMaxWidth()) {
            Column(modifier.padding(8.dp).weight(1f), Arrangement.Center) {
                Text(text = "Depart", style = MaterialTheme.typography.labelMedium)
                Text(text = departure)
            }
            Column(modifier.padding(8.dp).weight(1f), Arrangement.Center) {
                Text(text = "Arrive", style = MaterialTheme.typography.labelMedium)
                Text(text = destination)
            }
            IconButton({ onHeartClick() }, modifier.padding(8.dp).weight(0.5f)) {
                Icon(Icons.Default.Favorite, "remove from favorites")
            }
        }
    }
}

