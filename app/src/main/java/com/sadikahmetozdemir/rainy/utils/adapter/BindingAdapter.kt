package com.sadikahmetozdemir.rainy.utils.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.sadikahmetozdemir.rainy.R

@BindingAdapter("imageResource")
fun setImageResource(imageview: ImageView, url: String) {
    when (url) {
        "01d" -> imageview.setImageDrawable(imageview, R.drawable.png_clear)
        "01n" -> imageview.setImageDrawable(imageview, R.drawable.png_clear)
        "02d" -> imageview.setImageDrawable(imageview, R.drawable.png_few_clouds)
        "02n" -> imageview.setImageDrawable(imageview, R.drawable.png_few_clouds)
        "03d" -> imageview.setImageDrawable(imageview, R.drawable.png_scattered_clouds)
        "03n" -> imageview.setImageDrawable(imageview, R.drawable.png_scattered_clouds)
        "04d" -> imageview.setImageDrawable(imageview, R.drawable.png_scattered_clouds)
        "04n" -> imageview.setImageDrawable(imageview, R.drawable.png_scattered_clouds)
        "09d" -> imageview.setImageDrawable(imageview, R.drawable.png_shower_rain)
        "09n" -> imageview.setImageDrawable(imageview, R.drawable.png_shower_rain)
        "10d" -> imageview.setImageDrawable(imageview, R.drawable.png_rain)
        "10n" -> imageview.setImageDrawable(imageview, R.drawable.png_rain)
        "11d" -> imageview.setImageDrawable(imageview, R.drawable.png_thunderstorm)
        "11n" -> imageview.setImageDrawable(imageview, R.drawable.png_thunderstorm)
        "13d" -> imageview.setImageDrawable(imageview, R.drawable.png_snow)
        "13n" -> imageview.setImageDrawable(imageview, R.drawable.png_snow)
        "50d" -> imageview.setImageDrawable(imageview, R.drawable.ic_fog_svgrepo_com)
        "50n" -> imageview.setImageDrawable(imageview, R.drawable.ic_fog_svgrepo_com)
    }

}

private fun ImageView.setImageDrawable(imageview: ImageView, image: Int) {
    Glide.with(context).load(image).into(imageview)

}

