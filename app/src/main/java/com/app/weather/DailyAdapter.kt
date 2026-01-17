package com.app.weather

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DailyAdapter (
    val context : Context,
    val daylight_duration: List<Double>,
    val rain_sum: List<Double>,
    val precipitation_sum : List<Double>,
    val sunrise: List<String>,
    val sunset: List<String>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val time: List<String>,
    val wind_speed_10m_max: List<Double>
) : RecyclerView.Adapter<DailyAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rainPTV = itemView.findViewById<TextView>(R.id.rainTextViewinP)
        val temperatureMaxTV = itemView.findViewById<TextView>(R.id.temperatureMaxTextView)
        val temperatureMinTV = itemView.findViewById<TextView>(R.id.temperatureMinTextView)
        val windSpeedTV = itemView.findViewById<TextView>(R.id.WindSpeedTextViewKMH)
        val sunriseTV = itemView.findViewById<TextView>(R.id.sunriseTextView)
        val sunsetTV = itemView.findViewById<TextView>(R.id.sunsetTextView)
        val dailightDurationTV = itemView.findViewById<TextView>(R.id.DaylightTextView)
        val dateTV = itemView.findViewById<TextView>(R.id.dateTextView)
        val img = itemView.findViewById<ImageView>(R.id.weatherStatusImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.daily_adapter_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rain_sum.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.temperatureMaxTV.text = "Max Temp: ${temperature_2m_max[position].toString()}\u00B0"
        holder.temperatureMinTV.text = "Min Temp: ${temperature_2m_min[position].toString()}\u00B0"
        holder.windSpeedTV.text = "Wind Speed: ${wind_speed_10m_max[position].toString()}KM/H"
        val sunriseTime = convertDateTime(sunrise[position])
        holder.sunriseTV.text = "Sunrise: ${sunriseTime[0]}"
        val sunsetTime = convertDateTime(sunset[position])
        holder.sunsetTV.text = "Sunset: ${sunsetTime[0]}"
        val rainP = calculateRainPercentage(rain_sum[position], precipitation_sum[position])
        holder.rainPTV.text = "Rain : ${rainP}%"
        holder.dateTV.text = time[position]

        if (wind_speed_10m_max[position] <20){
            if(rainP>75){
                holder.img.setImageResource(R.drawable.storm)
            }else if (rainP>50) {
                holder.img.setImageResource(R.drawable.rainy)
            }else if (rainP>10){
                holder.img.setImageResource(R.drawable.cloudy)
            }else {
                holder.img.setImageResource(R.drawable.sun)
            }
        }else {
            holder.img.setImageResource(R.drawable.windy)
        }
        val daylightDurationConverted = secondsToHMS(daylight_duration[position].toInt())
        holder.dailightDurationTV.text = "Daylight Duration: $daylightDurationConverted"

    }

    fun secondsToHMS(seconds: Int): String {
        val hours = seconds / 3600
        val remainderMinutes = seconds % 3600
        val minutes = remainderMinutes / 60
        val remainderSeconds = remainderMinutes % 60
        return String.format("%02d:%02d:%02d", hours, minutes, remainderSeconds)
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertDateTime(dateTimeString: String): Array<String> {
        // Parse the input string into a LocalDateTime object
        val dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        // Define the desired output format
        val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

        // Format the date and time parts separately
        val formattedDate = dateTime.format(dateFormatter)
        val formattedTime = dateTime.format(timeFormatter)

        // Combine date and time in the desired format
        return arrayOf(formattedTime , formattedDate)
    }
}