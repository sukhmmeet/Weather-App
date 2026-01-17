package com.app.weather

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.weather.databinding.ActivityHourlyBinding

class HourlyActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHourlyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHourlyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnCurrent.setOnClickListener {
            val intent = Intent(this , HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnDaily.setOnClickListener {
            val intent = Intent(this , DailyActivity::class.java)
            startActivity(intent)
            finish()
        }
        val weatherData = LocationDataClass.weatherData
        val hourlyData = weatherData.hourly

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = HourlyAdapter(
            this ,
            hourlyData.rain ,
            hourlyData.showers ,
            hourlyData.temperature_2m ,
            hourlyData.temperature_80m ,
            hourlyData.time,
            hourlyData.wind_direction_10m,
            hourlyData.wind_speed_10m
        )


    }
}