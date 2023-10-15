package com.sadikahmetozdemir.rainy.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
        lifecycleScope.launch(Dispatchers.Main) {
            if (isInternetAvailable(requireContext())) {
                delay(2500)
                if (dataHelperManager.isFirstAttach()) {
                    viewModel.toIntro()
                    dataHelperManager.firstAttach()
                } else {
                    checkLocationPermission()
                    if (context?.let { isLocationEnabled(it) } == true) {
                        lifecycleScope.launch(Dispatchers.Main) {
                            lat = dataHelperManager.getLatitude()
                            lon = dataHelperManager.getLongitude()
                            viewModel.toHomePage(
                                lat, lon
                            )
                        }
                    } else {
                        showEnableLocationDialog(requireContext())
                    }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "İnternet bağlantınızı kontrol edin ve uygulamayı yeniden başlatın.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    }


    private fun checkLocationPermission() {
        val fineLocationPermission = PackageManager.PERMISSION_GRANTED ==
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )

        val coarseLocationPermission = PackageManager.PERMISSION_GRANTED ==
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )

        if (fineLocationPermission && coarseLocationPermission) {
            if (isLocationEnabled(requireContext())) {
                val task: Task<Location> = location.lastLocation
                task.addOnSuccessListener { location ->
                    if (location != null) {
                        val lat = location.latitude.toString()
                        val lon = location.longitude.toString()
                        lifecycleScope.launch {
                            dataHelperManager.saveLatitude(lat)
                            dataHelperManager.saveLongitude(lon)
                        }
                    }
                }
            } else {
                showEnableLocationDialog(requireContext())
            }
        } else {
            requestLocationPermissions()
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

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
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
                lat = dataHelperManager.getLatitude()
                lon = dataHelperManager.getLongitude()
                viewModel.toHomePage(lat, lon)
            }
        }
    }


}
