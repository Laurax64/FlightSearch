package com.example.flightsearch.data.favorite

class OfflineFavoriteRepository(
    private val favoriteDao: FavoriteDao,
) : FavoriteRepository {
    /**
     * Inserts a flight into the favorite table of the flight_search database
     */
    override suspend fun insert(favorite: Favorite) = favoriteDao.insert(favorite)

    /**
     * Retrieves als flights from the favorite table of the flight_search database
     */
    override fun getFavorites() = favoriteDao.getFavorites()

    /**
     * Retrieves a favorite from the favorite table of the flight_search database by iata_code
     */
    override fun getFavorite(
        departureCode: String,
        destinationCode: String,
    ) = favoriteDao.getFavorite(departureCode, destinationCode)

    /**
     * Removes a flight from the favorite table of the flight_search database by iata_code.
     *
     * @param departureCode The iata_code of the departure airport
     * @param destinationCode The iata_code of the destination airport
     */
    override suspend fun deleteByIataCodes(departureCode: String, destinationCode: String) =
        favoriteDao.deleteByIataCodes(departureCode, destinationCode)
}