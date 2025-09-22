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
import androidx.compose.material3.SearchBarDefaults
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
import com.example.flightsearch.ui.favorite.ShowFavorites
import com.example.flightsearch.ui.navigation.NavigationDestination

/**
 * Represents a navigation destination for the airports search screen
 */
object AirportSearchDestination : NavigationDestination {
    override val route = "airport_search"
}

/**
 * Displays a search bar and the corresponding search results.
 * If the search string is empty, the user's favorites are displayed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirportSearchScreen(
    airportSearchViewModel: AirportSearchViewModel =
        viewModel(factory = AppViewModelProvider.Factory),
    navigateToFlights: () -> Unit,
) {
    val searchString =
        airportSearchViewModel
            .getSearchString()
            .collectAsState(initial = "")
            .value
    val airports =
        airportSearchViewModel
            .getAirportsByText(searchString)
            .collectAsState(listOf())
            .value
    var active by remember { mutableStateOf(false) }
    val favorites =
        airportSearchViewModel
            .getFavorites()
            .collectAsState(initial = listOf())
            .value

    val colors1 = SearchBarDefaults.colors()
    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = searchString,
                onQueryChange = { airportSearchViewModel.saveSearchString(it) },
                onSearch = { active = false },
                expanded = active,
                onExpandedChange = { active = it },
                placeholder = { Text(text = "Enter departure airport") },
                leadingIcon = { Icon(Icons.Default.Search, "Search Icon") },
                colors = colors1.inputFieldColors,
            )
        },
        expanded = active,
        onExpandedChange = { active = it },
        shape = SearchBarDefaults.inputFieldShape,
        colors = colors1,
        tonalElevation = SearchBarDefaults.TonalElevation,
        shadowElevation = SearchBarDefaults.ShadowElevation,
        windowInsets = SearchBarDefaults.windowInsets,
        content = {
            if (searchString == "") {
                ShowFavorites(
                    favorites = favorites,
                    onHeartClick = { depCode: String, desCode: String ->
                        favorites.forEach {
                            if (it.departureCode == depCode && it.destinationCode == desCode) {
                                airportSearchViewModel.deleteFavorite(it)
                            }
                        }
                    },
                )
            } else {
                AirportSearchResults(
                    airports = airports,
                    onAirportClick = {
                        airportSearchViewModel.saveSearchString(it)
                        navigateToFlights()
                    },
                )
            }
        },
    )
}

/**
 * Displays the search results in a [LazyColumn]
 */
@Composable
fun AirportSearchResults(
    modifier: Modifier = Modifier,
    airports: List<Airport>,
    onAirportClick: (String) -> Unit,
) {
    LazyColumn(modifier.fillMaxWidth()) {
        items(items = airports, key = { airport -> airport.id }) {
            AirportCard(modifier, it, onAirportClick)
        }
    }
}

/**
 * Displays a clickable column for the given [Airport]
 */
@Composable
fun AirportCard(
    modifier: Modifier = Modifier,
    airport: Airport,
    onAirportClick: (String) -> Unit,
) {
    Card(
        modifier
            .fillMaxWidth()
            .clickable { onAirportClick((airport.iataCode)) }
            .padding(8.dp),
    ) {
        Column(modifier.padding(16.dp)) {
            Text(text = airport.iataCode, fontWeight = FontWeight.Bold)
            Text(text = airport.name)
        }
    }
}
