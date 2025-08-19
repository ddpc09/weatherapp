package com.floveit.weatherwidget.data.location

interface ReverseGeocoder {
    enum class LabelMode { City, Suburb, Auto } // you’ll use City/Suburb here
    suspend fun labelFor(lat: Double, lon: Double, mode: LabelMode = LabelMode.Auto): String?
}