package com.sadikahmetozdemir.rainy.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sadikahmetozdemir.rainy.R
import com.sadikahmetozdemir.rainy.base.BaseFragment
import com.sadikahmetozdemir.rainy.databinding.FragmentHomeBinding
import com.sadikahmetozdemir.rainy.utils.adapter.changeWeatherIcon
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date


@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home) {
    private val args: HomeFragmentArgs by navArgs()
    private var homeAdapter = HomeAdapter(arrayListOf())
    private val handler = Handler(Looper.getMainLooper())
    private var progressRunnable: Runnable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var lat = args.lat
        var lon = args.lon
        
        // Window insets için padding ekle
        ViewCompat.setOnApplyWindowInsetsListener(binding.mainContent) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, systemBars.bottom)
            insets
        }
        
        homeAdapter.itemClicked = {
            viewModel.getForecastFromRV(it)
        }
        viewModel.dailyWeather.observe(viewLifecycleOwner) {
            homeAdapter.updateDailyData((it.get(0).list))
        }


        binding.apply {
            rvChildItem.adapter = homeAdapter
            rvChildItem.setHasFixedSize(true)
            // Smooth scroll ve animasyon için
            val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvChildItem.layoutManager = layoutManager
            val itemAnimator = DefaultItemAnimator()
            itemAnimator.addDuration = 300
            itemAnimator.removeDuration = 300
            rvChildItem.itemAnimator = itemAnimator
        }
        binding.etSearch.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                viewModel.getForecastData()
                binding.etSearch.text?.clear()
                hideKeyboard(binding.etSearch)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        initObserve()
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

    }

    fun initObserve() {
        viewModel.weather.observe(viewLifecycleOwner) { item ->
            item.let {
                binding.tvCityName.text = it.name
                binding.tvWeather.text = it.weatherItemModel?.get(0)?.description
                val tempInt=it.mainModel?.temp?.toInt()
                binding.tvDegree.text = tempInt.toString()+ "°C"
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
//                    progressBar.visibility = View.VISIBLE
                    iconImageView.visibility = View.VISIBLE
                    ivShare.visibility = View.GONE
                    simulateProgress()
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
                    progressBar.visibility = View.GONE
                    iconImageView.visibility = View.GONE
                    ivShare.visibility = View.VISIBLE
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
            }
        }
    }

    fun setImage(imageView: ImageView, resource: Int) {
        imageView.setImageResource(resource)
    }


    fun updateProgressBar(value: Int) {
        binding.progressBar.progress = value

        val icons = listOf(
            R.drawable.ic_snow,
            R.drawable.ic_rain,
            R.drawable.ic_moon,
            R.drawable.ic_rainbow
        )

        val index = (value / 5) % icons.size
        setImage(binding.iconImageView, icons[index])
    }


    // İlerleme çubuğunu simüle eden bir işlev
    fun simulateProgress() {
        var value = 0
        val interval = 50 // Her 50 milisaniyede bir güncelle
        val maxProgress = 100

        progressRunnable = object : Runnable {
            override fun run() {
                if (value < maxProgress) {
                    value++
                    updateProgressBar(value)
                    handler.postDelayed(this, interval.toLong())
                } else {
                    progressRunnable = null
                }
            }
        }
        handler.postDelayed(progressRunnable!!, interval.toLong())
    }

    override fun onPause() {
        super.onPause()
        progressRunnable?.let { handler.removeCallbacks(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        progressRunnable?.let { handler.removeCallbacks(it) }
        progressRunnable = null
    }

    override fun onResume() {
        super.onResume()
        initObserve()
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

}

