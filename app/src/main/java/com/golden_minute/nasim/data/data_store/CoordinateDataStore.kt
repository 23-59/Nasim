package com.golden_minute.nasim.data.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class CoordinateDataStore(private val context: Context, latitude: String = "", longitude: String = "") {
    private val lat = stringPreferencesKey(latitude)
    private val lon = stringPreferencesKey(longitude)

    suspend fun saveLatitude(latitude: String) {
        context.dataStore.edit {
            it[lat] = latitude
        }
    }

    suspend fun saveLongitude(longitude: String) {
        context.dataStore.edit {
            it[lon] = longitude
        }
    }

    suspend fun getLatitude(): String {
        val pref = context.dataStore.data.first()
        return pref[lat] ?: ""
    }

    suspend fun getLongitude(): String {
        val pref = context.dataStore.data.first()
        return pref[lon] ?: ""
    }


}