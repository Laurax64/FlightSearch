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
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 * Interface which is implemented by room at compile time that provides methods for
 * the favorite table of the flight_search database
 */
@Dao
interface FavoriteDao {
    /**
     * Inserts a route into the favorite table of the flight_search database
     */
    @Insert
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
    fun getFavorites(): MutableList<Favorite>

    /**
     * Retrieves a favorite from the favorite table of the flight_search database by iata_code
     */
    @Query("""
        SELECT * FROM favorite
        WHERE departure_code = :departureCode AND destination_code = :destinationCode
        """)
    fun getFavorite(departureCode: String, destinationCode: String): Favorite
}