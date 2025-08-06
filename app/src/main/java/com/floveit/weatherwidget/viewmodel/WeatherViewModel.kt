package com.floveit.weatherwidget.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.floveit.weatherwidget.data.WeatherRepository
import com.floveit.weatherwidget.data.location.LocationRepository
import com.floveit.weatherwidget.ui.WeatherUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch


class WeatherViewModel(
    private val repository: WeatherRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _weatherState = MutableStateFlow(WeatherUiState())
    val weatherState: StateFlow<WeatherUiState> = _weatherState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _locationSuggestions = MutableStateFlow<List<String>>(emptyList())
    val locationSuggestions: StateFlow<List<String>> = _locationSuggestions

    private var debounceJob: Job? = null

    init {
        observeSearchQueryWithDebounce()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQueryWithDebounce() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300L)
                .filter { it.isNotBlank() }
                .distinctUntilChanged()
                .collect { query ->
                    val suggestions = locationRepository.getSuggestions(query)
                    _locationSuggestions.value = suggestions
                }
        }
    }

    fun loadWeather(city: String) {
        viewModelScope.launch {
            val result = repository.fetchWeather(city)
            _weatherState.value = result.fold(
                onSuccess = {
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
}