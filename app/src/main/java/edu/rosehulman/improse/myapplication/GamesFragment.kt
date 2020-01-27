package edu.rosehulman.improse.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import java.util.zip.Inflater

class GamesFragment : HasChildFragment(){

    val allGames = ArrayList<ImprovGame>();

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val games_view = inflater.inflate(R.layout.fragment_view_games, container,false)


        //child fragment
        val childFragMan = getChildFragmentManager();
        val childFragTrans = childFragMan.beginTransaction();
        allGames.add(ImprovGame(0, getString(R.string.yee_haw), getString(R.string.yee_haw_description), 0, 0f))
        val allData = ArrayList<String>()
        allGames.forEach{
            allData.add(it.name)
        }
        val fragB =  DataFragment("Games", this, allData);
        childFragTrans.replace(R.id.games_data, fragB);
        childFragTrans.addToBackStack("GameData");
        childFragTrans.commit();

        return games_view;
    }

    override fun switchToChildFragment(pos:Int){
        val ft = (context as MainActivity).supportFragmentManager.beginTransaction()
        ft.hide(parentFragment!!)
        ft.replace(R.id.content_layout, GamesChildFragment.newInstance(allGames.get(pos)))
        //ft.addToBackStack("GameParent")
        ft.commit()
    }

}