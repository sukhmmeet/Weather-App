package com.app.weather.WeatherAPIClassesCoordinates

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiInterface {
    @GET("forecast")
    fun getWeatherData(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("current") currentParameters: String?,
        @Query("hourly") hourlyParameters: String?,
        @Query("daily") dailyParameters: String?,
        @Query("timezone") timezone: String?
    ): Call<WeatherData?>?
}

