package com.floveit.weatherwidget.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.floveit.weatherwidget.data.DailyForecast
import com.floveit.weatherwidget.data.WeatherRepository
import com.floveit.weatherwidget.data.location.LocationRepository
import com.floveit.weatherwidget.data.location.PlaceSuggestion
import com.floveit.weatherwidget.data.location.ReverseGeocoder
import com.floveit.weatherwidget.data.preferences.PreferencesManager
import com.floveit.weatherwidget.ui.WeatherUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*


class WeatherViewModel(
    private val repository: WeatherRepository,
    private val locationRepository: LocationRepository,
    private val preferences: PreferencesManager,
    private val reverseGeocoder: ReverseGeocoder // NEW: to coarsen display label
) : ViewModel() {

    private val _weatherState = MutableStateFlow(WeatherUiState())
    val weatherState: StateFlow<WeatherUiState> = _weatherState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _locationSuggestions = MutableStateFlow<List<PlaceSuggestion>>(emptyList())
    val locationSuggestions: StateFlow<List<PlaceSuggestion>> = _locationSuggestions

    private var debounceJob: Job? = null

    init {
        observeSearchQueryWithDebounce()
        restoreLastLocationAndLoad()
    }

    private fun restoreLastLocationAndLoad() {
        viewModelScope.launch {
            combine(
                preferences.lastLocationQueryFlow,
                preferences.lastLocationLabelFlow
            ) { q, label -> q to label }
                .take(1)
                .collect { (q, label) ->
                    if (!q.isNullOrBlank()) {
                        // Show saved label immediately (if present)
                        if (!label.isNullOrBlank()) {
                            _weatherState.value = _weatherState.value.copy(city = label)
                        }
                        // Prefer a suburb-level label on cold start (less micro than feature names)
                        trySetReverseLabel(q, mode = ReverseGeocoder.LabelMode.Suburb, persist = label.isNullOrBlank())
                        loadWeather(q)
                    } else {
                        loadWeather("Kolkata")
                    }
                }
        }
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

    private fun loadWeather(q: String, days: Int = 5) {
        viewModelScope.launch {
            val current = repository.fetchWeather(q)
            val forecast = repository.fetchForecast(q, days)

            current.fold(onSuccess = { cw ->
                val (daily, hourly) = forecast.getOrNull() ?: (emptyList<DailyForecast>() to emptyList())
                setStateIfChanged(
                    WeatherUiState(
                        condition = cw.condition,
                        city = if (_weatherState.value.city.isNotBlank()) _weatherState.value.city else cw.city,
                        temperature = cw.temperature.toInt(),
                        conditionCode = cw.code,
                        isDay = cw.isDay,
                        feelsLike = cw.feelsLike.toInt(),
                        humidity = cw.humidity,
                        windSpeed = cw.windSpeed,
                        daily = daily,
                        hourly = hourly
                    )
                )
            }, onFailure = {
                setStateIfChanged(WeatherUiState(error = it.localizedMessage ?: "Unknown Error"))
            })
        }
    }

    fun onLocationSelected(s: PlaceSuggestion, days: Int = 5) {
        viewModelScope.launch {
            val q = "${s.lat},${s.lon}"       // robust for API
            val typedQuery = _searchQuery.value // read BEFORE clearing
            val tokens = typedQuery.lowercase().trim().split(Regex("[,\\s]+")).filter { it.isNotBlank() }

            // Decide desired coarseness:
            // - 1 token (e.g., "kolkata") -> City
            // - 2+ tokens (e.g., "salt lake kolkata") -> Suburb (one level less specific than micro)
            val mode = if (tokens.size <= 1) ReverseGeocoder.LabelMode.City
            else ReverseGeocoder.LabelMode.Suburb

            // Save both values
            preferences.saveLastLocation(q, s.displayName)

            // Show the suggestion label immediately
            _weatherState.value = _weatherState.value.copy(city = s.displayName)

            // Refine label to requested coarseness (City/Suburb) and persist if improved
            trySetReverseLabel(q, mode = mode, persist = true)

            // Fetch weather by coords
            loadWeather(q, days)

            // Clear editor
            updateSearchQuery("")
        }
    }

    /** Reverse-geocode "lat,lon" to a coarser label and optionally persist it. */
    private fun trySetReverseLabel(
        q: String,
        mode: ReverseGeocoder.LabelMode,
        persist: Boolean
    ) {
        val (lat, lon) = parseLatLon(q) ?: return
        viewModelScope.launch {
            reverseGeocoder.labelFor(lat, lon, mode)?.let { nice ->
                if (nice.isNotBlank() && nice != _weatherState.value.city) {
                    _weatherState.value = _weatherState.value.copy(city = nice)
                    if (persist) preferences.saveLastLocation(q, nice)
                }
            }
        }
    }

    private fun parseLatLon(q: String): Pair<Double, Double>? {
        val parts = q.split(",")
        val lat = parts.getOrNull(0)?.trim()?.toDoubleOrNull()
        val lon = parts.getOrNull(1)?.trim()?.toDoubleOrNull()
        return if (lat != null && lon != null) lat to lon else null
    }

    private fun setStateIfChanged(newState: WeatherUiState) {
        val old = _weatherState.value
        if (newState != old) _weatherState.value = newState
    }
}
