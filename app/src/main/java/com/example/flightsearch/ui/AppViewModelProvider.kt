package com.example.flightsearch.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearch.FlightSearchApplication
import com.example.flightsearch.ui.airport.AirportSearchViewModel
import com.example.flightsearch.ui.favorite.FavoriteFlightsViewModel
import com.example.flightsearch.ui.flight.FlightsViewModel

/**
* Provides Factory to create instances of all the app's view models
*/
object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                            as FlightSearchApplication)
                AirportSearchViewModel(
                    application.userPreferencesRepository,
                    application.airportRepository
                )
            }

        initializer {
            val application =
                (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlightSearchApplication)
            FavoriteFlightsViewModel(
                application.userPreferencesRepository,
                application.favoriteRepository
            )
        }

        initializer {
            val application =
                (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as FlightSearchApplication)
            FlightsViewModel(
                application.airportRepository,
                application.userPreferencesRepository,
            )
        }
    }
}

/**
 * Queries for an [Application] object and returns an instance of [FlightSearchApplication].
 */
fun CreationExtras.inventoryApplication(): FlightSearchApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlightSearchApplication)
