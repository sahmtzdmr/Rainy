package com.sadikahmetozdemir.rainy.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.sadikahmetozdemir.rainy.R
import com.sadikahmetozdemir.rainy.base.BaseFragment
import com.sadikahmetozdemir.rainy.databinding.FragmentHomeBinding
import com.sadikahmetozdemir.rainy.utils.adapter.changeWeatherIcon
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home) {
    private val args: HomeFragmentArgs by navArgs()

    private var homeAdapter = HomeAdapter(arrayListOf())


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lat = args.lat
        val lon = args.lon
//        viewModel.getDailyWeather(lat,lon)
        viewModel.dailyWeather.observe(viewLifecycleOwner) {
            homeAdapter.UpdateDailyData((it.get(0).list))
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
                binding.tvDegree.text = it.mainModel?.temp?.toString()
                binding.tvRainRate.text = it.mainModel?.humidity.toString() + " %"
                binding.tvWindSpeed.text = it.windModel?.speed.toString() + " km/h"
                it.weatherItemModel?.get(0)?.icon?.let { it1 ->
                    binding.ivWeather.changeWeatherIcon(
                        it1
                    )
                }
            }
        }
    }

//    private fun getCurrentWeather() {
//        viewModel.weather.observe(viewLifecycleOwner) {
//            homeAdapter.(
//                viewLifecycleOwner.lifecycle, it
//            )
//        }
//    }

}