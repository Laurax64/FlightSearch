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
package com.example.flightsearch

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.flightsearch.data.AirportRepository
import com.example.flightsearch.data.AppDatabase
import com.example.flightsearch.data.FavoriteRepository
import com.example.flightsearch.data.UserPreferencesRepository

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "search_string_preferences"
)

/**
 * Custom app entry point for manual dependency injection
 *
 * @property userPreferencesRepository The search string repository
 * @property airportRepository The airport repository
 * @property favoriteRepository The favorite repository
 */
class FlightSearchApplication: Application() {
    lateinit var userPreferencesRepository: UserPreferencesRepository
    lateinit var airportRepository: AirportRepository
    lateinit var favoriteRepository: FavoriteRepository
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    override fun onCreate() {
        super.onCreate()
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }
}