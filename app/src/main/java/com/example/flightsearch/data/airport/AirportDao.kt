package com.example.flightsearch.data.airport

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
     * Retrieves all [Airport]s from the airport table of the flight_search database
     */
    @Query("SELECT * FROM airport")
    fun getAllAirports(): Flow<List<Airport>>

    /**
     * Retrieves the [Airport] from the airport table of the flight_search database with the
     * given [iataCode]
     */
    @Query(
        """
        SELECT * FROM airport
        WHERE iata_code = :iataCode
        """,
    )
    fun getAirportByIataCode(iataCode: String): Flow<Airport>

    /**
     * Retrieves all [Airport]s from the airport table of the flight_search database that
     * do not have the given [iataCode] as their [Airport.iataCode]
     */
    @Query(
        """
        SELECT * FROM airport
        WHERE iata_code != :iataCode
        """,
    )
    fun getAllDestinationsFor(iataCode: String): Flow<List<Airport>>
}