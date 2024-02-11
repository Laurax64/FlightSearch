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
package com.example.flightsearch.ui.airport

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch.data.Airport
import com.example.flightsearch.ui.AppViewModelProvider
import com.example.flightsearch.ui.navigation.NavigationDestination

/**
 * Represents a navigation destination for the airports search screen
 */
object AirportSearchDestination : NavigationDestination {
    override val route = "airport_search"
}

/**
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirportSearchScreen(
    airportSearchViewModel: AirportSearchViewModel =
        viewModel(factory = AppViewModelProvider.Factory),
    navigateToFlights: () -> Unit

) {
    val searchString = airportSearchViewModel
        .getSearchString().collectAsState(initial = "").value
    val airports = airportSearchViewModel.getAirportsByText(searchString)
        .collectAsState(listOf()).value
    var active by remember { mutableStateOf(false) }

    SearchBar(
        query = searchString,
        onQueryChange = { airportSearchViewModel.saveSearchString(it) },
        onSearch = { active = false },
        active = active,
        onActiveChange = { active = it },
        placeholder = { Text(text = "Enter departure airport") },
        leadingIcon = { Icon(Icons.Default.Search, "Search Icon") }
    ) {
        AirportSearchResults(
            airports = airports,
            onAirportClick = {
                airportSearchViewModel.saveSearchString(it)
                navigateToFlights()
            }
        )
    }
}

@Composable
fun AirportSearchResults(
    modifier: Modifier = Modifier, airports: List<Airport>, onAirportClick: (String) -> Unit
) {
    LazyColumn(modifier.fillMaxWidth()) {
        items(items = airports, key = { airport -> airport.id }) {
            AirportCard(modifier, it, onAirportClick )
        }
    }
}

/**
 * Displays a clickable column for the given [Airport]
 */
@Composable
fun AirportCard(modifier: Modifier = Modifier, airport: Airport,
                onAirportClick: (String) -> Unit) {
    Card(modifier.fillMaxWidth().clickable { onAirportClick((airport.iataCode)) }
        .padding(8.dp)) {
        Column(modifier.padding(16.dp)) {
            Text(text = airport.iataCode, fontWeight = FontWeight.Bold)
            Text(text = airport.name)
        }
    }
}
