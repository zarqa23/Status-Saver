package com.nexgencoders.whatsappgb.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nexgencoders.whatsappgb.R
import com.nexgencoders.whatsappgb.model.CaptionData
import com.nexgencoders.whatsappgb.model.CaptionDataModel
import com.nexgencoders.whatsappgb.ui.adapter.ImageAdapter.ItemViewHolder

class CaptionDetailAdapter(private val dataList: List<CaptionData>,
                           private val clickListener: (String,Boolean) -> Unit ) :
    RecyclerView.Adapter<CaptionDetailAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.caption_detail_item_view, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = dataList[position]
        holder.title.text = data.text
        holder.btnShare.setOnClickListener {clickListener(data.text,false)  }
        holder.btnCopy.setOnClickListener { clickListener(data.text,true)  }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.tvCaption)
        var btnCopy: TextView = itemView.findViewById(R.id.btnCopy)
        var btnShare: TextView = itemView.findViewById(R.id.btnShare)
    }
}
