package edu.rosehulman.albalod.photobucket

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import edu.rosehulman.improse.myapplication.*


class GamesAdapters(val con : Context, val gf:HasChildFragment): ChildFragmentAdapter(){

    var allGames = ArrayList<ImprovGame>()

    override fun onCreateViewHolder(parent: ViewGroup, index: Int): DataViewHolder {
        val view = LayoutInflater.from(con).inflate(R.layout.data_card, parent, false)

        return DataViewHolder(view, this)
    }

    override fun getItemCount():Int {
        return allData.size
    }

    override fun add(data:String) {
        allData.add(data)
        notifyDataSetChanged()
    }

    @Suppress("UNCHECKED_CAST")
    override fun setBigData(data: ArrayList<Any>) {
        this.allGames = (data as ArrayList<ImprovGame>)
        notifyDataSetChanged()
    }

    override fun switchToChildFragment(pos: Int) {
        gf.switchToChildFragment(pos)
    }

}