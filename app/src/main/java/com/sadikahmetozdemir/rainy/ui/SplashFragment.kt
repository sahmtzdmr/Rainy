package com.sadikahmetozdemir.rainy.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.sadikahmetozdemir.rainy.R
import com.sadikahmetozdemir.rainy.base.BaseFragment
import com.sadikahmetozdemir.rainy.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment :
    BaseFragment<FragmentSplashBinding, SplashViewModel>(R.layout.fragment_splash) {
    lateinit var location: FusedLocationProviderClient
    var lat: String = ""
    var lon: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        location = LocationServices.getFusedLocationProviderClient(this.requireActivity())
//        if check
//        if (context?.let { isLocationEnabled(it) } == true) {
//            lifecycleScope.launch(Dispatchers.Main) {
//                delay(3000)
//                viewModel.toHomePage(lat, lon)
//            }
//        } else
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        val task: Task<Location> = location.lastLocation
        if (this.context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
            != PackageManager.PERMISSION_GRANTED && this.context?.let {
                ActivityCompat
                    .checkSelfPermission(
                        it,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
            return
        }
        task.addOnSuccessListener {
            if (it != null) {
                lat = it.latitude.toString()
                lon = it.longitude.toString()
                viewModel.toHomePage(lat, lon)

            } else {
                showEnableLocationDialog(this.requireContext())
                return@addOnSuccessListener
            }
        }
    }

    fun showEnableLocationDialog(context: Context) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Konum Hizmetleri")
            .setMessage("Konum hizmetleri etkin değil. Lütfen etkinleştirin.")
            .setPositiveButton("Ayarlar") { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)
                if (isLocationEnabled(context)){
                    viewModel.toHomePage(lat,lon)
                }
            }
            .setNegativeButton("İptal") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        alertDialog.show()
    }

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}
