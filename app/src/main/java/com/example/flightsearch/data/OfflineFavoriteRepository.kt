package com.example.flightsearch.data

class OfflineFavoriteRepository(private val favoriteDao: FavoriteDao): FavoriteRepository {
    /**
     * Inserts a flight into the favorite table of the flight_search database
     */
    override suspend fun insert(favorite: Favorite) = favoriteDao.insert(favorite)

    /**
     * Removes a flight from the favorite table of the flight_search database
     */
    override suspend fun delete(favorite: Favorite) = favoriteDao.delete(favorite)

    /**
     * Retrieves als flights from the favorite table of the flight_search database
     */
    override fun getFavorites() = favoriteDao.getFavorites()

    /**
     * Retrieves a favorite from the favorite table of the flight_search database by iata_code
     */
    override fun getFavorite(departureCode: String, destinationCode: String) =
        favoriteDao.getFavorite(departureCode, destinationCode)
}