package edu.rosehulman.improse.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.albalod.photobucket.GamesAdapters
import kotlinx.android.synthetic.main.fragment_view_games.*
import java.util.zip.Inflater

class GamesFragment : HasChildFragment(){

    val allGames = ArrayList<ImprovGame>()
    private lateinit var adapter: ChildFragmentAdapter
    var recyclerView: RecyclerView? = null

    init {
        allGames.add(ImprovGame(0, getString(R.string.yee_haw), getString(R.string.yee_haw_description), 0, 0f))
    }



    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val games_view = inflater.inflate(R.layout.fragment_view_games, container,false)
        recyclerView = game_recycler_view
//        adapter =


        return games_view;
    }

    override fun switchToChildFragment(pos: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}