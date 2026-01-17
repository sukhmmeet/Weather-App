package com.app.weather

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import java.util.Calendar;
import java.util.Date;
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.weather.databinding.ActivityHomeBinding
import java.text.SimpleDateFormat
import java.util.Locale

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val weatherData = LocationDataClass.weatherData
        val rain = weatherData.current.rain
        val temperature = weatherData.current.temperature_2m
        val windD = weatherData.current.wind_direction_10m
        val windS = weatherData.current.wind_speed_10m
        val CurrentTime = weatherData.current.time
        val precipitation = weatherData.current.precipitation
        val showers =weatherData.current.showers


        val hourlyData = weatherData.hourly

        val rainPercentage = rainPercentage(rain , precipitation)

        if (windS <20){
            if(rainPercentage>75){
                binding.weatherStatusImg.setImageResource(R.drawable.storm)
            }else if (rainPercentage>50) {
                binding.weatherStatusImg.setImageResource(R.drawable.rainy)
            }else if (rainPercentage>10){
                binding.weatherStatusImg.setImageResource(R.drawable.cloudy)
            }else {
                binding.weatherStatusImg.setImageResource(R.drawable.sun)
            }
        }else {
            binding.weatherStatusImg.setImageResource(R.drawable.windy)
        }

        binding.rainTextViewinP.text = "Rain : ${rainPercentage(rain , precipitation)}%"
        binding.rainTextViewinMM.text = "Rain : ${rain}mm"
        binding.tempTextView.text = "${temperature}\u2103"
        binding.windSpeedTextView.text = "Wind Speed : ${windS}km/h"
        val directionOfWind = getFullCardinalDirection(windD)
        binding.windDirectionTextView.text = "Wind Direction : $directionOfWind"
        binding.showerTextView.text = "Showers : ${showers}mm"

        val timeForData = Calendar.getInstance().time
        val date = android.text.format.DateFormat.format("dd/MM/yyyy", timeForData).toString()
        val time = getCurrentTimeIn12HourFormat()
        binding.timeTextView.text = timeForData.toString()


        binding.btnHourly.setOnClickListener {
            val intent = Intent(this, HourlyActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnDaily.setOnClickListener {
            val intent = Intent(this, DailyActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun getFullCardinalDirection(degrees: Int): String {
        val preciseDirections = arrayOf(
            "North", "North-Northeast", "Northeast", "East-Northeast",
            "East", "East-Southeast", "Southeast", "South-Southeast",
            "South", "South-Southwest", "Southwest", "West-Southwest",
            "West", "West-Northwest", "Northwest", "North-Northwest"
        )

        val normalizedDegrees = (degrees % 360 + 360) % 360
        val index = (normalizedDegrees * 16 / 360 + 0.5).toInt() % 16

        // Check if it's exactly North, East, South, or West
        return when (normalizedDegrees) {
            0 -> "North"
            90 -> "East"
            180 -> "South"
            270 -> "West"
            else -> preciseDirections[index]
        }
    }

    @SuppressLint("DefaultLocale")
    fun rainPercentage(rainMm: Double, precipitationMm: Double): Double {
        if (precipitationMm <= 0) {
            return 0.0
        }

        val rainPercentage = (rainMm / precipitationMm) * 100

        return rainPercentage.toDouble()
    }

    fun getCurrentTimeIn12HourFormat(): String {
        val currentTime = Date()
        val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return formatter.format(currentTime)
    }

}