package edu.rosehulman.improse.myapplication.Attendance

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.*
import edu.rosehulman.improse.myapplication.Constants
import edu.rosehulman.improse.myapplication.ImprovGame.ImprovGame
import edu.rosehulman.improse.myapplication.R
import kotlinx.android.synthetic.main.fragment_attendance.*
import kotlinx.android.synthetic.main.fragment_attendance.view.*

class AttendanceFragment : Fragment() {

    var meetings = ArrayList<MeetingData>()

    private lateinit var listenerRegistration: ListenerRegistration
    private val attendanceRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.MEETING)

    fun addSnapshotListener() {
        listenerRegistration = attendanceRef
            .orderBy(MeetingData.LAST_TOUCHED_KEY, Query.Direction.ASCENDING)
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    Log.d(Constants.TAG, "listen error", e)
                } else {
                    processSnapshotChanges(querySnapshot!!)
                }
            }
    }

    private fun processSnapshotChanges(querySnapshot: QuerySnapshot) {
        // Snapshots has documents and documentChanges which are flagged by type,
        // so we can handle C,U,D differently.
        for (documentChange in querySnapshot.documentChanges) {
            val meeting = MeetingData.fromSnapshot(documentChange.document)
            when (documentChange.type) {
                DocumentChange.Type.ADDED -> {
                    Log.d(Constants.TAG, "Adding $meeting")
                    meetings.add(0, meeting)
                }
                DocumentChange.Type.REMOVED -> {
                    Log.d(Constants.TAG, "Removing $meeting")
                    val index = meetings.indexOfFirst { it.id == meeting.id }
                    meetings.removeAt(index)
                }
                DocumentChange.Type.MODIFIED -> {
                    Log.d(Constants.TAG, "Modifying $meeting")
                    val index = meetings.indexOfFirst { it.id == meeting.id }
                    meetings[index] = meeting
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val attendanceView = inflater.inflate(R.layout.fragment_attendance, container, false)
        addSnapshotListener()
        attendanceView.attendance_button.setOnClickListener {
            addAttendee()
        }
        return attendanceView
    }

    private fun addAttendee() {
        //Validate the data in the EditTexts
        val inputDate: String = attendance_date_edittext.text.toString()
        val inputName: String = attendance_name_edittext.text.toString()

        //TODO: do some actual data validation here
        if (isInputDateBad(inputDate)) {
            Toast.makeText(context, R.string.bad_date_error, Toast.LENGTH_LONG).show()
            return
        }

        var index: Int = meetings.indexOfFirst { it.dateString == inputDate }

        if (index == -1) {
            val attender = ArrayList<String>()
            attender.add(inputName)
            attendanceRef.add(MeetingData(inputDate, attender))
        } else {
            if (meetings[index].attendees.contains(inputName)) {
                Toast.makeText(context, R.string.already_attending_error, Toast.LENGTH_LONG).show()
                attendance_name_edittext.text.clear()
                return
            }

            meetings[index].attendees.add(inputName)
            attendanceRef.document(meetings[index].id).set(meetings[index])
        }
        attendance_name_edittext.text.clear()


    }

    //Returns true if the date is invalid
    //Returns false otherwise
    private fun isInputDateBad(s: String): Boolean {
        //Date is expected to be in MM/DD/YYYY format, so it should be exactly 10 characters long
        if (s.length != 10) {
            return true
        }
        if (s[2] != '/' || s[5] != '/') {
            return true
        }
        val month: Int? = s.substring(0, 2).toIntOrNull()
        val day: Int? = s.substring(3, 5).toIntOrNull()
        val year: Int? = s.substring(6, 10).toIntOrNull()
        if (month == null || day == null || year == null) {
            return true
        }

        if (year < 2018) {
            return true
        }
        if (day < 1 || day > 31) {
            return true
        }
        if (month < 1 || month > 12) {
            return true
        }
        return false

    }

}