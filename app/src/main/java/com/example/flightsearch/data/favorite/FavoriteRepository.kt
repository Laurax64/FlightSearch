package com.example.flightsearch.data.favorite

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
     * Removes a flight from the favorite table of the flight_search database by iata_code.
     *
     * @param departureCode The iata_code of the departure airport
     * @param destinationCode The iata_code of the destination airport
     */
    suspend fun deleteByIataCodes(departureCode: String, destinationCode: String)

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