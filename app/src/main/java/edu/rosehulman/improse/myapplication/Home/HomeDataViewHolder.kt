package edu.rosehulman.improse.myapplication.Home

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.improse.myapplication.R

class HomeDataViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    val captionText: TextView = itemView.findViewById(R.id.data_text)

    lateinit var childAdapter : HomeAdapters

    constructor(itemView:View, adapter: HomeAdapters): this(itemView){
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


