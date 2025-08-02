package com.floveit.weatherwidget.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.floveit.weatherwidget.data.WeatherRepository
import com.floveit.weatherwidget.ui.WeatherUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {


    private val _weatherState = MutableStateFlow(WeatherUiState())
    val weatherState: StateFlow<WeatherUiState> = _weatherState

    fun loadWeather(city: String) {
        viewModelScope.launch {
            val result = repository.fetchWeather(city)
            _weatherState.value = result.fold(
                onSuccess = {
                    WeatherUiState(
                        condition = it.condition,
                        city = it.city,
                        temperature = it.temperature.toInt(),
                        conditionCode = it.code,    // <-- Add this
                        isDay = it.isDay            // <-- Add this
                    )
                },
                onFailure = {
                    WeatherUiState(error = it.localizedMessage ?: "Unknown Error")
                }
            )
        }
    }
}