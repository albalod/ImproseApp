package edu.rosehulman.improse.myapplication

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

abstract class ChildFragmentAdapter(): RecyclerView.Adapter<DataViewHolder>() {

    var allData = ArrayList<String>()

    override fun onBindViewHolder(viewHolder: DataViewHolder, index: Int) {
        viewHolder.bind(allData[index])
    }

    abstract fun add(data:String)
    abstract fun setBigData(data:ArrayList<Any>);
    abstract fun switchToChildFragment(pos:Int);

}