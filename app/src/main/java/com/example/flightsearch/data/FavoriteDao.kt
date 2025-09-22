package com.example.flightsearch.data

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
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: Favorite)

    /**
     * Removes a route from the favorite table of the flight_search database
     */
    @Delete
    suspend fun delete(favorite: Favorite)

    /**
     * Retrieves all favorites from the favorite table of the flight_search database
     */
    @Query("SELECT * FROM favorite")
    fun getFavorites(): Flow<MutableList<Favorite>>

    /**
     * Retrieves a favorite from the favorite table of the flight_search database by iata_code
     */
    @Query("""
        SELECT * FROM favorite
        WHERE departure_code = :departureCode AND destination_code = :destinationCode
        """)
    fun getFavorite(departureCode: String, destinationCode: String): Flow<Favorite>
}