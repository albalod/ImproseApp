package edu.rosehulman.improse.myapplication.Home

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
class ImprovEvent(
    val name:String = "",
    val details:String = "",
    val type: String = "")
    : Parcelable {
    @IgnoredOnParcel @get:Exclude var id = ""
    @IgnoredOnParcel @ServerTimestamp var lastTouched: Timestamp? = null

    companion object {
        const val LAST_TOUCHED_KEY = "lastTouched"

        fun fromSnapshot(snapshot: DocumentSnapshot): ImprovEvent {
            val improvEvent = snapshot.toObject(ImprovEvent::class.java)!!
            improvEvent.id = snapshot.id
            return improvEvent
        }
    }
}