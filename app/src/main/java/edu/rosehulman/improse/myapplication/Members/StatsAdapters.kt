package edu.rosehulman.improse.myapplication.Members

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

class StatsAdapters(val con : Context, val sf: StatsFragment): RecyclerView.Adapter<StatsDataViewHolder>() {

    var allMembers = ArrayList<ImprovMember>()

    private val memRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.MEMBERS)

    private lateinit var listenerRegistration: ListenerRegistration

    fun addSnapshotListener() {
        listenerRegistration = memRef
            .orderBy(ImprovMember.LAST_TOUCHED_KEY, Query.Direction.ASCENDING)
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    Log.d(Constants.MEMBERS, "listen error", e)
                } else {
                    processSnapshotChanges(querySnapshot!!)
                }
            }
    }

    private fun processSnapshotChanges(querySnapshot: QuerySnapshot) {
        // Snapshots has documents and documentChanges which are flagged by type,
        // so we can handle C,U,D differently.
        for (documentChange in querySnapshot.documentChanges) {
            val improvMember = ImprovMember.fromSnapshot(documentChange.document)
            when (documentChange.type) {
                DocumentChange.Type.ADDED -> {
                    Log.d(Constants.MEMBERS, "Adding $improvMember")
                    allMembers.add(0, improvMember)
                    notifyItemInserted(0)
                }
                DocumentChange.Type.REMOVED -> {
                    Log.d(Constants.MEMBERS, "Removing $improvMember")
                    val index = allMembers.indexOfFirst { it.id == improvMember.id }
                    allMembers.removeAt(index)
                    notifyItemRemoved(index)
                }
                DocumentChange.Type.MODIFIED -> {
                    Log.d(Constants.MEMBERS, "Modifying $improvMember")
                    val index = allMembers.indexOfFirst { it.id == improvMember.id }
                    allMembers[index] = improvMember
                    notifyItemChanged(index)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, index: Int): StatsDataViewHolder {
        val view = LayoutInflater.from(con).inflate(R.layout.data_card, parent, false)
        return StatsDataViewHolder(
            view,
            this
        )
    }

    override fun getItemCount():Int {
        return allMembers.size
    }

    fun switchToChildFragment(pos: Int) {
        sf.switchToChildFragment(allMembers[pos])
    }

    fun add(member: ImprovMember){
        memRef.add(member)
    }

    override fun onBindViewHolder(holder: StatsDataViewHolder, position: Int) {
        holder.bind(allMembers[position].name)
    }

}