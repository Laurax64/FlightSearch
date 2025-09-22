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
import androidx.compose.runtime.saveable.rememberSaveable
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
@Composable
fun FlightsScreen(
    flightsViewModel: FlightsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateBack: () -> Unit,
) {
    val iataCode =
        flightsViewModel
            .getIataCode()
            .collectAsState("")
            .value
    val favorites: MutableList<Favorite> =
        flightsViewModel
            .getFavorites()
            .collectAsState(mutableListOf())
            .value

    // Since updateFavoriteUiState is not a composable function it is not re-executed
    // when [favorites] changes
    flightsViewModel.updateFavoriteUiState(favorites)

    var isFavorite by remember { mutableStateOf(true) }

    if (iataCode != "") {
        val departureAirport =
            flightsViewModel
                .getAirportByIataCode(iataCode)
                .collectAsState(Airport())
                .value
        val destinationAirports =
            flightsViewModel
                .getDestinationsAirports(iataCode)
                .collectAsState(listOf())
                .value

        Scaffold(topBar = {
            FlightsTopBar(
                iataCode = iataCode,
                onBackClick = navigateBack,
            )
        }) {
            ShowFlights(
                modifier = Modifier.padding(it),
                departure = departureAirport,
                destinations = destinationAirports,
                onStarClick = { destCode: String ->

                    isFavorite =
                        flightsViewModel.favoriteUiState.favorites.any {
                            it.destinationCode == destCode &&
                                    it.departureCode == departureAirport.iataCode
                        }

                    if (!isFavorite) {
                        flightsViewModel.updateFavoriteUiState(
                            favorites,
                            Favorite(0, destCode, departureAirport.iataCode)
                        )
                        flightsViewModel.addFavorite()
                    }

                    flightsViewModel.favoriteUiState.favorites.forEach {
                        if (it.destinationCode == destCode &&
                            it.departureCode == departureAirport.iataCode
                        ) {
                            flightsViewModel.deleteFavorite(it)
                        }
                    }

                    flightsViewModel.updateFavoriteUiState(favorites)
                },
                { destCode: String ->
                    favorites.any {
                        it.destinationCode == destCode &&
                                it.departureCode == departureAirport.iataCode
                    }
                },
            )
        }
    }
}

/**
 * Displays the flight screen's top bar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightsTopBar(
    modifier: Modifier = Modifier,
    iataCode: String,
    onBackClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = "Flights from $iataCode",
                style = MaterialTheme.typography.titleLarge,
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, "Arrow back")
            }
        },
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
    onStarClick: (String) -> Unit,
    filledHeart: (String) -> Boolean,
) {
    LazyColumn(modifier.fillMaxWidth()) {
        items(destinations) {
            FlightCard(
                Modifier.fillMaxWidth(),
                departure,
                it,
                onStarClick,
                filledHeart(it.iataCode),
            )
        }
    }
}

/**
 * Displays a card containing a route between the given [Airport]s and a button
 * that lets the user add or remove the route from the favorite routes
 */
@Composable
fun FlightCard(
    modifier: Modifier = Modifier,
    departure: Airport,
    arrival: Airport,
    onHeartClick: (String) -> Unit,
    isFavorite: Boolean,
) {
    var filledHeart by rememberSaveable { mutableStateOf(isFavorite) }
    Card(
        modifier = modifier.padding(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier =
                    modifier
                        .padding(8.dp)
                        .weight(1f),
            ) {
                Text(text = "Depart", style = MaterialTheme.typography.labelMedium)
                Text(text = departure.iataCode)
                Text(text = departure.name)
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier =
                    modifier
                        .padding(8.dp)
                        .weight(1f),
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
                modifier =
                    modifier
                        .padding(8.dp)
                        .weight(0.5f),
            ) {
                if (filledHeart) {
                    Icon(
                        Icons.Default.Favorite,
                        "remove from favorites",
                    )
                } else {
                    Icon(
                        Icons.Default.FavoriteBorder,
                        "add to favorites",
                    )
                }
            }
        }
    }
}
