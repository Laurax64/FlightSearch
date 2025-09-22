package com.example.flightsearch.data

import kotlinx.coroutines.flow.Flow

/**
 * Interface for a repository for the [AirportDao] methods
 */
interface AirportRepository {
    /**
     * Retrieves all [Airport]s whose name or iata_code contains the users text input
     */
    fun getAirportsByText(searchString: String): Flow<List<Airport>>

    /**
     * Retrieves the [Airport] from the airport table of the flight_search database whose iata_code
     * matches the given iata code
     */
    fun getAirportByIataCode(iataCode: String): Flow<Airport>

    /**
     * Retrieves all [Airport]s from the airport table of the flight_search database whose
     * iata_code does not match the given iata code
     */
    fun getAllDestinationsFor(iataCode: String): Flow<List<Airport>>
}
