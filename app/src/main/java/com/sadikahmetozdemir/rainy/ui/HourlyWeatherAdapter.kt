package com.sadikahmetozdemir.rainy.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sadikahmetozdemir.rainy.core.shared.remote.hourly.HourlyWeatherItem
import com.sadikahmetozdemir.rainy.databinding.ItemHourlyWeatherBinding
import com.sadikahmetozdemir.rainy.utils.adapter.changeWeatherIcon
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class HourlyWeatherAdapter(
    private val hourlyWeatherList: ArrayList<HourlyWeatherItem>
) : RecyclerView.Adapter<HourlyWeatherAdapter.HourlyViewHolder>() {
    
    var itemClicked: ((HourlyWeatherItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val binding = ItemHourlyWeatherBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HourlyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        holder.bind(hourlyWeatherList[position])
    }

    override fun getItemCount(): Int = hourlyWeatherList.size

    fun updateHourlyData(newList: List<HourlyWeatherItem>) {
        hourlyWeatherList.clear()
        hourlyWeatherList.addAll(newList)
        notifyDataSetChanged()
    }

    inner class HourlyViewHolder(private val binding: ItemHourlyWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HourlyWeatherItem) {
            binding.apply {
                root.setOnClickListener {
                    itemClicked?.invoke(item)
                }
                
                // Saat formatı
                item.dt?.let { timestamp ->
                    val date = Date(timestamp * 1000)
                    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    tvHourlyTime.text = timeFormat.format(date)
                }

                // Hava durumu ikonu
                item.weather.getOrNull(0)?.icon?.let { icon ->
                    lavHourlyWeather.changeWeatherIcon(icon)
                }
            }
        }
    }
}
