package edu.rosehulman.improse.myapplication.MeetingRecords

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
class ImprovMeeting(
    val name:String = "",
    val description:String = "",
    var timesPlayed:Int= 0,
    var rating:Double = 0.0)
    : Parcelable {
    @IgnoredOnParcel @get:Exclude var id = ""
    @IgnoredOnParcel @ServerTimestamp var lastTouched: Timestamp? = null

    companion object {
        const val LAST_TOUCHED_KEY = "lastTouched"

        fun fromSnapshot(snapshot: DocumentSnapshot): ImprovMeeting {
            val improvMeeting = snapshot.toObject(ImprovMeeting::class.java)!!
            improvMeeting.id = snapshot.id
            return improvMeeting
        }
    }
}