package com.codepath.bitfit2

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.codepath.bitfit2.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var sleepEntryBtn: Button
    private var binding: ActivityMainBinding? = null
   private var ID = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        createNotificationChannel()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val supportFragmentManager = supportFragmentManager
        val fragmentManager: FragmentManager = supportFragmentManager

        val sleepListFragment: Fragment = SleepEntryFragment()
        val dashboardFragment: Fragment = DashboardFragment()
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.action_sleep_list

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

        // Set default selection

        val n: Notification = Notification.Builder(this)
            .setContentTitle("New mail from " + "test@gmail.com")
            .setContentText("Subject")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setWhen(System.currentTimeMillis())
            .build()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(ID++, n)

    }


    private fun createNotificationChannel() {
        val name: CharSequence = "BITFIT"
        val description = "Daily notification"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(
            "BITFIT", name,
            importance
        )
        channel.description = description

        val notificationManager = getSystemService(
            NotificationManager::class.java
        )
        notificationManager.createNotificationChannel(channel)
    }
}