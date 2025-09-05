package com.floveit.weatherwidget.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.floveit.weatherwidget.R
import com.floveit.weatherwidget.data.DailyForecast
import com.floveit.weatherwidget.data.HourlyForecast
import com.floveit.weatherwidget.viewmodel.WeatherViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.util.Locale


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun WeatherWidgetScreen(viewModel: WeatherViewModel = org.koin.androidx.compose.koinViewModel()) {
//    val state by viewModel.weatherState.collectAsState()
//    val query by viewModel.searchQuery.collectAsState()
//    val suggestions by viewModel.locationSuggestions.collectAsState()
//
//    val conditionText = weatherConditionDescriptions[state.conditionCode]?.let {
//        if (state.isDay) it.first else it.second
//    } ?: "Unknown"
//
//    val animationName = getAnimationForCondition(state.conditionCode, state.isDay)
//    val animationResId = getResIdForAnimation(animationName, isDay = state.isDay)
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Black)
//            .verticalScroll(rememberScrollState())
//    ) {
//        CurrentWeatherSection(
//            state = state,
//            conditionText = conditionText,
//            animationResId = animationResId,
//            query = query,
//            suggestions = suggestions,
//            onQueryChange = viewModel::updateSearchQuery,
//            onCitySelected = viewModel::onLocationSelected
//        )
//
//        if (state.daily.isNotEmpty()) {
//            Spacer(Modifier.height(12.dp))
//            ForecastRow(daily = state.daily, isDay = true)
//        }
//
//        if (state.hourly.isNotEmpty()) {
//            Spacer(Modifier.height(12.dp))
//            Image(
//                painter = painterResource(R.drawable.hourlyforecast),
//                contentDescription = "Humidity",
//                modifier = Modifier.size(48.dp)
//            )
//
//            Text(
//                text = "Next 24 hours",
//                color = Color.White,
//                fontSize = 14.sp,
//                modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
//            )
//            HourlyRow(hourly = state.hourly, isDay = state.isDay)
//        }
//        Spacer(Modifier.height(12.dp))
//    }
//}
//
//fun getResIdForAnimation(name: String, isDay: Boolean): Int {
//    return when (name) {
//        "sunny" -> R.raw.sunny1
//        "partly_cloudy" -> R.raw.paartlycloudy
//        "overcast" -> R.raw.overcast1
//        "mist_fog" -> R.raw.fog1
//        "rainy" -> R.raw.rain
//        "snowy" -> R.raw.snowy
//        "thunderstorm" -> R.raw.thunder2
//        "clear_night" -> R.raw.clearnight
//        "night_cloudy" -> R.raw.cloudynight
//        "night_rainy" -> R.raw.rain
//        "night_thunderstorm" -> R.raw.thunder2
//        else -> if(isDay){R.raw.sunny1} else{R.raw.clearnight}
//    }
//}
//
////fun getResIdForAnimation(name: String, isDay: Boolean): Int {
////    val gifname = R.raw.fog1new
////    return when (name) {
////        "sunny" -> gifname
////        "partly_cloudy" -> gifname
////        "overcast" -> gifname
////        "mist_fog" -> gifname
////        "rainy" -> gifname
////        "snowy" -> gifname
////        "thunderstorm" -> gifname
////        "clear_night" -> gifname
////        "night_cloudy" -> gifname
////        "night_rainy" -> gifname
////        "night_thunderstorm" -> gifname
////        else -> if(isDay){gifname} else{gifname}
////    }
////}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CurrentWeatherSection(
//    state: WeatherUiState,
//    conditionText: String,
//    animationResId: Int,
//    query: String,
//    suggestions: List<com.floveit.weatherwidget.data.location.PlaceSuggestion>,
//    onQueryChange: (String) -> Unit,
//    onCitySelected: (com.floveit.weatherwidget.data.location.PlaceSuggestion) -> Unit
//) {
//    val focusManager = LocalFocusManager.current
//    val keyboard = LocalSoftwareKeyboardController.current
//    val tfFocus = remember { FocusRequester() }
//
//    var editing by rememberSaveable { mutableStateOf(false) }
//    var tfFocused by remember { mutableStateOf(false) }
//
//    // Back button exits edit mode
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
//        // GIF (kept stable; only resId flows in)
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
//        // City row — tap to edit
//        if (!editing) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier
//                    .clip(RoundedCornerShape(8.dp))
//                    .clickable {
//                        editing = true
//                        onQueryChange(state.city.ifBlank { "" }) // prefill with current city if you want
//                    }
//                    .padding(horizontal = 8.dp, vertical = 6.dp)
//            ) {
//                Text(
//                    text = if (state.city.isBlank()) "Set city" else state.city,
//                    fontSize = 20.sp,
//                    color = Color.White
//                )
//                Spacer(Modifier.width(6.dp))
//                // (Optional) tiny hint that it's editable
//                Text("• edit", fontSize = 12.sp, color = Color.LightGray)
//            }
//        } else {
//            // Inline editor (replaces the city text while editing)
//            OutlinedTextField(
//                value = query,
//                onValueChange = onQueryChange,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .focusRequester(tfFocus)
//                    .onFocusChanged { tfFocused = it.isFocused },
//                singleLine = true,
//                label = { Text("Enter city / area") },
//                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
//                keyboardActions = KeyboardActions(
//                    onDone = {
//                        // Pick the first suggestion on Enter (keeps coord-precision)
//                        suggestions.firstOrNull()?.let { first ->
//                            onCitySelected(first)
//                        }
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
//                    Text(
//                        "Cancel",
//                        color = Color.White,
//                        modifier = Modifier
//                            .clip(RoundedCornerShape(6.dp))
//                            .clickable {
//                                editing = false
//                                onQueryChange("")
//                                focusManager.clearFocus()
//                                keyboard?.hide()
//                            }
//                            .padding(horizontal = 8.dp, vertical = 4.dp),
//                        fontSize = 12.sp
//                    )
//                }
//            )
//
//            // Auto-focus + keyboard when entering edit mode
//            LaunchedEffect(editing) {
//                if (editing) {
//                    tfFocus.requestFocus()
//                    keyboard?.show()
//                }
//            }
//
//            // Suggestions (only when focused)
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
//                                text = s.displayName,
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
//        Spacer(Modifier.height(8.dp))
//
//        // Weather details text
//        if (state.error != null) {
//            Text("Error: ${state.error}", color = Color.Red)
//        } else {
//            Text(text = conditionText, fontSize = 18.sp, color = Color.White)
//            Text(text = "${state.temperature}°C", fontSize = 36.sp, color = Color.White)
//
//            Spacer(Modifier.height(4.dp))
////            Row(
////                horizontalArrangement = Arrangement.spacedBy(12.dp),
////                modifier = Modifier.wrapContentWidth()
////            ) {
////                Text(text = "Feels: ${state.feelsLike}°C", color = Color.White)
////                Text(text = "Humidity: ${state.humidity}%", color = Color.White)
//////                Text(text = "Wind: ${state.windSpeed} km/h", color = Color.White)
////            }
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(12.dp),
//                modifier = Modifier.wrapContentWidth()
//            ) {
//                // Location row at top (if you want a pin icon before the city name):
//                Image(
//                    painter = painterResource(R.drawable.locatinon),
//                    contentDescription = "Humidity",
//                    modifier = Modifier.size(48.dp)
//                )
//                Text(text = if (state.city.isBlank()) "Set city" else state.city, color = Color.White)
//
//                // Feels like
//                Image(
//                    painter = painterResource(R.drawable.feelslike),
//                    contentDescription = "Humidity",
//                    modifier = Modifier.size(48.dp)
//                )
//                Text(text = "Feels: ${state.feelsLike}°C", color = Color.White)
//
//                // Humidity
//                Image(
//                    painter = painterResource(R.drawable.humidity),
//                    contentDescription = "Humidity",
//                    modifier = Modifier.size(48.dp)
//                )
//                Text(text = "Humidity: ${state.humidity}%", color = Color.White)
//
//                // Wind
////                Icon(
////                    painter = painterResource(R.drawable.ic_wind),
////                    contentDescription = "Wind",
////                    tint = Color.White,
////                    modifier = Modifier.size(16.dp)
////                )
////                Text(text = "Wind: ${state.windSpeed} km/h", color = Color.White)
//            }
//        }
//    }
//}
//
//
//
//
//@Composable
//private fun ForecastRow(daily: List<DailyForecast>, isDay: Boolean) {
//    androidx.compose.foundation.lazy.LazyRow(
//        modifier = Modifier
//            .fillMaxWidth()
//            .heightIn(min = 92.dp),
//        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
//    ) {
//        items(
//            items = daily,
//            key = { it.date } // ✅ stable key
//        ) { d ->
//            val animName = getAnimationForCondition(d.conditionCode, isDay = true)
////            val resId = getResIdForAnimation(animName, isDay = true)
//            val resId = getResIdForForecastAnimation(animName, isDay = true)
//
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier
//                    .width(72.dp)
//                    .padding(end = 12.dp)
//            ) {
//                Text(text = d.date.substringAfterLast('-'), color = Color.White, fontSize = 12.sp)
//                WeatherGifAnimation(resourceId = resId, modifier = Modifier.height(48.dp))
//                Text(text = "${d.maxTempC.toInt()}°/${d.minTempC.toInt()}°", color = Color.White, fontSize = 12.sp)
//            }
//        }
//    }
//}
//
//@Composable
//private fun HourlyRow(hourly: List<HourlyForecast>, isDay: Boolean) {
//    androidx.compose.foundation.lazy.LazyRow(
//        modifier = Modifier
//            .fillMaxWidth()
//            .heightIn(min = 100.dp),
//        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
//    ) {
//        items(hourly.size, key = { i -> hourly[i].time }) { i ->
//            val h = hourly[i]
//            val animName = getAnimationForCondition(h.conditionCode, isDay = h.isDay)
////            val resId = getResIdForAnimation(animName, isDay = h.isDay)
//            val resId = getResIdForHourlyAnimation(animName, isDay = h.isDay)
//
//
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier
//                    .width(72.dp)
//                    .padding(end = 12.dp)
//            ) {
//                Text(text = hourLabel(h.time), color = Color.White, fontSize = 12.sp)
//                WeatherGifAnimation(resourceId = resId, modifier = Modifier.height(40.dp))
//                Text(text = "${h.tempC.toInt()}°", color = Color.White, fontSize = 12.sp)
//            }
//        }
//    }
//}
//
//private fun hourLabel(time: String): String {
//    // "YYYY-MM-DD HH:MM" -> "1 PM"
//    return try {
//        val hhmm = time.substring(11, 16) // "HH:MM"
//        val (hStr, mStr) = hhmm.split(":")
//        var h = hStr.toInt()
//        val ampm = if (h >= 12) "PM" else "AM"
//        if (h == 0) h = 12 else if (h > 12) h -= 12
//        "$h $ampm"
//    } catch (_: Exception) { time }
//}




//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun WeatherWidgetScreen(viewModel: WeatherViewModel = org.koin.androidx.compose.koinViewModel()) {
//    val state by viewModel.weatherState.collectAsState()
//    val query by viewModel.searchQuery.collectAsState()
//    val suggestions by viewModel.locationSuggestions.collectAsState()
//
//    val conditionText = weatherConditionDescriptions[state.conditionCode]?.let {
//        if (state.isDay) it.first else it.second
//    } ?: "Unknown"
//
//    val animationName = getAnimationForCondition(state.conditionCode, state.isDay)
//    val animationResId = getResIdForAnimation(animationName, isDay = state.isDay)
//
//    var hourlyExpanded by rememberSaveable { mutableStateOf(false) }
//    var weeklyExpanded by rememberSaveable { mutableStateOf(false) }
//
//    val hourlyRot by animateFloatAsState(
//        targetValue = if (hourlyExpanded) 180f else 0f, label = "hourlyRot"
//    )
//    val weeklyRot by animateFloatAsState(
//        targetValue = if (weeklyExpanded) 180f else 0f, label = "weeklyRot"
//    )
//
//    // ⬇️ Use ONE vertical scroller for the whole screen
//    androidx.compose.foundation.lazy.LazyColumn(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Black),
//        contentPadding = PaddingValues(bottom = 12.dp)
//    ) {
//        // ── Current (animation → description → temperature → metrics)
//        item {
//            CurrentWeatherSection(
//                state = state,
//                conditionText = conditionText,
//                animationResId = animationResId,
//                query = query,
//                suggestions = suggestions,
//                onQueryChange = viewModel::updateSearchQuery,
//                onCitySelected = viewModel::onLocationSelected
//            )
//        }
//
//        // ── Hourly header (icon left, text right)
////        if (state.hourly.isNotEmpty()) {
////            item {
////                Spacer(Modifier.height(12.dp))
////                Row(
////                    verticalAlignment = Alignment.CenterVertically,
////                    modifier = Modifier.padding(horizontal = 16.dp)
////                ) {
////                    Image(
////                        painter = painterResource(R.drawable.hourlyforecast), // your icon
////                        contentDescription = "Hourly forecast",
////                        modifier = Modifier.size(18.dp)
////                    )
////                    Spacer(Modifier.width(8.dp))
////                    Text(
////                        text = "Hourly forecast",
////                        color = Color.White,
////                        fontSize = 14.sp
////                    )
////                }
////            }
////
////            // ── Hourly row (time top / icon middle / value bottom)
////            item {
////                Spacer(Modifier.height(6.dp))
////                HourlyRow(hourly = state.hourly, isDay = state.isDay)
////            }
////        }
//        // ── Hourly header (icon left • arrow center • text right) — collapsible
//        if (state.hourly.isNotEmpty()) {
//            item {
//                Spacer(Modifier.height(12.dp))
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable { hourlyExpanded = !hourlyExpanded }
//                        .padding(horizontal = 16.dp, vertical = 8.dp)
//                ) {
//                    // left icon
//                    Image(
//                        painter = painterResource(R.drawable.hourlyforecast), // your icon
//                        contentDescription = "Hourly",
//                        modifier = Modifier
//                            .align(Alignment.CenterStart)
//                            .size(18.dp)
//                    )
//                    // center arrow
//                    Image(
//                        painter = painterResource(R.drawable.arrow),   // add a chevron asset
//                        contentDescription = if (hourlyExpanded) "Collapse" else "Expand",
//                        modifier = Modifier
//                            .align(Alignment.Center)
//                            .size(36.dp)
//                            .rotate(hourlyRot)
//                    )
//                    // right text
//                    Text(
//                        text = "Hourly forecast",
//                        color = Color.White,
//                        fontSize = 14.sp,
//                        modifier = Modifier.align(Alignment.CenterEnd)
//                    )
//                }
//            }
//
//            // body: only when expanded
//            if (hourlyExpanded) {
//                item {
//                    Spacer(Modifier.height(6.dp))
//                    HourlyRow(hourly = state.hourly, isDay = state.isDay)
//                }
//            }
//        }
//
//
//        // ── Weekly header
////        if (state.daily.isNotEmpty()) {
////            item {
////                Spacer(Modifier.height(12.dp))
////                Text(
////                    text = "Weekly forecast",
////                    color = Color.White,
////                    fontSize = 14.sp,
////                    modifier = Modifier.padding(horizontal = 16.dp)
////                )
////                Spacer(Modifier.height(6.dp))
////            }
////
////            // ── Weekly rows: icon + date (left) …… max/min (right)
////            items(
////                items = state.daily,
////                key = { it.date }
////            ) { d ->
////                val name = getAnimationForCondition(d.conditionCode, isDay = true)
////                val resId = getResIdForForecastAnimation(name, isDay = true) // or getResIdForAnimation
////
////                Row(
////                    verticalAlignment = Alignment.CenterVertically,
////                    modifier = Modifier
////                        .fillMaxWidth()
////                        .padding(horizontal = 16.dp, vertical = 8.dp)
////                ) {
////                    WeatherGifAnimation(resourceId = resId, modifier = Modifier.size(36.dp))
////                    Spacer(Modifier.width(10.dp))
////                    Text(
////                        text = d.date, // or your preferred short label
////                        color = Color.White,
////                        fontSize = 13.sp
////                    )
////                    Spacer(Modifier.weight(1f))
////                    Row {
////                        Text("${d.maxTempC.toInt()}°", color = Color.White, fontSize = 13.sp)
////                        Spacer(Modifier.width(10.dp))
////                        Text("${d.minTempC.toInt()}°", color = Color.LightGray, fontSize = 13.sp)
////                    }
////                }
////            }
////        }
//        // ── Weekly header (arrow center • text right) — collapsible
//        if (state.daily.isNotEmpty()) {
//            item {
//                Spacer(Modifier.height(12.dp))
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable { weeklyExpanded = !weeklyExpanded }
//                        .padding(horizontal = 16.dp, vertical = 8.dp)
//                ) {
//                    // center arrow
//                    Image(
//                        painter = painterResource(R.drawable.arrow),  // same chevron asset
//                        contentDescription = if (weeklyExpanded) "Collapse" else "Expand",
//                        modifier = Modifier
//                            .align(Alignment.Center)
//                            .size(36.dp)
//                            .rotate(weeklyRot)
//                    )
//                    // right text
//                    Text(
//                        text = "Weekly forecast",
//                        color = Color.White,
//                        fontSize = 14.sp,
//                        modifier = Modifier.align(Alignment.CenterEnd)
//                    )
//                }
//            }
//
//            // body: only when expanded (emit rows into SAME LazyColumn)
//            if (weeklyExpanded) {
//                items(
//                    items = state.daily,
//                    key = { it.date }
//                ) { d ->
//                    val name = getAnimationForCondition(d.conditionCode, isDay = true)
//                    val resId = getResIdForForecastAnimation(name, isDay = true) // or your default
//
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp, vertical = 8.dp)
//                    ) {
//                        WeatherGifAnimation(resourceId = resId, modifier = Modifier.size(36.dp))
//                        Spacer(Modifier.width(10.dp))
//                        Text(text = d.date, color = Color.White, fontSize = 13.sp)
//                        Spacer(Modifier.weight(1f))
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Text("${d.maxTempC.toInt()}°", color = Color.White, fontSize = 13.sp)
//                            Spacer(Modifier.width(10.dp))
//                            Text("${d.minTempC.toInt()}°", color = Color.LightGray, fontSize = 13.sp)
//                        }
//                    }
//                }
//            }
//        }
//
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherWidgetScreen(viewModel: WeatherViewModel = koinViewModel()) {
    val state by viewModel.weatherState.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    val suggestions by viewModel.locationSuggestions.collectAsState()

    val conditionText = weatherConditionDescriptions[state.conditionCode]?.let {
        if (state.isDay) it.first else it.second
    } ?: "Unknown"

    val animationName = getAnimationForCondition(state.conditionCode, state.isDay)
    val animationResId = getResIdForAnimation(animationName, isDay = state.isDay)

    // One shared toggle for BOTH Hourly + Weekly
    var forecastsExpanded by rememberSaveable { mutableStateOf(false) }
    val forecastsRot by animateFloatAsState(
        targetValue = if (forecastsExpanded) 180f else 0f,
        label = "forecastsRot"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentPadding = PaddingValues(bottom = 12.dp)
    ) {
        // ── Current (animation → description → metrics → location/edit)
        item {
            CurrentWeatherSection(
                state = state,
                conditionText = conditionText,
                animationResId = animationResId,
                query = query,
                suggestions = suggestions,
                onQueryChange = viewModel::updateSearchQuery,
                onCitySelected = viewModel::onLocationSelected
            )
        }

        // ── One header that controls BOTH Hourly + Weekly (collapsed by default)
        item {
            Spacer(Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { forecastsExpanded = !forecastsExpanded }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // left icon
                Image(
                    painter = painterResource(R.drawable.hourlyforecast),
                    contentDescription = "Forecasts",
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(18.dp)
                )
                // center arrow
                Image(
                    painter = painterResource(R.drawable.arrow), // ensure this exists
                    contentDescription = if (forecastsExpanded) "Collapse" else "Expand",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(36.dp)
                        .rotate(forecastsRot)
                )
                // right text
//                Text(
//                    text = "Forecasts",
//                    color = Color.White,
//                    fontSize = 14.sp,
//                    modifier = Modifier.align(Alignment.CenterEnd)
//                )
            }
        }

        // ── Body: render Hourly then Weekly ONLY when expanded
        if (forecastsExpanded) {
            if (state.hourly.isNotEmpty()) {
                // Hourly label (non-clickable)
                item {
                    Spacer(Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.hourlyforecast),
                            contentDescription = "Hourly",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Hourly forecast", color = Color.White, fontSize = 14.sp)
                    }
                }
                // Hourly list (horizontal = fine)
                item {
                    HourlyRow(hourly = state.hourly, isDay = state.isDay)
                }
            }

            if (state.daily.isNotEmpty()) {
                // Weekly label
                item {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "Weekly forecast",
                        color = Color.White,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(Modifier.height(6.dp))
                }

                // Weekly rows emitted into the SAME LazyColumn
                weeklyForecastItems(state.daily)
            }
        }
    }
}

//private fun LazyListScope.weeklyForecastItems(daily: List<DailyForecast>) {
//    items(
//        items = daily,
//        key = { it.date }
//    ) { d ->
//        val name = getAnimationForCondition(d.conditionCode, isDay = true)
//        val resId = getResIdForForecastAnimation(name, isDay = true)
//
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp, vertical = 8.dp)
//        ) {
//            WeatherGifAnimation(resourceId = resId, modifier = Modifier.size(36.dp))
//            Spacer(Modifier.width(10.dp))
//            Text(text = d.date, color = Color.White, fontSize = 13.sp)
//            Spacer(Modifier.weight(1f))
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Text("${d.maxTempC.toInt()}°", color = Color.White, fontSize = 13.sp)
//                Spacer(Modifier.width(10.dp))
//                Text("${d.minTempC.toInt()}°", color = Color.LightGray, fontSize = 13.sp)
//            }
//        }
//    }
//}

private fun LazyListScope.weeklyForecastItems(daily: List<DailyForecast>) {
    items(
        items = daily,
        key = { it.date }
    ) { d ->
        val name = getAnimationForCondition(d.conditionCode, isDay = true)
        val resId = getResIdForForecastAnimation(name, isDay = true)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            WeatherGifAnimation(resourceId = resId, modifier = Modifier.size(36.dp))
            Spacer(Modifier.width(10.dp))

            // ⬇️ was: Text(text = d.date, ...)
            Text(text = dailyLabel(d.date), color = Color.White, fontSize = 13.sp)

            Spacer(Modifier.weight(1f))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("${d.maxTempC.toInt()}°", color = Color.White, fontSize = 13.sp)
                Spacer(Modifier.width(10.dp))
                Text("${d.minTempC.toInt()}°", color = Color.LightGray, fontSize = 13.sp)
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
        else -> if (isDay) R.raw.sunny1 else R.raw.clearnight
    }
}

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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Animation (top)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentAlignment = Alignment.Center
        ) { WeatherGifAnimation(resourceId = animationResId) }

        Spacer(Modifier.height(8.dp))

        // (Optional) City edit affordance; keep or remove as you like
//        if (!editing) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier
//                    .clip(RoundedCornerShape(8.dp))
//                    .clickable { editing = true; onQueryChange(state.city.ifBlank { "" }) }
//                    .padding(horizontal = 8.dp, vertical = 6.dp)
//            ) {
//
//                Text(
//                    text = if (state.city.isBlank()) "Set city" else state.city,
//                    fontSize = 18.sp, color = Color.White
//                )
//                Spacer(Modifier.width(6.dp))
//                Text("• edit", fontSize = 12.sp, color = Color.LightGray)
//            }
//        } else {
//            OutlinedTextField(
//                value = query,
//                onValueChange = onQueryChange,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .focusRequester(tfFocus)
//                    .onFocusChanged { tfFocused = it.isFocused },
//                singleLine = true,
//                label = { Text("Enter city / area") },
//                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
//                keyboardActions = KeyboardActions(
//                    onDone = {
//                        suggestions.firstOrNull()?.let(onCitySelected)
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
//                    Text(
//                        "Cancel", color = Color.White,
//                        modifier = Modifier
//                            .clip(RoundedCornerShape(6.dp))
//                            .clickable {
//                                editing = false
//                                onQueryChange("")
//                                focusManager.clearFocus()
//                                keyboard?.hide()
//                            }
//                            .padding(horizontal = 8.dp, vertical = 4.dp),
//                        fontSize = 12.sp
//                    )
//                }
//            )
//            LaunchedEffect(editing) { if (editing) { tfFocus.requestFocus(); keyboard?.show() } }
//
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
//                                text = s.displayName,
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
//                                color = Color.White, fontSize = 14.sp
//                            )
//                        }
//                    }
//                }
//            }
//        }

        Spacer(Modifier.height(8.dp))
        // Description (below animation / editor)
        Text(text = conditionText, fontSize = 18.sp, color = Color.White)
        Spacer(Modifier.height(8.dp))

// One row: Temperature, Feels like, Humidity
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.Top,
            modifier = Modifier.wrapContentWidth()
        ) {

            Text(text = "${state.temperature}°C", fontSize = 36.sp, color = Color.White)

            MetricTriple(
                iconRes = R.drawable.feelslike,     // ← your “feels like” icon
                label = "Feels like",
                value = "${state.feelsLike}°C"
            )
            MetricTriple(
                iconRes = R.drawable.humidity,      // ← your humidity icon
                label = "Humidity",
                value = "${state.humidity}%"
            )
        }
        if (!editing) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { editing = true; onQueryChange(state.city.ifBlank { "" }) }
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {

                Image(
                    painter = painterResource(R.drawable.location), // <-- ensure drawable name is correct
                    contentDescription = "Location",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = if (state.city.isBlank()) "Set city" else state.city,
                    fontSize = 18.sp, color = Color.White
                )
                Spacer(Modifier.width(6.dp))
                Text("• edit", fontSize = 12.sp, color = Color.LightGray)
            }
        } else {
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
                        suggestions.firstOrNull()?.let(onCitySelected)
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
                        "Cancel", color = Color.White,
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
            LaunchedEffect(editing) { if (editing) { tfFocus.requestFocus(); keyboard?.show() } }

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
                                color = Color.White, fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

    }
}

@Composable
private fun MetricTriple(
    iconRes: Int,
    label: String,
    value: String,
    iconSize: Dp = 32.dp
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.widthIn(min = 96.dp)
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = label,
            modifier = Modifier.size(iconSize)
        )
        Spacer(Modifier.height(2.dp))
        Text(text = label, color = Color.White, fontSize = 12.sp)
        Text(text = value, color = Color.White, fontSize = 14.sp)
    }
}



// ───────────────────────────────────────────────────────────────────────────────
// Hourly row: time (top) / icon (middle) / value (bottom)
// ───────────────────────────────────────────────────────────────────────────────
@Composable
private fun HourlyRow(hourly: List<HourlyForecast>, isDay: Boolean) {
    androidx.compose.foundation.lazy.LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(hourly.size, key = { i -> hourly[i].time }) { i ->
            val h = hourly[i]
            val animName = getAnimationForCondition(h.conditionCode, isDay = h.isDay)
            val resId = getResIdForHourlyAnimation(animName, isDay = h.isDay) // or getResIdForAnimation

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


// ───────────────────────────────────────────────────────────────────────────────
// Weekly forecast as a COLUMN: icon + date on left …… max/min on right
// ───────────────────────────────────────────────────────────────────────────────
//@Composable
//private fun WeeklyForecastColumn(daily: List<DailyForecast>) {
//    LazyColumn(
//        modifier = Modifier
//            .fillMaxWidth()
//            .heightIn(min = 92.dp),
//        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
//    ) {
//        items(items = daily, key = { it.date }) { d ->
//            val animName = getAnimationForCondition(d.conditionCode, isDay = true)
//            val resId = getResIdForForecastAnimation(animName, isDay = true)
//
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 8.dp)
//            ) {
//                // Left: small icon + date
//                WeatherGifAnimation(resourceId = resId, modifier = Modifier.size(36.dp))
//                Spacer(Modifier.width(10.dp))
//                Text(
//                    text = d.date, // show full date; change to label if you prefer
//                    color = Color.White,
//                    fontSize = 13.sp
//                )
//
//                // Gap in the middle
//                Spacer(Modifier.weight(1f))
//
//                // Right: High / Low with a small gap
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Text(
//                        text = "${d.maxTempC.toInt()}°",
//                        color = Color.White,
//                        fontSize = 13.sp
//                    )
//                    Spacer(Modifier.width(10.dp))
//                    Text(
//                        text = "${d.minTempC.toInt()}°",
//                        color = Color.LightGray,
//                        fontSize = 13.sp
//                    )
//                }
//            }
//        }
//    }
//}

private fun hourLabel(time: String): String {
    return try {
        val hhmm = time.substring(11, 16)
        val (hStr, _) = hhmm.split(":")
        var h = hStr.toInt()
        val ampm = if (h >= 12) "PM" else "AM"
        if (h == 0) h = 12 else if (h > 12) h -= 12
        "$h $ampm"
    } catch (_: Exception) { time }
}

private fun dailyLabel(dateStr: String): String {
    return try {
        val today = LocalDate.now()
        val d = LocalDate.parse(dateStr) // expects "YYYY-MM-DD"
        val diff = ChronoUnit.DAYS.between(today, d).toInt()
        when (diff) {
            0 -> "Today"
            1 -> "Tomorrow"
//            2 -> "Day after tomorrow"
            else -> d.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault())
        }
    } catch (_: DateTimeParseException) {
        // Fallback if format isn't parseable
        dateStr
    }
}

