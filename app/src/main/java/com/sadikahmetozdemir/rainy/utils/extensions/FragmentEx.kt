package com.sadikahmetozdemir.rainy.utils.extensions

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.sadikahmetozdemir.rainy.R

fun Fragment.snackbar(message: String) {
    this.let { fragment ->
        val view = fragment.requireView()
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackbar.setAction(R.string.text_action) { snackbar.dismiss() }
        snackbar.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        
        val snackbarView = snackbar.view
        val params: FrameLayout.LayoutParams = snackbarView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        params.setMargins(16, 16, 16, 0)
        snackbarView.layoutParams = params
        
        // Okunabilir arka plan rengi
        snackbarView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.russian_violet))
        snackbarView.alpha = 0.95f
        snackbarView.elevation = 8f
        
        // Text rengi
        snackbar.setTextColor(Color.WHITE)
        
        snackbar.show()
    }
}