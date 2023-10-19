package com.sadikahmetozdemir.rainy.utils.extensions

import android.graphics.Bitmap
import android.view.View
import androidx.core.graphics.applyCanvas
import androidx.core.view.ViewCompat

fun View.drawToBitmap(config: Bitmap.Config = Bitmap.Config.ARGB_8888): Bitmap {
    if (!ViewCompat.isLaidOut(this)) {
        throw IllegalStateException("View needs to be laid out before calling drawToBitmap()")
    }
    return Bitmap.createBitmap(width, height, config).applyCanvas {
        translate(-scrollX.toFloat(), -scrollY.toFloat())
        draw(this)
    }
}