package com.sadikahmetozdemir.rainy.ui

import androidx.lifecycle.MutableLiveData
import com.sadikahmetozdemir.rainy.base.BaseViewModel

class BottomSheetViewModel : BaseViewModel() {

    val bsEditText = MutableLiveData("")
    val checkboxTemp = MutableLiveData<Boolean>(false)
    val checkboxHumidity = MutableLiveData<Boolean>(false)
    val checkboxWindRate = MutableLiveData<Boolean>(false)
}