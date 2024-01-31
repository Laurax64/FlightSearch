package com.example.flightsearch.ui.airport

import androidx.lifecycle.SavedStateHandle
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
 *
 * @property airportRepository The airport repository
 */
class AirportSearchViewModel(
    savedStateHandle: SavedStateHandle,
    private val airportRepository: AirportRepository,
    val userPreferencesRepository: UserPreferencesRepository): ViewModel() {

    private val searchString: String = checkNotNull(savedStateHandle[
        AirportSearchDestination.searchStringArg])
    val airportSearchUiState: StateFlow<AirportSearchUiState> =
        airportRepository.getAirportsByText(searchString).map { airports ->
            AirportSearchUiState(airports)
        }.stateIn(
            scope = viewModelScope,
            // Flow is set to emits value for when app is on the foreground
            // 5 seconds stop delay is added to ensure it flows continuously
            // for cases such as configuration change
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AirportSearchUiState()
        )


    /**
     * Writes the users text input to the DataStore
     */
    fun saveSearchString(searchString: String) {
        viewModelScope.launch {
            userPreferencesRepository.saveSearchString(searchString)
        }
    }
}

/**
 * UI state for the airport search
 *
 * @property airports The current search result
 */
data class AirportSearchUiState(var airports: List<Airport> = listOf())
