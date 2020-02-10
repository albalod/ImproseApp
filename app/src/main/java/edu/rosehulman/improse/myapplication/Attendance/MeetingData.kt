package edu.rosehulman.improse.myapplication.Attendance

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import edu.rosehulman.improse.myapplication.ImprovGame.ImprovGame
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
class MeetingData(
    val dateString: String = "",
    val attendees: ArrayList<String> = ArrayList()
) : Parcelable {
    @IgnoredOnParcel @get:Exclude var id = ""
    @IgnoredOnParcel @ServerTimestamp var lastTouched: Timestamp? = null

    companion object {
        const val LAST_TOUCHED_KEY = "lastTouched"

        fun fromSnapshot(snapshot: DocumentSnapshot): MeetingData {
            val meetingData = snapshot.toObject(MeetingData::class.java)!!
            meetingData.id = snapshot.id
            return meetingData
        }
    }
}