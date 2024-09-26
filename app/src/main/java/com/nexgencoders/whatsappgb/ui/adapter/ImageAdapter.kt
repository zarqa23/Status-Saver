package com.nexgencoders.whatsappgb.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nexgencoders.whatsappgb.R
import com.nexgencoders.whatsappgb.model.Status
import com.nexgencoders.whatsappgb.ui.activity.DetailActivity
import com.nexgencoders.whatsappgb.imageeditor.ImageEditActivity
import com.nexgencoders.whatsappgb.ui.adapter.ImageAdapter.ItemViewHolder

class ImageAdapter(private val imagesList: List<Status>, private val type: String) :
    RecyclerView.Adapter<ItemViewHolder>() {
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_status, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val status = imagesList[position]
        if (status.isApi30) {
            Glide.with(context!!).load(status.documentFileUri).into(holder.imageView)
        } else {
            Glide.with(context!!).load(status.file).into(holder.imageView)
        }

        holder.imageView.setOnClickListener { v: View? ->
            if (type == "edit") {
                val detailIntent = Intent(holder.itemView.context, ImageEditActivity::class.java)
                detailIntent.putExtra("status", status)
                holder.itemView.context.startActivity(detailIntent)
            } else {
                val detailIntent = Intent(holder.itemView.context, DetailActivity::class.java)
                detailIntent.putExtra("uri", status.documentFileUri)
                detailIntent.putExtra("status", status)
                detailIntent.putExtra("isImage", true)
                holder.itemView.context.startActivity(detailIntent)
            }
        }
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.ivThumbnail)
    }
}
