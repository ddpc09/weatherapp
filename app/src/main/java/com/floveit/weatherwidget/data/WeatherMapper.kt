package com.floveit.weatherwidget.data

import com.floveit.weatherwidget.ui.WeatherCondition

class WeatherMapper {
    fun map(response: WeatherResponse): WeatherData {
        val conditionCode = response.current.condition.code
        val isDay = response.current.is_day == 1

        val condition = when (conditionCode) {
            in listOf(
                1063, 1072, 1150, 1153, 1168, 1171, 1180, 1183, 1186, 1189,
                1192, 1195, 1198, 1201, 1240, 1243, 1246
            ) -> WeatherCondition.Rain

            in listOf(1087, 1273, 1276) -> WeatherCondition.Thunder

            in listOf(1003, 1006, 1009) -> WeatherCondition.Cloudy

            in listOf(
                1066, 1114, 1117, 1210, 1213, 1216, 1219,
                1222, 1225, 1237, 1255, 1258, 1261, 1264
            ) -> WeatherCondition.Snow

            else -> WeatherCondition.Clear
        }

        return WeatherData(
            temperature = response.current.temp_c,
            city = response.location.name,
            condition = condition,
            code = conditionCode,
            isDay = isDay,
            feelsLike = response.current.feelslike_c,
            humidity = response.current.humidity,
            windSpeed = response.current.wind_kph
        )
    }
}