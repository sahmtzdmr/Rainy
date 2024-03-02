package com.sadikahmetozdemir.rainy.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sadikahmetozdemir.rainy.base.BaseViewModel
import com.sadikahmetozdemir.rainy.core.shared.remote.ShareModel

class BottomSheetViewModel : BaseViewModel() {

    val bsEditText = MutableLiveData("")
    val checkboxTemp = MutableLiveData<Boolean>(false)
    val checkboxHumidity = MutableLiveData<Boolean>(false)
    val checkboxWindRate = MutableLiveData<Boolean>(false)
    private val _message: MutableLiveData<ShareModel> = MutableLiveData()
    val message: LiveData<ShareModel> get() = _message

}