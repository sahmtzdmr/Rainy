package com.sadikahmetozdemir.rainy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        exitProcess(0)
    }
}