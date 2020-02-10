package edu.rosehulman.improse.myapplication.Members

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.improse.myapplication.R

class StatsDataViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    val captionText: TextView = itemView.findViewById(R.id.data_text)

    lateinit var childAdapter : StatsAdapters

    constructor(itemView: View, adapter: StatsAdapters): this(itemView){
        this.childAdapter = adapter

        itemView.setOnClickListener{
            adapter.switchToChildFragment(adapterPosition)
            true
        }
    }

    fun bind(data:String){
        Log.d("bind", "$data")
        captionText.text = data

    }
}