package com.nexgencoders.whatsappgb.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nexgencoders.whatsappgb.R
import com.nexgencoders.whatsappgb.database.entity.ScannedQrCodeEntity
import com.nexgencoders.whatsappgb.ui.adapter.ImageAdapter.ItemViewHolder
import com.nexgencoders.whatsappgb.utils.Common.generateQRCode

class ScannedQrAdapter(private val qrList: List<ScannedQrCodeEntity>,
                       private val onItemClick: (String) -> Unit ) :
    RecyclerView.Adapter<ScannedQrAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.scan_qr_item_view, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val qrlist = qrList[position]
        Glide.with(holder.itemView.context).load(generateQRCode(qrlist.qrCode,50)).into(holder.qrPreview)
        holder.title.text = qrlist.id.toString()
        holder.itemView.setOnClickListener { v: View? ->
            onItemClick(qrlist.qrCode)
        }
    }

    override fun getItemCount(): Int {
        return qrList.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.tvTitle)
        var qrPreview: ImageView = itemView.findViewById(R.id.imgQr)
    }
}
