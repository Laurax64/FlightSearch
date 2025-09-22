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
class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>,
) {
    private companion object {
        val SEARCH_STRING = stringPreferencesKey("search_string")
        const val TAG = "UserPreferencesRepo"
    }

    /* Each time the data in the DataStore is updated,
    a new Preferences object is emitted into the Flow */
    val searchString: Flow<String> =
        dataStore.data
            .catch {
                if (it is IOException) {
                    Log.e(TAG, "Error reading preferences.", it)
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map { preferences ->
                preferences[SEARCH_STRING] ?: ""
            }

    /**
     * Writes the users text input to the [DataStore].
     *
     * By setting up the key-value pair in the edit() method, the value is defined and initialized
     * until the app's cache or data is cleared.
     */
    suspend fun saveSearchString(userTextInput: String) {
        dataStore.edit { preferences ->
            preferences[SEARCH_STRING] = userTextInput
        }
    }
}
