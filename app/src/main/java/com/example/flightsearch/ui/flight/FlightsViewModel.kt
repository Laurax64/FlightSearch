package com.example.flightsearch.ui.flight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.AirportRepository
import com.example.flightsearch.data.UserPreferencesRepository
import com.example.flightsearch.ui.favorite.FavoriteFlightsScreen
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


/**
 * A [ViewModel] instance for the [FavoriteFlightsScreen]
 *
 * @property flightsUiState The flight's ui state
 *
 */
class FlightsViewModel(
    airportRepository: AirportRepository,
    userPreferencesRepository: UserPreferencesRepository): ViewModel() {
    val flightsUiState: StateFlow<FlightsUiState> =
        userPreferencesRepository.searchString.map { iataCode ->
            FlightsUiState(
                airportRepository.getAirportByIataCode(iataCode),
                airportRepository.getAllDestinationsFor(iataCode)
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = FlightsUiState(
                Airport(-1,"","",-1), listOf())
        )
}

/**
 * A ui state for flights
 *
 * @property airport The departure [Airport]
 * @property destinationAirports The destination [Airport]s
 */
data class FlightsUiState(val airport: Airport, val destinationAirports: List<Airport>)
