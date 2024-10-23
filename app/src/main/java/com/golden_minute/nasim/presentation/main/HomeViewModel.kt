package com.golden_minute.nasim.presentation.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.golden_minute.nasim.data.data_store.CoordinateDataStore
import com.golden_minute.nasim.domain.use_case.AppUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(application: Application, appUseCases: AppUseCases,coordinateDataStore: CoordinateDataStore) :
    AndroidViewModel(application) {

}