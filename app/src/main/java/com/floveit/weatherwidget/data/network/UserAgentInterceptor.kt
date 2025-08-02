package com.floveit.weatherwidget.data.network

import okhttp3.Interceptor
import okhttp3.Response

class UserAgentInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .header("User-Agent", "WeatherWidget/1.0 (Android; Kotlin)")
            .build()
        return chain.proceed(newRequest)
    }
}