package com.sadikahmetozdemir.rainy.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.sadikahmetozdemir.rainy.R
import com.sadikahmetozdemir.rainy.base.BaseFragment
import com.sadikahmetozdemir.rainy.core.shared.remote.IntroModel
import com.sadikahmetozdemir.rainy.databinding.FragmentIntroBinding
import com.sadikahmetozdemir.rainy.utils.DataHelperManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class IntroFragment : BaseFragment<FragmentIntroBinding, IntroViewModel>(R.layout.fragment_intro) {
    lateinit var location: FusedLocationProviderClient
    lateinit var dataHelperManager: DataHelperManager

    var lat: String = ""
    private var isLocationDialogShowing = false
    var lon: String = ""
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vpIntro.adapter = IntroAdapter(prepareIntroList())
//        binding.wormDotsIndicator.attachTo(viewPager2 = binding.vpIntro)
        binding.vpIntro.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                checkLocationPermission()
                if (position == prepareIntroList().size - 1) {
                    binding.btNext.text = getString(R.string.start)
                } else if (position == prepareIntroList().size - 2) {
                } else binding.btNext.text = getString(R.string.next)
            }
        })
        binding.btNext.setOnClickListener {
            if (binding.btNext.text == getString(R.string.start) && binding.vpIntro.currentItem == prepareIntroList().size - 1) {
                if (context?.let { isLocationEnabled(it) } == true) {
                    lifecycleScope.launch {
                        viewModel.onClickNext(
                            dataHelperManager.getLatitude(),
                            dataHelperManager.getLongitude()
                        )
                        dataHelperManager.firstAttach()
                    }
                } else {
                    showEnableLocationDialog(requireContext())
                    return@setOnClickListener
                }
            } else {
                binding.vpIntro.setCurrentItem(binding.vpIntro.currentItem + 1, true)
            }
        }
        location = LocationServices.getFusedLocationProviderClient(this.requireActivity())
        dataHelperManager = DataHelperManager(requireContext())
    }

    private fun prepareIntroList(): ArrayList<IntroModel> {

        return arrayListOf(
            IntroModel(
                backgroundId = R.drawable.walktrough_bg_one,
                drawableId = R.drawable.walktrough_item_one,
                tittle = getString(R.string.walktrough_one),
                description = getString(R.string.first_description)
            ),
            IntroModel(
                backgroundId = R.drawable.walkthrough_second_bg,
                drawableId = R.drawable.walkthrough_second,
                tittle = getString(R.string.walktrough_second),
                description = getString(R.string.second_description)
            ),
            IntroModel(
                backgroundId = R.drawable.walkthrough_third_bg,
                drawableId = R.drawable.walkthorough_third,
                tittle = getString(R.string.walktrough_third),
                description = getString(R.string.third_description)
            ),
        )
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
                        viewModel.onClickNext(lat = lat, lon = lon)
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
            } else {
                Toast.makeText(
                    context, context?.getText(R.string.need_permission), Toast.LENGTH_SHORT
                ).show()

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


}

