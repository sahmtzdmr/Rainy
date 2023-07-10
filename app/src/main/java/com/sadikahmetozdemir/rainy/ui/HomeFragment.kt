package com.sadikahmetozdemir.rainy.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.sadikahmetozdemir.rainy.R
import com.sadikahmetozdemir.rainy.base.BaseFragment
import com.sadikahmetozdemir.rainy.databinding.FragmentHomeBinding
import com.sadikahmetozdemir.rainy.utils.adapter.changeWeatherIcon
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home) {
    private val args: HomeFragmentArgs by navArgs()

    private var homeAdapter = HomeAdapter(arrayListOf())


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeAdapter.itemClicked = {
            viewModel.getForecastFromRV(it)
        }
        val lat = args.lat
        val lon = args.lon
//        viewModel.getDailyWeather(lat,lon)
        viewModel.dailyWeather.observe(viewLifecycleOwner) {
            homeAdapter.updateDailyData((it.get(0).list))
        }

        binding.apply {
            rvChildItem.adapter = homeAdapter
            rvChildItem.setHasFixedSize(true)

        }


//        viewModel.getForecastData("Londra")
        initObserve()
//        viewModel.getCurrentData(lat,lon)
//        viewModel.getDailyWeather(lat, lon)
    }

    fun initObserve() {
        viewModel.weather.observe(viewLifecycleOwner) { item ->
            item.let {
                binding.tvCityName.text = it.name
                binding.tvWeather.text = it.weatherItemModel?.get(0)?.description
                binding.tvDegree.text = it.mainModel?.temp?.toString() + "°C"
                binding.tvRainRate.text = it.mainModel?.humidity.toString() + " %"
                binding.tvWindSpeed.text = it.windModel?.speed.toString() + " km/h"
                it.weatherItemModel?.get(0)?.icon?.let { it1 ->
                    binding.ivWeather.changeWeatherIcon(
                        it1
                    )
                }
                binding.tvCurrentDate.text = getDate()
                binding.tvCurrentTime.text = getTime()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.apply {
                    pbLoading.visibility = View.VISIBLE
                    etSearch.visibility = View.GONE
                    ivSearch.visibility = View.GONE
                    tvWeather.visibility = View.GONE
                    tvCurrentDate.visibility = View.GONE
                    tvCurrentTime.visibility = View.GONE
                    ivRain.visibility = View.GONE
                    ivWind.visibility = View.GONE
                    tvWindSpeed.visibility = View.GONE
                    ivWeather.visibility = View.GONE
                    tvRainRate.visibility = View.GONE
                }
            } else {
                binding.apply {
                    pbLoading.visibility = View.GONE
                    etSearch.visibility = View.VISIBLE
                    ivSearch.visibility = View.VISIBLE
                    tvWeather.visibility = View.VISIBLE
                    tvCurrentDate.visibility = View.VISIBLE
                    tvCurrentTime.visibility = View.VISIBLE
                    ivRain.visibility = View.VISIBLE
                    ivWind.visibility = View.VISIBLE
                    tvWindSpeed.visibility = View.VISIBLE
                    ivWeather.visibility = View.VISIBLE
                    tvRainRate.visibility = View.VISIBLE
                }
            }}
    }

    fun getDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = Date()
        val formattedDate = dateFormat.format(currentDate)
        return formattedDate
    }

    fun getTime(): String {
        val timeFormat = SimpleDateFormat("HH:mm:ss")
        val currentTime = Date()
        val formattedTime = timeFormat.format(currentTime)
        return formattedTime

    }

//    private fun getCurrentWeather() {
//        viewModel.weather.observe(viewLifecycleOwner) {
//            homeAdapter.(
//                viewLifecycleOwner.lifecycle, it
//            )
//        }
//    }

}