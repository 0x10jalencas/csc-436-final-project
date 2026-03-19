package com.example.csc_436_final_project.ui.enneagram.rheti

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.io.IOException

data class RhetiPersistedState(
    val answersEncoded: String = "",  // e.g. "A_BBA__..."
    val index: Int = 0,
    val completed: Boolean = false,
    val lastPrimaryType: Int = 0,
    val lastWing: Int = 0,            // 0 means "none"
    val lastTimestampMs: Long = 0L
)

class RhetiPreferencesRepository(private val context: Context) {

    companion object {
        private val Context.dataStore by preferencesDataStore("rheti_prefs")

        private object Keys {
            val ANSWERS = stringPreferencesKey("answers_encoded")
            val INDEX = intPreferencesKey("index")
            val COMPLETED = booleanPreferencesKey("completed")

            val LAST_PRIMARY = intPreferencesKey("last_primary")
            val LAST_WING = intPreferencesKey("last_wing")
            val LAST_TS = longPreferencesKey("last_timestamp")
        }
    }

    val persistedFlow: Flow<RhetiPersistedState> =
        context.dataStore.data
            .catch { e ->
                if (e is IOException) emit(emptyPreferences())
                else throw e
            }
            .map { prefs ->
                RhetiPersistedState(
                    answersEncoded = prefs[Keys.ANSWERS] ?: "",
                    index = prefs[Keys.INDEX] ?: 0,
                    completed = prefs[Keys.COMPLETED] ?: false,
                    lastPrimaryType = prefs[Keys.LAST_PRIMARY] ?: 0,
                    lastWing = prefs[Keys.LAST_WING] ?: 0,
                    lastTimestampMs = prefs[Keys.LAST_TS] ?: 0L
                )
            }
            .distinctUntilChanged()

    suspend fun saveProgress(answersEncoded: String, index: Int, completed: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.ANSWERS] = answersEncoded
            prefs[Keys.INDEX] = index
            prefs[Keys.COMPLETED] = completed
        }
    }

    suspend fun saveLastResult(primaryType: Int, wing: Int?, timestampMs: Long) {
        context.dataStore.edit { prefs ->
            prefs[Keys.LAST_PRIMARY] = primaryType
            prefs[Keys.LAST_WING] = wing ?: 0
            prefs[Keys.LAST_TS] = timestampMs
        }
    }

    suspend fun clearProgressOnly() {
        context.dataStore.edit { prefs ->
            prefs.remove(Keys.ANSWERS)
            prefs.remove(Keys.INDEX)
            prefs.remove(Keys.COMPLETED)
        }
    }

    suspend fun clearAll() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}