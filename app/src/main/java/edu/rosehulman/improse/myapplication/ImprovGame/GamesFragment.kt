package edu.rosehulman.improse.myapplication.ImprovGame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.rosehulman.improse.myapplication.MainActivity
import edu.rosehulman.improse.myapplication.R

class GamesFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val games_view = inflater.inflate(R.layout.fragment_view_games, container,false)


        //child fragment
        val childFragMan = getChildFragmentManager();
        val childFragTrans = childFragMan.beginTransaction();

        val fragB = GameDataFragment(this);
        childFragTrans.replace(R.id.games_data, fragB);
        childFragTrans.commit();
        return games_view;
    }

      fun switchToChildFragment(game: ImprovGame){
        val ft = (context as MainActivity).supportFragmentManager.beginTransaction()
        ft.hide(parentFragment!!)
        ft.replace(R.id.content_layout, GamesChildFragment.newInstance(game))
        ft.commit()
    }

}