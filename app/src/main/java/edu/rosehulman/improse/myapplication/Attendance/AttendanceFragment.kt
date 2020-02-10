package edu.rosehulman.improse.myapplication.Attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import edu.rosehulman.improse.myapplication.Constants
import edu.rosehulman.improse.myapplication.ImprovGame.GameDataFragment
import edu.rosehulman.improse.myapplication.ImprovGame.ImprovGame
import edu.rosehulman.improse.myapplication.MainActivity
import edu.rosehulman.improse.myapplication.R

class AttendanceFragment : Fragment(){

    private val attendanceRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.MEMBERS)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val attendance_view = inflater.inflate(R.layout.fragment_attendance, container,false)

        return attendance_view
    }

}