package edu.rosehulman.improse.myapplication.Bluetooth

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.improse.myapplication.*
import edu.rosehulman.improse.myapplication.R

class BTAdapters(val con : Context, val bf: BluetoothFragment): RecyclerView.Adapter<BTViewHolder>() {

    var allDevices = ArrayList<String>()


    override fun onCreateViewHolder(parent: ViewGroup, index: Int): BTViewHolder {
        val view = LayoutInflater.from(con).inflate(R.layout.data_card, parent, false)
        return BTViewHolder(
            view,
            this
        )
    }

    override fun getItemCount(): Int {
        return allDevices.size
    }

    fun setDevice(pos: Int) {
        bf.setDevice(pos)
    }

    fun add(string: String) {
        allDevices.add(0, string)
        notifyItemInserted(0)
    }


    override fun onBindViewHolder(holder: BTViewHolder, position: Int) {
        holder.bind(allDevices[position])
    }
}

