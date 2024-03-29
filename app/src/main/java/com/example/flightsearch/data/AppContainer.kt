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

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val airportRepository: AirportRepository
    val favoriteRepository: FavoriteRepository
}

/**
 * [AppContainer] implementation that provides an instance of [OfflineAirportRepository]
 * and [OfflineFavoriteRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [AirportRepository]
     */
    override val airportRepository: AirportRepository by lazy {
        OfflineAirportRepository(AppDatabase.getDatabase(context).airportDao())
    }
    /**
     * Implementation for [FavoriteRepository]
     */
    override val favoriteRepository: FavoriteRepository by lazy {
        OfflineFavoriteRepository(AppDatabase.getDatabase(context).favoriteDao())
    }
}
