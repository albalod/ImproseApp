package edu.rosehulman.improse.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import edu.rosehulman.rosefire.Rosefire


class MainActivity : AppCompatActivity(),  LoginFragment.OnLoginPressed{

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var listenerRegistration: ListenerRegistration
    var showAll: Boolean = false;
    private val RC_SIGN_IN = 1
    private val ROSE_SIGN_IN = 2
    var userId:String? = null


    private val auth = FirebaseAuth.getInstance()
    lateinit var authStateListener:FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home, R.id.nav_attendance,
            R.id.nav_meetings, R.id.nav_games, R.id.nav_stats), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        if(savedInstanceState == null) {
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
               // switchToHomeFragment(user.uid)
            }
            else{
                Log.d("user", "need to login")
                switchToLoginFragment()
            }
        }

    }

    private fun switchToLoginFragment() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.content_layout, LoginFragment.newInstance())
        //ft.addToBackStack("lif")
        ft.commit()
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authStateListener)

    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authStateListener)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun roseLogin() {
        onRosefireLogin()
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
                            this@MainActivity, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}
