package edu.rosehulman.improse.myapplication.Bluetooth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.improse.myapplication.Home.HomeAdapters
import edu.rosehulman.improse.myapplication.Home.HomeFragment
import edu.rosehulman.improse.myapplication.R

class BTDataFragment(val parentF : BluetoothFragment, val names : List<String>): Fragment(){

    private lateinit var adapter: BTAdapters
    var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        recyclerView = inflater.inflate(R.layout.fragment_data_list, container, false) as RecyclerView

        adapter = BTAdapters(context!!, parentF)
        if(recyclerView != null) {
            recyclerView?.layoutManager = LinearLayoutManager(context)
            recyclerView?.setHasFixedSize(true)
            recyclerView?.adapter = adapter
            names.forEach {
                adapter.add(it)
            }
            Log.d("Adapter", "BT was set")
        }
        else{
            Log.d("Adapter", "BT was not set")
        }
        return recyclerView
    }

}