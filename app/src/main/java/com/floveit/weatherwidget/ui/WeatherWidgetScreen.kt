package com.floveit.weatherwidget.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.floveit.weatherwidget.R
import com.floveit.weatherwidget.viewmodel.WeatherViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherWidgetScreen(viewModel: WeatherViewModel = koinViewModel()) {
    val state by viewModel.weatherState.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    val suggestions by viewModel.locationSuggestions.collectAsState()

    val animationName = getAnimationForCondition(
        code = state.conditionCode,
        isDay = state.isDay
    )
    val animationResId = getResIdForAnimation(animationName, isDay = state.isDay)

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // Optional: auto-load a default city on first launch
        if (state.city.isBlank()) {
            viewModel.loadWeather("Kolkata")
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {

        Column(modifier = Modifier.fillMaxSize()) {

            // 🔍 Location input
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.updateSearchQuery(it) },
                label = { Text("Enter city name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged { isFocused = it.isFocused },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.White
                )
            )

            // 📍 Suggestions (only when focused)
            if (isFocused) {
                suggestions.take(5).forEach { suggestion ->
                    Text(
                        text = suggestion,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.loadWeather(suggestion)
                                viewModel.updateSearchQuery("")
                                focusManager.clearFocus()
                            }
                            .padding(start = 16.dp, top = 4.dp, bottom = 4.dp),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 🌤️ Weather animation (centered)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                WeatherGifAnimation(resourceId = animationResId)
            }
        }

        // 🌡️ Overlay weather text info
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp)
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