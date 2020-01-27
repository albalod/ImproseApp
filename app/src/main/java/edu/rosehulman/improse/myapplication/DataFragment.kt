package edu.rosehulman.improse.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.albalod.photobucket.GamesAdapters
import java.util.zip.Inflater

class DataFragment(val type: String, val parentF : HasChildFragment, val allData:ArrayList<String>): Fragment(){

    private lateinit var adapter: ChildFragmentAdapter
    var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        recyclerView = inflater.inflate(R.layout.fragment_data_list, container, false) as RecyclerView

        if(type.equals("Games")) {
            setAsGamesFragment()
        }

        return recyclerView
    }

    fun setAsGamesFragment(){
        adapter = GamesAdapters(context!!, parentF)
        if(recyclerView != null) {
            recyclerView?.layoutManager = LinearLayoutManager(context)
            recyclerView?.setHasFixedSize(true)
            recyclerView?.adapter = adapter
            allData.forEach{
                adapter.add(it)
                Log.d("Adapter", it)
            }

            Log.d("Adapter", "Adapter was set")
        }
        else{
            Log.d("Adapter", "Adapter was not set")
        }
    }




}