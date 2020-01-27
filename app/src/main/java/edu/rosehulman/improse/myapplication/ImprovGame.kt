package edu.rosehulman.improse.myapplication

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ImprovGame(

    val id:Int = 0,
    val name:String = "",
    val description:String = "",
    val timesPlayed:Int = 0,
    var rating:Float = 0f
)
    :Parcelable{
}