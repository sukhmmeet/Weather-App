package com.app.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.weather.WeatherAPIClassesCoordinates.WeatherApiInterface
import com.app.weather.WeatherAPIClassesCoordinates.WeatherData
import com.app.weather.databinding.ActivityMainBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        requestLocationPermission()
    }

    // ---------------- PERMISSION ----------------

    private fun requestLocationPermission() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->

            val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (granted) {
                getUserLocation()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    // ---------------- LOCATION ----------------

    @SuppressLint("MissingPermission")
    private fun getUserLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        ).addOnCompleteListener { task ->

            val location = task.result

            if (location != null) {
                val latitude = location.latitude.toString()
                val longitude = location.longitude.toString()

                LocationDataClass.latitude = latitude
                LocationDataClass.longitude = longitude

                fetchWeatherData(latitude, longitude)

            } else {
                Toast.makeText(
                    this,
                    "Unable to fetch location. Please turn on GPS.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // ---------------- WEATHER API ----------------

    private fun fetchWeatherData(lat: String, lon: String) {

        val currentParameters =
            "temperature_2m,precipitation,rain,showers,wind_speed_10m,wind_direction_10m"

        val hourlyParameters =
            "temperature_2m,rain,showers,wind_speed_10m,wind_direction_10m,temperature_80m"

        val dailyParameters =
            "temperature_2m_max,temperature_2m_min,sunrise,sunset,daylight_duration,rain_sum,precipitation_sum,wind_speed_10m_max"

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiInterface::class.java)

        retrofit.getWeatherData(
            lat,
            lon,
            currentParameters,
            hourlyParameters,
            dailyParameters,
            "auto"
        )?.enqueue(object : Callback<WeatherData?> {

            override fun onResponse(
                call: Call<WeatherData?>,
                response: Response<WeatherData?>
            ) {
                val weatherData = response.body()

                if (weatherData != null) {
                    LocationDataClass.weatherData = weatherData

                    startActivity(
                        Intent(this@MainActivity, HomeActivity::class.java)
                    )
                    finish()

                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Weather data not available",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<WeatherData?>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Network error",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
