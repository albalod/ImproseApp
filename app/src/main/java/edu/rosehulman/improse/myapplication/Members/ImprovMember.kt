package edu.rosehulman.improse.myapplication.Members

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
class ImprovMember(
    val name: String = "",
    val username: String = "",
    val position: String = "",
    var activeness: String = "",
    val isAdmin: Boolean = false
) : Parcelable {
    @IgnoredOnParcel @get:Exclude var id = ""
    @IgnoredOnParcel @ServerTimestamp var lastTouched: Timestamp? = null

    companion object {
        const val LAST_TOUCHED_KEY = "lastTouched"

        fun fromSnapshot(snapshot: DocumentSnapshot): ImprovMember {
            val improvMember = snapshot.toObject(ImprovMember::class.java)!!
            improvMember.id = snapshot.id
            return improvMember
        }
    }
}