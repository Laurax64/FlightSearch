package com.example.flightsearch.ui.flight

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.UserPreferencesRepository
import com.example.flightsearch.data.airport.Airport
import com.example.flightsearch.data.airport.AirportRepository
import com.example.flightsearch.data.favorite.Favorite
import com.example.flightsearch.data.favorite.FavoriteRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FlightsViewModel(
    airportRepository: AirportRepository,
    userPreferencesRepository: UserPreferencesRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    val flightsUiState = combine(
        userPreferencesRepository.searchString,
        favoriteRepository.getFavorites(),
        airportRepository.getAllAirports(),
    ) { iataCode, favorites, airports ->
        val destinations = airports.filter { it.iataCode != iataCode }
        val startingPoint = airports.first { it.iataCode == iataCode }
        FlightsUiState(
            startingPoint = startingPoint,
            flights =
                destinations.map { dest ->
                    createFlight(
                        favorites = favorites,
                        startingPoint = startingPoint,
                        destination = dest
                    )
                }
        )
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5_000),
        initialValue = FlightsUiState()
    )

    /**
     * Creates a flight object with the given parameters.
     * If the flight is a favorite, the isFavorite flag will be set to true.
     *
     * @param favorites The list of favorite flights
     * @param startingPoint The starting point of the flight
     * @param destination The destination of the flight
     */
    @VisibleForTesting
    fun createFlight(
        favorites: List<Favorite>,
        startingPoint: Airport,
        destination: Airport
    ) = Flight(
        startingPoint = startingPoint,
        destination = destination,
        isFavorite = favorites.any {
            it.departureCode == startingPoint.iataCode && it.destinationCode == destination.iataCode
        }
    )

    /**
     * Toggles the favorite status of a flight. If the flight is already a favorite, it will be
     * removed from the favorites list. If the flight is not a favorite, it will be added to the
     * favorites list.
     *
     * @param flight The flight to toggle the favorite status of
     */
    fun toggleIsFavorite(flight: Flight) {
        viewModelScope.launch {
            val departureCode = flight.startingPoint.iataCode
            val destinationCode = flight.destination.iataCode
            if (flight.isFavorite) {
                favoriteRepository.deleteByIataCodes(
                    departureCode = departureCode,
                    destinationCode = destinationCode
                )
            } else {
                favoriteRepository.insert(
                    Favorite(
                        departureCode = departureCode,
                        destinationCode = destinationCode
                    )
                )
            }
        }
    }

}

data class FlightsUiState(
    val flights: List<Flight> = listOf(),
    val startingPoint: Airport = Airport(),
)

data class Flight(
    val startingPoint: Airport,
    val destination: Airport,
    val isFavorite: Boolean,
)

