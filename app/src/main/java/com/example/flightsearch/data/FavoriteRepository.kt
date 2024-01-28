package com.example.flightsearch.data

/**
 * Interface for a repository for the [FavoriteDao] methods
 */
interface FavoriteRepository {
    /**
     * Inserts a route into the favorite table of the flight_search database
     */
    suspend fun insert(favorite: Favorite)

    /**
     * Removes a route from the favorite table of the flight_search database
     */
    suspend fun delete(favorite: Favorite)

    /**
     * Retrieves als flights from the favorite table of the flight_search database
     */
    fun getFavorites(): MutableList<Favorite>

    /**
     * Retrieves a favorite from the favorite table of the flight_search database by iata_code
     */
    fun getFavorite(departureCode: String, destinationCode: String): Favorite
}