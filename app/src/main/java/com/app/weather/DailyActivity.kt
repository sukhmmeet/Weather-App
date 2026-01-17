package com.app.weather

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.weather.databinding.ActivityDailyBinding

class DailyActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDailyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDailyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val weatherData = LocationDataClass.weatherData
        val dailyData = weatherData.daily

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = DailyAdapter(this ,
            dailyData.daylight_duration ,
            dailyData.rain_sum ,
            dailyData.precipitation_sum ,
            dailyData.sunrise ,
            dailyData.sunset ,
            dailyData.temperature_2m_max ,
            dailyData.temperature_2m_min ,
            dailyData.time ,
            dailyData.wind_speed_10m_max
        )
        recyclerView.adapter = adapter

        binding.btnHourly.setOnClickListener {
            val intent = Intent(this , HourlyActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnCurrent.setOnClickListener {
            val intent = Intent(this , HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}