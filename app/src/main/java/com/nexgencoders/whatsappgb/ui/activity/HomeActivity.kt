package com.nexgencoders.whatsappgb.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.nexgencoders.whatsappgb.R
import com.nexgencoders.whatsappgb.databinding.ActivityHomeBinding
import com.nexgencoders.whatsappgb.services.NLService
import com.nexgencoders.whatsappgb.ui.fragment.CaptionFragmentDirections
import com.nexgencoders.whatsappgb.ui.fragment.HomeFragmentDirections
import com.nexgencoders.whatsappgb.utils.Common.APP_DIR
import com.nexgencoders.whatsappgb.utils.Common.rateUs
import com.nexgencoders.whatsappgb.utils.Common.sendFeedback
import com.nexgencoders.whatsappgb.utils.Common.shareApp
import com.nexgencoders.whatsappgb.utils.Common.showMoreApps
import com.nexgencoders.whatsappgb.utils.toast

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.navigationBarColor = Color.parseColor("#EEEEEE")
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val onBackPressedCallback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val navController = findNavController(R.id.fragmentContainerView)
                    if (navController.currentDestination?.id == R.id.homeFragment){
                        finish()
                    }else{
                        navController.navigateUp()
                    }
                }
            }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        if (APP_DIR == null || APP_DIR!!.isEmpty()) {
            APP_DIR = getExternalFilesDir("WhatsappGB")!!.path
        }
        val intent = Intent(applicationContext, NLService::class.java)
        startService(intent)

        clickListener()
    }

    private fun clickListener() {

        with(binding.sideBar){
            val main = binding.main
            btnClose.setOnClickListener {
                if(main.isOpen) main.closeDrawer(GravityCompat.START, true)
            }
            btnPremium.setOnClickListener { toast("Coming Soon...") }
            tvFeedback.setOnClickListener { sendFeedback() }
            tvShare.setOnClickListener { shareApp()  }
            tvMoreapps.setOnClickListener { showMoreApps()  }
            tvRateUs.setOnClickListener { rateUs() }
            tvPrivacyPolicy.setOnClickListener {
                if(main.isOpen) main.closeDrawer(GravityCompat.START, true)
                val navController = findNavController(R.id.fragmentContainerView)
                val bundle = Bundle().apply {
                    putString("data", "privacy")
                }
                navController.navigate(R.id.whatsappWebFragment, bundle)
            }
        }
    }


    fun openDrawer() {
        if(!binding.main.isOpen) binding.main.openDrawer(GravityCompat.START, true)
    }



}