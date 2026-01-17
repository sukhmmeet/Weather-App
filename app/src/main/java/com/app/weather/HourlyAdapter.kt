package com.app.weather

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.app.weather.WeatherAPIClassesCoordinates.Hourly
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HourlyAdapter (
    val context : Context,
    val rain: List<Double>,
    val showers: List<Double>,
    val temperature_2m: List<Double>,
    val temperature_80m: List<Double>,
    val time: List<String>,
    val wind_direction_10m: List<Int>,
    val wind_speed_10m: List<Double>
) : RecyclerView.Adapter<HourlyAdapter.viewHolder>() {
    class viewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val temperatureTV = itemView.findViewById<TextView>(R.id.tempTextView)
        val windSpeedTV = itemView.findViewById<TextView>(R.id.WindSpeedTextViewKMH)
        val weatherImg = itemView.findViewById<ImageView>(R.id.weatherStatusImg)
        val windDirectionTV = itemView.findViewById<TextView>(R.id.WindDTV)
        val dateTv = itemView.findViewById<TextView>(R.id.dateTextView)
        val rainTv = itemView.findViewById<TextView>(R.id.rainTextViewMM)
        val showersTv = itemView.findViewById<TextView>(R.id.showerTextViewMM)
        val rainPercentageTv = itemView.findViewById<TextView>(R.id.rainTextViewinP)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.hourly_adapter_layout, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return rain.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.rainTv.text = "Rain : ${rain[position]}mm"
        holder.showersTv.text = "Showers : ${showers[position]}mm"
        holder.temperatureTV.text = "${temperature_2m[position]}°C"
        holder.windSpeedTV.text = "Wind Speed : ${wind_speed_10m[position]}km/h"
        val windDirection = getFullCardinalDirection(wind_direction_10m[position])
        holder.windDirectionTV.text = "Wind Direction : $windDirection"
        val rainPercentage = calculateRainPercentage(rain[position], showers[position])
        holder.rainPercentageTv.text = "Rain : ${rainPercentage}%"
        if (wind_speed_10m[position] <20){
            if(rainPercentage>75){
                holder.weatherImg.setImageResource(R.drawable.storm)
            }else if (rainPercentage>50) {
                holder.weatherImg.setImageResource(R.drawable.rainy)
            }else if (rainPercentage>10){
                holder.weatherImg.setImageResource(R.drawable.cloudy)
            }else {
                holder.weatherImg.setImageResource(R.drawable.sun)
            }
        }else {
            holder.weatherImg.setImageResource(R.drawable.windy)
        }
        holder.dateTv.text = convertDateTime(time[position])


    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertDateTime(dateTimeString: String): String {
        // Parse the input string into a LocalDateTime object
        val dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        // Define the desired output format
        val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

        // Format the date and time parts separately
        val formattedDate = dateTime.format(dateFormatter)
        val formattedTime = dateTime.format(timeFormatter)

        // Combine date and time in the desired format
        return "$formattedDate $formattedTime"
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
    fun calculateRainPercentage(rainfall: Double, showers: Double): Double {
        val totalObservedPrecipitation = rainfall + showers

        // Check if totalObservedPrecipitation is zero to avoid division by zero
        if (totalObservedPrecipitation == 0.0) {
            return 0.0 // or return another appropriate value
        }

        val rainPercentage = (rainfall / totalObservedPrecipitation) * 100
        return rainPercentage
    }
}