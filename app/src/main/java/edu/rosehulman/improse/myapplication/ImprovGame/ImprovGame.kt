package edu.rosehulman.improse.myapplication.ImprovGame

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import edu.rosehulman.improse.myapplication.Data
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
class ImprovGame(
    val name:String = "",
    val description:String = "",
    var timesPlayed:Int= 0,
    var rating:Double = 0.0)
    : Data {
    @IgnoredOnParcel @get:Exclude var id = ""
    @IgnoredOnParcel @ServerTimestamp var lastTouched: Timestamp? = null

    companion object {
        const val LAST_TOUCHED_KEY = "lastTouched"

        fun fromSnapshot(snapshot: DocumentSnapshot): ImprovGame {
            val improvGame = snapshot.toObject(ImprovGame::class.java)!!
            improvGame.id = snapshot.id
            return improvGame
        }
    }
}