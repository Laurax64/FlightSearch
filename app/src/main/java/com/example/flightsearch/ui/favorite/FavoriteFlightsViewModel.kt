package com.example.flightsearch.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.data.FavoriteRepository
import kotlinx.coroutines.flow.Flow
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

    /**
     * Retrieves als flights from the favorite table of the flight_search database
     */
    fun getFavorites(): Flow<List<Favorite>> {
        return favoriteRepository.getFavorites()
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



