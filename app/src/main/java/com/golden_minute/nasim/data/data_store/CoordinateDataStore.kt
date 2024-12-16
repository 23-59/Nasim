package com.golden_minute.nasim.data.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class CoordinateDataStore(private val context: Context) {
    private val lat = doublePreferencesKey("lat")
    private val lon = doublePreferencesKey("lon")

    suspend fun saveLatitude(latitude: Double) {
        context.dataStore.edit {
            it[lat] = latitude
        }
    }

    suspend fun saveLongitude(longitude: Double) {
        context.dataStore.edit {
            it[lon] = longitude
        }
    }

    val getLatitude: Flow<Double> = context.dataStore.data.map {
        it[lat] ?: 0.0
    }

    val getLongitude: Flow<Double> = context.dataStore.data.map {
        it[lon] ?: 0.0
    }



}