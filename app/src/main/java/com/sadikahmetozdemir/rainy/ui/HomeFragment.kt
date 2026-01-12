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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sadikahmetozdemir.rainy.R
import com.sadikahmetozdemir.rainy.base.BaseViewEvent
import kotlinx.coroutines.launch
import com.sadikahmetozdemir.rainy.base.BaseFragment
import com.sadikahmetozdemir.rainy.databinding.FragmentHomeBinding
import com.sadikahmetozdemir.rainy.utils.adapter.changeWeatherIcon
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt


@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home) {
    private val args: HomeFragmentArgs by navArgs()
    private var homeAdapter = HomeAdapter(arrayListOf())
    private var hourlyAdapter = HourlyWeatherAdapter(arrayListOf())
    private val handler = Handler(Looper.getMainLooper())
    private var progressRunnable: Runnable? = null
    private var isSearchVisible = false

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
        
        // BaseFragment'ın onViewCreated'ını çağır (baseEvent handling için)
        // Ama ShowMessage için özel snackbar gösterilecek
        
        homeAdapter.itemClicked = {
            viewModel.getForecastFromRV(it)
        }
        viewModel.dailyWeather.observe(viewLifecycleOwner) {
            if (it.isNotEmpty() && it[0].list.isNotEmpty()) {
                homeAdapter.updateDailyData((it.get(0).list))
                // Veri geldiğinde RecyclerView'ı görünür yap
                if (viewModel.weather.value != null && viewModel.loading.value == false) {
                    binding.rvChildItem.visibility = View.VISIBLE
                }
            }
        }

        viewModel.hourlyWeather.observe(viewLifecycleOwner) { hourlyResponse ->
            hourlyResponse?.list?.let { hourlyList ->
                hourlyAdapter.updateHourlyData(hourlyList)
                // Veri geldiğinde RecyclerView'ı görünür yap
                if (viewModel.weather.value != null && viewModel.loading.value == false) {
                    binding.rvHourlyWeather.visibility = View.VISIBLE
                }
            }
        }

        // Seçilen saatlik hava durumu observer
        viewModel.selectedHourlyWeather.observe(viewLifecycleOwner) { hourlyItem ->
            hourlyItem?.let { item ->
                // Ana ekrandaki hava durumu bilgilerini güncelle
                binding.apply {
                    // Sıcaklık
                    val tempInt = item.main?.temp?.roundToInt()
                    tvDegree.text = "${tempInt}°C"
                    
                    // Hava durumu açıklaması
                    item.weather.getOrNull(0)?.description?.let { description ->
                        tvWeather.text = description.replaceFirstChar { 
                            if (it.isLowerCase()) it.titlecase() else it.toString() 
                        }
                    }
                    
                    // Hava durumu ikonu
                    item.weather.getOrNull(0)?.icon?.let { icon ->
                        ivWeather.changeWeatherIcon(icon)
                    }
                    
                    // Rüzgar hızı
                    item.wind?.speed?.let { speed ->
                        tvWindSpeed.text = "${speed} km/h"
                    }
                    
                    // Nem
                    item.main?.humidity?.let { humidity ->
                        tvRainRate.text = "${humidity} %"
                    }
                    
                    // Tarih ve saat bilgisi
                    item.dt?.let { timestamp ->
                        val date = Date(timestamp * 1000)
                        // Tarih formatı
                        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        tvCurrentDate.text = dateFormat.format(date)
                        // Saat formatı
                        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                        tvCurrentTime.text = timeFormat.format(date)
                    }
                }
            }
        }

        // Saatlik adapter click listener
        hourlyAdapter.itemClicked = { hourlyItem ->
            viewModel.setSelectedHourlyWeather(hourlyItem)
        }

        // Arama ikonu click listener
        binding.ivSearch.setOnClickListener {
            if (isSearchVisible) {
                // Arama kutusu görünürse arama yap
                viewModel.getForecastData()
                binding.etSearch.text?.clear()
                hideKeyboard(binding.etSearch)
            } else {
                // Arama kutusu gizliyse göster
                isSearchVisible = true
                binding.etSearch.visibility = View.VISIBLE
                binding.etSearch.requestFocus()
                // Klavye göster
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.etSearch, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        binding.apply {
            // Günlük hava durumu RecyclerView
            rvChildItem.adapter = homeAdapter
            rvChildItem.setHasFixedSize(true)
            val dailyLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvChildItem.layoutManager = dailyLayoutManager
            val dailyItemAnimator = DefaultItemAnimator()
            dailyItemAnimator.addDuration = 300
            dailyItemAnimator.removeDuration = 300
            rvChildItem.itemAnimator = dailyItemAnimator

            // Saatlik hava durumu RecyclerView (dikey)
            rvHourlyWeather.adapter = hourlyAdapter
            rvHourlyWeather.setHasFixedSize(true)
            val hourlyLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvHourlyWeather.layoutManager = hourlyLayoutManager
            val hourlyItemAnimator = DefaultItemAnimator()
            hourlyItemAnimator.addDuration = 300
            hourlyItemAnimator.removeDuration = 300
            rvHourlyWeather.itemAnimator = hourlyItemAnimator
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
            item?.let {
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
                    tvLoadingMessage.visibility = View.VISIBLE
                    tvErrorMessage.visibility = View.GONE
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
                    tvCityName.visibility = View.GONE
                    tvDegree.visibility = View.GONE
                    rvChildItem.visibility = View.GONE
                    
                    // 2 saniye sonra loading mesajını gizle
                    handler.postDelayed({
                        if (binding.tvLoadingMessage.visibility == View.VISIBLE) {
                            binding.tvLoadingMessage.visibility = View.GONE
                        }
                    }, 2000)
                }
            } else {
                binding.apply {
                    progressBar.visibility = View.GONE
                    iconImageView.visibility = View.GONE
                    tvLoadingMessage.visibility = View.GONE
                    etSearch.visibility = View.VISIBLE
                    ivSearch.visibility = View.VISIBLE
                    
                    // Sadece veri varsa göster
                    val hasWeatherData = viewModel.weather.value != null
                    val hasDailyData = viewModel.dailyWeather.value?.isNotEmpty() == true && 
                                      viewModel.dailyWeather.value?.get(0)?.list?.isNotEmpty() == true
                    
                    if (hasWeatherData) {
                        tvWeather.visibility = View.VISIBLE
                        tvCurrentDate.visibility = View.VISIBLE
                        tvCurrentTime.visibility = View.VISIBLE
                        ivRain.visibility = View.VISIBLE
                        ivWind.visibility = View.VISIBLE
                        tvWindSpeed.visibility = View.VISIBLE
                        ivWeather.visibility = View.VISIBLE
                        tvRainRate.visibility = View.VISIBLE
                        tvCityName.visibility = View.VISIBLE
                        tvDegree.visibility = View.VISIBLE
                        // Günlük hava durumu verisi varsa RecyclerView'ı göster
                        if (hasDailyData) {
                            rvChildItem.visibility = View.VISIBLE
                        }
                        // Saatlik hava durumu verisi varsa göster
                        if (viewModel.hourlyWeather.value != null) {
                            rvHourlyWeather.visibility = View.VISIBLE
                        }
                        tvErrorMessage.visibility = View.GONE
                    } else {
                        // Hata durumu - 2 saniye boyunca hata mesajı göster
                        iconImageView.visibility = View.VISIBLE
                        tvErrorMessage.visibility = View.VISIBLE
                        
                        // 2 saniye sonra hata mesajını gizle
                        handler.postDelayed({
                            binding.iconImageView.visibility = View.GONE
                            binding.tvErrorMessage.visibility = View.GONE
                        }, 2000)
                    }
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

