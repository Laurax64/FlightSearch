package com.example.flightsearch.ui.flight

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.AirportRepository
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.data.FavoriteRepository
import com.example.flightsearch.data.UserPreferencesRepository
import com.example.flightsearch.ui.favorite.FavoriteFlightsScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * A [ViewModel] instance for the [FavoriteFlightsScreen]
 */
class FlightsViewModel(
    private val airportRepository: AirportRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val favoriteRepository: FavoriteRepository,
) : ViewModel() {
    var favoriteUiState by mutableStateOf(FavoritesUiState())
        private set

    /**
     * Updates the [favoriteUiState] with the value provided in the argument
     */
    fun updateFavoriteUiState(
        favorites: MutableList<Favorite>,
        currentFavorite: Favorite = Favorite(),
    ) {
        favoriteUiState = FavoritesUiState(favorites, currentFavorite)
    }

    /**
     * Retrieves all flights from the favorite table of the flight_search database
     */
    fun getFavorites(): Flow<MutableList<Favorite>> = favoriteRepository.getFavorites()

    /**
     * Inserts a flight into the favorite table of the flight_search database
     */
    fun addFavorite() {
        viewModelScope.launch {
            favoriteRepository.insert(favoriteUiState.currentFavorite)
        }
    }

    fun deleteFavorite(favorite: Favorite) {
        viewModelScope.launch {
            favoriteRepository.delete(favorite)
        }
    }

    /**
     * Returns the current search string which has been set to the iata code
     * of the recently selected airport
     */
    fun getIataCode(): Flow<String> = userPreferencesRepository.searchString

    /**
     * Returns the airport whose iata code matches the given iatacode
     */
    fun getAirportByIataCode(iataCode: String): Flow<Airport> =
        airportRepository.getAirportByIataCode(iataCode)

    /**
     * Retrieves all Airports from the airport table of the flight_search database whose
     * iata_code does not match the given iata code
     */
    fun getDestinationsAirports(iataCode: String): Flow<List<Airport>> =
        airportRepository.getAllDestinationsFor(iataCode)
}

/**
 * Represents the UI state of the user's favorites
 *
 * @property favorites The list of favorite items
 */
data class FavoritesUiState(
    val favorites: MutableList<Favorite> = mutableListOf(),
    val currentFavorite: Favorite = Favorite(),
)
