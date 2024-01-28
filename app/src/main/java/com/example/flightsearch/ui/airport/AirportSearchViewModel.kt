package com.example.flightsearch.ui.airport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.AirportRepository
import com.example.flightsearch.data.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing the UI state related to airport search
 *
 * This ViewModel interacts with the [UserPreferencesRepository] to manage the search string
 *
 * @property userPreferencesRepository The user preference repository
 * @property airportRepository The airport repository
 * @property airportSearchUiState The airport search ui state
 */
class AirportSearchViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val airportRepository: AirportRepository): ViewModel() {
    val airportSearchUiState: StateFlow<AirportSearchUiState> =
        userPreferencesRepository.searchString.map { searchString ->
            AirportSearchUiState(
                searchString, airportRepository.getAirportsByText(searchString))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = AirportSearchUiState("", listOf())
        )

    /**
     * Stores the search string
     */
    fun storeSearchString(searchString: String) {
        viewModelScope.launch { userPreferencesRepository.storeSearchString(searchString) }
    }
}

/**
 * UI state for the airport search
 *
 * @property searchString The current search string
 *
 */
data class AirportSearchUiState(
    var searchString: String,
    var airports: List<Airport>)
