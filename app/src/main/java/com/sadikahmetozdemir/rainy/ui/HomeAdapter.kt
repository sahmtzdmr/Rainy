package com.sadikahmetozdemir.rainy.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sadikahmetozdemir.rainy.databinding.CustomForecastItemBinding
import com.sadikahmetozdemir.rainy.utils.extensions.load
import sadikahmetozdemir.rainy.core.shared.remote.daily.DailyWeatherResponse

class HomeAdapter(private val dailyWeatherResponse: ArrayList<DailyWeatherResponse.WeatherList>) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val binding=
           CustomForecastItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dailyWeatherResponse[position])   }

    inner class ViewHolder(val binding: CustomForecastItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DailyWeatherResponse.WeatherList) {
            binding.apply {
                tvChildTime.text= item.main?.temp.toString()
//                ivChildWeather.load(url= item.weather[position].icon)
                tvChildDegree.text=item.main?.temp?.toString()
            }
        }
    }

    override fun getItemCount(): Int {
        return dailyWeatherResponse.size
    }
    fun UpdateDailyData(dailyData:List<DailyWeatherResponse.WeatherList>) {
        dailyWeatherResponse.clear()
        dailyWeatherResponse.addAll(dailyData)
        Log.d("TAG", "UpdateDailyData: ${dailyData}")
        notifyDataSetChanged()
    }
}



