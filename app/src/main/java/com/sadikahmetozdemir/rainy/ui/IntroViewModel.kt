package com.sadikahmetozdemir.rainy.ui

import com.sadikahmetozdemir.rainy.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor() : BaseViewModel() {
    fun onClickNext(lat: String, lon: String) {
        navigate(IntroFragmentDirections.actionIntroFragmentToHomeFragment(lat, lon))
    }
}