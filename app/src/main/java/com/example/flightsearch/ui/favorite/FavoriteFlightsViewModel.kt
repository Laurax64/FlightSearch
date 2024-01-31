package com.example.flightsearch.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.data.FavoriteRepository
import com.example.flightsearch.data.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * A [ViewModel] instance for the [FavoriteFlightsScreen]
 *
 * @property favoriteRepository The [FavoriteRepository]
 * @property favoriteFlightsUiState The [FavoriteFlightsUiState]
 *
 */
class FavoriteFlightsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val favoriteRepository: FavoriteRepository): ViewModel() {


    val favoriteFlightsUiState: StateFlow<FavoriteFlightsUiState> =
        favoriteRepository.getFavorites().map {
                FavoriteFlightsUiState(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = FavoriteFlightsUiState(listOf())
        )

    /**
     * Deletes a [Favorite] from the favorite's list
     */
    fun delete(favorite: Favorite) {
        viewModelScope.launch { favoriteRepository.delete(favorite) }
    }

    /**
     * * Writes the users text input to the DataStore
     */
    fun saveSearchString(searchString: String) {
        viewModelScope.launch {
            userPreferencesRepository.saveSearchString(searchString)
        }
    }
}

/**
 * Ui state for the user's favorite flights
 */
data class FavoriteFlightsUiState(
    var favoriteList: List<Favorite>
)

/**
 * Ui state for the user's search string
 */
data class SearchStringUiState(
    var searchString: String = ""
)
