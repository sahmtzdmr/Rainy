package com.sadikahmetozdemir.rainy.ui

import android.content.Context

interface PermissionManager {

    fun onLocationPermissionGranted()
    fun onLocationPermissionDenied()
    fun checkLocationPermission()
    fun requestLocationPermissions()
    fun showEnableLocationDialog(context: Context)
    fun isLocationEnabled(context: Context): Boolean
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    )
}
