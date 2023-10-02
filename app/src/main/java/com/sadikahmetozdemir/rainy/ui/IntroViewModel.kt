package com.sadikahmetozdemir.rainy.ui

import com.sadikahmetozdemir.rainy.base.BaseViewModel

class IntroViewModel: BaseViewModel() {
    fun onClickNext(lat:String,lon:String){
        navigate(IntroFragmentDirections.actionIntroFragmentToHomeFragment(lat, lon))
    }
}