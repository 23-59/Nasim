package com.golden_minute.nasim.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.golden_minute.nasim.data.data_store.CoordinateDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(private val coordinateDataStore: CoordinateDataStore) :
    ViewModel() {


    var lat by mutableDoubleStateOf(0.0)
    var lon by mutableDoubleStateOf(0.0)
    var turnOffSplashScreen = true

    fun initializeCoordinates() {
        viewModelScope.launch {
            launch {
                coordinateDataStore.getLatitude.collectLatest {
                    lat = it
                }
            }
            launch {
                coordinateDataStore.getLongitude.collectLatest {
                    lon = it
                }
            }
            delay(1000)
            turnOffSplashScreen = false
        }

    }

}