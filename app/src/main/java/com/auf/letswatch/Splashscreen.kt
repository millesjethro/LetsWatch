package com.auf.letswatch

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.auf.letswatch.databinding.ActivitySplashscreenBinding
import kotlin.Long


class Splashscreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashscreenBinding
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideMenus()
        binding.imageView3.animate().rotation(360f).setDuration(5000).start()
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                val gotoLogin = Intent(this@Splashscreen, Login::class.java )
                startActivity(gotoLogin)
            }
        }.start()

    }
    @RequiresApi(Build.VERSION_CODES.R)
    fun hideMenus(){
        supportActionBar?.hide()
        window.insetsController?.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        window.insetsController?.hide(WindowInsets.Type.statusBars())
        window.insetsController?.hide(WindowInsets.Type.navigationBars())
    }
}