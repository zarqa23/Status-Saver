package com.nexgencoders.whatsappgb.ui.activity

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.widget.MediaController
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.nexgencoders.whatsappgb.databinding.ActivityDetailBinding
import com.nexgencoders.whatsappgb.model.Status
import com.nexgencoders.whatsappgb.utils.Common.copyFile
import com.nexgencoders.whatsappgb.utils.gone
import com.nexgencoders.whatsappgb.utils.visible

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var isImage:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.navigationBarColor = Color.parseColor("#EEEEEE")
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getData()
    }

    private fun getData() {
        if (intent != null) {
            val status: Status? = intent.getParcelableExtra<Status>("status")
            isImage = intent.getBooleanExtra("isImage",false)
            if (isImage){
                binding.videoCard.gone()
                binding.imgCard.visible()
            if (status != null) {
                if (status.isApi30) {
                    Glide.with(this).load(status.documentFileUri).into(binding.previewImg)
                } else {
                    Glide.with(this).load(status.file).into(binding.previewImg)
                }
            }
            }else{
                binding.videoCard.visible()
                binding.imgCard.gone()
                playVideo(status!!)
            }
            binding.btnBack.setOnClickListener { finish() }
            binding.btnShare.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND)
                if (isImage) {
                    shareIntent.setType("image/jpg")
                }else{
                    shareIntent.setType("image/mp4")
                }
                if (status != null) {
                    if (status.isApi30) {
                        shareIntent.putExtra(Intent.EXTRA_STREAM, status.documentFileUri)
                    } else {
                        shareIntent.putExtra(
                            Intent.EXTRA_STREAM,
                            Uri.parse("file://" + status.file!!.absolutePath)
                        )
                    }
                }
                startActivity(Intent.createChooser(shareIntent, "Share image"))
            }
            binding.btnSave.setOnClickListener {
                if (status != null) {
                    copyFile(status, this, binding.main)
                }
            }
        }
    }

    private fun playVideo(status: Status) {
            val mediaControls = binding.videoViewWrapper
            val videoView = binding.videoFull

            val mediaController =
                MediaController(this, false)

            videoView.setOnPreparedListener { mp: MediaPlayer ->
                mp.start()
                mediaController.show(0)
                mp.isLooping = true
            }

            videoView.setMediaController(mediaController)
            mediaController.setMediaPlayer(videoView)

            if (status.isApi30) {
                videoView.setVideoURI(status.documentFileUri)
            } else {
                videoView.setVideoURI(Uri.fromFile(status.file))
            }
            videoView.requestFocus()

            (mediaController.parent as ViewGroup).removeView(
                mediaController
            )

            if (mediaControls.parent != null) {
                mediaControls.removeView(mediaController)
            }

            mediaControls.addView(mediaController)
    }

}