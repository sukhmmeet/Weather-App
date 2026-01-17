package com.app.weather.WeatherAPIClassesCoordinates

data class Daily(
    val daylight_duration: List<Double>,
    val rain_sum: List<Double>,
    val precipitation_sum : List<Double>,
    val sunrise: List<String>,
    val sunset: List<String>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val time: List<String>,
    val wind_speed_10m_max: List<Double>
)