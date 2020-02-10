package edu.rosehulman.improse.myapplication.Members

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.improse.myapplication.R

class StatsDataFragment(val parentF : StatsFragment): Fragment(){

    private lateinit var adapter: StatsAdapters
    var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        recyclerView = inflater.inflate(R.layout.fragment_data_list, container, false) as RecyclerView

        adapter = StatsAdapters(context!!, parentF)
        if(recyclerView != null) {
            recyclerView?.layoutManager = LinearLayoutManager(context)
            recyclerView?.setHasFixedSize(true)
            recyclerView?.adapter = adapter
            adapter.addSnapshotListener()
            Log.d("Adapter", "Adapter was set")

            //adapter.add(ImprovGame(getString(R.string.yee_haw), getString(R.string.yee_haw_description), 0, 0.0))
        }
        else{
            Log.d("Adapter", "Adapter was not set")
        }
        return recyclerView
    }

}