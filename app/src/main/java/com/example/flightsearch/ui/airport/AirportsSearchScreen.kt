package com.example.flightsearch.ui.airport

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch.R
import com.example.flightsearch.data.airport.Airport
import com.example.flightsearch.data.airport.getFlag
import com.example.flightsearch.ui.AppViewModelProvider
import com.example.flightsearch.ui.components.FlightsLazyVerticalGrid
import com.example.flightsearch.ui.components.calculateColumns
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
    modifier: Modifier = Modifier,
    viewModel: AirportSearchViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToFlights: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val airports = uiState.airports
    var searchString by rememberSaveable { mutableStateOf(uiState.searchString) }
    val favorites = uiState.favorites
    var expanded by rememberSaveable { mutableStateOf(false) }
    val flightsLazyColumn = @Composable {
        FlightsLazyVerticalGrid(
            flights = favorites,
            onHeartClick = { flight ->
                viewModel.deleteFavorite(
                    departureCode = flight.startingPoint.iataCode,
                    destinationCode = flight.destination.iataCode
                )
            },
        )
    }
    SearchBar(
        modifier = modifier.fillMaxWidth(), colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface,
        ), inputField = {
            SearchBarDefaults.InputField(
                colors = SearchBarDefaults.inputFieldColors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                ),
                modifier = Modifier.fillMaxWidth(),
                query = searchString,
                onQueryChange = {
                    searchString = it
                    viewModel.saveSearchString(it)
                },
                onSearch = {
                    expanded = false
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text(text = stringResource(R.string.search_placeholder)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_search_24),
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    if (searchString != "") {
                        IconButton(
                            onClick = {
                                searchString = ""
                                viewModel.saveSearchString("")
                            }) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_close_24),
                                contentDescription = stringResource(R.string.close_search),
                            )
                        }
                    }
                }
            )
        }, expanded = true, onExpandedChange = { expanded = it }, content = {
            if (searchString.isNotBlank()) {
                AirportSearchResults(
                    airports = airports,
                    onAirportClick = {
                        viewModel.saveSearchString(it)
                        navigateToFlights(it)
                    },
                )
            } else {
                flightsLazyColumn.invoke()
            }

        })
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
    LazyVerticalGrid(columns = GridCells.Fixed(count = calculateColumns()), modifier = modifier) {
        items(airports) {
            AirportListItem(
                airport = it,
                onAirportClick = onAirportClick
            )
            HorizontalDivider()
        }
    }
}

@Composable
fun AirportListItem(
    modifier: Modifier = Modifier,
    airport: Airport,
    onAirportClick: (String) -> Unit
) {
    ListItem(
        modifier = modifier.clickable { onAirportClick(airport.iataCode) },
        leadingContent = { Text(text = airport.getFlag()) },
        headlineContent = {
            Text(
                text = airport.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            Text(
                text = "${airport.passengers} " + stringResource(R.string.passengers),
            )
        },
        trailingContent = {
            Text(
                text = airport.iataCode,
            )
        }

    )
}





