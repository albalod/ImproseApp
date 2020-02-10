package edu.rosehulman.improse.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_suggestions.view.*

const val TEXT = "text"

class SuggestionFragment : Fragment(){

    private val suggstionsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.SUGGESTIONS)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val suggestionView =  inflater.inflate(R.layout.fragment_suggestions, container,false)

        val submitButton = suggestionView.suggestions_submit
        val suggestionText = suggestionView.suggestions_edittext

        submitButton.setOnClickListener(){

            if(suggestionText.text.toString() != ""){

                val toAdd = hashMapOf<String, String>(
                    TEXT to suggestionText.text.toString()
                )
                suggstionsRef.add(toAdd)

                Toast.makeText(context, getString(R.string.suggestion_submitted), Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(context, getString(R.string.suggestion_denied), Toast.LENGTH_LONG).show()
            }
        }


        return suggestionView;
    }






}