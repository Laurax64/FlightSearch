package com.example.flightsearch.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.flightsearch.R
import com.example.flightsearch.data.airport.Airport
import com.example.flightsearch.ui.flight.Flight


/**
 * Calculates the number of columns to display in a [LazyVerticalGrid] based on the device's
 * window width size class.
 *
 * @return The number of columns to display
 */
@Composable
fun calculateColumns(): Int =
    when (currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.EXPANDED -> 2
        else -> 1
    }

/**
 * A lazy column of flight cards
 *
 * @param modifier Modifier to apply to this composable
 * @param flights The list of flights to display
 * @param onHeartClick A function to call when the heart icon is clicked
 */
@Composable
fun FlightsColumn(
    modifier: Modifier = Modifier,
    flights: List<Flight>,
    onHeartClick: (Flight) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = calculateColumns()),
        modifier = modifier
    ) {
        items(flights) { flight ->
            FlightCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                flight = flight,
                onHeartClick = { onHeartClick(flight) }
            )
        }
    }
}

/**
 * A card containing a flight and a heart icon to toggle the favorite status of the flight.
 *
 * @param modifier Modifier to apply to this composable
 * @param flight The flight to display
 * @param onHeartClick A function to call when the heart icon is clicked
 */
@Composable
private fun FlightCard(
    modifier: Modifier = Modifier,
    flight: Flight,
    onHeartClick: (String) -> Unit,
) {
    val startingPoint = flight.startingPoint
    val destination = flight.destination
    val isFavorite = flight.isFavorite

    OutlinedCard(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                AirportColumn(
                    airport = startingPoint,
                    title = stringResource(R.string.starting_point)
                )
                AirportColumn(
                    airport = destination,
                    title = stringResource(R.string.destination)
                )
            }
            FilledTonalIconToggleButton(
                checked = isFavorite,
                colors = IconButtonDefaults.iconToggleButtonColors(
                    checkedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    checkedContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                onCheckedChange = {
                    onHeartClick(destination.iataCode)
                },
            ) {
                if (isFavorite) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_favorite_24),
                        contentDescription = stringResource(R.string.remove_from_favorites),
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.baseline_favorite_border_24),
                        contentDescription = stringResource(R.string.add_to_favorites),
                    )
                }
            }
        }

    }
}

@Composable
private fun AirportColumn(airport: Airport, title: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        Text(
            text = airport.iataCode + " " + airport.name,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}