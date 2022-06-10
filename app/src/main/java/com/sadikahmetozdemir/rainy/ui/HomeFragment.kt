package com.sadikahmetozdemir.rainy.ui

import android.os.Bundle
import android.view.View
import com.sadikahmetozdemir.rainy.R
import com.sadikahmetozdemir.rainy.base.BaseFragment
import com.sadikahmetozdemir.rainy.databinding.FragmentHomeBinding
import com.sadikahmetozdemir.rainy.utils.Constants
import com.sadikahmetozdemir.rainy.utils.extensions.load
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCurrentWeather("Manisa")
        initObserve()
    }

    fun initObserve() {
        viewModel.weather.observe(viewLifecycleOwner) { item ->
            item.let {
                binding.tvDegree.text = it.mainModel?.temp?.toString()
                binding.tvRainRate.text = it.mainModel?.humidity.toString() + " %"
                binding.tvWindSpeed.text = it.windModel?.speed.toString() + " km/h"
                it.weatherItemModel?.get(0)?.icon
                binding.ivWeather.load(url = Constants.ICON + it.weatherItemModel?.get(0)?.icon + "@2x.png")

            }
        }
    }

}