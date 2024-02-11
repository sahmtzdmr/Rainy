package com.sadikahmetozdemir.rainy.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sadikahmetozdemir.rainy.databinding.CustomForecastItemBinding
import com.sadikahmetozdemir.rainy.utils.adapter.changeWeatherIcon
import sadikahmetozdemir.rainy.core.shared.remote.daily.DailyWeatherResponse
import kotlin.math.roundToInt

class HomeAdapter(private val dailyWeatherResponse: ArrayList<DailyWeatherResponse.WeatherList>) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    var itemClicked: ((String) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CustomForecastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dailyWeatherResponse[position])
    }

    inner class ViewHolder(val binding: CustomForecastItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DailyWeatherResponse.WeatherList) {
            binding.apply {
                tvChildName.text = item.name
                lavChildWeather.setOnClickListener {
                    item.name?.let { it1 -> itemClicked?.invoke(it1) }
                }
                lavChildWeather.changeWeatherIcon(item.weather.get(0).icon.toString())
                val tempInt = item.main?.temp?.roundToInt()
                tvChildDegree.text = tempInt.toString() + "°C"
            }
        }
    }

    override fun getItemCount(): Int {
        return dailyWeatherResponse.size
    }

    fun updateDailyData(dailyData: List<DailyWeatherResponse.WeatherList>) {
        dailyWeatherResponse.clear()
        dailyWeatherResponse.addAll(dailyData)
        Log.d("TAG", "UpdateDailyData: ${dailyData}")
        notifyDataSetChanged()
    }
}



