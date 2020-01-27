package edu.rosehulman.improse.myapplication

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class DataViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    val captionText: TextView = itemView.findViewById(R.id.data_text)

    lateinit var childAdapter : ChildFragmentAdapter

    constructor(itemView:View, adapter: ChildFragmentAdapter): this(itemView){
        this.childAdapter = adapter

        itemView.setOnClickListener{
            adapter.switchToChildFragment(adapterPosition)
            true
        }
    }

    fun bind(data:String){
        Log.d("bind", "$data")
        captionText.text =data

    }



}