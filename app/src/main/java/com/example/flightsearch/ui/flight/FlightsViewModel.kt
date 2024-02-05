package com.example.flightsearch.ui.flight

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.AirportRepository
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.data.FavoriteRepository
import com.example.flightsearch.ui.favorite.FavoriteFlightsScreen
import com.example.flightsearch.ui.flight.FlightsDestination.departureAirport
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


/**
 * A [ViewModel] instance for the [FavoriteFlightsScreen]
 *
 * @property departureAirport The departure airport
 *
 */
class FlightsViewModel(
    savedStateHandle: SavedStateHandle,
    airportRepository: AirportRepository,
    val favoriteRepository: FavoriteRepository
): ViewModel() {
    private val searchString: String = checkNotNull(
        savedStateHandle[departureAirport])

    var flightsUiState: StateFlow<FlightsUiState> =
        airportRepository.getAllDestinationsFor(searchString).map {
            FlightsUiState(
                searchString, destinationAirports = it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FlightsUiState(destinationAirports = listOf())
        )

    fun deleteFlightFromFavorites(favorite: Favorite) {
        viewModelScope.launch {
            favoriteRepository.delete(favorite)
        }
    }



}

/**
 * A ui state for flights
 */
data class FlightsUiState(
    val departureIataCode: String = "",
    val destinationAirports: List<Airport>,
)



