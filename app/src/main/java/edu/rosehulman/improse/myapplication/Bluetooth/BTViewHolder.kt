package edu.rosehulman.improse.myapplication.Bluetooth

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.improse.myapplication.R

class BTViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    val captionText: TextView = itemView.findViewById(R.id.data_text)

    lateinit var childAdapter : BTAdapters

    constructor(itemView:View, adapter: BTAdapters): this(itemView){
        this.childAdapter = adapter

        itemView.setOnClickListener{
            adapter.setDevice(adapterPosition)
            true
        }
    }

    fun bind(data:String){
        Log.d("bind", "$data")
        captionText.text = data

    }
}
