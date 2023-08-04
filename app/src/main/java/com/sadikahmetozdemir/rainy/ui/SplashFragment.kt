package com.sadikahmetozdemir.rainy.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.sadikahmetozdemir.rainy.R
import com.sadikahmetozdemir.rainy.base.BaseFragment
import com.sadikahmetozdemir.rainy.base.BaseViewEvent
import com.sadikahmetozdemir.rainy.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment :
    BaseFragment<FragmentSplashBinding, SplashViewModel>(R.layout.fragment_splash) {
    lateinit var location: FusedLocationProviderClient
    private val isLocationDialogShowing = false

    var lat: String = ""
    var lon: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        location = LocationServices.getFusedLocationProviderClient(this.requireActivity())
        if (context?.let { isLocationEnabled(it) } == true) {
            lifecycleScope.launch(Dispatchers.Main) {
                delay(4000)
                checkLocationPermission()
            }
        } else
            showEnableLocationDialog(this.requireContext())
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

        } else {
            isLocationEnabled(requireContext())
        }

        task.addOnSuccessListener {
            if (it != null) {
                lat = it.latitude.toString()
                lon = it.longitude.toString()
                viewModel.toHomePage(lat, lon)
            }
        }
    }

    fun showEnableLocationDialog(context: Context) {
        isLocationDialogShowing
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Konum Hizmetleri")
            .setCancelable(false)
            .setMessage("Konum hizmetleri etkin değil. Lütfen etkinleştirin.")
            .setPositiveButton("Ayarlar") { dialog, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)
                !isLocationDialogShowing
                if (!isLocationDialogShowing) {
                    dialog.cancel()
                }
            }

            .create()
        alertDialog.show()


    }

    fun showSnackbar(message: String) {
        this.let { view ->
            val snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
            snackbar.setAction(R.string.text_action) { snackbar.dismiss() }
            val view = snackbar.view
            val params: FrameLayout.LayoutParams = view.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.BOTTOM
            view.layoutParams = params
            snackbar.setBackgroundTint(Color.TRANSPARENT)
            snackbar.show()
        }
    }

    override fun onResume() {
        super.onResume()
        if (context?.let { isLocationEnabled(it) } == true) {
            lifecycleScope.launch(Dispatchers.Main) {
                delay(4000)
                checkLocationPermission()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()

    }

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}
