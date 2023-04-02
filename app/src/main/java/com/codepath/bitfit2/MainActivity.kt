package com.codepath.bitfit2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.codepath.bitfit2.R.id
import com.codepath.bitfit2.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    lateinit var sleepEntryBtn: Button
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        val supportFragmentManager = supportFragmentManager
        val fragmentManager: FragmentManager = supportFragmentManager

        val sleepListFragment: Fragment = SleepEntryFragment()
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // handle navigation selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.action_sleep_list -> fragment = sleepListFragment
                R.id.action_dashboard -> fragment = sleepListFragment
            }
            fragmentManager.beginTransaction().replace(R.id.rlContainer, fragment).commit()

            true
        }

        // Set default selection
        bottomNavigationView.selectedItemId = R.id.action_sleep_list

        // set on click listener for log sleep entry btn
        sleepEntryBtn = findViewById<Button>(R.id.sleep_record_btn)

        sleepEntryBtn.setOnClickListener {
            val sleepEntryIntent = Intent(this, LogActivity::class.java)
            startActivity(sleepEntryIntent)
        }
    }
}