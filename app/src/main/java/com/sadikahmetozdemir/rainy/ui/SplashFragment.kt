package com.sadikahmetozdemir.rainy.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
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
    BaseFragment<FragmentSplashBinding, SplashViewModel>(R.layout.fragment_splash),
    PermissionManager {
    lateinit var location: FusedLocationProviderClient
    private var isLocationDialogShowing = true
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

    override fun onStoragePermissionGranted() {
//        Toast.makeText(requireContext(), "grantedSt", Toast.LENGTH_SHORT).show()
    }

    override fun onStoragePermissionDenied() {
//        Toast.makeText(requireContext(), "deniedstor", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("MissingPermission")
    override fun onLocationPermissionGranted() {
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
    }

    override fun onLocationPermissionDenied() {
        Toast.makeText(
            requireContext(),
            "Konumunuzun hava olaylarını görmek için ayarlardan konum izni veriniz.",
            Toast.LENGTH_SHORT
        ).show()
    }


    override fun checkLocationPermission() {
        if (hasLocationPermission()) {
            onLocationPermissionGranted()
        } else {
            requestLocationPermissions()
        }
    }

    override fun checkStoragePermission() {
    }

    override fun requestLocationPermissions() {
        lifecycleScope.launch(Dispatchers.Main) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) || !shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {
                if (!dataHelperManager.isFirstAttach()) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ), LOCATION_PERMISSION_REQUEST_CODE
                    )
                } else {
                }

            }
        }

    }

    override fun requestStoragePermissions() {
    }

    override fun showEnableLocationDialog(context: Context) {
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

    override fun showEnableStorageDialog(context: Context) {
    }

    override fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )

    }

    fun hasLocationPermission(): Boolean {

        val coarseLocationPermissionStatus = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val fineLocationPermissionStatus = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        return (coarseLocationPermissionStatus == PackageManager.PERMISSION_GRANTED) && (fineLocationPermissionStatus == PackageManager.PERMISSION_GRANTED)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onLocationPermissionGranted()
            } else {
                onLocationPermissionDenied()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


    }


    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            if (dataHelperManager.isFirstAttach()) {
                viewModel.toIntro()
                dataHelperManager.firstAttach()
            } else if (context?.let { isLocationEnabled(it) } == true) {
                checkLocationPermission()
                lifecycleScope.launch(Dispatchers.Main) {
                    delay(2500)
                    lat = dataHelperManager.getLatitude()
                    lon = dataHelperManager.getLongitude()
                    viewModel.toHomePage(lat, lon)
                }
            }
        }

    }
}
