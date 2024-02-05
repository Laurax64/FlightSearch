package com.example.flightsearch.ui.airport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.AirportRepository
import com.example.flightsearch.data.UserPreferencesRepository
import com.example.flightsearch.ui.SearchBarUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing the UI state related to airport search
 *
 * @property airportRepository The airport repository
 */
class AirportSearchViewModel(
    private val airportRepository: AirportRepository,
    val userPreferencesRepository: UserPreferencesRepository): ViewModel() {

    val searchBarUiState: StateFlow<SearchBarUiState> =
        userPreferencesRepository.searchString.map { searchString ->
            SearchBarUiState(searchString)}
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SearchBarUiState()
            )

    val aiportSearchUiState: StateFlow<AirportSearchUiState> =
        airportRepository.getAirportsByText(searchBarUiState.value.searchString).map {
            AirportSearchUiState(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = AirportSearchUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5000L
    }

    /**
    * Stores the search string
    */
    fun storeSearchString(searchString: String = "") {
        viewModelScope.launch {
            userPreferencesRepository.saveSearchString(searchString)
        }
    }

    /**
     * Retrieves all [Airport]s whose name or iata_code contains the users text input
     */
    fun getAirportsByText(searchString: String) = airportRepository.getAirportsByText(searchString)


}

/**
 * UI state for the airport search
 *
 * @property airports The current search result
 */
data class AirportSearchUiState(var airports: List<Airport> = listOf())
