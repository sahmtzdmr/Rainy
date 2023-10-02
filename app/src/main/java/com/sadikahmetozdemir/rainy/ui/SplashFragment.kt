package com.sadikahmetozdemir.rainy.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.sadikahmetozdemir.rainy.R
import com.sadikahmetozdemir.rainy.base.BaseFragment
import com.sadikahmetozdemir.rainy.databinding.FragmentSplashBinding
import com.sadikahmetozdemir.rainy.utils.DataHelperManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment :
    BaseFragment<FragmentSplashBinding, SplashViewModel>(R.layout.fragment_splash) {
    lateinit var location: FusedLocationProviderClient
    private var isLocationDialogShowing = false
    lateinit var dataHelperManager: DataHelperManager

    var lat: String = ""
    var lon: String = ""
    private val LOCATION_PERMISSION_REQUEST_CODE = 1


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        location = LocationServices.getFusedLocationProviderClient(this.requireActivity())
        dataHelperManager = DataHelperManager(requireContext())
        checkLocationPermission()
        lifecycleScope.launch(Dispatchers.Main) {
            delay(2500)
            if (dataHelperManager.isFirstAttach()){
                viewModel.toIntro()
                dataHelperManager.firstAttach()
            }
            else{
                if (context?.let { isLocationEnabled(it) } == true) {
                    lifecycleScope.launch {
                        checkLocationPermission()
                        viewModel.toHomePage(dataHelperManager.getLatitude(),dataHelperManager.getLongitude())
                    }
                } else {
                    showEnableLocationDialog(requireContext())
                }
            }
        }

    }

    private fun checkLocationPermission() {
        val task: Task<Location> = location.lastLocation
        if (this.context?.let {
                ActivityCompat.checkSelfPermission(
                    it, android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && this.context?.let {
                ActivityCompat.checkSelfPermission(
                    it, android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermissions()

        } else {
            isLocationEnabled(requireContext())
        }
        task.addOnSuccessListener {
            if (it != null) {
                lat = it.latitude.toString()
                lon = it.longitude.toString()
//                viewModel.toHomePage(lat, lon)
                lifecycleScope.launch(Dispatchers.Main) {
                    dataHelperManager.saveLatitude(it.latitude.toString())
                    dataHelperManager.saveLongitude(it.longitude.toString())

                }
            }
        }
    }

    private fun requestLocationPermissions() {
        if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION) || shouldShowRequestPermissionRationale(
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            if (!isLocationDialogShowing) {
                showEnableLocationDialog(requireContext())
                isLocationDialogShowing = true
            }

        } else {
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ), LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    fun showEnableLocationDialog(context: Context) {
        isLocationDialogShowing
        val explain = R.string.need_permission
        val alertDialog =
            AlertDialog.Builder(context).setTitle("Konum Hizmetleri").setCancelable(false)
                .setMessage(explain).setPositiveButton("Ayarlar") { dialog, _ ->
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    context.startActivity(intent)
                    !isLocationDialogShowing
                    if (!isLocationDialogShowing) {
                        dialog.dismiss()
                    }
                }.setNegativeButton("İptal") { dialog, _ ->
                    dataHelperManager = DataHelperManager(requireContext())
                    lifecycleScope.launch(Dispatchers.Default) {
                        lat = dataHelperManager.getLatitude()
                        lon = dataHelperManager.getLongitude()
                        viewModel.toHomePage(lat = lat, lon = lon)
                    }
                    dialog.dismiss()

                }
                .setCancelable(false)

                .create()
        alertDialog.show()


    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isLocationEnabled(requireContext())
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        }
    }
    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onResume() {
        super.onResume()
        if (context?.let { isLocationEnabled(it) } == true) {
            lifecycleScope.launch(Dispatchers.Main) {
                delay(2500)
//                checkLocationPermission()
            }
        }
    }



}
