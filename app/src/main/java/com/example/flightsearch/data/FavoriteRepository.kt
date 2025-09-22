package com.example.flightsearch.data

import kotlinx.coroutines.flow.Flow

/**
 * Interface for a repository for the [FavoriteDao] methods
 */
interface FavoriteRepository {
    /**
     * Inserts a flight into the favorite table of the flight_search database
     */
    suspend fun insert(favorite: Favorite)

    /**
     * Removes a flight from the favorite table of the flight_search database
     */
    suspend fun delete(favorite: Favorite)

    /**
     * Retrieves all flights from the favorite table of the flight_search database
     */
    fun getFavorites(): Flow<MutableList<Favorite>>

    /**
     * Retrieves a favorite from the favorite table of the flight_search database by iata_code
     */
    fun getFavorite(
        departureCode: String,
        destinationCode: String,
    ): Flow<Favorite>
}
