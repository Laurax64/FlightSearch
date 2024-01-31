package com.example.flightsearch.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.flightsearch.data.UserPreferencesRepository
import kotlinx.coroutines.coroutineScope

/**
 * A ui state for the search bar that is shared across multiple screens
 */
class SearchBarUiState(val userPreferencesRepository: UserPreferencesRepository) {

    /**
     * Gets the current search string
     */
    @Composable
    fun getSearchString(): String {
       return userPreferencesRepository.searchString.collectAsState("").value
    }

    /**
     * Saves the current search string
     */
    suspend fun saveSearchString(searchString: String) {
        coroutineScope {
            userPreferencesRepository.saveSearchString(searchString)
        }
    }

}
