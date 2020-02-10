package edu.rosehulman.improse.myapplication.MeetingRecords

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import edu.rosehulman.improse.myapplication.Constants
import edu.rosehulman.improse.myapplication.R

class MeetingsAdapters(val con : Context, val mf: MeetingFragment): RecyclerView.Adapter<MeetingDataViewHolder>() {

    var allMeetings = ArrayList<ImprovMeeting>()

    private val meetRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.MEMBERS)

    private lateinit var listenerRegistration: ListenerRegistration

    fun addSnapshotListener() {
        listenerRegistration = meetRef
            .orderBy(ImprovMeeting.LAST_TOUCHED_KEY, Query.Direction.ASCENDING)
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
            val improvMeeting = ImprovMeeting.fromSnapshot(documentChange.document)
            when (documentChange.type) {
                DocumentChange.Type.ADDED -> {
                    Log.d(Constants.MEMBERS, "Adding $improvMeeting")
                    allMeetings.add(0, improvMeeting)
                    notifyItemInserted(0)
                }
                DocumentChange.Type.REMOVED -> {
                    Log.d(Constants.MEMBERS, "Removing $improvMeeting")
                    val index = allMeetings.indexOfFirst { it.id == improvMeeting.id }
                    allMeetings.removeAt(index)
                    notifyItemRemoved(index)
                }
                DocumentChange.Type.MODIFIED -> {
                    Log.d(Constants.MEMBERS, "Modifying $improvMeeting")
                    val index = allMeetings.indexOfFirst { it.id == improvMeeting.id }
                    allMeetings[index] = improvMeeting
                    notifyItemChanged(index)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, index: Int): MeetingDataViewHolder {
        val view = LayoutInflater.from(con).inflate(R.layout.data_card, parent, false)
        return MeetingDataViewHolder(
            view,
            this
        )
    }

    override fun getItemCount():Int {
        return allMeetings.size
    }

    fun switchToChildFragment(pos: Int) {
        mf.switchToChildFragment(allMeetings[pos])
    }

    fun add(meeting: ImprovMeeting){
        meetRef.add(meeting)
    }

    override fun onBindViewHolder(holder: MeetingDataViewHolder, position: Int) {
        holder.bind(allMeetings[position].name)
    }

}