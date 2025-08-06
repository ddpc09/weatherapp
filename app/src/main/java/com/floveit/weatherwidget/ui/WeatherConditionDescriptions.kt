package com.floveit.weatherwidget.ui

val weatherConditionDescriptions = mapOf(
    // Clear / Sunny
    1000 to Pair("Sunny", "Clear"),

    // Cloudy group
    1003 to Pair("Partly cloudy", "Partly cloudy"),
    1006 to Pair("Cloudy", "Cloudy"),
    1009 to Pair("Overcast", "Overcast"),

    // Fog / Mist
    1030 to Pair("Mist", "Mist"),
    1135 to Pair("Fog", "Fog"),
    1147 to Pair("Freezing fog", "Freezing fog"),

    // Rain group
    1063 to Pair("Patchy rain possible", "Patchy rain possible"),
    1072 to Pair("Patchy freezing drizzle possible", "Patchy freezing drizzle possible"),
    1150 to Pair("Patchy light drizzle", "Patchy light drizzle"),
    1153 to Pair("Light drizzle", "Light drizzle"),
    1168 to Pair("Freezing drizzle", "Freezing drizzle"),
    1171 to Pair("Heavy freezing drizzle", "Heavy freezing drizzle"),
    1180 to Pair("Patchy light rain", "Patchy light rain"),
    1183 to Pair("Light rain", "Light rain"),
    1186 to Pair("Moderate rain at times", "Moderate rain at times"),
    1189 to Pair("Moderate rain", "Moderate rain"),
    1192 to Pair("Heavy rain at times", "Heavy rain at times"),
    1195 to Pair("Heavy rain", "Heavy rain"),
    1198 to Pair("Light freezing rain", "Light freezing rain"),
    1201 to Pair("Moderate or heavy freezing rain", "Moderate or heavy freezing rain"),
    1240 to Pair("Light rain shower", "Light rain shower"),
    1243 to Pair("Moderate or heavy rain shower", "Moderate or heavy rain shower"),
    1246 to Pair("Torrential rain shower", "Torrential rain shower"),
    1273 to Pair("Patchy light rain with thunder", "Patchy light rain with thunder"),
    1276 to Pair("Moderate or heavy rain with thunder", "Moderate or heavy rain with thunder"),

    // Snow group
    1066 to Pair("Patchy snow possible", "Patchy snow possible"),
    1114 to Pair("Blowing snow", "Blowing snow"),
    1117 to Pair("Blizzard", "Blizzard"),
    1210 to Pair("Patchy light snow", "Patchy light snow"),
    1213 to Pair("Light snow", "Light snow"),
    1216 to Pair("Moderate snow at times", "Moderate snow at times"),
    1219 to Pair("Moderate snow", "Moderate snow"),
    1222 to Pair("Heavy snow at times", "Heavy snow at times"),
    1225 to Pair("Heavy snow", "Heavy snow"),
    1237 to Pair("Ice pellets", "Ice pellets"),
    1255 to Pair("Light snow showers", "Light snow showers"),
    1258 to Pair("Moderate or heavy snow showers", "Moderate or heavy snow showers"),
    1261 to Pair("Light showers of ice pellets", "Light showers of ice pellets"),
    1264 to Pair("Moderate or heavy showers of ice pellets", "Moderate or heavy showers of ice pellets"),
    1279 to Pair("Patchy light snow with thunder", "Patchy light snow with thunder"),
    1282 to Pair("Moderate or heavy snow with thunder", "Moderate or heavy snow with thunder"),

    // Sleet
    1069 to Pair("Patchy sleet possible", "Patchy sleet possible"),
    1204 to Pair("Light sleet", "Light sleet"),
    1207 to Pair("Moderate or heavy sleet", "Moderate or heavy sleet"),
    1249 to Pair("Light sleet showers", "Light sleet showers"),
    1252 to Pair("Moderate or heavy sleet showers", "Moderate or heavy sleet showers"),

    // Thunder
    1087 to Pair("Thundery outbreaks possible", "Thundery outbreaks possible")
)