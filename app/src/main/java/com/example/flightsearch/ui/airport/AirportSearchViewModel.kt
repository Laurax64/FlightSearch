package com.example.flightsearch.ui.airport

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.UserPreferencesRepository
import com.example.flightsearch.data.airport.Airport
import com.example.flightsearch.data.airport.AirportRepository
import com.example.flightsearch.data.favorite.FavoriteRepository
import com.example.flightsearch.ui.flight.Flight
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing the UI state related to airport search
 *
 * @property airportRepository The airport repository
 */
class AirportSearchViewModel(
    private val airportRepository: AirportRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    val favoriteRepository: FavoriteRepository,
) : ViewModel() {

    val uiState = combine(
        userPreferencesRepository.searchString,
        airportRepository.getAllAirports(),
        favoriteRepository.getFavorites()
    ) { searchString, airports, favorites ->
        Log.d("AirportSearchViewModel", "codes: ${airports.joinToString(", ") { it.iataCode }}")
        AirportSearchUiState(
            searchString = searchString,
            airports = airports.filter { // filter airports by search string
                it.iataCode.startsWith(searchString) ||
                        it.name.contains(searchString)
            }.sortedBy { it.name }.sortedBy { it.iataCode },
            favorites = favorites.map { favorite -> // map favorites to flights
                Flight(
                    startingPoint = airports.first { it.iataCode == favorite.departureCode },
                    destination = airports.first { it.iataCode == favorite.destinationCode },
                    isFavorite = true
                )
            },
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AirportSearchUiState()
    )

    fun saveSearchString(searchString: String) {
        viewModelScope.launch {
            userPreferencesRepository.saveSearchString(searchString)
        }
    }

    /**
     * Deletes a favorite flight from the database by its iata_code
     *
     * @param departureCode The iata_code of the departure airport
     * @param destinationCode The iata_code of the destination airport
     */
    fun deleteFavorite(departureCode: String, destinationCode: String) {
        viewModelScope.launch {
            favoriteRepository.deleteByIataCodes(
                departureCode = departureCode,
                destinationCode = destinationCode
            )
        }
    }
}

data class AirportSearchUiState(
    val searchString: String = "",
    val airports: List<Airport> = listOf(),
    val favorites: List<Flight> = listOf()
)
