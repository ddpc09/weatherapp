package com.floveit.weatherwidget.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.floveit.weatherwidget.R
import com.floveit.weatherwidget.viewmodel.WeatherViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

//@Composable
//fun WeatherWidgetScreen(viewModel: WeatherViewModel = koinViewModel()) {
//    val state by viewModel.weatherState.collectAsState()
//
//    LaunchedEffect(Unit) {
//        viewModel.loadWeather("Kolkata")
//    }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        // 🔆 Weather animation in background
//        WeatherAnimation(condition = state.condition)
//
//        // 🌤️ Overlay weather text info
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier.align(Alignment.Center)
//        ) {
//            if (state.error != null) {
//                Text("Error: ${state.error}", color = Color.Red)
//            } else {
//                Text(text = state.city, fontSize = 20.sp)
//                Text(text = "${state.temperature}°C", fontSize = 36.sp)
//            }
//        }
//    }
//}

@Composable
fun WeatherWidgetScreen(viewModel: WeatherViewModel = koinViewModel()) {
    val state by viewModel.weatherState.collectAsState()
    val weatherState by viewModel.weatherState.collectAsState()

    val animationName = getAnimationForCondition(
        code = weatherState.conditionCode,
        isDay = weatherState.isDay
    )
//    val animationResId = getResIdForAnimation(animationName)

    val animationResId = getResIdForAnimation(animationName, isDay = state.isDay)

    LaunchedEffect(Unit) {
        while (true) {
//            viewModel.loadWeather("Kolkata")
            viewModel.loadWeatherFromLocation()
            delay(15 * 60 * 1000) // 15 minutes
        }
    }


    Box(modifier = Modifier.fillMaxSize().background(color = Color.Black)) {
        // 🌞 Fullscreen test GIF animation
        WeatherGifAnimation(resourceId = animationResId, modifier = Modifier.fillMaxSize())

        // 🌡️ Overlay text info
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            if (state.error != null) {
                Text("Error: ${state.error}", color = Color.Red)
            } else {
                Text(text = state.city, fontSize = 20.sp, color = Color.White)
                Text(text = "${state.temperature}°C", fontSize = 36.sp, color = Color.White)
                Text(text = "Feels like: ${state.feelsLike}°C", color = Color.White)
                Text(text = "Humidity: ${state.humidity}%", color = Color.White)
                Text(text = "Wind: ${state.windSpeed} km/h", color = Color.White)
            }
        }
    }
}

fun getResIdForAnimation(name: String, isDay: Boolean): Int {
    return when (name) {
        "sunny" -> R.raw.sunny1
        "partly_cloudy" -> R.raw.paartlycloudy
        "overcast" -> R.raw.overcast1
        "mist_fog" -> R.raw.fog1
        "rainy" -> R.raw.rain
        "snowy" -> R.raw.snowy
        "thunderstorm" -> R.raw.thunder2
        "clear_night" -> R.raw.clearnight
        "night_cloudy" -> R.raw.cloudynight
        "night_rainy" -> R.raw.rain
        "night_thunderstorm" -> R.raw.thunder2
        else -> if(isDay){R.raw.sunny1} else{R.raw.clearnight}
    }
}