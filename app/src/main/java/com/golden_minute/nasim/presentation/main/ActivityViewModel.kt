package com.golden_minute.nasim.presentation.main

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.golden_minute.nasim.data.data_store.CoordinateDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(private val coordinateDataStore: CoordinateDataStore) :
    ViewModel() {
    var lat by mutableDoubleStateOf(0.0)
    var lon by mutableDoubleStateOf(0.0)
    var turnOffSplashScreen = false
        init {
                viewModelScope.launch {

                    combine(coordinateDataStore.getLatitude,coordinateDataStore.getLongitude) { latitude , longitude ->
                        lat = latitude
                        lon = longitude
                        latitude != 0.0 && longitude != 0.0
                    }.collectLatest { conditionMet ->
                            delay(1000)
                            turnOffSplashScreen = true


                    }

                }
            }
        }
