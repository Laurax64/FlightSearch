package com.example.flightsearch.ui.airport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.AirportRepository
import com.example.flightsearch.data.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing the UI state related to airport search
 *
 * @property airportRepository The airport repository
 */
class AirportSearchViewModel(
    private val airportRepository: AirportRepository,
    private val userPreferencesRepository: UserPreferencesRepository): ViewModel() {

    /**
    * Stores the search string in the [UserPreferencesRepository]
    */
    fun saveSearchString(searchString: String) {
        viewModelScope.launch {
            userPreferencesRepository.saveSearchString(searchString)
        }
    }

    /**
    * Returns the search string [Flow]
    */
    fun getSearchString(): Flow<String> {
        return userPreferencesRepository.searchString
    }

    /**
     * Returns the list of [Airport]'s that contain the search String
     */

    fun getAirportsByText(searchString: String): Flow<List<Airport>> {
        return airportRepository.getAirportsByText(searchString)
    }
}
