package com.example.flightsearch.ui.flight

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch.R
import com.example.flightsearch.data.airport.Airport
import com.example.flightsearch.data.airport.getFlag
import com.example.flightsearch.ui.AppViewModelProvider
import com.example.flightsearch.ui.components.FlightsColumn
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
    modifier: Modifier = Modifier,
    viewModel: FlightsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateBack: () -> Unit,
) {
    val uiState by viewModel.flightsUiState.collectAsStateWithLifecycle()

    FlightsScreen(
        modifier = modifier,
        startingPoint = uiState.startingPoint,
        flights = uiState.flights,
        onHeartClick = viewModel::toggleIsFavorite,
        navigateBack = navigateBack,
    )
}

@Composable
private fun FlightsScreen(
    modifier: Modifier = Modifier,
    startingPoint: Airport,
    flights: List<Flight>,
    onHeartClick: (Flight) -> Unit,
    navigateBack: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            FlightsTopBar(
                onBackClick = navigateBack,
                startingPoint = startingPoint
            )
        }
    ) { paddingValues ->
        FlightsColumn(
            modifier = Modifier.padding(paddingValues),
            flights = flights,
            onHeartClick = onHeartClick
        )
    }
}

/**
 * The flight screen's top bar
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun FlightsTopBar(
    modifier: Modifier = Modifier,
    startingPoint: Airport,
    onBackClick: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(id = R.string.flights_from, startingPoint.iataCode),
            )
        },
        actions = {
            Text(
                modifier = Modifier.padding(16.dp),
                text = startingPoint.getFlag(), fontSize = 32.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                    contentDescription = stringResource(id = R.string.go_back)
                )
            }
        },
    )
}

@Preview
@Composable
fun FlightScreenPreview() {
    FlightsScreen(
        startingPoint = Airport(iataCode = "SEA", name = "Seattle"),
        flights = listOf(
            Flight(
                startingPoint = Airport(iataCode = "SEA", name = "Seattle"),
                destination = Airport(iataCode = "LAX", name = "Los Angeles"),
                isFavorite = true
            ),
            Flight(
                startingPoint = Airport(iataCode = "SEA", name = "Seattle"),
                destination = Airport(iataCode = "SFO", name = "San Francisco"),
                isFavorite = false
            ),
            Flight(
                startingPoint = Airport(iataCode = "SEA", name = "Seattle"),
                destination = Airport(iataCode = "ORD", name = "Chicago"),
                isFavorite = true
            ),
            Flight(
                startingPoint = Airport(iataCode = "SEA", name = "Seattle"),
                destination = Airport(iataCode = "DFW", name = "Dallas"),
                isFavorite = false
            ),
        ),
        onHeartClick = {},
        navigateBack = {}
    )
}