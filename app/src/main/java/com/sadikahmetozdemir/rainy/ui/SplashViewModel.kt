package com.sadikahmetozdemir.rainy.ui

import com.sadikahmetozdemir.rainy.base.BaseViewModel
import com.sadikahmetozdemir.rainy.core.shared.repository.DefaultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() :
    BaseViewModel() {
    fun toHomePage(lat: String, lon: String) {
        navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment(lat, lon))
    }
    fun toIntro(){
        navigate(SplashFragmentDirections.actionSplashFragmentToIntroFragment())
    }

}