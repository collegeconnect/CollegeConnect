package com.college.collegeconnect.activities

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.college.collegeconnect.R
import com.college.collegeconnect.databinding.ActivityNavigationBinding
import com.college.collegeconnect.datamodels.SaveSharedPreference.getPop
import com.college.collegeconnect.datamodels.SaveSharedPreference.getUser
import com.college.collegeconnect.datamodels.SaveSharedPreference.setPop
import com.college.collegeconnect.datamodels.SaveSharedPreference.setUser
import com.college.collegeconnect.datamodels.SaveSharedPreference.setUserName
import com.college.collegeconnect.settingsActivity.SettingsActivity
import com.college.collegeconnect.ui.attendance.AttendanceFragment
import com.college.collegeconnect.ui.home.HomeFragment
import com.college.collegeconnect.ui.notes.NotesFragment
import com.college.collegeconnect.ui.tools.ToolsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class Navigation : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityNavigationBinding

    var homefrag: Fragment = HomeFragment()
    var attenfrag: Fragment = AttendanceFragment()
    var notefrag: Fragment = NotesFragment()
    var toolsfrag: Fragment = ToolsFragment()
    var builder: AlertDialog.Builder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // For test notifications
        /*
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Token", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("navigation", msg);
//                        Toast.makeText(navigation.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
           */if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            channel.description = CHANNEL_DESC
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
        if (intent.getStringExtra("fragment") != null && intent.getStringExtra("fragment") == "attenfrag") {
            Log.d("navigation", "onCreate: attenfrag called from notificaion")
            loadFragments(AttendanceFragment())
        }
        builder = MaterialAlertDialogBuilder(this)
        act = this
        setUserName(this, FirebaseAuth.getInstance().currentUser!!.email)

        // Set initials and dp
        if (FirebaseAuth.getInstance().currentUser!!.displayName != null) setUser(this@Navigation, FirebaseAuth.getInstance().currentUser!!.displayName) else {
            val name = getUser(this@Navigation)
        }
        val random = Random()
        color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))

        setSupportActionBar(binding.toolbarnav)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        loadFragments(homefrag)

        // In app updates
        // Creates instance of the manager.
        val appUpdateManager = AppUpdateManagerFactory.create(this)

        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && // For a flexible update, use AppUpdateType.FLEXIBLE
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult( // Pass the intent that is returned by 'getAppUpdateInfo()'.
                        appUpdateInfo, // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                        AppUpdateType.IMMEDIATE, // The current activity making the update request.
                        this, // Include a request code to later monitor this update request.
                        MY_REQUEST_CODE
                    )
                } catch (e: SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this@Navigation, SettingsActivity::class.java))
                //                Dialog();
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //    @Override
    //    public boolean onSupportNavigateUp() {
    //        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    //        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
    //                || super.onSupportNavigateUp();
    //    }
    fun feedbackPop() {
        builder!!.setTitle("Feedback")
        builder!!.setMessage("Consider taking a one minute feedback?")
        builder!!.setPositiveButton("Sure") { dialog: DialogInterface?, which: Int -> startActivity(Intent(this@Navigation, FeedbackActivity::class.java)) }
        builder!!.setNegativeButton("Exit") { dialog: DialogInterface, which: Int ->
            dialog.dismiss()
            finish()
        }
        val alertDialog = builder!!.create()
        alertDialog.show()
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        var fragment: Fragment? = null
        when (menuItem.itemId) {
            R.id.nav_home -> fragment = homefrag
            R.id.nav_attendance -> fragment = attenfrag
            R.id.nav_notes -> fragment = notefrag
            R.id.nav_tools -> fragment = toolsfrag
        }
        return loadFragments(fragment)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentById(R.id.fragmentContainer) is HomeFragment) {
//            if (SaveSharedPreference.getPop(this) % 7 == 0) {
//                feedbackPop();
//            } else
            finish()
        } else if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate()
        } else super.onBackPressed()
    }

    private fun loadFragments(fragment: Fragment?): Boolean {
        if (fragment != null) {
            Log.d("navigation", "loadFragments: Frag is loaded")
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
            return true
        }
        return false
    }

    override fun onDestroy() {
        setPop(this, getPop(this) + 1)
        super.onDestroy()
    }

    companion object {
        private const val MY_REQUEST_CODE = 10
        var color = 0

        @JvmField
        var CHANNEL_ID = "Notification"
        private const val CHANNEL_NAME = "Notification Channel"
        private const val CHANNEL_DESC = "app notification"
        var act: Activity? = null

        @JvmStatic
        fun generateColor(): Int {
            return color
        }
    }
}
