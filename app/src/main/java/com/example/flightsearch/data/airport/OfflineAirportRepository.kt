package com.example.flightsearch.data.airport

/**
 * Repository for the [AirportDao] methods
 */
class OfflineAirportRepository(
    private val airportDao: AirportDao,
) : AirportRepository {

    /**
     * Retrieves all [Airport]s from the airport table of the flight_search database
     */
    override fun getAllAirports() = airportDao.getAllAirports()

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