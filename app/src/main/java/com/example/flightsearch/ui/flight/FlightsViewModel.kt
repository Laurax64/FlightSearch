package com.example.flightsearch.ui.flight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.AirportRepository
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.data.FavoriteRepository
import com.example.flightsearch.data.UserPreferencesRepository
import com.example.flightsearch.ui.favorite.FavoriteFlightsScreen
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


/**
 * A [ViewModel] instance for the [FavoriteFlightsScreen]
 *
 * @property flightsUiState The flight's ui state
 *
 */
class FlightsViewModel(
    airportRepository: AirportRepository,
    userPreferencesRepository: UserPreferencesRepository,
    private val favoriteRepository: FavoriteRepository
): ViewModel() {
    val flightsUiState: StateFlow<FlightsUiState> =
        userPreferencesRepository.searchString.map { iataCode ->
            FlightsUiState(
                airportRepository.getAirportByIataCode(iataCode),
                airportRepository.getAllDestinationsFor(iataCode),
                favoriteRepository.getFavorites()
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = FlightsUiState(
                Airport(-1, "", "", -1), listOf(), mutableListOf()
            )
        )


    /**
     * Deletes the given [Favorite] from the favorites list if it is contained in the
     * favorite list or adds it to the favorites list
     */
    fun changeFavorite() {
        val currentFlight = flightsUiState.value.currentFlight
        if(flightsUiState.value.favoriteList.contains(currentFlight)){
            viewModelScope.launch { favoriteRepository.delete(currentFlight) }
        }
        else if(currentFlight.id != -1){
            viewModelScope.launch { favoriteRepository.insert(currentFlight) }
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
    val airport: Airport,
    val destinationAirports: List<Airport>,
    var favoriteList: MutableList<Favorite>,
    val currentFlight: Favorite = Favorite()
)



