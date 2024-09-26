package com.nexgencoders.whatsappgb.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nexgencoders.whatsappgb.R
import com.nexgencoders.whatsappgb.model.HomeDataModel

class HomeAdapter(private val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private var data = ArrayList<HomeDataModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.home_data_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemList = data[position]
        with(holder){
            icon.setImageResource(itemList.icon)
            itemName.text = itemList.title
            itemView.setOnClickListener {
                onItemClick(itemList.ref)
            }
            if(itemList.title == ""){
                itemName.visibility = View.GONE
            }
            else{
                itemName.visibility = View.VISIBLE
            }
        }
    }

    fun setData(items: ArrayList<HomeDataModel>) {
        if (items.isNotEmpty()) {
            data = items
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icon: ImageView = itemView.findViewById(R.id.icon)
        var itemName: TextView = itemView.findViewById(R.id.title)

    }

}