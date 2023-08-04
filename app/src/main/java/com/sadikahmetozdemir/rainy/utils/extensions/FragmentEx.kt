package com.sadikahmetozdemir.rainy.utils.extensions

import android.graphics.Color
import android.view.Gravity
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.sadikahmetozdemir.rainy.R

fun Fragment.snackbar(message: String) {
    this.let { view ->
        val snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
        snackbar.setAction(R.string.text_action) { snackbar.dismiss() }
        val view = snackbar.view
        val params: FrameLayout.LayoutParams = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.BOTTOM
        view.layoutParams = params
        snackbar.setBackgroundTint(Color.TRANSPARENT)
        snackbar.show()
    }
}