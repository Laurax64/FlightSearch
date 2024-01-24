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
package com.example.flightsearch.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * Concrete class implementation to access data store
 *
 * @param dataStore The [DataStore] instance
 * @property searchString The users text input [Flow]
 */
class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {
    private companion object {
        val USER_TEXT_INPUT = stringPreferencesKey("user text input")
        const val TAG = "UserPreferencesRepo"
    }

    /* Each time the data in the DataStore is updated,
    a new Preferences object is emitted into the Flow */
    val searchString: Flow<String> = dataStore.data
        .catch {
            if(it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
        preferences[USER_TEXT_INPUT] ?: ""
        }

    /**
     * Writes the users text input to the [DataStore].
     *
     * By setting up the key-value pair in the edit() method, the value is defined and initialized
     * until the app's cache or data is cleared.
     */
    suspend fun storeSearchString(userTextInput: String) {
        dataStore.edit {preferences ->
            preferences[USER_TEXT_INPUT] = userTextInput
        }
    }



}