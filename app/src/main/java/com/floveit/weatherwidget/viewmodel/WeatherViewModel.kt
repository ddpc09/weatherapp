package com.floveit.weatherwidget.viewmodel

import android.util.Log
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
                    Log.d("WeatherLog", """
                        ✅ Weather fetched:
                        City: ${it.city}
                        Temp: ${it.temperature}°C
                        Feels like: ${it.feelsLike}°C
                        Condition: ${it.condition} (code: ${it.code})
                        Is Day: ${it.isDay}
                    """.trimIndent())
                    WeatherUiState(
                        condition = it.condition,
                        city = it.city,
                        temperature = it.temperature.toInt(),
                        conditionCode = it.code,
                        isDay = it.isDay,
                        feelsLike = it.feelsLike.toInt(),
                        humidity = it.humidity,
                        windSpeed = it.windSpeed
                    )
                },
                onFailure = {
                    WeatherUiState(error = it.localizedMessage ?: "Unknown Error")
                }
            )
        }
    }

    fun loadWeatherFromLocation() {
        viewModelScope.launch {
            val result = repository.fetchWeatherForCurrentLocation()
            _weatherState.value = result.fold(
                onSuccess = {
                    Log.d("WeatherLog", """
                    ✅ Weather fetched (via location):
                    City: ${it.city}
                    Temp: ${it.temperature}°C
                    Feels like: ${it.feelsLike}°C
                    Condition: ${it.condition} (code: ${it.code})
                    Is Day: ${it.isDay}
                """.trimIndent())
                    WeatherUiState(
                        condition = it.condition,
                        city = it.city,
                        temperature = it.temperature.toInt(),
                        conditionCode = it.code,
                        isDay = it.isDay,
                        feelsLike = it.feelsLike.toInt(),
                        humidity = it.humidity,
                        windSpeed = it.windSpeed
                    )
                },
                onFailure = {
                    Log.e("WeatherLog", "❌ Failed to fetch weather by location: ${it.localizedMessage}")
                    WeatherUiState(error = it.localizedMessage ?: "Unknown Error")
                }
            )
        }
    }
}