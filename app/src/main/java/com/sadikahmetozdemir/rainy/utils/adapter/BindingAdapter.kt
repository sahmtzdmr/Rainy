package com.sadikahmetozdemir.rainy.utils.adapter

import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.sadikahmetozdemir.rainy.R

@BindingAdapter("imageResource")
fun setResource(imageview: ImageView, url: String?) {
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
    Glide.with(context).load(image)
        .placeholder(R.drawable.png_snow)
        .into(imageview)


}

fun LottieAnimationView.changeWeatherIcon(weatherCondition: String) {
    val iconFileName = getIconFileName(weatherCondition)
    setAnimation(iconFileName)
    playAnimation()
}

private fun getIconFileName(weatherCondition: String): Int {

    return when (weatherCondition) {
        "01d" -> R.raw.weather_day_clear_sky
        "01n" -> R.raw.weather_night_clear_sky
        "02d" -> R.raw.weather_day_few_clouds
        "02n" -> R.raw.weather_night_few_clods
        "03d" -> R.raw.weather_day_scattered_clouds
        "03n" -> R.raw.weather_night_scattered_clouds
        "04d" -> R.raw.weather_day_broken_clouds
        "04n" -> R.raw.weather_night_broken_clouds
        "09d" -> R.raw.weather_day_shower_rains
        "09n" -> R.raw.weather_night_shower_rains
        "10d" -> R.raw.weather_day_rain
        "10n" -> R.raw.weather_night_rain
        "11d" -> R.raw.weather_day_thunderstorm
        "11n" -> R.raw.weather_night_thunderstorm
        "13d" -> R.raw.weather_day_snow
        "13n" -> R.raw.weather_night_snow
        "50d" -> R.raw.weather_day_mist
        "50n" -> R.raw.weather_night_mist

        else -> R.raw.weather_night_mist
    }
}

@BindingAdapter("backgroundResource")
fun setBackgroundResource(constraintLayout: ConstraintLayout, url: String?) {
    when (url) {
        "01d" -> constraintLayout.setBackgroundResource(
            constraintLayout.resources.getIdentifier(
                "bg_screen_cloudly",
                "drawable",
                constraintLayout.context.packageName
            )
        )
        "01n" -> constraintLayout.setBackgroundResource(
            constraintLayout.resources.getIdentifier(
                "bg_screen_night",
                "drawable",
                constraintLayout.context.packageName
            )
        )
        "02d" -> constraintLayout.setBackgroundResource(
            constraintLayout.resources.getIdentifier(
                "bg_screen_cloudly",
                "drawable",
                constraintLayout.context.packageName
            )
        )
        "02n" -> constraintLayout.setBackgroundResource(
            constraintLayout.resources.getIdentifier(
                "bg_screen_night",
                "drawable",
                constraintLayout.context.packageName
            )
        )
        "03d" -> constraintLayout.setBackgroundResource(
            constraintLayout.resources.getIdentifier(
                "bg_screen_cloudly",
                "drawable",
                constraintLayout.context.packageName
            )
        )
        "03n" -> constraintLayout.setBackgroundResource(
            constraintLayout.resources.getIdentifier(
                "bg_screen_night",
                "drawable",
                constraintLayout.context.packageName
            )
        )
        "04d" -> constraintLayout.setBackgroundResource(
            constraintLayout.resources.getIdentifier(
                "bg_screen_cloudly",
                "drawable",
                constraintLayout.context.packageName
            )
        )
        "04n" -> constraintLayout.setBackgroundResource(
            constraintLayout.resources.getIdentifier(
                "bg_screen_night",
                "drawable",
                constraintLayout.context.packageName
            )
        )
        "09d" -> constraintLayout.setBackgroundResource(
            constraintLayout.resources.getIdentifier(
                "bg_screen_cloudly",
                "drawable",
                constraintLayout.context.packageName
            )
        )
        "09n" -> constraintLayout.setBackgroundResource(
            constraintLayout.resources.getIdentifier(
                "bg_screen_night",
                "drawable",
                constraintLayout.context.packageName
            )
        )
        "10d" -> constraintLayout.setBackgroundResource(
            constraintLayout.resources.getIdentifier(
                "bg_screen_cloudly",
                "drawable",
                constraintLayout.context.packageName
            )
        )
        "10n" -> constraintLayout.setBackgroundResource(
            constraintLayout.resources.getIdentifier(
                "bg_screen_night",
                "drawable",
                constraintLayout.context.packageName
            )
        )
        "11d" -> constraintLayout.setBackgroundResource(
            constraintLayout.resources.getIdentifier(
                "bg_screen_cloudly",
                "drawable",
                constraintLayout.context.packageName
            )
        )
        "11n" -> constraintLayout.setBackgroundResource(
            constraintLayout.resources.getIdentifier(
                "bg_screen_night",
                "drawable",
                constraintLayout.context.packageName
            )
        )
        "13d" -> constraintLayout.setBackgroundResource(
            constraintLayout.resources.getIdentifier(
                "bg_screen_cloudly",
                "drawable",
                constraintLayout.context.packageName
            )
        )
        "13n" -> constraintLayout.setBackgroundResource(
            constraintLayout.resources.getIdentifier(
                "bg_screen_night",
                "drawable",
                constraintLayout.context.packageName
            )
        )
        "50d" -> constraintLayout.setBackgroundResource(
            constraintLayout.resources.getIdentifier(
                "bg_screen_cloudly",
                "drawable",
                constraintLayout.context.packageName
            )
        )
        "50n" -> constraintLayout.setBackgroundResource(
            constraintLayout.resources.getIdentifier(
                "bg_screen_night",
                "drawable",
                constraintLayout.context.packageName
            )
        )
    }
}

