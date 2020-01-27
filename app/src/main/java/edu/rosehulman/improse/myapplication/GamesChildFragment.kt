import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import edu.rosehulman.improse.myapplication.GamesFragment
import edu.rosehulman.improse.myapplication.ImprovGame
import edu.rosehulman.improse.myapplication.R
import kotlinx.android.synthetic.main.fragment_game_child.view.*

const val ARG_GAME = "Game"

class GamesChildFragment : Fragment() {

    var game: ImprovGame? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            game = it.getParcelable(ARG_GAME)
        }

        val getGame = game
        if (getGame != null) {

        } else {
            Log.d("null", "game was null")
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_child, container, false)
        val rootView = view

        if (game == null) {
            Log.d("null", "game was null")
        }
        if (game != null) {
            rootView?.game_c_title?.text = game?.name
            rootView?.game_c_description?.text = game?.description
        } else {
            Log.d("wrong", "something went wrong")
        }

        val backButton: Button = view.game_child_back
        backButton.setOnClickListener{
            Log.d("Back", "Calling back")
           /* val ft = parentFragment?.fragmentManager?.beginTransaction()
            ft?.remove(this)
            val gf = GamesFragment()
            ft?.replace(R.id.content_layout, gf)
            ft?.show(gf)
            //parentFragment?.fragmentManager?.popBackStack("GameParent", FragmentManager.POP_BACK_STACK_INCLUSIVE)
            ft?.commit()*/

        }

        return view;
    }

    companion object {
        @JvmStatic
        fun newInstance(game:ImprovGame) =
            GamesChildFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_GAME, game)
                }
            }
    }

}