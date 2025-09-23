package com.example.flightsearch.data

import android.content.Context
import com.example.flightsearch.data.airport.AirportRepository
import com.example.flightsearch.data.airport.OfflineAirportRepository
import com.example.flightsearch.data.favorite.FavoriteRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val airportRepository: AirportRepository
    val favoriteRepository: FavoriteRepository
}

/**
 * [AppContainer] implementation that provides an instance of [com.example.flightsearch.data.airport.OfflineAirportRepository]
 * and [OfflineFavoriteRepository]
 */
class AppDataContainer(
    private val context: Context,
) : AppContainer {
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
