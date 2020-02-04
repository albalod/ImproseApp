package edu.rosehulman.improse.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.login_fragment.view.*

class LoginFragment: Fragment() {

    var loginListen: OnLoginPressed? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.login_fragment, container, false)

        view.login_button.setOnClickListener{
            loginListen?.roseLogin()
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnLoginPressed) {
            loginListen = context
        } else {
            throw RuntimeException(context.toString() + " is not listening")
        }
    }

    override fun onDetach() {
        super.onDetach()
        loginListen = null
    }

    interface OnLoginPressed {
        fun roseLogin()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

}
