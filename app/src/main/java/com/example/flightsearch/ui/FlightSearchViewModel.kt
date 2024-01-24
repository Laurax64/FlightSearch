/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.flightsearch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearch.FlightSearchApplication
import com.example.flightsearch.data.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * View model for Flight Search app components
 *
 * @param userPreferencesRepository The [UserPreferencesRepository]
 *
 */
class FlightSearchViewModel(private val userPreferencesRepository: UserPreferencesRepository): ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[APPLICATION_KEY] as FlightSearchApplication)
                FlightSearchViewModel(application.userPreferencesRepository)
            }
        }
    }

    /**
     * Stores the search string
     */
    fun storeSearchString(searchString: String) {
        viewModelScope.launch {
            userPreferencesRepository.storeSearchString(searchString)
        }
    }
    val uiState: StateFlow<FlightSearchUiState> =
        userPreferencesRepository.searchString.map { searchString ->
             FlightSearchUiState(searchString)}
                 .stateIn(
                     scope = viewModelScope,
                     started = SharingStarted.WhileSubscribed(5_000),
                     initialValue = FlightSearchUiState()
                 )
        }

    data class FlightSearchUiState(val searchString: String = "")
