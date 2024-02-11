package com.example.flightsearch.ui.flight

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
 *
 */
class FlightsViewModel(
    private val airportRepository: AirportRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val favoriteRepository: FavoriteRepository,
): ViewModel() {

    fun getFavorites(): Flow<List<Favorite>> {
        return favoriteRepository.getFavorites()
    }

    fun addFavorite(favorite: Favorite) {
        viewModelScope.launch {
            favoriteRepository.insert(favorite)
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
    fun getIataCode(): Flow<String> {
        return userPreferencesRepository.searchString
    }

    /**
     * Returns the airport whose iata code matches the given iatacode
     */
    fun getAirportByIataCode(iataCode: String): Flow<Airport> {
        return airportRepository.getAirportByIataCode(iataCode)
    }

    /**
     * Retrieves all Airports from the airport table of the flight_search database whose
     * iata_code does not match the given iata code
     */
    fun getDestinationsAirports(iataCode: String): Flow<List<Airport>> {
        return airportRepository.getAllDestinationsFor(iataCode)
    }
}



