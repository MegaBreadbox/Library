package com.example.mtgdeckbuilder.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserSelectedDeck(private val dataStore: DataStore<Preferences>): SelectedDeckRepository {

    private val deckKey: Flow<Int> = dataStore.data
        .catch {
            if(it is IOException) {
                Log.e(TAG,"Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map {preferences ->
            preferences[DECK_ID] ?: 0

    }

    companion object{
        val DECK_ID = intPreferencesKey("deck_id")
        const val TAG = "UserSelectedDeckRepo"
    }

    override fun readDeck(): Flow<Int> {
        return deckKey
    }

    override suspend fun changeSelectedDeck(deckKey: Int) {
        dataStore.edit {preferences ->
            preferences[DECK_ID] = deckKey

        }
    }
}