package edu.rosehulman.improse.myapplication.Members

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import edu.rosehulman.improse.myapplication.Constants
import edu.rosehulman.improse.myapplication.R
import kotlinx.android.synthetic.main.fragment_stats_child.view.*

const val ARG_MEMBER = "Members"
class StatsChildFragment : Fragment() {

    var member: ImprovMember? = null
    var meetingsAttended = 0;
    var countText: TextView? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            member = it.getParcelable(ARG_MEMBER)
        }

        val getGame = member
        if (getGame != null) {

        } else {
            Log.d("null", "game was null")
        }

        getMeetings()
    }

    fun getMeetings(){

        val meetingRef = FirebaseFirestore.getInstance().collection(Constants.MEETING)

        meetingRef.whereArrayContains("attendees", member!!.username).get().addOnSuccessListener {documents ->
            for (document in documents){
                meetingsAttended++
                updateCount();
                Log.d("count", "${document.id} => ${document.data}")
            }
        }.addOnFailureListener(){
            Log.w("error", "Error getting documents: ", it)
        }

        Log.d("result", meetingsAttended.toString())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_stats_child, container, false)
        countText = view.stats_c_meetings
        if (member == null) {
            Log.d("null", "member was null")
        }
        if (member != null) {
            view?.stats_c_title?.text = member?.name
            view?.stats_c_meetings?.text = getString(R.string.stats_c_meetings, meetingsAttended.toString())
            view?.stats_c_activeness?.text = getString(R.string.stats_c_activeness, member?.activeness)
            view?.stats_c_position?.text = getString(R.string.stats_c_position, member?.position)
            view?.stats_c_username?.text = getString(R.string.stats_c_username, member?.username)

        } else {
            Log.d("wrong", "something went wrong")
        }


        return view;
    }

    fun updateCount(){
        countText?.text = getString(R.string.stats_c_meetings, meetingsAttended.toString())
    }

    companion object {
        @JvmStatic
        fun newInstance(member: ImprovMember) =
            StatsChildFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_MEMBER, member)
                }
            }
    }
}