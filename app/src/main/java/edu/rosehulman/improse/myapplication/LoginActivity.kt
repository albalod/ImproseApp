package edu.rosehulman.improse.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.app_bar_login.*
import kotlinx.android.synthetic.main.login_activity.view.*


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        val login_toolbar: Toolbar = findViewById(R.id.login_toolbar)
        setSupportActionBar(login_toolbar)

        val button = findViewById<Button>(R.id.login_button)

        button.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

}
