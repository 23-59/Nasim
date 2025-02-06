package com.golden_minute.nasim.presentation.onboarding

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.golden_minute.nasim.data.data_store.CoordinateDataStore
import com.golden_minute.nasim.domain.CoordinateResponseType
import com.golden_minute.nasim.domain.model.coordinate_response.SearchResponse
import com.golden_minute.nasim.domain.use_case.AppUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "WelcomeScreenViewModel"

@HiltViewModel
class WelcomeScreenViewModel @Inject constructor(
    private val app: Application,
    private val useCases: AppUseCases,
    private val coordinateDataStore: CoordinateDataStore
) : AndroidViewModel(app) {
    private var _searchValue = mutableStateOf("")
    val searchValue: State<String> = _searchValue

    private var _coordinates = mutableStateListOf<SearchResponse>()
    val coordinates: SnapshotStateList<SearchResponse> = _coordinates


    private var _selectedItem = mutableStateOf(Pair(0.0, 0.0))
    val selectedItem: State<Pair<Double, Double>> = _selectedItem


    fun onEvent(event: WelcomeScreenEvents) {
        when (event) {
            is WelcomeScreenEvents.OnSearchValueChanges -> {
                viewModelScope.launch {
                    _selectedItem.value = Pair(0.0, 0.0)
                    _searchValue.value = event.searchValue

                    when (val result = useCases.getSearchedCitiesInfo(event.searchValue)) {

                        is CoordinateResponseType.Error -> Toast.makeText(
                            app.baseContext,
                            result.error,
                            Toast.LENGTH_SHORT
                        ).show()

                        is CoordinateResponseType.OK -> {
                            if (event.searchValue.isNotBlank()) {
                                _coordinates.clear()
                                _coordinates.addAll(result.places)
                            } else _coordinates.clear()
                        }
                    }

                    Log.i(TAG, "onEvent: ")
                }
            }

            WelcomeScreenEvents.SaveData -> {
                viewModelScope.launch(Dispatchers.IO) {
                    coordinateDataStore.saveLatitude(selectedItem.value.first)
                    coordinateDataStore.saveLongitude(selectedItem.value.second)
                }
            }

            is WelcomeScreenEvents.OnSelectItem -> _selectedItem.value = Pair(event.lat, event.lon)

        }
    }
}



