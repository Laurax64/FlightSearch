/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
}