package edu.rosehulman.improse.myapplication.Members

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.rosehulman.improse.myapplication.MainActivity
import edu.rosehulman.improse.myapplication.R

class StatsFragment : Fragment(){


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val stats_view = inflater.inflate(R.layout.fragment_statistics, container,false)

        //child fragment
        val childFragMan = getChildFragmentManager();
        val childFragTrans = childFragMan.beginTransaction();
        val fragS =  StatsDataFragment(this);
        childFragTrans.replace(R.id.stats_data, fragS);
        childFragTrans.commit();

        return stats_view;

    }


    fun switchToChildFragment(member: ImprovMember) {
        val ft = (context as MainActivity).supportFragmentManager.beginTransaction()
        ft.hide(parentFragment!!)
        ft.replace(R.id.content_layout, StatsChildFragment.newInstance(member))
        ft.commit()
    }
}