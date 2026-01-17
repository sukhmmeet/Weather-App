package com.app.weather.WeatherAPIClassesCoordinates

data class Current(
    val interval: Int,
    val precipitation: Double,
    val rain: Double,
    val showers: Double,
    val temperature_2m: Double,
    val time: String,
    val wind_direction_10m: Int,
    val wind_speed_10m: Double
)