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
    val animationResId = getResIdForAnimation(animationName)

    LaunchedEffect(Unit) {
        viewModel.loadWeather("Kolkata")
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
            }
        }
    }
}

fun getResIdForAnimation(name: String): Int {
    return when (name) {
        "sunny" -> R.raw.testsunnygif
        "partly_cloudy" -> R.raw.testsunnygif
        "overcast" -> R.raw.testsunnygif
        "mist_fog" -> R.raw.testsunnygif
        "rainy" -> R.raw.testsunnygif
        "snowy" -> R.raw.testsunnygif
        "thunderstorm" -> R.raw.testsunnygif
        "clear_night" -> R.raw.testsunnygif
        "night_cloudy" -> R.raw.testsunnygif
        "night_rainy" -> R.raw.testsunnygif
        "night_thunderstorm" -> R.raw.testsunnygif
        else -> R.raw.testsunnygif
    }
}