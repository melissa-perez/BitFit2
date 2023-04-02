package com.codepath.bitfit2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.codepath.bitfit2.R.id
import com.codepath.bitfit2.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var sleepEntryBtn: Button
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // dashboard/button part of root activity main
        // recyclerview
        val supportFragmentManager = supportFragmentManager
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(id.content, SleepEntryFragment(), null).commit()

        // set on click listener for log sleep entry btn
        sleepEntryBtn = findViewById<Button>(R.id.sleep_record_btn)

        sleepEntryBtn.setOnClickListener {
            val sleepEntryIntent = Intent(this, LogActivity::class.java)
            startActivity(sleepEntryIntent)
        }
    }
}