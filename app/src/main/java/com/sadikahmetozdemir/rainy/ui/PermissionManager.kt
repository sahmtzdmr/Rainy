package com.sadikahmetozdemir.rainy.ui

import android.content.Context

interface PermissionManager {
    fun onStoragePermissionGranted()
    fun onStoragePermissionDenied()
    fun onLocationPermissionGranted()
    fun onLocationPermissionDenied()
    fun checkLocationPermission()
    fun checkStoragePermission()
    fun requestLocationPermissions()
    fun requestStoragePermissions()
    fun showEnableLocationDialog(context: Context)
    fun showEnableStorageDialog(context: Context)
    fun isLocationEnabled(context: Context): Boolean
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    )
}
