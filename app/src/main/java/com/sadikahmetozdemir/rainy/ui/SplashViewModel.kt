package com.sadikahmetozdemir.rainy.ui

import com.sadikahmetozdemir.rainy.base.BaseViewModel
import com.sadikahmetozdemir.rainy.core.shared.repository.DefaultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private var defaultRepository: DefaultRepository) :
    BaseViewModel() {
    fun toHomePage(lat: String, lon: String) {
        navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment(lat, lon))
    }

}