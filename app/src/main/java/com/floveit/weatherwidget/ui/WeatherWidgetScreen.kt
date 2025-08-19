package com.floveit.weatherwidget.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import com.floveit.weatherwidget.data.DailyForecast
import com.floveit.weatherwidget.data.HourlyForecast


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherWidgetScreen(viewModel: WeatherViewModel = org.koin.androidx.compose.koinViewModel()) {
    val state by viewModel.weatherState.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    val suggestions by viewModel.locationSuggestions.collectAsState()

    val conditionText = weatherConditionDescriptions[state.conditionCode]?.let {
        if (state.isDay) it.first else it.second
    } ?: "Unknown"

    val animationName = getAnimationForCondition(state.conditionCode, state.isDay)
    val animationResId = getResIdForAnimation(animationName, isDay = state.isDay)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
    ) {
        CurrentWeatherSection(
            state = state,
            conditionText = conditionText,
            animationResId = animationResId,
            query = query,
            suggestions = suggestions,
            onQueryChange = viewModel::updateSearchQuery,
            onCitySelected = viewModel::onLocationSelected
        )

        if (state.daily.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            ForecastRow(daily = state.daily, isDay = true)
        }

        if (state.hourly.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Next 24 hours",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
            )
            HourlyRow(hourly = state.hourly, isDay = state.isDay)
        }
        Spacer(Modifier.height(12.dp))
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

//fun getResIdForAnimation(name: String, isDay: Boolean): Int {
//    val gifname = R.raw.fog1new
//    return when (name) {
//        "sunny" -> gifname
//        "partly_cloudy" -> gifname
//        "overcast" -> gifname
//        "mist_fog" -> gifname
//        "rainy" -> gifname
//        "snowy" -> gifname
//        "thunderstorm" -> gifname
//        "clear_night" -> gifname
//        "night_cloudy" -> gifname
//        "night_rainy" -> gifname
//        "night_thunderstorm" -> gifname
//        else -> if(isDay){gifname} else{gifname}
//    }
//}

//@Composable
//private fun CurrentWeatherSection(
//    state: WeatherUiState,
//    conditionText: String,
//    animationResId: Int
//) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp)
//    ) {
//        // GIF area
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(220.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            WeatherGifAnimation(resourceId = animationResId)
//        }
//
//        Spacer(Modifier.height(8.dp))
//
//        if (state.error != null) {
//            Text("Error: ${state.error}", color = Color.Red)
//        } else {
//            Text(text = state.city, fontSize = 20.sp, color = Color.White)
//            Text(text = conditionText, fontSize = 18.sp, color = Color.White)
//            Text(text = "${state.temperature}°C", fontSize = 36.sp, color = Color.White)
//
//            Spacer(Modifier.height(4.dp))
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(12.dp),
//                modifier = Modifier.wrapContentWidth()
//            ) {
//                Text(text = "Feels: ${state.feelsLike}°C", color = Color.White)
//                Text(text = "Humidity: ${state.humidity}%", color = Color.White)
//                Text(text = "Wind: ${state.windSpeed} km/h", color = Color.White)
//            }
//        }
//    }
//}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//private fun CurrentWeatherSection(
//    state: WeatherUiState,
//    conditionText: String,
//    animationResId: Int,
//    query: String,
//    suggestions: List<String>,
//    onQueryChange: (String) -> Unit,
//    onCitySelected: (String) -> Unit
//) {
//    val focusManager = LocalFocusManager.current
//    val keyboard = LocalSoftwareKeyboardController.current
//    val tfFocus = remember { FocusRequester() }
//    var editing by rememberSaveable { mutableStateOf(false) }
//    var tfFocused by remember { mutableStateOf(false) }
//
//    // Enable back button to exit edit mode
//    androidx.activity.compose.BackHandler(enabled = editing) {
//        editing = false
//        onQueryChange("")
//        focusManager.clearFocus()
//        keyboard?.hide()
//    }
//
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp)
//    ) {
//        // GIF
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(220.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            WeatherGifAnimation(resourceId = animationResId)
//        }
//
//        Spacer(Modifier.height(8.dp))
//
//        // City: click to edit
//        if (!editing) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier
//                    .clip(RoundedCornerShape(8.dp))
//                    .clickable {
//                        editing = true
//                        onQueryChange(state.city) // prefill with current city (or "")
//                    }
//                    .padding(horizontal = 8.dp, vertical = 4.dp)
//            ) {
//                Text(
//                    text = if (state.city.isBlank()) "Set city" else state.city,
//                    fontSize = 20.sp,
//                    color = Color.White
//                )
//                Spacer(Modifier.width(6.dp))
//                // optional edit icon (remove if you don’t use material-icons-extended)
//                androidx.compose.material3.Icon(
//                    imageVector = androidx.compose.material.icons.Icons.Outlined.Edit,
//                    contentDescription = "Edit city",
//                    tint = Color.White,
//                    modifier = Modifier.size(18.dp)
//                )
//            }
//        } else {
//            // Inline editor shown in place of the city text
//            OutlinedTextField(
//                value = query,
//                onValueChange = onQueryChange,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .focusRequester(tfFocus)
//                    .onFocusChanged { tfFocused = it.isFocused },
//                singleLine = true,
//                label = { Text("Enter city name") },
//                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
//                keyboardActions = KeyboardActions(
//                    onDone = {
//                        if (query.isNotBlank()) onCitySelected(query)
//                        editing = false
//                        onQueryChange("")
//                        focusManager.clearFocus()
//                        keyboard?.hide()
//                    }
//                ),
//                colors = TextFieldDefaults.outlinedTextFieldColors(
//                    focusedBorderColor = Color.White,
//                    unfocusedBorderColor = Color.Gray,
//                    cursorColor = Color.White
//                ),
//                trailingIcon = {
//                    IconButton(onClick = {
//                        editing = false
//                        onQueryChange("")
//                        focusManager.clearFocus()
//                        keyboard?.hide()
//                    }) {
//                        androidx.compose.material3.Icon(
//                            imageVector = androidx.compose.material.icons.Icons.Outlined.Close,
//                            contentDescription = "Cancel",
//                            tint = Color.White
//                        )
//                    }
//                }
//            )
//
//            // Focus & show keyboard when entering edit mode
//            LaunchedEffect(editing) {
//                if (editing) {
//                    tfFocus.requestFocus()
//                    keyboard?.show()
//                }
//            }
//
//            // Suggestions under the field
//            if (tfFocused && suggestions.isNotEmpty()) {
//                Spacer(Modifier.height(6.dp))
//                androidx.compose.material3.Card(
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = androidx.compose.material3.CardDefaults.cardColors(
//                        containerColor = Color(0xCC1A1A1A)
//                    )
//                ) {
//                    Column(Modifier.padding(vertical = 4.dp)) {
//                        suggestions.take(5).forEach { s ->
//                            Text(
//                                text = s,
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .clickable {
//                                        onCitySelected(s)
//                                        editing = false
//                                        onQueryChange("")
//                                        focusManager.clearFocus()
//                                        keyboard?.hide()
//                                    }
//                                    .padding(horizontal = 16.dp, vertical = 8.dp),
//                                color = Color.White,
//                                fontSize = 14.sp
//                            )
//                        }
//                    }
//                }
//            }
//        }
//
//        // Rest of the weather text
//        if (state.error != null) {
//            Text("Error: ${state.error}", color = Color.Red)
//        } else {
//            Text(text = conditionText, fontSize = 18.sp, color = Color.White)
//            Text(text = "${state.temperature}°C", fontSize = 36.sp, color = Color.White)
//
//            Spacer(Modifier.height(4.dp))
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(12.dp),
//                modifier = Modifier.wrapContentWidth()
//            ) {
//                Text(text = "Feels: ${state.feelsLike}°C", color = Color.White)
//                Text(text = "Humidity: ${state.humidity}%", color = Color.White)
//                Text(text = "Wind: ${state.windSpeed} km/h", color = Color.White)
//            }
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentWeatherSection(
    state: WeatherUiState,
    conditionText: String,
    animationResId: Int,
    query: String,
    suggestions: List<com.floveit.weatherwidget.data.location.PlaceSuggestion>,
    onQueryChange: (String) -> Unit,
    onCitySelected: (com.floveit.weatherwidget.data.location.PlaceSuggestion) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    val tfFocus = remember { FocusRequester() }

    var editing by rememberSaveable { mutableStateOf(false) }
    var tfFocused by remember { mutableStateOf(false) }

    // Back button exits edit mode
    androidx.activity.compose.BackHandler(enabled = editing) {
        editing = false
        onQueryChange("")
        focusManager.clearFocus()
        keyboard?.hide()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // GIF (kept stable; only resId flows in)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentAlignment = Alignment.Center
        ) {
            WeatherGifAnimation(resourceId = animationResId)
        }

        Spacer(Modifier.height(8.dp))

        // City row — tap to edit
        if (!editing) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        editing = true
                        onQueryChange(state.city.ifBlank { "" }) // prefill with current city if you want
                    }
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Text(
                    text = if (state.city.isBlank()) "Set city" else state.city,
                    fontSize = 20.sp,
                    color = Color.White
                )
                Spacer(Modifier.width(6.dp))
                // (Optional) tiny hint that it's editable
                Text("• edit", fontSize = 12.sp, color = Color.LightGray)
            }
        } else {
            // Inline editor (replaces the city text while editing)
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(tfFocus)
                    .onFocusChanged { tfFocused = it.isFocused },
                singleLine = true,
                label = { Text("Enter city / area") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        // Pick the first suggestion on Enter (keeps coord-precision)
                        suggestions.firstOrNull()?.let { first ->
                            onCitySelected(first)
                        }
                        editing = false
                        onQueryChange("")
                        focusManager.clearFocus()
                        keyboard?.hide()
                    }
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.White
                ),
                trailingIcon = {
                    Text(
                        "Cancel",
                        color = Color.White,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable {
                                editing = false
                                onQueryChange("")
                                focusManager.clearFocus()
                                keyboard?.hide()
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 12.sp
                    )
                }
            )

            // Auto-focus + keyboard when entering edit mode
            LaunchedEffect(editing) {
                if (editing) {
                    tfFocus.requestFocus()
                    keyboard?.show()
                }
            }

            // Suggestions (only when focused)
            if (tfFocused && suggestions.isNotEmpty()) {
                Spacer(Modifier.height(6.dp))
                androidx.compose.material3.Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = Color(0xCC1A1A1A)
                    )
                ) {
                    Column(Modifier.padding(vertical = 4.dp)) {
                        suggestions.take(5).forEach { s ->
                            Text(
                                text = s.displayName,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onCitySelected(s)
                                        editing = false
                                        onQueryChange("")
                                        focusManager.clearFocus()
                                        keyboard?.hide()
                                    }
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Weather details text
        if (state.error != null) {
            Text("Error: ${state.error}", color = Color.Red)
        } else {
            Text(text = conditionText, fontSize = 18.sp, color = Color.White)
            Text(text = "${state.temperature}°C", fontSize = 36.sp, color = Color.White)

            Spacer(Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.wrapContentWidth()
            ) {
                Text(text = "Feels: ${state.feelsLike}°C", color = Color.White)
                Text(text = "Humidity: ${state.humidity}%", color = Color.White)
                Text(text = "Wind: ${state.windSpeed} km/h", color = Color.White)
            }
        }
    }
}



//@Composable
//private fun ForecastRow(daily: List<DailyForecast>, isDay: Boolean) {
//    androidx.compose.foundation.lazy.LazyRow(
//        modifier = Modifier
//            .fillMaxWidth()       // ⬅️ was fillMaxSize(), which could steal height
//            .heightIn(min = 92.dp),
//        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
//    ) {
//        items(daily.size) { i ->
//            val d = daily[i]
//            val animName = getAnimationForCondition(d.conditionCode, isDay = true)
//            val resId = getResIdForAnimation(animName, isDay = true)
//
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier
//                    .width(72.dp)
//                    .padding(end = 12.dp)
//            ) {
//                Text(text = d.date.substringAfterLast('-'), color = Color.White, fontSize = 12.sp)
//                WeatherGifAnimation(resourceId = resId, modifier = Modifier.height(48.dp))
//                Text(
//                    text = "${d.maxTempC.toInt()}°/${d.minTempC.toInt()}°",
//                    color = Color.White,
//                    fontSize = 12.sp
//                )
//            }
//        }
//    }
//}

@Composable
private fun ForecastRow(daily: List<DailyForecast>, isDay: Boolean) {
    androidx.compose.foundation.lazy.LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 92.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
    ) {
        items(
            items = daily,
            key = { it.date } // ✅ stable key
        ) { d ->
            val animName = getAnimationForCondition(d.conditionCode, isDay = true)
            val resId = getResIdForAnimation(animName, isDay = true)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(72.dp)
                    .padding(end = 12.dp)
            ) {
                Text(text = d.date.substringAfterLast('-'), color = Color.White, fontSize = 12.sp)
                WeatherGifAnimation(resourceId = resId, modifier = Modifier.height(48.dp))
                Text(text = "${d.maxTempC.toInt()}°/${d.minTempC.toInt()}°", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun HourlyRow(hourly: List<HourlyForecast>, isDay: Boolean) {
    androidx.compose.foundation.lazy.LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
    ) {
        items(hourly.size, key = { i -> hourly[i].time }) { i ->
            val h = hourly[i]
            val animName = getAnimationForCondition(h.conditionCode, isDay = h.isDay)
            val resId = getResIdForAnimation(animName, isDay = h.isDay)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(72.dp)
                    .padding(end = 12.dp)
            ) {
                Text(text = hourLabel(h.time), color = Color.White, fontSize = 12.sp)
                WeatherGifAnimation(resourceId = resId, modifier = Modifier.height(40.dp))
                Text(text = "${h.tempC.toInt()}°", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}

private fun hourLabel(time: String): String {
    // "YYYY-MM-DD HH:MM" -> "1 PM"
    return try {
        val hhmm = time.substring(11, 16) // "HH:MM"
        val (hStr, mStr) = hhmm.split(":")
        var h = hStr.toInt()
        val ampm = if (h >= 12) "PM" else "AM"
        if (h == 0) h = 12 else if (h > 12) h -= 12
        "$h $ampm"
    } catch (_: Exception) { time }
}
