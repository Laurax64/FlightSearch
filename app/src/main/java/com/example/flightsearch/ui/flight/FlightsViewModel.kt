package com.example.flightsearch.ui.flight

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.AirportRepository
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.data.FavoriteRepository
import com.example.flightsearch.ui.favorite.FavoriteFlightsScreen
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
    private val departureAirport: Airport = checkNotNull(
        savedStateHandle[
            FlightsDestination.departureAirport]
    )
    var flightsUiState: StateFlow<FlightsUiState> =
        airportRepository.getAllDestinationsFor(departureAirport.iataCode).map {
            FlightsUiState(destinationAirports = it)
        }.stateIn(
            scope = viewModelScope,
            // Flow is set to emits value for when app is on the foreground
            // 5 seconds stop delay is added to ensure it flows continuously
            // for cases such as configuration change
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
 *
 * @property airport The departure [Airport]
 * @property destinationAirports The destination [Airport]s
 */
data class FlightsUiState(
    val airport: Airport = Airport(-1,"","" ,-1),
    val destinationAirports: List<Airport>,
)



