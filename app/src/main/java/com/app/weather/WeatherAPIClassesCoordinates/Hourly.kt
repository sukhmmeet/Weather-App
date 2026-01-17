package com.app.weather.WeatherAPIClassesCoordinates

data class Hourly(
    val rain: List<Double>,
    val showers: List<Double>,
    val temperature_2m: List<Double>,
    val temperature_80m: List<Double>,
    val time: List<String>,
    val wind_direction_10m: List<Int>,
    val wind_speed_10m: List<Double>
)