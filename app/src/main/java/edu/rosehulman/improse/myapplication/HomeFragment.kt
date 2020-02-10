package edu.rosehulman.improse.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import java.util.zip.Inflater

class HomeFragment : Fragment(){
     fun switchToChildFragment(pos: Int) {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val home_view = inflater.inflate(R.layout.fragment_home, container,false)

        //child fragment
        val childFragMan = getChildFragmentManager();
        val childFragTrans = childFragMan.beginTransaction();
       // val fragB =  DataFragment("Home", this, ArrayList<String>());
       // childFragTrans.add(R.id.home_data, fragB);
        childFragTrans.addToBackStack("GameData");
        childFragTrans.commit();

        return home_view
    }

}