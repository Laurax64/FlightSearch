package com.example.flightsearch.data.favorite

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Interface which is implemented by room at compile time that provides methods for
 * the favorite table of the flight_search database
 */
@Dao
interface FavoriteDao {
    /**
     * Inserts a route into the favorite table of the flight_search database
     */
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insert(favorite: Favorite)

    /**
     * Removes a route from the favorite table of the flight_search database
     */
    @Delete
    suspend fun delete(favorite: Favorite)

    /**
     * Removes a flight from the favorite table of the flight_search database by iata_code.
     *
     * @param departureCode The iata_code of the departure airport
     * @param destinationCode The iata_code of the destination airport
     */
    @Query(
        value = """
            DELETE FROM favorite
            WHERE departure_code = :departureCode AND destination_code = :destinationCode
            """
    )
    suspend fun deleteByIataCodes(departureCode: String, destinationCode: String)

    /**
     * Retrieves all favorites from the favorite table of the flight_search database
     */
    @Query("SELECT * FROM favorite")
    fun getFavorites(): Flow<MutableList<Favorite>>

    /**
     * Retrieves a favorite from the favorite table of the flight_search database by iata_code
     */
    @Query(
        """
        SELECT * FROM favorite
        WHERE departure_code = :departureCode AND destination_code = :destinationCode
        """,
    )
    fun getFavorite(
        departureCode: String,
        destinationCode: String,
    ): Flow<Favorite>
}