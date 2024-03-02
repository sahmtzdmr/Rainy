package com.sadikahmetozdemir.rainy.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import com.sadikahmetozdemir.rainy.R
import com.sadikahmetozdemir.rainy.base.BaseBottomSheet
import com.sadikahmetozdemir.rainy.databinding.ShareBottomsheetBinding

class BottomSheetFragment :
    BaseBottomSheet<ShareBottomsheetBinding, BottomSheetViewModel>(R.layout.share_bottomsheet) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDatas()
    }

    fun shareData() {
        viewModel.apply {
            checkboxTemp.observe(viewLifecycleOwner) { isChecked ->
                binding.cbTemp.isChecked = isChecked
            }
            checkboxHumidity.observe(viewLifecycleOwner) { isChecked ->
                binding.cbHumidity.isChecked = isChecked
            }
            checkboxWindRate.observe(viewLifecycleOwner) { isChecked ->
                binding.cbWind.isChecked = isChecked
            }
            //TODO() should be reworked. Never go inside observe
            message.observe(viewLifecycleOwner) { item ->
                item.let {
                    it.shareMessage = binding.etMessage.text.toString()
                    Log . d ("TAG", "shareData: ")
                }

            }
        }
    }

    fun initDatas() {
        binding.apply {
            cbTemp.setOnCheckedChangeListener { _, isChecked ->
                viewModel.checkboxTemp.value = isChecked

            }
            cbHumidity.setOnCheckedChangeListener { _, isChecked ->
                viewModel.checkboxHumidity.value = isChecked
            }
            cbWind.setOnCheckedChangeListener { _, isChecked ->
                viewModel.checkboxWindRate.value = isChecked
            }
            shareButton.setOnClickListener {
                shareData()
            }
        }


    }
}

