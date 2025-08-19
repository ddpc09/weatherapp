package com.floveit.weatherwidget.di

import com.floveit.weatherwidget.data.ForecastMapper
import com.floveit.weatherwidget.data.WeatherMapper
import com.floveit.weatherwidget.data.WeatherRepository
import com.floveit.weatherwidget.data.WeatherRepositoryImpl
import com.floveit.weatherwidget.data.location.LocationRepository
import com.floveit.weatherwidget.data.location.LocationRepositoryImpl
import com.floveit.weatherwidget.data.location.ReverseGeocoder
import com.floveit.weatherwidget.data.location.ReverseGeocoderImpl
import com.floveit.weatherwidget.data.network.WeatherApiService
import com.floveit.weatherwidget.viewmodel.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.floveit.weatherwidget.data.network.UserAgentInterceptor
import com.floveit.weatherwidget.data.preferences.PreferencesManager
import okhttp3.OkHttpClient


val appModule = module {

    single { WeatherMapper() }

    single {
        OkHttpClient.Builder()
            .addInterceptor(UserAgentInterceptor())
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }

    single {
        WeatherRepositoryImpl(
            apiService = get(),
            mapper = get(),
            apiKey = "139850d338ae46aaafa62608253107",
            forecastMapper = get()
        ) as WeatherRepository
    }

    single<LocationRepository> { LocationRepositoryImpl() }

    single { PreferencesManager(get()) }

    single { ForecastMapper() }

    single<ReverseGeocoder> { ReverseGeocoderImpl(get()) }

    viewModel { WeatherViewModel(get(), get(), get(), get()) }

}
