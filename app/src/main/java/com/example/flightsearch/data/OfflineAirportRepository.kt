package com.example.flightsearch.data

/**
 * Repository for the [AirportDao] methods
 */
class OfflineAirportRepository(
    private val airportDao: AirportDao,
) : AirportRepository {
    /**
     * Retrieves all [Airport]s whose name or iata_code contains the users text input
     */
    override fun getAirportsByText(searchString: String) =
        airportDao.getAirportsByText(searchString)

    /**
     * Retrieves the [Airport] from the airport table of the flight_search database whose iata_code
     * matches the given iata code
     */
    override fun getAirportByIataCode(iataCode: String) = airportDao.getAirportByIataCode(iataCode)

    /**
     * Retrieves all [Airport]s from the airport table of the flight_search database whose
     * iata_code does not match the given iata code
     */
    override fun getAllDestinationsFor(iataCode: String) =
        airportDao.getAllDestinationsFor(iataCode)
}
