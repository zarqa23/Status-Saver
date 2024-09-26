package com.nexgencoders.whatsappgb.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nexgencoders.whatsappgb.R
import com.nexgencoders.whatsappgb.model.CaptionDataModel
import com.nexgencoders.whatsappgb.ui.adapter.ImageAdapter.ItemViewHolder

class CaptionCategoryAdapter(
    private val dataList: List<CaptionDataModel>,
    private val onItemClick: (String, Boolean) -> Unit
) :
    RecyclerView.Adapter<CaptionCategoryAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.caption_cat_item_view, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = dataList[position]
        holder.title.text = data.title
        holder.icon.setImageResource(data.icon)

        holder.itemView.setOnClickListener { v: View? ->
            if ((position + 1) % 3 == 0) {
                onItemClick(data.title,true)
            } else {
                onItemClick(data.title, false)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.cat_title)
        var icon: ImageView = itemView.findViewById(R.id.cat_icon)
    }
}
