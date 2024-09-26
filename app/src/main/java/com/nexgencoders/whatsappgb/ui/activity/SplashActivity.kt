package com.nexgencoders.whatsappgb.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nexgencoders.whatsappgb.databinding.ActivitySplashBinding
import com.nexgencoders.whatsappgb.utils.startActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleClicks()
    }

    private fun handleClicks() {
        binding.btnStart.setOnClickListener {
            startActivity(HomeActivity::class.java)
            finish()
        }
    }


    companion object {
        private const val LOG_TAG = "SplashActivity"
    }
}