package edu.rosehulman.improse.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import edu.rosehulman.rosefire.Rosefire
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity: AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var listenerRegistration: ListenerRegistration
    var showAll: Boolean = false;
    private val ROSE_SIGN_IN = 1
    var userId:String? = null


    private val auth = FirebaseAuth.getInstance()
    lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        login_button.setOnClickListener(){
            onRosefireLogin()
        }

        if(savedInstanceState!= null) {
            initializeAuthListener()
        }
        else{
            auth.signOut()
            initializeAuthListener()
        }
    }

    fun initializeAuthListener() {

        Log.d("user", "initialize listener")

        authStateListener = FirebaseAuth.AuthStateListener { auth ->
            Log.d("user", "made listener")
            val user = auth.currentUser
            if(user != null){
                userId = user.uid
                Log.d("user", "user not null")
                switchToMainActivity(user.uid)
            }
            else{
                Log.d("user", "need to login")
                //switchToLoginFragment()
            }
        }

    }

    private fun switchToMainActivity(uid: String) {
        val intent = Intent(this, MainActivity::class.java)
            .putExtra(Constants.UID, uid)
        startActivity(intent)
    }


    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authStateListener)

    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authStateListener)
    }


    fun onRosefireLogin() {
        val signInIntent = Rosefire.getSignInIntent(this, getString(R.string.reg_token))
        startActivityForResult(signInIntent, ROSE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ROSE_SIGN_IN) {
            val result = Rosefire.getSignInResultFromIntent(data)
            if (!result.isSuccessful) { // The user cancelled the login
                return
            }
            FirebaseAuth.getInstance().signInWithCustomToken(result.token)
                .addOnCompleteListener(
                    this
                ) { task ->
                    Log.d("rose_fire",
                        "signInWithCustomToken:onComplete:" + task.isSuccessful
                    )
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // you should use an AuthStateListener to handle the logic for
                    // signed in user and a signed out user.
                    if (!task.isSuccessful) {
                        Log.w("rose_fire",
                            "signInWithCustomToken",
                            task.exception
                        )
                        Toast.makeText(
                            this@LoginActivity, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}
