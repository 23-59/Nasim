package com.golden_minute.nasim.presentation.onboarding

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.golden_minute.nasim.data.data_store.CoordinateDataStore
import com.golden_minute.nasim.domain.CoordinateResponse
import com.golden_minute.nasim.domain.CoordinateResponseItem
import com.golden_minute.nasim.domain.use_case.AppUseCases
import com.golden_minute.nasim.domain.use_case.GetCoordinate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
private const val TAG = "WelcomeScreenViewModel"

@HiltViewModel
class WelcomeScreenViewModel @Inject constructor(
    app: Application,
    private val useCases: AppUseCases,
    private val coordinateDataStore: CoordinateDataStore
) : AndroidViewModel(app) {


    private var _searchValue = mutableStateOf("")
    val searchValue: State<String> = _searchValue

    private var _selectedCoordinate = mutableStateOf(CoordinateResponseItem())
    val selectedCoordinate: State<CoordinateResponseItem> = _selectedCoordinate

     private var _coordinates = mutableStateListOf<CoordinateResponseItem>()
    val coordinates: SnapshotStateList<CoordinateResponseItem> = _coordinates

    private var _selectedItem = mutableStateOf(Pair(0f,0f))
    val selectedItem: State<Pair<Float,Float>> = _selectedItem


    fun onEvent(event: WelcomeScreenEvents) {
        when (event) {
            is WelcomeScreenEvents.OnSearchValueChanges -> {
                viewModelScope.launch {
                    _selectedItem.value = Pair(0f,0f)
                    _searchValue.value = event.searchValue
                     if (event.searchValue.isNotBlank()){
                         val newCoordinates = useCases.getCoordinate(event.searchValue).toMutableStateList()
                        _coordinates.clear()
                        _coordinates.addAll(newCoordinates)
                    }
                    else _coordinates.clear()


                    Log.i(TAG, "onEvent: ")
                }
            }

            WelcomeScreenEvents.SaveData -> {
                viewModelScope.launch(Dispatchers.IO) {
                    coordinateDataStore.saveLatitude(selectedItem.value.first.toString())
                    coordinateDataStore.saveLongitude(selectedItem.value.second.toString())
                }
            }

            is WelcomeScreenEvents.OnSelectItem -> {
                Log.i(TAG, "onEvent: ")
                _selectedItem.value =Pair(event.lat,event.lon)
            }
        }
    }
}