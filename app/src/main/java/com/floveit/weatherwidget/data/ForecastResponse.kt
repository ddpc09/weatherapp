package com.floveit.weatherwidget.data

data class ForecastResponse(
    val location: LocationData,
    val current: CurrentWeatherData,
    val forecast: ForecastBlock
)

data class ForecastBlock(
    val forecastday: List<ForecastDayDTO>
)

data class ForecastDayDTO(
    val date: String,                  // e.g., "2025-08-12"
    val day: DayDTO,
    val hour: List<HourDTO> = emptyList()
)

data class DayDTO(
    val maxtemp_c: Double,
    val mintemp_c: Double,
    val condition: WeatherConditionData
)

data class HourDTO(
    val time: String,                  // e.g., "2025-08-12 14:00"
    val temp_c: Double,
    val is_day: Int,
    val condition: WeatherConditionData
)

