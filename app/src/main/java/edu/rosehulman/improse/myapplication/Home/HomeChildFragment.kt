package edu.rosehulman.improse.myapplication.Home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.rosehulman.improse.myapplication.R
import kotlinx.android.synthetic.main.fragment_home_child.view.*

const val ARG_EVENT = "Event"

class HomeChildFragment : Fragment() {

    var event: ImprovEvent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            event = it.getParcelable(ARG_EVENT)
        }

        val getEvent = event
        if (getEvent != null) {

        } else {
            Log.d("null", "event was null")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_child, container, false)
        val rootView = view

        if (event == null) {
            Log.d("null", "event was null")
        }
        if (event != null) {
            rootView?.event_c_title?.text = event?.name
            rootView?.event_c_description?.text = getString(R.string.details, event?.details)
            rootView?.event_c_type?.text = getString(R.string.type, event?.type)

        } else {
            Log.d("wrong", "something went wrong")
        }

        //val backButton: Button = view.game_child_back
//        backButton.setOnClickListener{
//            Log.d("Back", "Calling back")
//            /* val ft = parentFragment?.fragmentManager?.beginTransaction()
//             ft?.remove(this)
//             val gf = GamesFragment()
//             ft?.replace(R.id.content_layout, gf)
//             ft?.show(gf)
//             //parentFragment?.fragmentManager?.popBackStack("GameParent", FragmentManager.POP_BACK_STACK_INCLUSIVE)
//             ft?.commit()*/
//            //onDestroyView()
//
//        }

        return view;
    }

    companion object {
        @JvmStatic
        fun newInstance(event: ImprovEvent) =
            HomeChildFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_EVENT, event)
                }
            }
    }
}

