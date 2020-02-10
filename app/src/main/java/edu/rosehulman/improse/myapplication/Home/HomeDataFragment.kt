package edu.rosehulman.improse.myapplication.ImprovGame

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

class HomeDataFragment(val parentF : HomeFragment): Fragment(){

    private lateinit var adapter: HomeAdapters
    var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        recyclerView = inflater.inflate(R.layout.fragment_data_list, container, false) as RecyclerView

        adapter = HomeAdapters(context!!, parentF)
        if(recyclerView != null) {
            recyclerView?.layoutManager = LinearLayoutManager(context)
            recyclerView?.setHasFixedSize(true)
            recyclerView?.adapter = adapter
            adapter.addSnapshotListener()
            Log.d("Adapter", "HomeAdapter was set")
        }
        else{
            Log.d("Adapter", "Adapter was not set")
        }
        return recyclerView
    }

}