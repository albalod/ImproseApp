package edu.rosehulman.improse.myapplication.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.rosehulman.improse.myapplication.ImprovGame.HomeDataFragment
import edu.rosehulman.improse.myapplication.MainActivity
import edu.rosehulman.improse.myapplication.R

class HomeFragment : Fragment(){


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val home_view = inflater.inflate(R.layout.fragment_home, container,false)

        //child fragment
        val childFragMan = getChildFragmentManager();
        val childFragTrans = childFragMan.beginTransaction();
        val fragA =  HomeDataFragment(this);
        childFragTrans.add(R.id.home_data, fragA);
       // childFragTrans.addToBackStack("GameData");
        childFragTrans.commit();
        return home_view
    }

    fun switchToChildFragment(event: ImprovEvent) {
        val ft = (context as MainActivity).supportFragmentManager.beginTransaction()
        ft.hide(parentFragment!!)
        ft.replace(R.id.content_layout, HomeChildFragment.newInstance(event))
        ft.commit()
    }

}