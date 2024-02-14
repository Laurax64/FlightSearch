package com.example.flightsearch.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.data.FavoriteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * A [ViewModel] instance for the [FavoriteFlightsScreen]
 *
 * @property favoriteRepository The [FavoriteRepository]
 *
 */
class FavoriteFlightsViewModel(
    private val favoriteRepository: FavoriteRepository
): ViewModel() {

    val favoriteUiState: StateFlow<FavoriteUiState> =
        favoriteRepository.getFavorites().map { FavoriteUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = FavoriteUiState()
            )
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    /**
     * Deletes a [Favorite] from the favorite table of the flight_search database
     */
    fun delete(favorite: Favorite) {
        viewModelScope.launch {
            favoriteRepository.delete(favorite)
        }
    }
}

/**
 * Ui State for the favorite screen
 */
data class FavoriteUiState(val favorites: MutableList<Favorite> = mutableListOf())