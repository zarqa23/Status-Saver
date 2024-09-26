package com.nexgencoders.whatsappgb.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nexgencoders.whatsappgb.R
import com.nexgencoders.whatsappgb.model.Status
import com.nexgencoders.whatsappgb.ui.activity.DetailActivity
import com.nexgencoders.whatsappgb.utils.visible

class VideoAdapter(videoList: List<Status>, container: RelativeLayout) :
    RecyclerView.Adapter<VideoAdapter.ItemViewHolder>() {
    private val videoList: List<Status> = videoList
    private var context: Context? = null
    private val container: RelativeLayout = container

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        context = parent.context
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_status, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val status: Status = videoList[position]
        holder.btnPlay.visible()

        if (status.isApi30) {
            Glide.with(holder.itemView.context).load(status.documentFileUri)
                .into(holder.imageView)
        } else {
            Glide.with(holder.itemView.context).load(status.file).into(holder.imageView)
        }
        holder.imageView.setOnClickListener { v: View? ->
            val detailIntent = Intent(holder.itemView.context, DetailActivity::class.java)
            detailIntent.putExtra("uri",status.documentFileUri)
            detailIntent.putExtra("status",status)
            detailIntent.putExtra("isImage",false)
            holder.itemView.context.startActivity(detailIntent)
        }
        val inflater: LayoutInflater = LayoutInflater.from(context)
//        val view1: View = inflater.inflate(R.layout.view_video_full_screen, null)

//        holder.imageView.setOnClickListener { v ->
//            val alertDg = AlertDialog.Builder(context)
//            val mediaControls: FrameLayout = view1.findViewById<FrameLayout>(R.id.videoViewWrapper)
//
//            if (view1.parent != null) {
//                (view1.parent as ViewGroup).removeView(view1)
//            }
//
//            alertDg.setView(view1)
//
//            val videoView: VideoView = view1.findViewById<VideoView>(R.id.video_full)
//
//            val mediaController = MediaController(context, false)
//
//            videoView.setOnPreparedListener(OnPreparedListener { mp: MediaPlayer ->
//                mp.start()
//                mediaController.show(0)
//                mp.setLooping(true)
//            })
//
//            videoView.setMediaController(mediaController)
//            mediaController.setMediaPlayer(videoView)
//
//            if (status.isApi30()) {
//                videoView.setVideoURI(status.getDocumentFile().getUri())
//            } else {
//                videoView.setVideoURI(Uri.fromFile(status.getFile()))
//            }
//            videoView.requestFocus()
//
//            (mediaController.parent as ViewGroup).removeView(mediaController)
//
//            if (mediaControls.getParent() != null) {
//                mediaControls.removeView(mediaController)
//            }
//
//            mediaControls.addView(mediaController)
//
//            val alert2 = alertDg.create()
//
//            alert2.window!!.attributes.windowAnimations = R.style.SlidingDialogAnimation
//            alert2.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            alert2.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            alert2.show()
//        }
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var btnPlay: ImageView = itemView.findViewById(R.id.btn_play)
        var imageView: ImageView = itemView.findViewById(R.id.ivThumbnail)
    }
}
