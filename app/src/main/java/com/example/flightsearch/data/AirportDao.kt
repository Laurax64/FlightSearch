package com.example.flightsearch.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Interface which is implemented by room at compile time that provides methods for
 * the airport table of the flight_search database
 */
@Dao
interface AirportDao {
    /**
     * Retrieves all [Airport]s whose name or iata_code contains the users text input
     */
    @Query(
        """
        SELECT * FROM airport
        WHERE name LIKE :textInput OR iata_code Like :textInput
        ORDER BY passengers DESC  
        """
    )
    fun getAll(textInput: String): Flow<List<Airport>>

    /**
     * Retrieves an [Airport] from the airport table of the flight_search database whose iata_code
     * matches the given iata code
     */
    @Query(
        """
        SELECT * FROM airport
        WHERE iata_code = :iataCode
        """
    )
    fun getByIataCode(iataCode: String): Flow<Airport>
}