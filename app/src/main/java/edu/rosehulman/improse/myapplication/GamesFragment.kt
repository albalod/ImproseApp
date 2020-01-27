package edu.rosehulman.improse.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import java.util.zip.Inflater

class GamesFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val games_view = inflater.inflate(R.layout.fragment_view_games, container,false)


        //child fragment
        val childFragMan = getChildFragmentManager();
        val childFragTrans = childFragMan.beginTransaction();
        val fragB =  DataFragment();
        childFragTrans.add(R.id.games_data, fragB);
        childFragTrans.addToBackStack("GameData");
        childFragTrans.commit();

        return games_view;

    }

}