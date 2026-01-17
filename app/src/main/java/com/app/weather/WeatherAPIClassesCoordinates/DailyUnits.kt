package com.app.weather.WeatherAPIClassesCoordinates

data class DailyUnits(
    val daylight_duration: String,
    val rain_sum: String,
    val precipitation_sum : String,
    val sunrise: String,
    val sunset: String,
    val temperature_2m_max: String,
    val temperature_2m_min: String,
    val time: String,
    val wind_speed_10m_max: String
)