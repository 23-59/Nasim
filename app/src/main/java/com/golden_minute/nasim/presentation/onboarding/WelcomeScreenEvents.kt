package com.golden_minute.nasim.presentation.onboarding

sealed class WelcomeScreenEvents {
    data class OnSearchValueChanges(val searchValue: String) : WelcomeScreenEvents()
    data object SaveData : WelcomeScreenEvents()
    data class OnSelectItem(val lat: Float,val lon:Float) : WelcomeScreenEvents()
}