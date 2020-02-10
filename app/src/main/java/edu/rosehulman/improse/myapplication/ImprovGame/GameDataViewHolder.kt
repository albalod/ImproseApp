package edu.rosehulman.improse.myapplication.ImprovGame

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.albalod.photobucket.GamesAdapters
import edu.rosehulman.improse.myapplication.R

class GameDataViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    val captionText: TextView = itemView.findViewById(R.id.data_text)

    lateinit var childAdapter : GamesAdapters

    constructor(itemView:View, adapter: GamesAdapters): this(itemView){
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