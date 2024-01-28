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
    userPreferencesRepository: UserPreferencesRepository,
    private val favoriteRepository: FavoriteRepository): ViewModel() {

    val favoriteFlightsUiState: StateFlow<FavoriteFlightsUiState> =
        userPreferencesRepository.searchString.map { searchString ->
                FavoriteFlightsUiState(searchString, favoriteRepository.getFavorites())
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = FavoriteFlightsUiState("", listOf())
        )

    /**
     * Deletes a [Favorite] from the favorite's list
     */
    fun delete(favorite: Favorite) {
        viewModelScope.launch { favoriteRepository.delete(favorite) }
    }
}

/**
 * Ui state for the user's favorite flights
 *
 * @property searchString The user's search text
 * @property favoriteList The user's [Favorite]s
 */
data class FavoriteFlightsUiState(
    var searchString: String,
    var favoriteList: List<Favorite>
)

