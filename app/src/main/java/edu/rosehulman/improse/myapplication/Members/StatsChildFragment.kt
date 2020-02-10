package edu.rosehulman.improse.myapplication.Members

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.rosehulman.improse.myapplication.R

const val ARG_MEMBER = "Members"
class StatsChildFragment : Fragment() {

    var member: ImprovMember? = null

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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stats_child, container, false)
        val rootView = view

        if (member == null) {
            Log.d("null", "game was null")
        }
        if (member != null) {
            //rootView?.stats_c_title?.text = game?.name
            //rootView?.game_c_description?.text = game?.description
        } else {
            Log.d("wrong", "something went wrong")
        }

        //val backButton: Button = view.game_child_back
        // backButton.setOnClickListener{
        // Log.d("Back", "Calling back")
        /* val ft = parentFragment?.fragmentManager?.beginTransaction()
         ft?.remove(this)
         val gf = GamesFragment()
         ft?.replace(R.id.content_layout, gf)
         ft?.show(gf)
         //parentFragment?.fragmentManager?.popBackStack("GameParent", FragmentManager.POP_BACK_STACK_INCLUSIVE)
         ft?.commit()*/
        //onDestroyView()

        // }

        return view;
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