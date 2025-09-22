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
     * Retrieves all [Airport]s whose name or iata_code starts with the users text input
     */
    @Query(
        """
        SELECT * FROM airport
        WHERE name LIKE :searchString || '%' OR iata_code LIKE :searchString || '%'
        ORDER BY passengers DESC  
        """
    )
    fun getAirportsByText(searchString: String): Flow<List<Airport>>

    /**
     *
     * Retrieves the [Airport] from the airport table of the flight_search database whose iata_code
     * matches the given iata code
     */
    @Query(
        """
        SELECT * FROM airport
        WHERE iata_code = :iataCode
        """
    )
    fun getAirportByIataCode(iataCode: String): Flow<Airport>

    /**
     *
     * Retrieves all [Airport]s from the airport table of the flight_search database whose
     * iata_code does not match the given iata code
     */
    @Query(
        """
        SELECT * FROM airport
        WHERE iata_code != :iataCode
        """
    )
    fun getAllDestinationsFor(iataCode: String): Flow<List<Airport>>
}