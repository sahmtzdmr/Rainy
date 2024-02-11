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
import androidx.navigation.fragment.navArgs
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
    BaseFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home), PermissionManager {
    private val args: HomeFragmentArgs by navArgs()
    private var homeAdapter = HomeAdapter(arrayListOf())
    val handler = Handler(Looper.getMainLooper())
    private val STORAGE_PERMISSION_CODE = 101

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var lat = args.lat
        var lon = args.lon
        homeAdapter.itemClicked = {
            viewModel.getForecastFromRV(it)
        }
        viewModel.dailyWeather.observe(viewLifecycleOwner) {
            homeAdapter.updateDailyData((it.get(0).list))
        }
        binding.ivShare.setOnClickListener {
            checkStoragePermission()
        }

        binding.apply {
            rvChildItem.adapter = homeAdapter
            rvChildItem.setHasFixedSize(true)

        }
        binding.etSearch.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
                viewModel.getForecastData()
            binding.etSearch.text?.clear()
            hideKeyboard(binding.etSearch)
            return@setOnKeyListener true
            throw RuntimeException("Test Crash")
        }
        false

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

        when (value) {
            in 0..5 -> setImage(binding.iconImageView, R.drawable.ic_snow)
            in 5..10 -> setImage(binding.iconImageView, R.drawable.ic_rain)
            in 10..15 -> setImage(binding.iconImageView, R.drawable.ic_moon)
            in 15..20 -> setImage(binding.iconImageView, R.drawable.ic_rainbow)
            in 20..25 -> setImage(binding.iconImageView, R.drawable.ic_snow)
            in 25..30 -> setImage(binding.iconImageView, R.drawable.ic_rain)
            in 30..35 -> setImage(binding.iconImageView, R.drawable.ic_moon)
            in 35..40 -> setImage(binding.iconImageView, R.drawable.ic_rainbow)
            in 40..45 -> setImage(binding.iconImageView, R.drawable.ic_snow)
            in 45..50 -> setImage(binding.iconImageView, R.drawable.ic_rain)
            in 50..55 -> setImage(binding.iconImageView, R.drawable.ic_moon)
            in 55..60 -> setImage(binding.iconImageView, R.drawable.ic_rainbow)
            in 60..65 -> setImage(binding.iconImageView, R.drawable.ic_snow)
            in 65..70 -> setImage(binding.iconImageView, R.drawable.ic_rain)
            in 70..75 -> setImage(binding.iconImageView, R.drawable.ic_moon)
            in 75..80 -> setImage(binding.iconImageView, R.drawable.ic_rainbow)
            in 80..85 -> setImage(binding.iconImageView, R.drawable.ic_snow)
            in 85..90 -> setImage(binding.iconImageView, R.drawable.ic_rain)
            in 90..95 -> setImage(binding.iconImageView, R.drawable.ic_moon)
            in 95..100 -> setImage(binding.iconImageView, R.drawable.ic_rainbow)

            else -> binding.iconImageView.setImageResource(0)
        }

    }

    // İlerleme çubuğunu simüle eden bir işlev
    fun simulateProgress() {
        var value = 0
        val interval = 50 // Her 100 milisaniyede bir güncelle
        val maxProgress = 100

        handler.postDelayed(object : Runnable {
            override fun run() {
                if (value < maxProgress) {
                    value++
                    updateProgressBar(value)
                    handler.postDelayed(this, interval.toLong())
                }
            }
        }, interval.toLong())
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        initObserve()
    }
    override fun onStoragePermissionGranted() {
        BottomSheetFragment().show(requireActivity().supportFragmentManager,"bs")

    }

    override fun onStoragePermissionDenied() {
        showEnableStorageDialog(requireContext())

    }

    override fun onLocationPermissionGranted() {
    }

    override fun onLocationPermissionDenied() {
    }

    override fun checkLocationPermission() {
    }

    fun hasStoragePermission(): Boolean {
        val storageWrPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val storageReadPermission = Manifest.permission.READ_EXTERNAL_STORAGE
        val permissionWrStatus =
            ContextCompat.checkSelfPermission(requireContext(), storageWrPermission)
        val permissionReadStatus =
            ContextCompat.checkSelfPermission(requireContext(), storageReadPermission)
        return (permissionWrStatus == PackageManager.PERMISSION_GRANTED) && (permissionReadStatus == PackageManager.PERMISSION_GRANTED)

    }

    override fun checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            onStoragePermissionGranted()
        } else {
            if (hasStoragePermission()) {
                onStoragePermissionGranted()
            } else {
                requestStoragePermissions()
            }
        }

    }

    override fun requestLocationPermissions() {
    }

    override fun requestStoragePermissions() {
        val permissions = arrayListOf(
            Manifest.permission.READ_MEDIA_IMAGES
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

        } else {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        ),
                        STORAGE_PERMISSION_CODE
                    )
                    return
                }
            }
        }

    }

    override fun showEnableLocationDialog(context: Context) {
    }

    override fun showEnableStorageDialog(context: Context) {
        val builder = AlertDialog.Builder(context).setTitle("Veri İzni").setCancelable(false)
            .setMessage(getString(R.string.data_permission))
            .setPositiveButton("Ayarlar") { dialog, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                startActivity(intent)
            }.setNegativeButton("İptal") { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
            .create()

        builder.show()

    }

    override fun isLocationEnabled(context: Context): Boolean {
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            STORAGE_PERMISSION_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onStoragePermissionGranted()
            } else {
                onStoragePermissionDenied()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


    }

    companion object Screenshot {
        private fun takeScreenshot(view: View): Bitmap {
            view.isDrawingCacheEnabled = true
            view.buildDrawingCache(true)
            val b = Bitmap.createBitmap(view.drawingCache)
            view.isDrawingCacheEnabled = false
            return b
        }

        fun takeScreenshotOfRootView(v: View): Bitmap {
            return takeScreenshot(v.rootView)
        }


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

