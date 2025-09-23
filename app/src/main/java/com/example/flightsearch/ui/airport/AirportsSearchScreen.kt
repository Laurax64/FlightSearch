package com.example.flightsearch.ui.airport

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch.R
import com.example.flightsearch.data.airport.Airport
import com.example.flightsearch.data.airport.getFlag
import com.example.flightsearch.ui.AppViewModelProvider
import com.example.flightsearch.ui.components.FlightsColumn
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
        FlightsColumn(
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
                })
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
            AirportCard(
                modifier = Modifier.padding(4.dp),
                airport = it,
                onAirportClick = onAirportClick
            )
            /*
            ListItem(
                leadingContent = { Text(text = it.getFlag(), fontSize = 40.sp) },
                headlineContent = { Text(it.name) },
                supportingContent = { Text("${it.passengers} " + stringResource(R.string.passengers)) },
                trailingContent = { Text(text = it.iataCode) },
                modifier = Modifier.clickable { onAirportClick(it.iataCode) }
            )

             */
        }
    }
}

@Composable
fun AirportCard(
    modifier: Modifier = Modifier,
    airport: Airport,
    onAirportClick: (String) -> Unit
) {
    OutlinedCard(
        modifier = modifier.clickable { onAirportClick(airport.iataCode) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = airport.getFlag(),
                fontSize = 40.sp,
                modifier = Modifier.padding(end = 16.dp)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = airport.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${airport.passengers} " + stringResource(R.string.passengers),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = airport.iataCode,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}




