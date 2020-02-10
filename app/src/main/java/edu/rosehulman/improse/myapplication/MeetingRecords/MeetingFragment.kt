package edu.rosehulman.improse.myapplication.MeetingRecords

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.rosehulman.improse.myapplication.R

class MeetingFragment : Fragment(){


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val meeting_view = inflater.inflate(R.layout.fragment_meeting_records, container,false)

        //child fragment
        val childFragMan = getChildFragmentManager();
        val childFragTrans = childFragMan.beginTransaction();
        val fragM =  MeetingDataFragment(this);
        childFragTrans.add(R.id.meeting_data, fragM);
        childFragTrans.commit();
        return meeting_view
    }

    fun switchToChildFragment(meeting: ImprovMeeting) {

    }

}