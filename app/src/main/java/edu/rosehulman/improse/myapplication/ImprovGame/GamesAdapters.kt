package edu.rosehulman.albalod.photobucket

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import edu.rosehulman.improse.myapplication.*
import edu.rosehulman.improse.myapplication.ImprovGame.GameDataViewHolder
import edu.rosehulman.improse.myapplication.ImprovGame.GamesFragment
import edu.rosehulman.improse.myapplication.ImprovGame.ImprovGame
import edu.rosehulman.improse.myapplication.R


class GamesAdapters(val con : Context, val gf: GamesFragment): RecyclerView.Adapter<GameDataViewHolder>() {

    var allGames = ArrayList<ImprovGame>()

    private val gamesRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.GAME_REF)

    private lateinit var listenerRegistration: ListenerRegistration

     fun addSnapshotListener() {
        listenerRegistration = gamesRef
            .orderBy(ImprovGame.LAST_TOUCHED_KEY, Query.Direction.ASCENDING)
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    Log.d(Constants.GAME_REF, "listen error", e)
                } else {
                    processSnapshotChanges(querySnapshot!!)
                }
            }
    }

    private fun processSnapshotChanges(querySnapshot: QuerySnapshot) {
        // Snapshots has documents and documentChanges which are flagged by type,
        // so we can handle C,U,D differently.
        for (documentChange in querySnapshot.documentChanges) {
            val improvGame = ImprovGame.fromSnapshot(documentChange.document)
            when (documentChange.type) {
                DocumentChange.Type.ADDED -> {
                    Log.d(Constants.GAME_REF, "Adding $improvGame")
                    allGames.add(0, improvGame)
                    notifyItemInserted(0)
                }
                DocumentChange.Type.REMOVED -> {
                    Log.d(Constants.GAME_REF, "Removing $improvGame")
                    val index = allGames.indexOfFirst { it.id == improvGame.id }
                    allGames.removeAt(index)
                    notifyItemRemoved(index)
                }
                DocumentChange.Type.MODIFIED -> {
                    Log.d(Constants.GAME_REF, "Modifying $improvGame")
                    val index = allGames.indexOfFirst { it.id == improvGame.id }
                    allGames[index] = improvGame
                    notifyItemChanged(index)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, index: Int): GameDataViewHolder {
        val view = LayoutInflater.from(con).inflate(R.layout.data_card, parent, false)
        return GameDataViewHolder(
            view,
            this
        )
    }

    override fun getItemCount():Int {
        return allGames.size
    }

    @Suppress("UNCHECKED_CAST")

     fun switchToChildFragment(pos: Int) {
        gf.switchToChildFragment(allGames[pos])
    }

    fun add(game: ImprovGame){
        gamesRef.add(game)
    }

    override fun onBindViewHolder(holder: GameDataViewHolder, position: Int) {
        holder.bind(allGames[position].name)
    }

}