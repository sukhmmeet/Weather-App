package com.app.weather

import com.app.weather.WeatherAPIClassesCoordinates.WeatherData

class LocationDataClass {
    companion object {
        lateinit var longitude : String
        lateinit var latitude : String
        lateinit var weatherData : WeatherData
    }
}