package edu.rosehulman.improse.myapplication.Home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import edu.rosehulman.improse.myapplication.*
import edu.rosehulman.improse.myapplication.R

class HomeAdapters(val con : Context, val hf: HomeFragment): RecyclerView.Adapter<HomeDataViewHolder>() {

    var allEvents = ArrayList<ImprovEvent>()

    private val eventsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.EVENTS)

    private lateinit var listenerRegistration: ListenerRegistration

    fun addSnapshotListener() {
        listenerRegistration = eventsRef
            .orderBy(ImprovEvent.LAST_TOUCHED_KEY, Query.Direction.ASCENDING)
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    Log.d(Constants.EVENTS, "listen error", e)
                } else {
                    processSnapshotChanges(querySnapshot!!)
                }
            }
    }

    private fun processSnapshotChanges(querySnapshot: QuerySnapshot) {
        // Snapshots has documents and documentChanges which are flagged by type,
        // so we can handle C,U,D differently.
        for (documentChange in querySnapshot.documentChanges) {
            val improvEvent = ImprovEvent.fromSnapshot(documentChange.document)
            when (documentChange.type) {
                DocumentChange.Type.ADDED -> {
                    Log.d(Constants.EVENTS, "Adding $improvEvent")
                    allEvents.add(0, improvEvent)
                    notifyItemInserted(0)
                }
                DocumentChange.Type.REMOVED -> {
                    Log.d(Constants.EVENTS, "Removing $improvEvent")
                    val index = allEvents.indexOfFirst { it.id == improvEvent.id }
                    allEvents.removeAt(index)
                    notifyItemRemoved(index)
                }
                DocumentChange.Type.MODIFIED -> {
                    Log.d(Constants.GAME_REF, "Modifying $improvEvent")
                    val index = allEvents.indexOfFirst { it.id == improvEvent.id }
                    allEvents[index] = improvEvent
                    notifyItemChanged(index)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, index: Int): HomeDataViewHolder {
        val view = LayoutInflater.from(con).inflate(R.layout.data_card, parent, false)
        return HomeDataViewHolder(
            view,
            this
        )
    }

    override fun getItemCount():Int {
        return allEvents.size
    }

    fun switchToChildFragment(pos: Int) {
        hf.switchToChildFragment(allEvents[pos])
    }

    override fun onBindViewHolder(holder: HomeDataViewHolder, position: Int) {
        holder.bind(allEvents[position].name)
    }

}