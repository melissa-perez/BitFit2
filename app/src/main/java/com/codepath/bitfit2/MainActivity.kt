package com.codepath.bitfit2

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.codepath.bitfit2.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    lateinit var sleepEntryBtn: Button
    private var binding: ActivityMainBinding? = null
   private var ID = 1
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        // Build the notification
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, "channel_id")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("BitFit Daily Reminder")
            .setContentText("Log your sleep for the day!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(1, builder.build())

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val supportFragmentManager = supportFragmentManager
        val fragmentManager: FragmentManager = supportFragmentManager

        val sleepListFragment: Fragment = SleepEntryFragment()
        val dashboardFragment: Fragment = DashboardFragment()
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // set on click listener for log sleep entry btn
        sleepEntryBtn = findViewById<Button>(R.id.sleep_record_btn)

        sleepEntryBtn.setOnClickListener {
            val sleepEntryIntent = Intent(this, LogActivity::class.java)
            startActivity(sleepEntryIntent)
        }
        // handle navigation selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.action_sleep_list -> {
                    fragment = sleepListFragment
                    sleepEntryBtn.visibility = VISIBLE
                }
                R.id.action_dashboard -> {
                    fragment = dashboardFragment
                    sleepEntryBtn.visibility = INVISIBLE
                }
            }
            fragmentManager.beginTransaction().replace(R.id.content, fragment).commit()

            true
        }

        bottomNavigationView.selectedItemId = R.id.action_sleep_list

    }


    private fun createNotificationChannel() {
        val name: CharSequence = "My Channel"
        val description = "My Channel Description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("channel_id", name, importance)
        channel.description = description
        val notificationManager = getSystemService(
            NotificationManager::class.java
        )
        notificationManager.createNotificationChannel(channel)
    }
}