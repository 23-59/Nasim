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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "WelcomeScreenViewModel"

class WelcomeScreenViewModel (
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

    private var job: Job? = null


    fun onEvent(event: WelcomeScreenEvents) {
        when (event) {
            is WelcomeScreenEvents.OnSearchValueChanges -> {
                job = viewModelScope.launch(Dispatchers.IO) {
                    _selectedItem.value = Pair(0.0, 0.0)
                    _searchValue.value = event.searchValue

                    when (val result = useCases.getSearchedCitiesInfo(event.searchValue)) {

                        is CoordinateResponseType.Error -> {
                            if (event.searchValue.isNotBlank())
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    app.baseContext,
                                    result.error,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

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
                    delay(1000)
                }
            }

            is WelcomeScreenEvents.OnSelectItem -> _selectedItem.value = Pair(event.lat, event.lon)
            WelcomeScreenEvents.ClearTextField -> {
                job?.cancel()
                _searchValue.value = ""
                job = null
            }
        }
    }
}



